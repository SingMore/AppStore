package com.example.appstore.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appstore.R;
import com.example.appstore.bean.AppManageInfo;
import com.example.appstore.utils.UIUtils;

import java.util.List;

/**
 * Created by SingMore on 2017/6/3.
 */

public class MyAppManageAdapter extends BaseAdapter {
    List<AppManageInfo> userInfos;
    List<AppManageInfo> systemInfos;

    public MyAppManageAdapter(List<AppManageInfo> userInfos, List<AppManageInfo> systemInfos) {
        this.userInfos = userInfos;
        this.systemInfos = systemInfos;
    }
    @Override
    public int getCount() {
        return userInfos.size() + systemInfos.size() + 2;
    }

    @Override
    public AppManageInfo getItem(int position) {
        if(position == 0||position == userInfos.size() + 1){
            return null;
        }else if(position < userInfos.size() + 1){
            return userInfos.get(position - 1);
        }else{
            return systemInfos.get(position - userInfos.size() - 2);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText("用户应用(" + userInfos.size() + ")");
            tv.setTextSize(18);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        }else if(position == userInfos.size() + 1){
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText("系统应用(" + systemInfos.size() + ")");
            tv.setTextSize(18);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        }

        ViewHolder holder = null;
        if(convertView != null && convertView instanceof RelativeLayout){
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_appmanage, null);

            holder.iv_appmanage_icon = (ImageView) convertView.findViewById(R.id.iv_appmanage_icon);
            holder.tv_appmanage_name = (TextView) convertView.findViewById(R.id.tv_appmanage_name);
            holder.tv_appmanage_location = (TextView) convertView.findViewById(R.id.tv_appmanage_location);

            convertView.setTag(holder);
        }

        AppManageInfo info = getItem(position);
        holder.iv_appmanage_icon.setImageDrawable(info.getIcon2());
        holder.tv_appmanage_name.setText(info.getName());

        if(info.isSDcard()){
            holder.tv_appmanage_location.setText("SD卡应用");
        }else{
            holder.tv_appmanage_location.setText("手机应用");
        }

        return convertView;
    }

    class ViewHolder{
        public ImageView iv_appmanage_icon;
        public TextView tv_appmanage_name;
        public TextView tv_appmanage_location;
    }
}
