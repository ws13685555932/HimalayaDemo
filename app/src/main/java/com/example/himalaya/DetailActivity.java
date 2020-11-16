package com.example.himalaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.himalaya.adapters.DetailListAdapter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.RoundRectImageView;
import com.example.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements IAlbumDetailViewCallback, UILoader.OnRetryListener, DetailListAdapter.ItemClickedListener {

    public static final String TAG = "DetailActivity";

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
    private FrameLayout mFlDetailContainer;
    private UILoader mUiLoader;
    private long mCurrentId = -1;

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
        mFlDetailContainer = findViewById(R.id.fl_detail_container);

        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mUiLoader.setOnRetryListener(DetailActivity.this);
            mFlDetailContainer.removeAllViews();
            mFlDetailContainer.addView(mUiLoader);
        }

        mIvLargeCover = findViewById(R.id.iv_large_cover);
        mIvSmallCover = findViewById(R.id.iv_small_cover);
        mTvAlbumTitle = findViewById(R.id.tv_album_title);
        mTvAlbumAuthor = findViewById(R.id.tv_album_author);


    }

    private View createSuccessView(ViewGroup container) {
        View successView = LayoutInflater.from(this).inflate(R.layout.view_detail, container, false);

        mRvDetailList = successView.findViewById(R.id.rv_detail_list);
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
        mDetailListAdapter.setOnItemClickedListener(this);
        return successView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

        LogUtil.d(TAG, "onDetailListLoaded, track size -->" +tracks.size());
        if (tracks != null && tracks.size() != 0) {
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIState.SUCCESS);
            }
            LogUtil.d(TAG, "onDetailListLoaded");
            if (mDetailListAdapter != null) {
                mDetailListAdapter.updateData(tracks);
            }
        } else {
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIState.EMPTY_DATA);
            }
        }
    }

    @Override
    public void onAlbumLoaded(Album album) {
//        if (mUiLoader != null) {
//            mUiLoader.updateStatus(UILoader.UIState.LOADING);
//        }
        // 获取专辑的详情内容
        mCurrentId = album.getId();
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail(album.getId(), mCurrentPage);
        }

        mTvAlbumTitle.setText(album.getAlbumTitle());
        mTvAlbumAuthor.setText(album.getAnnouncer().getNickname());
        if (mIvLargeCover != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(album.getCoverUrlLarge())
                    .into(new BitmapImageViewTarget(mIvLargeCover) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mIvLargeCover.setImageBitmap(resource);
                            ImageBlur.makeBlur(mIvLargeCover, DetailActivity.this);
                        }
                    });
        }
        Glide.with(this).load(album.getCoverUrlLarge()).into(mIvSmallCover);
    }

    @Override
    public void onNetworkError(int errorCode, String errorMessage) {
        // 请求发生错误，显示网络异常状态
        mUiLoader.updateStatus(UILoader.UIState.NETWORK_ERROR);
    }

    @Override
    public void onRetryClick() {
        // 用户网络不佳的时候，点击了重新加载
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail(mCurrentId, mCurrentPage);
        }
    }

    @Override
    public void onItemClick(int position, List<Track> detailData) {
        // 设置播放器数据
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayList(detailData, position);

        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }
}
