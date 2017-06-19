package com.example.appstore.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.appstore.activity.MainActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by SingMore on 2017/2/15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private List<AppCompatActivity> activities = new LinkedList<AppCompatActivity>();
    private Activity mCurActivity;//当前的Activity
    private long mPreTime;

    /**
     * @return 得到当前的Activity
     */
    public Activity getCurActivity() {
        return mCurActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();//初始化页面跳转传递的数据等
        initView();//初始化视图
        initData();//初始化数据
        initListener();//初始化Listener
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        activities.remove(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mCurActivity = this;
        super.onResume();
    }

    public void initListener() {
    }

    public void initData() {
    }

    public abstract void initView();

    public void init() {
    }

    /**
     * 完全退出
     */
    public void exitAll() {
        for(AppCompatActivity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 监听返回键退出
     */
    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2s
                Toast.makeText(getApplicationContext(), "再按一次,退出AppStore", Toast.LENGTH_SHORT).show();
                mPreTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();
    }
}
