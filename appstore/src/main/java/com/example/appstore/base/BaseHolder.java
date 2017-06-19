package com.example.appstore.base;

import android.view.View;

/**
 * Created by SingMore on 2017/2/8.
 */

public abstract class BaseHolder<T> {
    public View mHolderView;
    private T mData;

    public BaseHolder() {
        mHolderView = initHolderView();
        //绑定Tag
        mHolderView.setTag(this);
    }

    public View getHolderView() {
        return mHolderView;
    }

    public void setDataAndShow(T result) {
        //保存数据
        mData = result;
        //刷新显示
        refreshShow(result);
    }

    public abstract View initHolderView();

    public abstract void refreshShow(T result);
}
