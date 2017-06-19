package com.example.appstore.factory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.appstore.activity.DetailActivity;
import com.example.appstore.adapter.ListViewAdapter;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by SingMore on 2017/2/10.
 */

public abstract class ListViewFactory {

    /**
     * 创建一个ListView
     */
    public ListView createListView() {
        ListView listView = new ListView(UIUtils.getContext());
        //listView简单设置
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setFastScrollEnabled(true);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        return listView;
    }

    /**
     * 创建一个带有数据的ListView
     */
    public ListView createListViewWithData(List<AppInfo> dataList) {
        final ListView listView = new ListView(UIUtils.getContext());
        //listView简单设置
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setFastScrollEnabled(true);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        final List<AppInfo> mDataList = dataList;

        //listView设置适配器
        final ListViewAdapter adapter = new ListViewAdapter<AppInfo>(mDataList) {
            @Override
            public List<AppInfo> onLoadMore() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return loadMore();
            }
        };
        listView.setAdapter(adapter);

        //listView设置条目点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getHeaderViewsCount();

                if(adapter.getItemViewType(position) == ListViewAdapter.VIEWTYPE_LOADMORE) {
                    //如果是点击了加载更多，则重新加载更多数据
                    adapter.processLoadMore();
                }else {
                    //跳转到详情页面
                    AppInfo appInfo = mDataList.get(position);
                    Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("appInfo", appInfo);
                    UIUtils.getContext().startActivity(intent);
                }
            }
        });
        return listView;
    }

    public abstract List<AppInfo> loadMore();

}
