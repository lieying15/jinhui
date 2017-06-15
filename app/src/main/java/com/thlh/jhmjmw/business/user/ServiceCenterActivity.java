package com.thlh.jhmjmw.business.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HQ on 2016/4/6. 客服中心页
 */
public class ServiceCenterActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "ServiceCenterActivity";
    @BindView(R.id.service_center_phone_tv)
    TextView serviceCenterPhoneTv;
    @BindView(R.id.service_center_rip)
    RippleRelativeLayout serviceCenterRip;


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, ServiceCenterActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_service_center);
        ButterKnife.bind(this);
        serviceCenterRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleView) {
                Tos.show(getResources().getString(R.string.call_phone));
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {

    }




    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
