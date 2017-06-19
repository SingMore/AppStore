package com.example.appstore.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SingMore on 2017/2/8.
 * 常规BaseAdapter的抽取方法
 */

public abstract class BaseAdapterCommon<T> extends BaseAdapter {
    private List<T> dataList = new ArrayList<T>();

    public BaseAdapterCommon(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if(dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(dataList != null) {
            return dataList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
