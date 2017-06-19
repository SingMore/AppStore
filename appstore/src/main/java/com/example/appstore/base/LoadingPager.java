package com.example.appstore.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.utils.UIUtils;
import com.example.appstore.R;

/**
 * Created by SingMore on 2017/2/7.
 */

public abstract class LoadingPager extends FrameLayout {

    public static final int	STATE_NONE		= -1;			// 默认情况
    public static final int	STATE_LOADING	= 0;			// 正在请求网络
    public static final int	STATE_ERROR		= 1;			// 错误状态
    public static final int	STATE_SUCCESS	= 2;			// 成功状态

    public int mCurState = STATE_NONE;
    private View mLoadingView;
    private View mErrorView;
    private View mSuccessView;

    public LoadingPager(Context context) {
        super(context);
        initView();

    }

    private void initView() {
        //错误页面
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        this.addView(mErrorView);
        //设置错误页面的点击事件
        mErrorView.findViewById(R.id.btn_error_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新触发加载数据
                loadData();
            }
        });

        //加载页面
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        this.addView(mLoadingView);

        refreshUI();
    }

    private void refreshUI() {
        // 控制loadingView视图显示隐藏
        mLoadingView.setVisibility(((mCurState == STATE_LOADING) || (mCurState == STATE_NONE)) ? VISIBLE : INVISIBLE);

        // 控制errorView视图显示隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? VISIBLE : INVISIBLE);

        if (mSuccessView == null && mCurState == STATE_SUCCESS) {
            // 创建成功视图
            mSuccessView = initSuccessView();
            this.addView(mSuccessView);
        }

        if (mSuccessView != null) {
            // 控制success视图显示隐藏
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? VISIBLE : INVISIBLE);
        }
    }

    /**
     * 暴露给外界使用，加载数据
     */
    public void loadData() {
        //数据加载成功或者正在加载中，不再进行加载
        if(mCurState == STATE_SUCCESS || mCurState == STATE_LOADING) {
            return;
        }
        //保证每次执行的时候都是loading的视图，而不是上一次执行的视图
        mCurState = STATE_LOADING;
        refreshUI();
        //异步加载数据
//        new Thread(new LoadDataTask()).start();
        //使用线程池管理
        ThreadPoolFactory.getNormalThreadPool().executeTask(new LoadDataTask());
    }

    class LoadDataTask implements Runnable {

        @Override
        public void run() {
            //真正的去加载数据
            LoadedResult tempState = initData();
            //处理加载结果
            mCurState = tempState.getState();
            //在主线程刷新UI
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI();
                }
            });
        }
    }

    /**
     * 真正加载数据，必须实现，但是不知道具体实现，定义为抽象方法，让子类具体实现
     * @return
     */
    public abstract LoadedResult initData();

    public abstract View initSuccessView();

    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS), ERROR(STATE_ERROR);
        int state;

        public int getState() {
            return state;
        }

        LoadedResult(int state) {
            this.state = state;
        }
    }
}
