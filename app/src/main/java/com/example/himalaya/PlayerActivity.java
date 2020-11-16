package com.example.himalaya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IPlayerViewCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerViewCallback {

    public static final String TAG = "PlayerActivity";

    private ImageView mBtnControl;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // 测试播放
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);

        initView();
        initEvent();
        startPlay();
    }

    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }

    private void initEvent() {
        mBtnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "Play button clicked! current isPlaying -->" + mPlayerPresenter.isPlay());
                // 如果现在的状态是播放，那么暂停
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                } else {
                    // 如果是非播放的，那么让播放器播放
                    mPlayerPresenter.play();

                }

            }
        });
    }

    private void initView() {
        mBtnControl = findViewById(R.id.btn_play_or_pause);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    @Override
    public void onPlayStart() {
        // 开始播放，修改UI成暂停的按钮
        if (mBtnControl != null) {
            mBtnControl.setImageResource(R.mipmap.stop);
        }

    }

    @Override
    public void onPlayPause() {
        if (mBtnControl != null) {
            mBtnControl.setImageResource(R.mipmap.play);
        }

    }

    @Override
    public void onPlayStop() {
        if (mBtnControl != null) {
            mBtnControl.setImageResource(R.mipmap.play);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onNextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> tracks) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void onProgressChange(long currentProgress, long total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }
}
