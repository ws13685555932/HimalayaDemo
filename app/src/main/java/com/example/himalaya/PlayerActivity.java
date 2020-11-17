package com.example.himalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.himalaya.adapters.PlayerTrackPageAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IPlayerViewCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerViewCallback {

    public static final String TAG = "PlayerActivity";

    private ImageView mBtnControl;
    private PlayerPresenter mPlayerPresenter;

    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private TextView mTvTrackDuration;
    private TextView mTvCurrPosition;
    private SeekBar mSbProgress;
    private int mCurrentProgress;
    private boolean mIsUserTouchProgressbar = false;
    private ImageView mBtnPlayPre;
    private ImageView mBtnPlayNext;
    private TextView mTvTrackTitle;
    private ViewPager mVpTrackCover;
    private PlayerTrackPageAdapter mPlayerTrackPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initView();
        registerPresenter();

        initEvent();
        startPlay();
    }

    private void registerPresenter() {
        // 测试播放
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        mPlayerPresenter.getPlayList();
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

        mSbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressbar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 手离开拖动进度条时更新
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });

        mBtnPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放下一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });

        mBtnPlayPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放前一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });
    }

    private void initView() {
        mBtnControl = findViewById(R.id.btn_play_or_pause);
        mTvTrackDuration = findViewById(R.id.tv_track_duration);
        mTvCurrPosition = findViewById(R.id.tv_current_position);
        mSbProgress = findViewById(R.id.sb_track_seek_bar);
        mBtnPlayPre = findViewById(R.id.btn_play_pre);
        mBtnPlayNext = findViewById(R.id.btn_play_next);
        mTvTrackTitle = findViewById(R.id.tv_track_title);
        mVpTrackCover = findViewById(R.id.vp_track_cover);
        // 创建适配器
        mPlayerTrackPageAdapter = new PlayerTrackPageAdapter();
        // 配置适配器
        mVpTrackCover.setAdapter(mPlayerTrackPageAdapter);
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
        if (mPlayerTrackPageAdapter != null) {
            // 把数据设置到适配器里
            mPlayerTrackPageAdapter.setData(tracks);
        }

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {
        mSbProgress.setMax(total);
        // 更新播放进度，更新进度条
        String totalDuration;
        String currPosition;
        if (total > 1000 * 60 * 60) {
            totalDuration = mHourFormat.format(total);
            currPosition = mHourFormat.format(currentProgress);
        } else {
            totalDuration = mMinFormat.format(total);
            currPosition = mMinFormat.format(currentProgress);
        }

        if (mTvTrackDuration != null) {
            mTvTrackDuration.setText(totalDuration);
        }
        if (mTvCurrPosition != null) {
            mTvCurrPosition.setText(currPosition);
        }

        if (!mIsUserTouchProgressbar) {
            // 更新进度条，计算当前进度
            mSbProgress.setProgress(currentProgress);
        }


    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track) {
        //设置节目标题
        if (mTvTrackTitle != null) {
            mTvTrackTitle.setText(track.getTrackTitle());
        }
        // 当前节目而改变的时候，我们就获取当前节目中位置

    }
}
