package com.example.himalaya.base;

public interface IBasePresenter<T> {

    void registerViewCallback(T t);

    void unRegisterViewCallback(T t);
}
