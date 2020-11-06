package com.example.himalaya.interfaces;

public interface IRecommendPresenter {

    void getRecommendList();

    void registerViewCallback(IRecommendViewCallback callback);

    void unregisterViewCallback(IRecommendViewCallback callback);
}
