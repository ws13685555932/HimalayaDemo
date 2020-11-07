package com.example.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.himalaya.R;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    public static final String TAG = "RecommendListAdapter";
    private List<Album> mAlbumList = new ArrayList<>();
    private OnRecommendItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 载入View
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        // 设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int clickPosition = (int) v.getTag();
                    mItemClickListener.onItemClick(clickPosition, mAlbumList.get(position));
                }
                LogUtil.d(TAG, "holder itemView clicked --> " + v.getTag());
            }
        });
        holder.setData(mAlbumList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mAlbumList != null) {
            return mAlbumList.size();
        }
        return 0;
    }

    public void updataData(List<Album> albumList) {
        if (mAlbumList != null) {
            mAlbumList.clear();
            mAlbumList.addAll(albumList);
        }
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            ImageView ivAlbumCover = itemView.findViewById(R.id.iv_album_cover);
            TextView tvAlbumTitle = itemView.findViewById(R.id.tv_album_title);
            TextView tvAlbumDescription = itemView.findViewById(R.id.tv_album_description);
            TextView tvAlbumPlayCount = itemView.findViewById(R.id.tv_album_play_count);
            TextView tvAlbumContentSize = itemView.findViewById(R.id.tv_album_content_size);

            tvAlbumTitle.setText(album.getAlbumTitle());
            tvAlbumDescription.setText(album.getAlbumIntro());
            tvAlbumPlayCount.setText(album.getPlayCount() + "");
            tvAlbumContentSize.setText(album.getIncludeTrackCount() + "");
            Glide.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(ivAlbumCover);
        }
    }

    public void setOnRecommendItemClickListener(OnRecommendItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnRecommendItemClickListener {
        void onItemClick(int position, Album album);
    }

}
