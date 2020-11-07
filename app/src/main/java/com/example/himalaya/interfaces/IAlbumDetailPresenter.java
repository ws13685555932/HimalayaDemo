package com.example.himalaya.interfaces;

public interface IAlbumDetailPresenter {

    void pull2RefreshMore();

    void loadMore();

    // 获取专辑详情
    void getAlbumDetail(long albumId, int page);

    void registerViewCallback(IAlbumDetailViewCallback callback);

    void unregisterViewCallback(IAlbumDetailViewCallback callback);
}
