package com.example.himalaya.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.himalaya.R;
import com.example.himalaya.base.BaseApplication;

public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mNetworkErrorView;
    private View mEmptyDataView;
    private OnRetryListener mOnRetryListener = null;

    public enum UIState {
        LOADING, SUCCESS, NETWORK_ERROR, EMPTY_DATA, NONE

    }

    public UIState mCurrentStatus = UIState.NONE;

    public UILoader(@NonNull Context context) {
        super(context, null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);

        init();
    }

    public void updateStatus(UIState state) {
        mCurrentStatus = state;
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    // 初始化UI
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        // 加载中
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        // 根据状态设置是否可见
        mLoadingView.setVisibility(mCurrentStatus == UIState.LOADING ? VISIBLE : GONE);

        // 成功
        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        // 根据状态设置是否可见
        mSuccessView.setVisibility(mCurrentStatus == UIState.SUCCESS ? VISIBLE : GONE);

        // 网络出错
        if (mNetworkErrorView == null) {
            mNetworkErrorView = getNetworkErrorView();
            addView(mNetworkErrorView);
        }
        // 根据状态设置是否可见
        mNetworkErrorView.setVisibility(mCurrentStatus == UIState.NETWORK_ERROR ? VISIBLE : GONE);

        // 数据为空
        if (mEmptyDataView == null) {
            mEmptyDataView = getEmptyDataView();
            addView(mEmptyDataView);
        }
        // 根据状态设置是否可见
        mEmptyDataView.setVisibility(mCurrentStatus == UIState.EMPTY_DATA ? VISIBLE : GONE);

    }

    private View getEmptyDataView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
    }

    protected View getNetworkErrorView() {
        View networkErrorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view, this, false);
        networkErrorView.findViewById(R.id.ll_network_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重新获取数据
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetryClick();
                }
            }
        });
        return networkErrorView;
    }

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view, this, false);
    }

    protected abstract View getSuccessView(ViewGroup container);

    public interface OnRetryListener {
        void onRetryClick();
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

}
