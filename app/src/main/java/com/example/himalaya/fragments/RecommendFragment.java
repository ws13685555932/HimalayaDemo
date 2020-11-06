package com.example.himalaya.fragments;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.example.himalaya.adapters.RecommendListAdapater;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.presenters.RecommendPresenter;
import com.example.himalaya.utils.Constant;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRvRecommend;
    private RecommendListAdapater mRecommendListAdapater;
    private RecommendPresenter mRecommendPresenter;

    @Override
    protected View onSubViewCreated(LayoutInflater layoutInflater, ViewGroup container) {
        // View加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false);

        mRvRecommend = mRootView.findViewById(R.id.rv_recommend);
        // 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvRecommend.setLayoutManager(linearLayoutManager);
        mRvRecommend.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        // 设置适配器
        mRecommendListAdapater = new RecommendListAdapater();
        mRvRecommend.setAdapter(mRecommendListAdapater);

        // 拿到显示的数据
        mRecommendPresenter = RecommendPresenter.getInstance();
        mRecommendPresenter.registerViewCallback(this);
        mRecommendPresenter.getRecommendList();

        // 返回View
        return mRootView;
    }


    @Override
    public void onRecommendListLoaded(List<Album> result) {
        mRecommendListAdapater.updataData(result);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unregisterViewCallback(this);
        }
    }
}
