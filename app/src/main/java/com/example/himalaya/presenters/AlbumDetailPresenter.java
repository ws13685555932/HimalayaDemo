package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static AlbumDetailPresenter mInstance = null;
    private Album mTargetAlbum;

    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private AlbumDetailPresenter() {

    }

    public static AlbumDetailPresenter getInstance() {
        if (mInstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (mInstance == null) {
                    mInstance = new AlbumDetailPresenter();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {

    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback callback) {
        if (mCallbacks != null && ! mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mTargetAlbum != null) {
                callback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }

    public void setTargetAlbum(Album targetAlbum) {
        this.mTargetAlbum = targetAlbum;
    }
}
