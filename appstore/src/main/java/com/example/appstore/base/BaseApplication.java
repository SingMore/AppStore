package com.example.appstore.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.appstore.conf.Constants;
import com.example.appstore.utils.UIUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by SingMore on 2016/12/6.
 * 程序的入口类,定义一个全局的盒子，里面放置的对象，属性，方法都是全局可以调用
 */

public class BaseApplication extends Application {

    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadId;
    private static Looper mMainThreadLooper;
    private static Handler mMainHandler;

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Handler getMainHandler() {
        return mMainHandler;
    }

    /**
     * 程序的入口
     */
    @Override
    public void onCreate() {
        //上下文
        mContext = getApplicationContext();

        //主线程
        mMainThread = Thread.currentThread();

        /**
         * myPid--->processId
         * myUid--->UserId
         * myTid--->ThreadId
         */
        //主线程Id
        mMainThreadId = android.os.Process.myPid();

        //主线程Looper
        mMainThreadLooper = getMainLooper();

        //主线程Handler
        mMainHandler = new Handler();

        //初始化SDK
        initSDK();

        super.onCreate();
    }

    /**
     * 初始化集成的BmobSDK
     */
    private void initSDK() {
        //初始化BmobSDK
        //第一：默认初始化
        //Bmob.initialize(UIUtils.getContext(), Constants.BMOB_APPID);

        /**
         *   第二：设置BmobConfig,
         *   允许设置请求超时时间、
         *   文件分片上传时每片的大小、
         *   文件的过期时间(单位为秒)，自v3.4.7版本开始
         */
        BmobConfig config = new BmobConfig.Builder(UIUtils.getContext())
                //设置appkey
                .setApplicationId(Constants.BMOB_APPID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(15)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(3000)
                .build();
        Bmob.initialize(config);

        //初始化讯飞语音SDK
        SpeechUtility.createUtility(UIUtils.getContext(), SpeechConstant.APPID + "=58034434");
    }
}
