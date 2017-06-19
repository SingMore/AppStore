package com.example.appstore.holder;

import android.view.View;
import android.widget.TextView;

import com.example.appstore.R;
import com.example.appstore.base.BaseHolder;
import com.example.appstore.bean.ChatMessage;
import com.example.appstore.utils.UIUtils;

import java.text.SimpleDateFormat;

/**
 * Created by SingMore on 2017/3/20.
 */

public class IncomingMsgHolder extends BaseHolder<ChatMessage> {

    TextView tv_date;
    TextView tv_msg;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_from_msg, null);
        tv_date = (TextView) view.findViewById(R.id.tv_from_time_chat);
        tv_msg = (TextView) view.findViewById(R.id.tv_from_msg_chat);
        return view;
    }

    @Override
    public void refreshShow(ChatMessage result) {
        //设置数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tv_date.setText(sdf.format(result.getDate()));
        tv_msg.setText(result.getMsg());

    }
}
