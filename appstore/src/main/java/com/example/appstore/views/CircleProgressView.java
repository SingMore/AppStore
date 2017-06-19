package com.example.appstore.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appstore.R;
import com.example.appstore.utils.UIUtils;

/**
 * Created by SingMore on 2017/3/16.
 */

public class CircleProgressView extends LinearLayout {

    private ImageView mIcon;
    private TextView mNote;

    private boolean mProgressEnable;
    private long mMax = 100;
    private long mProgress;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.inflate_circleprogressview, this);

        mIcon = (ImageView) view.findViewById(R.id.circleProgressView_iv_icon);
        mNote = (TextView) view.findViewById(R.id.circleProgressView_tv_note);
    }

    /**
     * 是否允许进度
     */
    public void setProgressEnable(boolean progressEnable) {
        mProgressEnable = progressEnable;
    }

    /**
     * 进度最大值
     */
    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 进度当前进度
     */
    public void setProgress(long progress) {
        mProgress = progress;
        invalidate();
    }

    /**
     * 设置图标
     */
    public void setIcon(int resId) {
        mIcon.setImageResource(resId);
    }

    /**
     * 设置文字
     */
    public void setNote(String note) {
        mNote.setText(note);
    }

    // onDraw onMeasure onLayout
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO
        super.onDraw(canvas);// 绘制背景,透明图片

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // TODO
        super.dispatchDraw(canvas);// 绘制具体的内容(图片和文字)

        if (mProgressEnable) {
            RectF oval = new RectF(mIcon.getLeft(), mIcon.getTop(), mIcon.getRight(), mIcon.getBottom());
            float startAngle = -90;

            float sweepAngle = mProgress * 360.f / mMax;

            boolean useCenter = false;// 是否保留两条边
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(UIUtils.dp2px(3));
            paint.setAntiAlias(true);// 消除锯齿
            canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
        }
    }
}
