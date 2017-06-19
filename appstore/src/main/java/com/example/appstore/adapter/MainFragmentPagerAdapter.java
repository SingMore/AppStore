package com.example.appstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.appstore.factory.FragmentFactory;
import com.example.appstore.utils.LogUtils;

/**
 * Created by SingMore on 2017/3/8.
 * FragmentPagerAdapter 会缓存Fragment，如果Fragment比较多，不建议使用
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mMainTitles;

    public MainFragmentPagerAdapter(FragmentManager fm, String[] mainTitles) {
        super(fm);
        this.mMainTitles = mainTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMainTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        //创建Fragment
        Fragment fragment = FragmentFactory.getFragment(position);
        LogUtils.sf("初始化" + mMainTitles[position]);
        return fragment;
    }

    @Override
    public int getCount() {
        if (mMainTitles != null) {
            return mMainTitles.length;
        }
        return 0;
    }
}
