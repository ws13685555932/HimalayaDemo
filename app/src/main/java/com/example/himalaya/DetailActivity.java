package com.example.himalaya;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.views.RoundRectImageView;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements IAlbumDetailViewCallback {

    private ImageView mIvLargeCover;
    private RoundRectImageView mIvSmallCover;
    private TextView mTvAlbumTitle;
    private TextView mTvAlbumAuthor;
    private AlbumDetailPresenter mInstance;
    private AlbumDetailPresenter mAlbumDetailPresenter;

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
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {

        mTvAlbumTitle.setText(album.getAlbumTitle());
        mTvAlbumAuthor.setText(album.getAnnouncer().getNickname());
        Glide.with(this).load(album.getCoverUrlLarge()).into(mIvLargeCover);
        Glide.with(this).load(album.getCoverUrlLarge()).into(mIvSmallCover);
    }
}
