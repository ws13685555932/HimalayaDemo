package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {

    void pull2RefreshMore();

    void loadMore();

    // 获取专辑详情
    void getAlbumDetail(long albumId, int page);

}
