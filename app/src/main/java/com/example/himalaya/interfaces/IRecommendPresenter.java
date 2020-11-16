package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;

public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {

    void getRecommendList();
}
