package com.example.appstore.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.adapter.MyAppManageAdapter;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.bean.AppManageInfo;
import com.example.appstore.manager.AppInfoManager;
import com.example.appstore.utils.AppUtils;
import com.example.appstore.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SingMore on 2017/6/3.
 */

public class AppManageActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_appmanage_avail_inner;
    private TextView tv_appmanage_avail_sd;
    private ListView lv_app_manage;
    private TextView tv_app_manage_header;

    private List<AppManageInfo> userInfos;
    private List<AppManageInfo> systemInfos;

    private AppManageInfo appInfo;
    private MyAppManageAdapter adapter;
    private PopupWindow popupWindow;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(adapter == null){
                tv_app_manage_header.setText("用户应用(" + userInfos.size() + ")");
                //创建adapter
                adapter = new MyAppManageAdapter(userInfos, systemInfos);
                lv_app_manage.setAdapter(adapter);
            }else{
                tv_app_manage_header.setText("用户应用(" + userInfos.size() + ")");
                adapter.notifyDataSetChanged();
            }
        };
    };


    @Override
    public void initView() {
        setContentView(R.layout.activity_appmanage);
        findViewById();
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        long sdAvailSpace = initAvailSpace(sdPath);
        String sdSize = Formatter.formatFileSize(this, sdAvailSpace);
        tv_appmanage_avail_sd.setText("sd卡可用："+sdSize);

        String dataPath = Environment.getDataDirectory().getAbsolutePath();
        long dataAvailSpace = initAvailSpace(dataPath);
        String dataSize = Formatter.formatFileSize(this, dataAvailSpace);
        tv_appmanage_avail_inner.setText("磁盘可用："+dataSize);
    }

    @Override
    public void initData() {
        new Thread(){
            public void run() {
                List<AppManageInfo> appInfos = AppInfoManager.getAppInfos(UIUtils.getContext());
                userInfos = new ArrayList<AppManageInfo>();
                systemInfos = new ArrayList<AppManageInfo>();

                for(AppManageInfo info : appInfos){
                    if(info.isSystem()){
                        systemInfos.add(info);
                    }else{
                        userInfos.add(info);
                    }
                }
                handler.sendEmptyMessage(0);
            };
        }.start();
    }

    @Override
    public void initListener() {
        //滑动监听事件
        lv_app_manage.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userInfos != null && systemInfos != null) {
                    if(firstVisibleItem >= userInfos.size() + 1) {
                        tv_app_manage_header.setText("系统应用(" + systemInfos.size() + ")");
                    }else{
                        tv_app_manage_header.setText("用户应用(" + userInfos.size() + ")");
                    }
                }
            }
        });

        //条目点击事件
        lv_app_manage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == userInfos.size() + 1) {
                    return;
                }else if(position < userInfos.size() + 1){
                    appInfo = userInfos.get(position - 1);
                }else{
                    appInfo = systemInfos.get(position - userInfos.size() - 2);
                }
                //弹出小窗口
                showPopupWindow(view);
            }
        });
    }

    protected void showPopupWindow(View item) {
        View view = View.inflate(this, R.layout.pop_app_manage, null);

        TextView tv_app_pop_uninstall = (TextView) view.findViewById(R.id.tv_app_pop_uninstall);
        TextView tv_app_pop_start = (TextView) view.findViewById(R.id.tv_app_pop_start);
        TextView tv_app_pop_share = (TextView) view.findViewById(R.id.tv_app_pop_share);

        tv_app_pop_uninstall.setOnClickListener(this);
        tv_app_pop_start.setOnClickListener(this);
        tv_app_pop_share.setOnClickListener(this);

        //给弹出窗口设置动画
        AnimationSet set = initAnimation();
        view.startAnimation(set);

        //创建popupwindow对象
        popupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAsDropDown(item, UIUtils.dp2px(200), -item.getHeight());

    }

    /**
     * 初始化动画
     */
    private AnimationSet initAnimation() {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(500);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(500);
        sa.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        return set;
    }

    private long initAvailSpace(String path) {
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long blockCount = statFs.getAvailableBlocks();
        return blockSize*blockCount;
    }

    private void findViewById() {
        tv_appmanage_avail_inner = (TextView) findViewById(R.id.tv_appmanage_avail_inner);
        tv_appmanage_avail_sd = (TextView) findViewById(R.id.tv_appmanage_avail_sd);
        lv_app_manage = (ListView) findViewById(R.id.lv_app_manage);
        tv_app_manage_header = (TextView) findViewById(R.id.tv_app_manage_header);
    }

    @Override
    public void onClick(View v) {
        if(popupWindow != null){
            popupWindow.dismiss();
        }
        switch (v.getId()) {
            case R.id.tv_app_pop_uninstall:
                uninstall();
                break;
            case R.id.tv_app_pop_start:
                start();
                break;
            case R.id.tv_app_pop_share:
                share();
                break;
        }
    }

    //分享应用
    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "分享给你一个好玩的应用：" + appInfo.getName());
        startActivity(intent);
    }

    //打开应用
    private void start() {
        AppUtils.openApp(UIUtils.getContext(), appInfo.getPackageName());
    }

    //卸载应用
    private void uninstall() {
        if(appInfo.isSystem()){
            Toast.makeText(this, "系统应用不能卸载", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }
}
