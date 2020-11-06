package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IRecommendPresenter;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.utils.Constant;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest.TAG;

public class RecommendPresenter implements IRecommendPresenter {

    private static RecommendPresenter sInstance = null;
    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();

    private RecommendPresenter() {

    }

    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取推荐内容
     */
    @Override
    public void getRecommendList() {
        // 封装参数
        Map<String, String> map = new HashMap<>();
        // 参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constant.RECOMMEND_SIZE + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                // 数据获取成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        handleRecommendResult(albumList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                // 数据获取出错
                LogUtil.d(TAG, "error code -->" + i);
                LogUtil.d(TAG, "error message -->" + s);

            }
        });
    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !this.mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }


    private void handleRecommendResult(List<Album> albumList) {
        for (IRecommendViewCallback callback :
                mCallbacks) {
            callback.onRecommendListLoaded(albumList);
        }
    }
}
