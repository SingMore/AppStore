package com.example.appstore.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SingMore on 2017/2/7.
 * 常用的抽取BaseFragment的方式
 */

public abstract class BaseFragmentCommon extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    public void init() {

    }

    /**
     * 初始化View，而且是必须实现，但是不知道具体实现。定义成抽象方法，供子类具体实现
     * @return
     */
    public abstract View initView();

    public void initData() {

    }

    public void initListener() {

    }

}
