package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendViewCallback {

    void onRecommendListLoaded(List<Album> result);

    // 网络不佳
    void onNetworkError();

    // 数据为空
    void onEmptyData();

    // 正在加载
    void onLoading();
}
