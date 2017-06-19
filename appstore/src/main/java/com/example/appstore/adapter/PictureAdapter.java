package com.example.appstore.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.appstore.R;
import com.example.appstore.bean.CarouselPic;
import com.example.appstore.conf.Constants;
import com.example.appstore.utils.BitmapHelper;
import com.example.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by SingMore on 2017/2/10.
 */

public class PictureAdapter extends PagerAdapter {

    private List<CarouselPic> mPicList;


    public PictureAdapter(List<CarouselPic> picList) {
        this.mPicList = picList;
    }
    @Override
    public int getCount() {
        if (mPicList != null) {
            return Integer.MAX_VALUE;   //实现无限轮播
            // return mDatas.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % mPicList.size();

        ImageView iv = new ImageView(UIUtils.getContext());
        iv.setScaleType(ScaleType.FIT_XY);

        iv.setImageResource(R.mipmap.ic_default);

        //得到轮播图的URL地址
        String picUrl = mPicList.get(position).getPic().getFileUrl();
        BitmapHelper.display(iv, picUrl);

        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
