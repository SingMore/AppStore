package com.example.appstore.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appstore.R;
import com.example.appstore.base.BaseHolder;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.bean.DownLoadInfo;
import com.example.appstore.manager.DownloadManager;
import com.example.appstore.manager.DownloadManager.DownLoadObserver;
import com.example.appstore.utils.AppUtils;
import com.example.appstore.utils.BitmapHelper;
import com.example.appstore.utils.StringUtils;
import com.example.appstore.utils.UIUtils;
import com.example.appstore.views.CircleProgressView;

import java.io.File;

/**
 * Created by SingMore on 2017/3/13.
 */

public class NormalHolder extends BaseHolder<AppInfo> implements DownLoadObserver {
    ImageView appIcon;
    TextView appName;
    TextView appSize;
    TextView appDownloadNum;
    RatingBar appStars;
    CircleProgressView cpv_item;
    TextView appDes;

    private AppInfo mData;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
        appIcon = (ImageView) view.findViewById(R.id.iv_item_appicon);
        appName = (TextView) view.findViewById(R.id.tv_item_appname);
        appSize = (TextView) view.findViewById(R.id.tv_item_appsize);
        appDownloadNum = (TextView) view.findViewById(R.id.tv_item_appdownloadnum);
        appStars = (RatingBar) view.findViewById(R.id.rb_item_appstars);
        cpv_item = (CircleProgressView) view.findViewById(R.id.cpv_item);
        appDes = (TextView) view.findViewById(R.id.tv_item_appdes);

        cpv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processClick();
            }
        });
        return view;
    }

    @Override
    public void refreshShow(AppInfo result) {
        mData = result;

        //设置数据
        appIcon.setImageResource(R.mipmap.ic_default);//设置默认图标
        BitmapHelper.display(appIcon, result.getIcon().getFileUrl());

        appName.setText(result.getName());
        appSize.setText("大小：" + StringUtils.formatFileSize(result.getSize()));
        appDownloadNum.setText("下载量：" + result.getDownloadCount() + "万+");
        appStars.setRating(result.getStars());
        appDes.setText(result.getDes());


        //根据不同的状态给用户提示
        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(result);
        refreshCircleProgressViewUI(info);
    }

    /**
     * 状态(编程记录) |  给用户的提示(ui展现)
     * -------------|----------------------
     * 未下载			|下载
     * 下载中			|显示进度条
     * 暂停下载		|继续下载
     * 等待下载		|等待中...
     * 下载失败 		|重试
     * 下载完成 		|安装
     * 已安装 		|打开
     */
    public void refreshCircleProgressViewUI(DownLoadInfo info) {
        switch (info.state) {
            case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                cpv_item.setNote("下载");
                cpv_item.setIcon(R.drawable.ic_download);
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                cpv_item.setProgressEnable(true);
                cpv_item.setMax(info.max);
                cpv_item.setProgress(info.curProgress);
                int progress = (int) (info.curProgress * 100.f / info.max + .5f);
                cpv_item.setNote(progress + "%");
                cpv_item.setIcon(R.drawable.ic_pause);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                cpv_item.setNote("继续下载");
                cpv_item.setIcon(R.drawable.ic_resume);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                cpv_item.setNote("等待中...");
                cpv_item.setIcon(R.drawable.ic_pause);
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                cpv_item.setNote("重试");
                cpv_item.setIcon(R.drawable.ic_redownload);
                break;
            case DownloadManager.STATE_DOWNLOADED:// 下载完成
                cpv_item.setProgressEnable(false);
                cpv_item.setNote("安装");
                cpv_item.setIcon(R.drawable.ic_install);
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                cpv_item.setNote("打开");
                cpv_item.setIcon(R.drawable.ic_install);
                break;

        }
    }

    /**
     * 处理下载按钮的点击事件
     * 状态(编程记录) | 用户行为(触发操作)
     * ------------ | -----------------
     * 未下载			| 去下载
     * 下载中			| 暂停下载
     * 暂停下载	    | 断点继续下载
     * 等待下载		| 取消下载
     * 下载失败 		| 重试下载
     * 下载完成 		| 安装应用
     * 已安装 		| 打开应用
     */
    private void processClick() {
        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);

        switch (info.state) {
            case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                doDownLoad(info);
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                pauseDownLoad(info);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                doDownLoad(info);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                cancelDownLoad(info);
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                doDownLoad(info);
                break;
            case DownloadManager.STATE_DOWNLOADED:// 下载完成
                installApk(info);
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                openApk(info);
                break;

        }

    }


    /**
     * 打开应用
     */
    private void openApk(DownLoadInfo info) {
        AppUtils.openApp(UIUtils.getContext(), info.packageName);
    }

    /**
     * 安装应用
     */
    private void installApk(DownLoadInfo info) {
        File apkFile = new File(info.savePath);
        AppUtils.installApp(UIUtils.getContext(), apkFile);
    }

    /**
     * 取消下载
     */
    private void cancelDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().cancel(info);

    }

    /**
     * 暂停下载
     */
    private void pauseDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().pause(info);
    }

    /**
     * 开始下载
     */
    private void doDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().downLoad(info);
    }


    @Override
    public void onDownLoadInfoChange(final DownLoadInfo info) {
        // 过滤DownLoadInfo
        if (!info.packageName.equals(mData.packageName)) {
            return;
        }

        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                refreshCircleProgressViewUI(info);
            }
        });
    }
}
