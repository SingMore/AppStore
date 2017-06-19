package com.example.appstore.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by SingMore on 2017/2/8.
 * 创建线程池，执行任务，提交任务，移除任务
 */

public class ThreadPoolProxy {

    //  ThreadPoolExecutor  线程池的实现类
    private ThreadPoolExecutor mThreadPoolExecutor;//只需创建一次，单例模式
    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime;

    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
        mKeepAliveTime = keepAliveTime;
        getThreadPoolExecutor();
    }

    /**
     * ThreadPoolExecutor(
     * int corePoolSize, 核心的线程数
     * int maximumPoolSize,最大线程数
     * long keepAliveTime,保持时间
     * TimeUnit unit,时间单位
     * BlockingQueue<Runnable> workQueue,缓存队列/阻塞队列
     * ThreadFactory threadFactory,线程工厂
     * RejectedExecutionHandler handler 异常捕获器
     * )
     */
    public ThreadPoolExecutor getThreadPoolExecutor() {
        //双重检查加锁
        if(mThreadPoolExecutor == null) {
            synchronized (ThreadPoolProxy.class) {
                if(mThreadPoolExecutor == null) {
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();//无界队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();//丢弃任务并抛出异常
                    mThreadPoolExecutor = new ThreadPoolExecutor(
                            mCorePoolSize,
                            mMaximumPoolSize,
                            mKeepAliveTime,
                            unit,
                            workQueue,
                            threadFactory,
                            handler
                    );
                }
            }
        }
        return mThreadPoolExecutor;
    }

    /**
     * 执行任务
     * @param runnable
     */
    public void executeTask(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    /**
     * 提交任务,可根据返回值Future判断任务是否成功完成
     * @param runnable
     */
    public Future<?> submitTask(Runnable runnable) {
        return mThreadPoolExecutor.submit(runnable);
    }

    /**
     * 移除任务
     * @param runnable
     */
    public void removeTask(Runnable runnable) {
        mThreadPoolExecutor.remove(runnable);
    }
}
