package com.example.appstore.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.example.appstore.R;
import com.example.appstore.base.BaseHolder;
import com.example.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by SingMore on 2017/2/9.
 */
public class LoadMoreHolder extends BaseHolder<Integer> {
    @ViewInject(R.id.ll_item_loading)
    LinearLayout mItemLoading;

    @ViewInject(R.id.ll_item_reload)
    LinearLayout mItemReload;

    public static final int STATE_NONE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_RELOAD = 2;
    
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void refreshShow(Integer result) {
        mItemLoading.setVisibility(View.GONE);
        mItemReload.setVisibility(View.GONE);
        switch (result) {
            case STATE_NONE:
                break;
            case STATE_LOADING:
                mItemLoading.setVisibility(View.VISIBLE);
                break;
            case STATE_RELOAD:
                mItemReload.setVisibility(View.VISIBLE);
                break;
        }
    }
}
