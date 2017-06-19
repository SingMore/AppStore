package com.example.appstore.utils;

import android.view.View;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by SingMore on 2017/2/9.
 */

public class BitmapHelper {
    public static BitmapUtils mBitmapUtils;
    static {//BitmapUtils只创建一次
        mBitmapUtils = new BitmapUtils(UIUtils.getContext());
    }

    public static <T extends View> void display(T container, String uri) {
        mBitmapUtils.display(container, uri);
    }
}
