package com.example.appstore.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by SingMore on 2017/2/10.
 */

public class InnerViewPager extends ViewPager {

    public InnerViewPager(Context context) {
        super(context);
    }

    public InnerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 处理内部ViewPager的滑动事件
     * 左右滑动-->自己处理
     * 上下滑动-->父亲处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float downX = 0;
        float downY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getRawX();
                float moveY = ev.getRawY();

                int diffX = (int) (moveX - downX);
                int diffY = (int) (moveY - downY);
                // 左右滚动的绝对值 > 上下滚动的绝对值
                if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
                    // 请求父亲不拦截事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {// 上下
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
