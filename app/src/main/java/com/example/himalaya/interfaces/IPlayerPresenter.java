package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresenter<IPlayerViewCallback>{

    void play();

    void pause();

    void stop();

    void playNext();

    void playPre();

    void switchPlayMode(XmPlayListControl.PlayMode mode);

    void getPlayList();

    /**
     * 根据节目在列表中的位置进行播放
     * @param index 节目在列表中的位置
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     * @param progress 进度
     */

    void seekTo(int progress);

    /**
     * 判断播放器是否正在播放
     * @return 是否在播放
     */
    boolean isPlay();
}
