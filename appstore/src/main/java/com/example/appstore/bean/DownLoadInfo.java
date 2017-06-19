package com.example.appstore.bean;

import com.example.appstore.manager.DownloadManager;

/**
 * Created by SingMore on 2017/2/13.
 */

public class DownLoadInfo {

    public String savePath; //应用安装包保存路径
    public String downloadUrl;//应用apk下载路径
    public int state = DownloadManager.STATE_UNDOWNLOAD; //默认状态就是未下载
    public String packageName;//应用包名
    public long max;//应用文件大小
    public long curProgress;//当前的下载进度
    public Runnable	task;//下载的任务
}
