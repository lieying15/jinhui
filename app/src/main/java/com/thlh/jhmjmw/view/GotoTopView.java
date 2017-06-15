package com.thlh.jhmjmw.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thlh.jhmjmw.R;

/**
 * 用于回顶部的控件
 */
public class GotoTopView extends LinearLayout {
    public static final   int TYPE_SINGLE = 1;
    public static final   int TYPE_DOUBLE = 2;
    private Context context;
    private int type;
    private ImageView singleIv,doubleLeftIv,doubleRightIv;

    public GotoTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public GotoTopView(Context context) {
        super(context);
        this.context = context;
        initView();

    }

    public GotoTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }




    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gototop, this, true);

        singleIv = ((ImageView) view.findViewById(R.id.gototop_single));
        doubleLeftIv = ((ImageView) view.findViewById(R.id.gototop_double_top));
        doubleRightIv = ((ImageView) view.findViewById(R.id.gototop_double_back));
        doubleRightIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                IndexActivity.activityStart(context);
                ((Activity)context).finish();
            }
        });
        setType(TYPE_SINGLE);
    }


    public void setTopClick(View.OnClickListener onClickListener){
        singleIv.setOnClickListener(onClickListener);
        doubleLeftIv.setOnClickListener(onClickListener);
    }
    
    public void setType(int type){
        switch (type){
            case TYPE_SINGLE:
                singleIv.setVisibility(VISIBLE);
                doubleLeftIv.setVisibility(GONE);
                doubleRightIv.setVisibility(GONE);break;
            case TYPE_DOUBLE:
                singleIv.setVisibility(GONE);
                doubleLeftIv.setVisibility(VISIBLE);
                doubleRightIv.setVisibility(VISIBLE);break;
        }
    }
}
