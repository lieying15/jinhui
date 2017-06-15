package com.thlh.jhmjmw.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.search.SearchActivity;
import com.thlh.jhmjmw.business.user.info.InfoManageActivity;
import com.thlh.viewlib.tablayout.MsgView;

/**
 * Created by HQ on 2016/3/30.
 */
public class HeaderHomePage extends RelativeLayout {

    private Context context;
    private RelativeLayout homeheaderInfoRl;
    private FrameLayout flHomeheaderScan;
    private LinearLayout llHomeheaderSeach;
    private TextView homeheaderInfoTv;

    private OnClickScanListener mOnClickScanListener;
    private Activity activity;
    public HeaderHomePage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initListener();

    }

    public HeaderHomePage(Context context) {
        super(context);
        this.context = context;
        initView();
        initListener();

    }

    public HeaderHomePage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initListener();
    }


    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_header_homepage, this, true);
        flHomeheaderScan= (FrameLayout) view.findViewById(R.id.homeheader_scan_fl);
        llHomeheaderSeach = (LinearLayout) view.findViewById(R.id.homeheader_seach_ll);
        homeheaderInfoRl = (RelativeLayout) view.findViewById(R.id.homeheader_info_rl);
        homeheaderInfoTv = (MsgView) view.findViewById(R.id.homeheader_info_tv);

    }

    public void initListener() {
        homeheaderInfoRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity !=null){
                    InfoManageActivity.activityStart(activity);

                }
            }
        });

        llHomeheaderSeach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            if(activity !=null){
                SearchActivity.activityStart(activity);

            }
            }
        });

        flHomeheaderScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickScanListener.onClick(view);

            }
        });

    }

    public void setHomeheaderInfoTv(String number) {
        if(number.equals("0")||number.equals("")){
            this.homeheaderInfoTv.setVisibility(INVISIBLE);
        }else {
            this.homeheaderInfoTv.setText(number);
            this.homeheaderInfoTv.setVisibility(VISIBLE);
        }
    }

    public interface OnClickScanListener {
        void onClick(View view);

    }

    public void setOnClickScanListener(OnClickScanListener onClickListener) {
        mOnClickScanListener = onClickListener;
    }


    public void setActivityContext(Activity activity){
        this.activity = activity;
    }





}
