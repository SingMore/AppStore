package com.example.appstore.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.appstore.utils.UIUtils;
import com.example.appstore.views.flyoutin.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * Created by SingMore on 2017/2/13.
 */

public class RecommendAdapter implements StellarMap.Adapter {

    private List<String> mDataList;
    private static final int PAGER_SIZE	= 15;	// 每页显示多少条数据

    public RecommendAdapter(List<String> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getGroupCount() {// 有多少组
        int groupCount = mDataList.size() / PAGER_SIZE;
        // 如果不能整除,还有余数的情况
        if (mDataList.size() % PAGER_SIZE > 0) {// 有余数
            groupCount++;
        }
        return groupCount;
    }

    @Override
    public int getCount(int group) {// 每组有多少条数据
        if (group == getGroupCount() - 1) {// 来到了最后一组
            // 是否有余数
            if (mDataList.size() % PAGER_SIZE > 0) {// 有余数
                return mDataList.size() % PAGER_SIZE;// 返回具体的余数值就可以
            }
        }
        return PAGER_SIZE;// 0-15
    }

    @Override
    public View getView(int group, int position, View convertView) {// 返回具体的view
        TextView tv = new TextView(UIUtils.getContext());
        // group:代表第几组
        // position:几组中的第几个位置
        int index = group * PAGER_SIZE + position;
        tv.setText(mDataList.get(index));

        // random对象
        Random random = new Random();
        // 随机大小
        tv.setTextSize(random.nextInt(6) + 15);// 15-21
        // 随机的颜色
        int alpha = 255;//
        int red = random.nextInt(180) + 30;// 30-210
        int green = random.nextInt(180) + 30;
        int blue = random.nextInt(180) + 30;
        int argb = Color.argb(alpha, red, green, blue);
        tv.setTextColor(argb);

        return tv;
    }

    @Override
    public int getNextGroupOnPan(int group, float degree) {
        // TODO
        return 0;
    }

    @Override
    public int getNextGroupOnZoom(int group, boolean isZoomIn) {
        // TODO
        return 0;
    }
}
