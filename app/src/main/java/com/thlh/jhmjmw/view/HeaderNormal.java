package com.thlh.jhmjmw.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;

/**
 * Created by HQ on 2016/3/30.
 */
public class HeaderNormal extends RelativeLayout {

    private ImageView headerNomalLeftIv;
    private FrameLayout headerNomalLeftFl;
    private TextView headerTitleTv,headerNomalRightTv;
    private ImageView headerNomalRightIv;
    private FrameLayout headerNomalRightFl;
    private Context context;
    private String mTitleText;

    private OnClickListener backOnClickListener;

    public HeaderNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initListener();
        initDate(attrs, defStyle);
    }

    public HeaderNormal(Context context) {
        super(context);
        this.context = context;
        initView();
        initListener();

    }

    public HeaderNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initListener();
        initDate(attrs, 0);
    }




    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_header_normal, this, true);

        headerNomalLeftIv = (ImageView) view.findViewById(R.id.header_nomal_left_iv);
        headerNomalLeftFl = (FrameLayout) view.findViewById(R.id.header_nomal_left_fl);
        headerTitleTv = (TextView) view.findViewById(R.id.header_title_tv);
        headerNomalRightIv = (ImageView) view.findViewById(R.id.header_nomal_right_iv);
        headerNomalRightTv = (TextView) view.findViewById(R.id.header_nomal_right_tv);
        headerNomalRightFl = (FrameLayout) view.findViewById(R.id.header_nomal_right_fl);

        backOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) HeaderNormal.this.context;
                activity.finish();
            }
        };
    }

    private void initListener() {
        headerNomalLeftFl.setOnClickListener(backOnClickListener);

    }

    private void initDate(AttributeSet attrs,int defStyle){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HeaderNormal, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.HeaderNormal_title:
                    mTitleText = a.getString(attr);
                    break;
            }
        }
//        mTitleText = a.getString(R.styleable.HeaderNormal_title);
        a.recycle();

        headerTitleTv.setText(mTitleText);

    }


    public void setTitle(String title){
        headerTitleTv.setText(title);
    }

    public void setLeftListener(OnClickListener listener){
        headerNomalLeftFl.setOnClickListener(listener);
    }



    public void setRightListener(OnClickListener listener){
        headerNomalRightFl.setOnClickListener(listener);
    }

    public void setRightVisible(int  visibility){
        headerNomalRightFl.setVisibility(visibility);
    }

    public void setRightText(String text){
        headerNomalRightTv.setText(text);
        headerNomalRightTv.setVisibility(VISIBLE);
    }
    public void setRightTextColor(int  color){
        headerNomalRightTv.setTextColor(color);
        headerNomalRightTv.setVisibility(VISIBLE);

    }

    public void setRightImage(int resource){
        headerNomalRightIv.setImageResource(resource);
        headerNomalRightIv.setVisibility(VISIBLE);
    }

    public void setLeftImage(int resource){
        headerNomalLeftIv.setImageResource(resource);
        headerNomalLeftIv.setVisibility(VISIBLE);
    }

    public void setLeftVisible(int  visibility){
        headerNomalLeftIv.setVisibility(visibility);
    }

    public TextView getHeaderNomalRightTv() {
        return headerNomalRightTv;
    }

    public void setHeaderNomalRightTv(TextView headerNomalRightTv) {
        this.headerNomalRightTv = headerNomalRightTv;
    }
}
