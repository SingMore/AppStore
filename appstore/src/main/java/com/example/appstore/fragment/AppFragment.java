package com.example.appstore.fragment;

import android.view.View;
import android.widget.ListView;

import com.example.appstore.base.BaseFragment;
import com.example.appstore.base.LoadingPager.LoadedResult;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.factory.ListViewFactory;
import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.manager.MyProtocol;

import java.util.List;

/**
 * Created by SingMore on 2017/2/7.
 */
public class AppFragment extends BaseFragment {
    private List<AppInfo> mDataList;//ListView的数据源
    private MyProtocol protocol;

    @Override
    public LoadedResult initData() {
        if(protocol == null) {
            protocol = new MyProtocol();
        }
        //使用线程池管理
        ThreadPoolFactory.getNormalThreadPool().executeTask(new LoadAppThread());

        if(mDataList == null) {
            return LoadedResult.ERROR;
        }
        return LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView() {
        ListView listView = new ListViewFactory() {

            @Override
            public List<AppInfo> loadMore() {
                return protocol.queryByType("application", mDataList.size());
            }
        }.createListViewWithData(mDataList);

        return listView;
    }

    /**
     * 使用子线程去加载网络数据
     */
    class LoadAppThread implements Runnable {
        @Override
        public void run() {
            mDataList = protocol.queryByType("application", 0);
        }
    }

}
