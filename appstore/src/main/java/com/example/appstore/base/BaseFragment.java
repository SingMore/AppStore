package com.example.appstore.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appstore.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by SingMore on 2017/2/7.
 */
/**
 * 页面显示分析
 * Fragment共性-->页面共性-->视图的展示
 *
 * 任何应用其实就只有3种页面类型
 * ① 加载页面
 * ② 错误页面
 * ③ 成功页面
 * ①②两种页面一个应用基本是固定的
 * 每一个fragment/activity对应的页面③就不一样
 * 进入应用的时候显示①,②③需要加载数据之后才知道显示哪个
 *
 * 数据加载的流程
 * ① 触发加载  	进入页面开始加载/点击某一个按钮的时候加载
 * ② 异步加载数据  -->显示加载视图
 * ③ 处理加载结果
 *    ① 成功-->显示成功视图
 *    ② 失败-->显示加载失败的视图
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPager loadingPager;

    public LoadingPager getLoadingPager() {
        return loadingPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(loadingPager == null) {
            loadingPager = new LoadingPager(UIUtils.getContext()) {
                @Override
                public LoadedResult initData() {
                    return BaseFragment.this.initData();
                }

                @Override
                public View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }
            };
        }

        return loadingPager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 检查服务器获取的结果
     */
    public LoadingPager.LoadedResult checkState(Object obj) {
        if(obj == null) {
            return LoadingPager.LoadedResult.ERROR;
        }
        //如果是list
        if(obj instanceof List) {
            if(((List) obj).size() == 0) {
                return LoadingPager.LoadedResult.ERROR;
            }
        }
        //如果是map
        if(obj instanceof Map) {
            if(((Map) obj).size() == 0) {
                return LoadingPager.LoadedResult.ERROR;
            }
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }


    /**
     * LoadingPager的同名方法，具体实现数据的加载
     * @return
     */
    public abstract LoadingPager.LoadedResult initData();

    public abstract View initSuccessView();

}
