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

    void onProgressChange(long currentProgress, long total);

    void onAdLoading();

    void onAdFinished();


}
