package com.example.appstore.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.appstore.base.BaseApplication;

/**
 * Created by SingMore on 2016/12/6.
 */

public class UIUtils {
    /*** 得到上下文*/
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**得到Resources对象*/
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**得到String.xml中的字符串*/
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**得到String.xml中的字符串,带占位符*/
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**得到String.xml中的字符串数组*/
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**得到colors.xml中的颜色*/
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**得到dimens.xml中的尺寸*/
    public static float getDimen(int dimenId) {
        return getResource().getDimension(dimenId);
    }

    /**得到drawable中的图片*/
    public static Drawable getDrawable(int drawableId) {
        return getResource().getDrawable(drawableId);
    }

    /**填充布局文件*/
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    /**dp转换成px*/
    public static int dp2px(int dp){
        float density = getResource().getDisplayMetrics().density;
        return (int)(dp * density + 0.5);
    }

    /**px转换成dp*/
    public static int px2dp(int px){
        float density = getResource().getDisplayMetrics().density;
        return (int)(px / density + 0.5);
    }

    /**得到应用程序的包名*/
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**得到Handler*/
    public static Handler getHandler() {
        return BaseApplication.getMainHandler();
    }

    /**得到主线程中的Looper*/
    public static Looper getLooper() {
        return BaseApplication.getMainThreadLooper();
    }

    /**得到主线程*/
    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    /**得到主线程Id*/
    public static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    //判断当前是否运行在主线程中
    public static boolean isRunInMainThread(){
        return getMainThreadId() == android.os.Process.myTid();
    }

    //使其运行在主线程中
    public static void runInMainThread(Runnable runnable){
        if(isRunInMainThread()){
            runnable.run();
        }else{
            getHandler().post(runnable);
        }
    }

    //延迟执行任务
    public static void postTaskDelay(Runnable task, int delayMillis) {
        getHandler().postDelayed(task, delayMillis);
    }

    //移除任务
    public static void removeTask(Runnable task) {
        getHandler().removeCallbacks(task);
    }

}
