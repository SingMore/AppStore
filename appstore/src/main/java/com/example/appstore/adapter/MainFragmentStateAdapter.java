package com.example.appstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.appstore.factory.FragmentFactory;
import com.example.appstore.utils.LogUtils;

/**
 * Created by SingMore on 2017/3/8.
 * FragmentStatePagerAdapter 缓存的是Fragment的状态
 */

public class MainFragmentStateAdapter extends FragmentStatePagerAdapter {
    private String[] mMainTitles;

    public MainFragmentStateAdapter(FragmentManager fm, String[] mainTitles) {
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
