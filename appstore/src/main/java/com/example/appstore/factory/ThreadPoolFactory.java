package com.example.appstore.factory;

import com.example.appstore.manager.ThreadPoolProxy;

/**
 * Created by SingMore on 2017/2/8.
 * 线程池工厂，创建普通线程池，下载线程池
 */

public class ThreadPoolFactory {
    static ThreadPoolProxy mNormalThreadPool;
    static ThreadPoolProxy mDownThreadPool;

    /**
     * 创建普通线程池（5，5，3000）
     * @return
     */
    public static ThreadPoolProxy getNormalThreadPool() {
        if(mNormalThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if(mNormalThreadPool == null) {
                    mNormalThreadPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalThreadPool;
    }

    /**
     * 创建下载线程池（3，3，3000）
     * @return
     */
    public static ThreadPoolProxy getDownThreadPool() {
        if(mDownThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if(mDownThreadPool == null) {
                    mDownThreadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownThreadPool;
    }
}
