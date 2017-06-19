package com.example.appstore.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.bean.AppInfo;
import com.example.appstore.factory.ListViewFactory;
import com.example.appstore.manager.MyProtocol;
import com.example.appstore.utils.LogUtils;
import com.example.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by SingMore on 2017/3/17.
 */

public class SearchResultActivity extends BaseActivity {
    private String search_content;
    private LinearLayout ll_searchresult;
    private RelativeLayout rl_searchresult;

    private MyProtocol protocol;
    private List<AppInfo> appInfos;
    private Button bt_refresh;

    public void init() {
        search_content = getIntent().getStringExtra("search_content");
        LogUtils.e("wqz", "搜索内容是：" + search_content);
        if (protocol == null) {
            protocol = new MyProtocol();
        }
    }

    public void initView() {
        setContentView(R.layout.activity_searchresult);
        ll_searchresult = (LinearLayout) findViewById(R.id.ll_searchresult);
        rl_searchresult = (RelativeLayout) findViewById(R.id.rl_searchresult);
        bt_refresh = (Button) findViewById(R.id.bt_refresh_search);
    }

    public void initData() {
        new LoadSearchTask().start();
        refreshUI();
    }

    @Override
    public void initListener() {
        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    /**
     * 根据搜索的结果刷新界面
     */
    private void refreshUI() {
        if (appInfos == null || appInfos.size() == 0) {
            //LogUtils.e("未搜索到您要查找的内容");
            Toast.makeText(UIUtils.getContext(), "未搜索到您要查找的内容", Toast.LENGTH_SHORT).show();
            return;
        }

        ListView listView = new ListViewFactory() {
            @Override
            public List<AppInfo> loadMore() {
                return null;
            }
        }.createListViewWithData(appInfos);
        ll_searchresult.addView(listView);

        rl_searchresult.setVisibility(View.INVISIBLE);
        ll_searchresult.setVisibility(View.VISIBLE);
    }

    /**
     * 子线程执行搜索任务
     */
    class LoadSearchTask extends Thread {

        @Override
        public void run() {
            appInfos = protocol.searchByName(search_content);
        }
    }


}
