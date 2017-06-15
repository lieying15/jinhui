package com.thlh.jhmjmw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.thlh.baselib.R;

/**
 * Created by HQ on 2016/3/25.
 */
public class TabLine extends View {
//    private int offset =  BaseApplication.width /5; // tab动画移动偏移量
//    private int offset =  getMeasuredWidth() /5; // tab动画移动偏移量
    private Paint mPaint;
    private int mheight,mwidth;

    public TabLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TabLine(Context context) {
        super(context);
        initView(context);
    }

    public TabLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int offset =  getMeasuredWidth() /5;
        mheight = getMeasuredHeight();
        mwidth = getMeasuredWidth();
        mPaint.setColor(getResources().getColor(R.color.black));
        canvas.drawRect(0, 0, offset * 2, mheight, mPaint);
        canvas.drawRect(offset * 3, 0,mwidth, mheight, mPaint);

    }



}
