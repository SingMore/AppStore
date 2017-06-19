package com.example.appstore.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appstore.R;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.bean.DownLoadInfo;
import com.example.appstore.manager.DownloadManager;
import com.example.appstore.manager.DownloadManager.DownLoadObserver;
import com.example.appstore.utils.AppUtils;
import com.example.appstore.utils.BitmapHelper;
import com.example.appstore.utils.StringUtils;
import com.example.appstore.utils.UIUtils;
import com.example.appstore.views.ProgressButton;

import java.io.File;

/**
 * Created by SingMore on 2017/2/13.
 */

public class DetailActivity extends BaseActivity implements DownLoadObserver {
    private ProgressButton btn_download;
    private ImageView iv_screen0;
    private ImageView iv_screen1;
    private ImageView iv_screen2;
    private ImageView iv_screen3;
    private ImageView iv_screen4;
    private ImageView iv_appicon;
    private TextView tv_appname;
    private RatingBar rb_appstars;
    private TextView tv_appdownloadnum;
    private TextView tv_appversion;
    private TextView tv_apptime;
    private TextView tv_appsize;
    private TextView tv_appauthor;
    private TextView tv_appdes;

    private AppInfo mData;

    @Override
    public void init() {
        mData = (AppInfo) getIntent().getSerializableExtra("appInfo");
        // 初始化的时候把当前页面加到观察者集合里面去
        DownloadManager.getInstance().addObserver(this);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_detail);
        findViewById();

        //拿到数据显示到控件上
        displayData();

    }

    @Override
    public void initListener() {
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理点击事件
                processClick();
            }
        });
    }

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


    /**
     * 收到数据改变，更新UI
     */
    @Override
    public void onDownLoadInfoChange(final DownLoadInfo info) {
        // 过滤DownLoadInfo
        if (!info.packageName.equals(mData.packageName)) {
            return;
        }

        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                refreshProgressBtnUI(info);
            }
        });
    }


    /**
     * 刷新下载按钮的显示
     */
    private void refreshProgressBtnUI(DownLoadInfo info) {
        btn_download.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (info.state) {
            case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                btn_download.setText("下载");
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                btn_download.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                btn_download.setProgressEnable(true);
                btn_download.setMax(info.max);
                btn_download.setProgress(info.curProgress);
                int progress = (int) (info.curProgress * 100.f / info.max + .5f);
                btn_download.setText(progress + "%");
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                btn_download.setText("继续下载");
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                btn_download.setText("等待中...");
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                btn_download.setText("重试");
                break;
            case DownloadManager.STATE_DOWNLOADED:// 下载完成
                btn_download.setProgressEnable(false);
                btn_download.setText("安装");
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                btn_download.setText("打开");
                break;
        }
    }


    private void findViewById() {
        btn_download = (ProgressButton) findViewById(R.id.app_detail_download_btn);
        iv_screen0 = (ImageView) findViewById(R.id.iv_screen0);
        iv_screen1 = (ImageView) findViewById(R.id.iv_screen1);
        iv_screen2 = (ImageView) findViewById(R.id.iv_screen2);
        iv_screen3 = (ImageView) findViewById(R.id.iv_screen3);
        iv_screen4 = (ImageView) findViewById(R.id.iv_screen4);
        iv_appicon = (ImageView) findViewById(R.id.iv_detail_appicon);
        tv_appname = (TextView) findViewById(R.id.tv_detail_appname);
        rb_appstars = (RatingBar) findViewById(R.id.rb_detail_appstars);
        tv_appdownloadnum = (TextView) findViewById(R.id.tv_detail_appdownloadnum);
        tv_appversion = (TextView) findViewById(R.id.tv_detail_appversion);
        tv_apptime = (TextView) findViewById(R.id.tv_detail_time);
        tv_appsize = (TextView) findViewById(R.id.tv_detail_appsize);
        tv_appauthor = (TextView) findViewById(R.id.tv_detail_appauthor);
        tv_appdes = (TextView) findViewById(R.id.tv_detail_appdes);
    }

    /**
     * 根据拿到的数据显示到相应的控件上面
     */
    private void displayData() {
        tv_appname.setText(mData.getName());
        rb_appstars.setRating(mData.getStars());
        tv_appdownloadnum.setText("下载量：" + mData.getDownloadCount() + "万+");
        tv_appversion.setText("版本：" + mData.getVersion());
        tv_apptime.setText("更新时间：" + mData.getDate());
        tv_appsize.setText("大小：" + StringUtils.formatFileSize(mData.getSize()));
        tv_appauthor.setText("作者：" + mData.getAuthor());
        tv_appdes.setText(mData.getDes());

        //设置默认的显示图片
        iv_screen0.setImageResource(R.mipmap.ic_default);
        iv_screen1.setImageResource(R.mipmap.ic_default);
        iv_screen2.setImageResource(R.mipmap.ic_default);
        iv_screen3.setImageResource(R.mipmap.ic_default);
        iv_screen4.setImageResource(R.mipmap.ic_default);
        iv_appicon.setImageResource(R.mipmap.ic_default);

        BitmapHelper.display(iv_screen0, mData.getScreen0().getFileUrl());
        BitmapHelper.display(iv_screen1, mData.getScreen1().getFileUrl());
        BitmapHelper.display(iv_screen2, mData.getScreen2().getFileUrl());
        BitmapHelper.display(iv_screen3, mData.getScreen3().getFileUrl());
        BitmapHelper.display(iv_screen4, mData.getScreen4().getFileUrl());

        BitmapHelper.display(iv_appicon, mData.getIcon().getFileUrl());

        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);
        refreshProgressBtnUI(info);
    }
}
