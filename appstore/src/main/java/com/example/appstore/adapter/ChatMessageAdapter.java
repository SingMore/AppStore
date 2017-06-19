package com.example.appstore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appstore.R;
import com.example.appstore.base.BaseHolder;
import com.example.appstore.bean.ChatMessage;
import com.example.appstore.holder.IncomingMsgHolder;
import com.example.appstore.holder.OutcomingMsgHolder;
import com.example.appstore.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by SingMore on 2017/3/20.
 */

public class ChatMessageAdapter extends BaseAdapter {
    private List<ChatMessage> mDataList;

    public static final int TYPE_INCOMING = 0;//接受消息类型
    public static final int TYPE_OUTCOMING = 1;//发送消息类型

    public ChatMessageAdapter(List<ChatMessage> list) {
        this.mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType() == ChatMessage.Type.INCOMING) {
            return TYPE_INCOMING;
        }
        return TYPE_OUTCOMING;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            if(getItemViewType(position) == TYPE_INCOMING) {
                convertView = View.inflate(UIUtils.getContext(), R.layout.item_from_msg, null);
                holder = new ViewHolder();
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_from_time_chat);
                holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_from_msg_chat);
                convertView.setTag(holder);
            }else if(getItemViewType(position) == TYPE_OUTCOMING) {
                convertView = View.inflate(UIUtils.getContext(), R.layout.item_to_msg, null);
                holder = new ViewHolder();
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_to_time_chat);
                holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_to_msg_chat);
                convertView.setTag(holder);
            }
        }else {
             holder = (ViewHolder) convertView.getTag();
        }
        //得到数据
        ChatMessage result = mDataList.get(position);
        //设置数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tv_date.setText(sdf.format(result.getDate()));
        holder.tv_msg.setText(result.getMsg());

        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        TextView tv_msg;
    }

}