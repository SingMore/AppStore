package com.example.appstore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.appstore.base.BaseHolder;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.holder.LoadMoreHolder;
import com.example.appstore.holder.NormalHolder;
import com.example.appstore.manager.DownloadManager;
import com.example.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by SingMore on 2017/3/9.
 */

public abstract class ListViewAdapter<T> extends BaseAdapter {
    private List<T> mDataList;
    public static final int VIEWTYPE_NORMAL = 0;//ListView常规类型
    public static final int VIEWTYPE_LOADMORE = 1;//ListView加载更多类型
    private BaseHolder mLoadMoreHolder;
    private LoadMoreTask mLoadMoreTask;

    public ListViewAdapter(List<T> list) {
        this.mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size() + 1;
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @return ListView里面显示几种类型，默认是1
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //如果滑倒底部，则是加载更多类型
        if(position == getCount() - 1) {
            return VIEWTYPE_LOADMORE;
        }else {
            return VIEWTYPE_NORMAL;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if(convertView == null) {
            if(getItemViewType(position) == VIEWTYPE_LOADMORE) {
                holder = getLoadMoreHolder();
            }else {
                holder = getNormalHolder();
            }
        }else {
            holder = (BaseHolder) convertView.getTag();
        }

        //如果是加载更多
        if(getItemViewType(position) == VIEWTYPE_LOADMORE) {
            processLoadMore();
        }else {
            //得到数据
            T result = mDataList.get(position);
            holder.setDataAndShow(result);
        }

        return holder.mHolderView;
    }

    /**
     * 处理加载更多
     */
    public void processLoadMore() {
        if(mLoadMoreTask == null) {
            // 修改loadmore当前的视图为加载中
            int state = LoadMoreHolder.STATE_LOADING;
            mLoadMoreHolder.setDataAndShow(state);

            //开启一个任务执行加载更多数据
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolFactory.getNormalThreadPool().executeTask(mLoadMoreTask);
        }
    }

    /**
     * 返回一个常规条目的Holder
     */
    public BaseHolder getNormalHolder() {
        NormalHolder normalHolder = new NormalHolder();
        // 初始化的时候把normalHolder加到观察者集合里面去
        DownloadManager.getInstance().addObserver(normalHolder);
        return normalHolder;
    }

    /**
     * 返回一个加载更多的Holder
     */
    public BaseHolder getLoadMoreHolder() {
        if(mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    class LoadMoreTask implements Runnable {
        @Override
        public void run() {
            // 真正开始请求网络加载更多数据
            List<T> loadMoreDatas = null;
            // 根据加载更多的数据,处理 loadmore的状态
            int state = LoadMoreHolder.STATE_LOADING;
            try {
                loadMoreDatas = onLoadMore();
                // 得到返回数据,处理结果
                if (loadMoreDatas == null) {// 没有更多数据
                    state = LoadMoreHolder.STATE_NONE;
                } else {
                    state = LoadMoreHolder.STATE_LOADING;
                }
            } catch (Exception e) {
                e.printStackTrace();
                state = LoadMoreHolder.STATE_RELOAD;
            }

            // 临时存放加载更多得到的状态和数据
            final int tempState = state;
            final List<T> tempLoadMoreDatas = loadMoreDatas;

            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    // 刷新loadmore视图
                    mLoadMoreHolder.setDataAndShow(tempState);
                    // 刷新listview视图 返回加载更多过后得到的数据 mDatas.addAll()
                    if (tempLoadMoreDatas != null) {
                        mDataList.addAll(tempLoadMoreDatas);// listview数据源更新
                        notifyDataSetChanged();// 刷新listview
                    }
                }
            });

            mLoadMoreTask = null;
        }
    }

    public abstract List<T> onLoadMore();

}
