package com.example.appstore.fragment;

import android.view.View;

import com.example.appstore.adapter.RecommendAdapter;
import com.example.appstore.base.BaseFragment;
import com.example.appstore.base.LoadingPager.LoadedResult;
import com.example.appstore.bean.RecommendInfo;
import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.manager.MyProtocol;
import com.example.appstore.utils.UIUtils;
import com.example.appstore.views.flyoutin.ShakeListener;
import com.example.appstore.views.flyoutin.StellarMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SingMore on 2017/2/7.
 */
public class RecommendFragment extends BaseFragment {
    private List<String> mDataList;
    private ShakeListener mShakeListener;
    private MyProtocol protocol;
    List<RecommendInfo> recommendInfos;

    @Override
    public LoadedResult initData() {
        if(protocol == null) {
            protocol = new MyProtocol();
        }
        //使用线程池管理
        ThreadPoolFactory.getNormalThreadPool().executeTask(new LoadRecommendThread());

        if(recommendInfos == null) {
            return LoadedResult.ERROR;
        }else {
            mDataList = new ArrayList<String>();
            for(RecommendInfo info : recommendInfos) {
                mDataList.add(info.getRecommendWord());
            }
            return LoadedResult.SUCCESS;
        }
    }

    @Override
    public View initSuccessView() {
        // 返回成功的视图
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());

        // 设置adapter
        final RecommendAdapter adapter = new RecommendAdapter(mDataList);
        stellarMap.setAdapter(adapter);

        // 设置第一页的时候显示
        stellarMap.setGroup(0, true);
        // 设置把屏幕拆分成多少个格子
        stellarMap.setRegularity(15, 20);// 总的就有300个格子

        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake() {
                int groupIndex = stellarMap.getCurrentGroup();
                if (groupIndex == adapter.getGroupCount() - 1) {
                    groupIndex = 0;
                } else {
                    groupIndex++;
                }
                stellarMap.setGroup(groupIndex, true);
            }
        });
        return stellarMap;
    }

    /**
     * 使用子线程去加载网络数据
     */
    class LoadRecommendThread implements Runnable {
        @Override
        public void run() {
            recommendInfos = protocol.loadRecommendWords();
        }
    }

    @Override
    public void onResume() {
        if (mShakeListener != null) {
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mShakeListener != null) {
            mShakeListener.pause();
        }
        super.onPause();
    }
}

