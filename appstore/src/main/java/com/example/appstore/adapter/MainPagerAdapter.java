package com.example.appstore.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appstore.utils.UIUtils;

/**
 * Created by SingMore on 2017/3/8.
 * 主页内容区域的ViewPager常规数据适配器
 */

public class MainPagerAdapter extends PagerAdapter {
    private String[] mMainTitles;

    public MainPagerAdapter(String[] mainTitles) {
        this.mMainTitles = mainTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMainTitles[position];
    }

    @Override
    public int getCount() {
        return mMainTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView tv = new TextView(UIUtils.getContext());
        tv.setText(mMainTitles[position]);
        container.addView(tv);
        return tv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
