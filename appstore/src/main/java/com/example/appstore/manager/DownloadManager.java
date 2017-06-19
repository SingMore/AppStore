package com.example.appstore.manager;

import com.example.appstore.bean.AppInfo;
import com.example.appstore.bean.DownLoadInfo;
import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.utils.AppUtils;
import com.example.appstore.utils.FileUtils;
import com.example.appstore.utils.IOUtils;
import com.example.appstore.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by SingMore on 2017/2/13.
 */

public class DownloadManager {

    public static final int	STATE_UNDOWNLOAD		= 0;	// 未下载
    public static final int	STATE_DOWNLOADING		= 1;	// 下载中
    public static final int	STATE_PAUSEDOWNLOAD		= 2;	// 暂停下载
    public static final int	STATE_WAITINGDOWNLOAD	= 3;	// 等待下载
    public static final int	STATE_DOWNLOADFAILED	= 4;	// 下载失败
    public static final int	STATE_DOWNLOADED		= 5;	// 下载完成
    public static final int	STATE_INSTALLED			= 6;	// 已安装

    public static DownloadManager instance;
    // 记录正在下载的一些downLoadInfo
    public Map<String, DownLoadInfo> mDownLoadInfoMaps = new HashMap<String, DownLoadInfo>();

    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }


    /**用户点击了下载按钮*/
    public void downLoad(DownLoadInfo info) {
        mDownLoadInfoMaps.put(info.packageName, info);

        if(mDownLoadInfoMaps.size() <= 3) {
            //如果当前一起下载的应用数不超过3个，则是下载中状态
            info.state = STATE_DOWNLOADING;
        }else {
            //当前状态: 等待状态
            info.state = STATE_WAITINGDOWNLOAD;
        }
        notifyObservers(info);

        // 得到线程池,执行任务
        DownLoadTask task = new DownLoadTask(info);
        info.task = task;// downInfo身上的task赋值
        ThreadPoolFactory.getDownThreadPool().executeTask(task);
    }

    /**暂停下载*/
    public void pause(DownLoadInfo info) {
        //当前状态: 暂停
        info.state = STATE_PAUSEDOWNLOAD;
        notifyObservers(info);
    }

    /**取消下载*/
    public void cancel(DownLoadInfo info) {
        Runnable task = info.task;
        // 找到线程池,移除任务
        ThreadPoolFactory.getDownThreadPool().removeTask(task);

        //当前状态: 未下载
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);
    }


    class DownLoadTask implements Runnable {
        DownLoadInfo	mInfo;

        public DownLoadTask(DownLoadInfo info) {
            super();
            mInfo = info;
        }

        @Override
        public void run() {
            // 正在发起网络请求下载apk
            try {
				//当前状态: 下载中
                mInfo.state = STATE_DOWNLOADING;
                notifyObservers(mInfo);

                long initRange = 0;
                File saveApk = new File(mInfo.savePath);
                if (saveApk.exists()) {
                    initRange = saveApk.length();// 未下载完成的apk已有的长度
                }
                mInfo.curProgress = initRange;// 处理初始进度
                // 下载地址
                String url = mInfo.downloadUrl;
                HttpUtils httpUtils = new HttpUtils();
                // 相关参数
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("range", initRange + "");// ①处理请求
                ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
                if (responseStream.getStatusCode() == 200) {
                    InputStream in = null;
                    FileOutputStream out = null;
                    boolean isPause = false;
                    try {
                        in = responseStream.getBaseStream();
                        File saveFile = new File(mInfo.savePath);
                        out = new FileOutputStream(saveFile, true);// ②处理文件的写入

                        byte[] buffer = new byte[1024];
                        int len = -1;

                        while ((len = in.read(buffer)) != -1) {
                            if (mInfo.state == STATE_PAUSEDOWNLOAD) {
                                isPause = true;
                                break;
                            }
                            out.write(buffer, 0, len);
                            mInfo.curProgress += len;
                            if(mInfo.curProgress > mInfo.max) {
                                mInfo.curProgress = mInfo.max;
                            }
							//当前状态: 下载中
                            mInfo.state = STATE_DOWNLOADING;
                            notifyObservers(mInfo);
                        }

                        if (isPause) {// 用户暂停了下载走到这里来了
							//当前状态: 暂停
                            mInfo.state = STATE_PAUSEDOWNLOAD;
                            notifyObservers(mInfo);
                        } else {// 下载完成走到这里来
							//当前状态: 下载完成
                            mInfo.state = STATE_DOWNLOADED;
                            notifyObservers(mInfo);
                        }
                    } finally {
                        IOUtils.close(out);
                        IOUtils.close(in);
                    }

                } else {
					//当前状态: 下载失败
                    mInfo.state = STATE_DOWNLOADFAILED;
                    notifyObservers(mInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
				//当前状态: 下载失败
                mInfo.state = STATE_DOWNLOADFAILED;
                notifyObservers(mInfo);
            }

        }
    }

    /**
     * @des 暴露当前状态,也就是需要提供downLoadInfo
     * @call 外界需要知道最新的state的时候
     */
    public DownLoadInfo getDownLoadInfo(AppInfo appInfo) {
        // 已安装
        if (AppUtils.isInstalled(UIUtils.getContext(), appInfo.packageName)) {
            DownLoadInfo info = generateDownLoadInfo(appInfo);
            info.state = STATE_INSTALLED;// 已安装
            return info;
        }
        // 下载完成
        DownLoadInfo info = generateDownLoadInfo(appInfo);
        File saveApk = new File(info.savePath);
        if (saveApk.exists()) {// 如果存在我们的下载目录里面
            if (saveApk.length() == appInfo.size) {
                info.state = STATE_DOWNLOADED;// 下载完成
                return info;
            }
        }
        //下载中、暂停下载、等待下载、下载失败
        DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(appInfo.packageName);
        if (downLoadInfo != null) {
            return downLoadInfo;
        }

        // 未下载
        DownLoadInfo tempInfo = generateDownLoadInfo(appInfo);
        tempInfo.state = STATE_UNDOWNLOAD;// 未下载
        return tempInfo;
    }

    /**
     * 根据AppInfo生成一个DownLoadInfo,进行一些常规的赋值,也就是对一些常规属性赋值(除了state之外的属性)
     */
    public DownLoadInfo generateDownLoadInfo(AppInfo appInfo) {
        String dir = FileUtils.getDir("download");// sdcard/android/data/包名/download
        File file = new File(dir, appInfo.packageName + ".apk");
        // 保存路径  sdcard/android/data/包名/download/com.changba.apk
        String savePath = file.getAbsolutePath();

        // 初始化一个downLoadInfo
        DownLoadInfo info = new DownLoadInfo();
        // 相关赋值
        info.savePath = savePath;
        info.downloadUrl = appInfo.apk.getFileUrl();
        info.packageName = appInfo.packageName;
        info.max = appInfo.size;
        info.curProgress = 0;
        return info;
    }


    /*=============== 自定义观察者设计模式  begin ===============*/
    public interface DownLoadObserver {
        void onDownLoadInfoChange(DownLoadInfo info);
    }

    List<DownLoadObserver>	downLoadObservers	= new LinkedList<DownLoadObserver>();

    /**添加观察者*/
    public void addObserver(DownLoadObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!downLoadObservers.contains(observer))
                downLoadObservers.add(observer);
        }
    }

    /**删除观察者*/
    public synchronized void deleteObserver(DownLoadObserver observer) {
        downLoadObservers.remove(observer);
    }

    /**通知观察者数据改变*/
    public void notifyObservers(DownLoadInfo info) {
        for (DownLoadObserver observer : downLoadObservers) {
            observer.onDownLoadInfoChange(info);
        }
    }

	/*=============== 自定义观察者设计模式  end ===============*/
}
