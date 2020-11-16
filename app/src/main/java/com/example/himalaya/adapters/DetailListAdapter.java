package com.example.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private List<Track> mDetailData = new ArrayList<>();
    private SimpleDateFormat mUpdateTimeFormat;
    private SimpleDateFormat mDurationFormat;
    private ItemClickedListener mOnItemClickListener;

    public DetailListAdapter() {
        mUpdateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDurationFormat = new SimpleDateFormat("mm:ss");
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        holder.bindData(mDetailData.get(position), position);
        final View itemView = holder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    // 需要有列表和位置
                    mOnItemClickListener.onItemClick(position, mDetailData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDetailData != null) {
            return mDetailData.size();
        }
        return 0;
    }

    public void updateData(List<Track> tracks) {
        if (mDetailData != null) {
            mDetailData.clear();

            mDetailData.addAll(tracks);
        }
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{



        public InnerHolder(@NonNull View itemView) {
            super(itemView);


        }

        public void bindData(Track track, int position){
            TextView tvItemId = itemView.findViewById(R.id.tv_item_id);
            TextView tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            TextView tvTrackPlayCount = itemView.findViewById(R.id.tv_track_play_count);
            TextView tvTrackDuration = itemView.findViewById(R.id.tv_track_duration);
            TextView tvUpdateTime = itemView.findViewById(R.id.tv_update_time);

            tvItemId.setText((position+1) + "");
            tvTrackTitle.setText(track.getTrackTitle());
            tvTrackPlayCount.setText(track.getPlayCount() + "");

            int duration = track.getDuration() * 1000;
            String durationText = mDurationFormat.format(duration);
            tvTrackDuration.setText(durationText);

            String updateTimeText = mUpdateTimeFormat.format(track.getUpdatedAt());
            tvUpdateTime.setText(updateTimeText);
        }
    }

    public void setOnItemClickedListener(ItemClickedListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface ItemClickedListener {
        void onItemClick(int position, List<Track> detailData);
    }

}
