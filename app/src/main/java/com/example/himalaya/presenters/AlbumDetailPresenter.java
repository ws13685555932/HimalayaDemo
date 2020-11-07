package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.utils.Constant;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG = "AlbumDetailPresenter";
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
    public void getAlbumDetail(long albumId, int page) {
        // 根据页码和专辑拿到数据
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.PAGE_SIZE, Constant.SIZE_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                LogUtil.d(TAG, "");
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    handleAblumDetailResult(tracks);
                    LogUtil.d(TAG, "track size --> " + tracks.size());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "error message -->" + s);
            }
        });
    }

    private void handleAblumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onDetailListLoaded(tracks);
        }
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
