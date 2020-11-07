package com.example.himalaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.himalaya.adapters.DetailListAdapter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.views.RoundRectImageView;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import kotlin.collections.UIntIterator;

public class DetailActivity extends AppCompatActivity implements IAlbumDetailViewCallback {

    private ImageView mIvLargeCover;
    private RoundRectImageView mIvSmallCover;
    private TextView mTvAlbumTitle;
    private TextView mTvAlbumAuthor;
    private AlbumDetailPresenter mInstance;
    private AlbumDetailPresenter mAlbumDetailPresenter;

    private int mCurrentPage = 1;
    private View mViewById;
    private RecyclerView mRvDetailList;
    private DetailListAdapter mDetailListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();


        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);



    }

    private void initView() {
        mIvLargeCover = findViewById(R.id.iv_large_cover);
        mIvSmallCover = findViewById(R.id.iv_small_cover);
        mTvAlbumTitle = findViewById(R.id.tv_album_title);
        mTvAlbumAuthor = findViewById(R.id.tv_album_author);
        mRvDetailList = findViewById(R.id.tv_detail_list);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvDetailList.setLayoutManager(layoutManager);
        // 设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mRvDetailList.setAdapter(mDetailListAdapter);
        // 设置item的上下间距
        mRvDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
            }
        });

    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        mDetailListAdapter.updateData(tracks);
    }

    @Override
    public void onAlbumLoaded(Album album) {
        // 获取专辑的详情内容
        mAlbumDetailPresenter.getAlbumDetail(album.getId(), mCurrentPage);

        mTvAlbumTitle.setText(album.getAlbumTitle());
        mTvAlbumAuthor.setText(album.getAnnouncer().getNickname());
        if (mIvLargeCover != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(album.getCoverUrlLarge())
                    .into(new BitmapImageViewTarget(mIvLargeCover){
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mIvLargeCover.setImageBitmap(resource);
                            ImageBlur.makeBlur(mIvLargeCover, DetailActivity.this);
                        }
                    });
        }
        Glide.with(this).load(album.getCoverUrlLarge()).into(mIvSmallCover);
    }
}
