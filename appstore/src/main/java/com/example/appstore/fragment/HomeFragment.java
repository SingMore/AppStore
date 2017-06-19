package com.example.appstore.fragment;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.appstore.R;
import com.example.appstore.adapter.PictureAdapter;
import com.example.appstore.base.BaseFragment;
import com.example.appstore.base.LoadingPager.LoadedResult;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.bean.CarouselPic;
import com.example.appstore.factory.ListViewFactory;
import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.manager.MyProtocol;
import com.example.appstore.utils.UIUtils;
import com.example.appstore.views.InnerViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by SingMore on 2017/2/7.
 */
public class HomeFragment extends BaseFragment {
    @ViewInject(R.id.vp_home_picture)
    InnerViewPager vp_home_picture;

    @ViewInject(R.id.ll_home_picture_indicator)
    LinearLayout ll_home_picture_indicator;

    private List<CarouselPic> mPicList;//轮播图数据源
    private List<AppInfo> mDataList;//ListView的数据源
    private AutoScrollTask mAutoScrollTask;//自动轮播的任务
    private MyProtocol protocol;
    private View picView;//轮播图视图
    private ListViewFactory factory;


    public LoadedResult initData() {
        if(protocol == null) {
            protocol = new MyProtocol();
        }
        //使用线程池管理
        ThreadPoolFactory.getNormalThreadPool().executeTask(new LoadHomeThread());

        if(mDataList == null || mPicList == null) {
            return LoadedResult.ERROR;
        }
        return LoadedResult.SUCCESS;

    }

    public View initSuccessView() {
        ListView listView = new ListViewFactory() {
            @Override
            public List<AppInfo> loadMore() {
                return protocol.loadPartData(mDataList.size());
            }
        }.createListViewWithData(mDataList);

        //初始化轮播图
        initPics();

        //把轮播图加到ListView的头部
        listView.addHeaderView(picView);

        return listView;
    }

    /**
     * 初始化轮播图
     */
    private void initPics() {
        picView = View.inflate(UIUtils.getContext(), R.layout.fragment_home_pic, null);
        ViewUtils.inject(this, picView);

        //设置适配器
        vp_home_picture.setAdapter(new PictureAdapter(mPicList));
        //添加小点
        addPoints();

        // 设置curItem为count/2
        int diff = Integer.MAX_VALUE / 2 % mPicList.size();
        int index = Integer.MAX_VALUE / 2;
        vp_home_picture.setCurrentItem(index - diff);

        // 自动轮播
        mAutoScrollTask = new AutoScrollTask();
        mAutoScrollTask.start();

        //监听页面滑动事件
        vp_home_picture.setOnPageChangeListener(new PicPagerChangeListener());

        //设置页面的触摸事件，触摸停止轮播
        vp_home_picture.setOnTouchListener(new PicTouchListener());
    }

    /**
     * 添加轮播图右下方的指示点
     */
    private void addPoints() {
        for (int i = 0; i < mPicList.size(); i++) {
            View indicatorView = new View(UIUtils.getContext());
            indicatorView.setBackgroundResource(R.mipmap.indicator_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dp2px(5), UIUtils.dp2px(5));// dp-->px
            // 左边距
            params.leftMargin = UIUtils.dp2px(5);
            // 下边距
            params.bottomMargin = UIUtils.dp2px(5);

            //添加小点到线性布局中
            ll_home_picture_indicator.addView(indicatorView, params);

            // 默认选中效果
            if (i == 0) {
                indicatorView.setBackgroundResource(R.mipmap.indicator_selected);
            }
        }
    }


    /**
     * 自动轮播的任务
     */
    class AutoScrollTask implements Runnable {
        /**开始轮播*/
        public void start() {
            UIUtils.postTaskDelay(this, 2000);
        }

        public void stop() {
            UIUtils.removeTask(this);
        }

        /**结束轮播*/
        @Override
        public void run() {
            vp_home_picture.setCurrentItem(vp_home_picture.getCurrentItem()+1);
            // 结束-->再次开始
            start();
        }
    }

    /**
     * 轮播图切换监听
     */
    class PicPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            position = position % mPicList.size();

            for (int i = 0; i < mPicList.size(); i++) {
                View indicatorView = ll_home_picture_indicator.getChildAt(i);
                // 还原背景
                indicatorView.setBackgroundResource(R.mipmap.indicator_normal);

                if (i == position) {
                    indicatorView.setBackgroundResource(R.mipmap.indicator_selected);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * 轮播图触摸监听
     */
    class PicTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mAutoScrollTask.stop();
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    mAutoScrollTask.start();
                    break;
            }
            return false;
        }
    }


    /**
     * 使用子线程去加载网络数据
     */
    class LoadHomeThread implements Runnable {
        @Override
        public void run() {
            mPicList = protocol.loadCarouselPics();
            mDataList = protocol.loadPartData(0);
        }
    }

}
