package com.example.himalaya.presenters;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IPlayerPresenter;
import com.example.himalaya.interfaces.IPlayerViewCallback;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {
    public static final String TAG = "PlayerPresenter";

    private final XmPlayerManager mPlayerManager;
    private boolean isPlayListSet = false;
    private List<IPlayerViewCallback> mCallbacks = new ArrayList<>();
    private Track mCurrTrack;

    private PlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getContext());
        // 广告相关的接口
        mPlayerManager.addAdsStatusListener(this);

        // 注册播放器相关的接口
        mPlayerManager.addPlayerStatusListener(this);

    }

    private static PlayerPresenter sPlayerPresenter = null;

    public static PlayerPresenter getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;
    }

    public void setPlayList(List<Track> playList, int index) {

        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(playList, index);
            isPlayListSet = true;
            mCurrTrack= playList.get(index);
        } else {
            LogUtil.d(TAG, "mPlayerManager is not null");
        }


    }

    @Override
    public void play() {
        if (isPlayListSet && mPlayerManager != null) {
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playNext() {
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }
    }

    @Override
    public void playPre() {
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {
        if (mPlayerManager != null) {
            List<Track> playList = mPlayerManager.getPlayList();
            for (IPlayerViewCallback callback : mCallbacks) {
                callback.onListLoaded(playList);
            }
        }
    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {
        // 更新播放器的进度
        mPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlay() {
        // 返回当前是否正在播放
        return mPlayerManager.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayerViewCallback callback) {
        callback.onTrackUpdate(mCurrTrack);
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerViewCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    @Override
    public void onStartGetAdsInfo() {

    }

    // ==================广告相关的回调 start==================
    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(TAG, "onGetAdsInfo");
    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(TAG, "onAdsStartBuffering");
    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(TAG, "onAdsStopBuffering");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        LogUtil.d(TAG, "onStartPlayAds");
    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(TAG, "onCompletePlayAds");
    }

    @Override
    public void onError(int what, int extra) {
        LogUtil.d(TAG, "onError what => " + what + "extra => " + extra);
    }
    // ==================广告相关的回调 end==================

    // ==================播放器相关的回调 start==================
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG, "onPlayStart...");
        for (IPlayerViewCallback callback : mCallbacks) {
            callback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG, "onPlayPause...");
        for (IPlayerViewCallback callback : mCallbacks) {
            callback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG, "onPlayStop...");
        for (IPlayerViewCallback callback : mCallbacks) {
            callback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG, "onSoundPlayComplete...");
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG, "onSoundPrepared...");
    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        LogUtil.d(TAG, "onSoundSwitch...");
        if (lastModel != null) {
            LogUtil.d(TAG, "last model --> " + lastModel.getKind());
        }
        LogUtil.d(TAG, "cur model --> " + curModel.getKind());
        // curModel 代表的是当前播放的内容
        // 通过getKind()方法获取它是什么类型，track表示是track类型
        if (curModel instanceof Track) {
            Track currentTrack = (Track) curModel;
            mCurrTrack = currentTrack;
            LogUtil.d(TAG, "track title --> " + currentTrack.getTrackTitle());
            for (IPlayerViewCallback callback : mCallbacks) {
                callback.onTrackUpdate(mCurrTrack);
            }
        }
    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG, "onBufferingStart...");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG, "onBufferingStop...");
    }

    @Override
    public void onBufferProgress(int progress) {
        LogUtil.d(TAG, "onBufferProgress...");
    }

    @Override
    public void onPlayProgress(int currentPos, int duration) {
        // 单位是毫秒
        for (IPlayerViewCallback callback : mCallbacks) {
            callback.onProgressChange(currentPos, duration);
        }
        LogUtil.d(TAG, "onPlayProgress...");

    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG, "onError e --->" + e);
        return false;
    }
    // ==================播放器相关的回调 end==================
}
