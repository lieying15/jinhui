package com.thlh.jhmjmw.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.user.info.InfoManageActivity;
import com.thlh.jhmjmw.business.goods.search.SearchActivity;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.viewlib.tablayout.MsgView;

/**
 * Created by HQ on 2016/3/30.
 */
public class HeaderIndex extends RelativeLayout {

    private Context context;
    private RelativeLayout homeheaderInfoRl;
    private FrameLayout ScanFl;
    private LinearLayout seachLl;
    private TextView homeheaderInfoTv;
    private ImageView searchIv;

    private OnClickScanListener mOnClickScanListener;
    private Activity activity;
    public HeaderIndex(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initListener();

    }

    public HeaderIndex(Context context) {
        super(context);
        this.context = context;
        initView();
        initListener();

    }

    public HeaderIndex(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initListener();
    }


    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_header_index, this, true);
        ScanFl= (FrameLayout) view.findViewById(R.id.homeheader_scan_fl);
        seachLl = (LinearLayout) view.findViewById(R.id.homeheader_seach_ll);
        homeheaderInfoRl = (RelativeLayout) view.findViewById(R.id.homeheader_info_rl);
        searchIv = (ImageView) view.findViewById(R.id.homeheader_seach_iv);
        homeheaderInfoTv = (MsgView) view.findViewById(R.id.homeheader_info_tv);
    }

    public void initListener() {
        homeheaderInfoRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity !=null){
                    if(SPUtils.getIsLogin()){
                        InfoManageActivity.activityStart(activity);
                    }else {
                        LoginActivity.activityStart(activity);
                    }
                }
            }
        });

        seachLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            if(activity !=null){
                SearchActivity.activityStart(activity);
            }
            }
        });

        ScanFl.setOnClickListener(new OnClickListener() {
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
