package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerViewCallback {

    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 播放暂停
     */
    void onPlayPause();

    void onPlayStop();

    void onPlayError();

    void onNextPlay(Track track);

    void onPrePlay(Track track);

    void onListLoaded(List<Track> tracks);

    void onPlayModeChange(XmPlayListControl.PlayMode mode);

    void onProgressChange(int currentProgress, int total);

    void onAdLoading();

    void onAdFinished();

    /**
     * 更新当前节目信息
     * @param track 节目信息
     */
    void onTrackUpdate(Track track);

}
