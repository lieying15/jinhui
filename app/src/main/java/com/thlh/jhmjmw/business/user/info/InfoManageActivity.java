package com.thlh.jhmjmw.business.user.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.view.DialogInfoManage;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.tablayout.MsgView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息管理页面
 */
public class InfoManageActivity extends BaseViewActivity {
    private final String TAG = "InfoManageActivity";
    @BindView(R.id.info_manage_header)
    HeaderNormal infoManageHeader;
    @BindView(R.id.info_system_num_tv)
    MsgView infoSystemNumTv;
    @BindView(R.id.info_system_ll)
    LinearLayout infoSystemLl;
    @BindView(R.id.info_promotion_num_tv)
    MsgView infoPromotionNumTv;
    @BindView(R.id.info_promotion_ll)
    LinearLayout infoPromotionLl;
    @BindView(R.id.info_transportation_num_tv)
    MsgView infoTransportationNumTv;
    @BindView(R.id.info_transportation_ll)
    LinearLayout infoTransportationLl;
    @BindView(R.id.info_coupon_num_tv)
    MsgView infoCouponNumTv;
    @BindView(R.id.info_coupon_ll)
    LinearLayout infoCouponLl;

    private DialogInfoManage.Builder infoDialog ;
    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, InfoManageActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {


    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_info_manage);
        ButterKnife.bind(this);
        infoDialog = new  DialogInfoManage.Builder(this);
//        infoManageHeader.setRightImage(R.drawable.icon_setting_white);
//        infoManageHeader.setRightListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                infoDialog.setButtonListener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                }).create().show();
//            }
//        });



    }


    @Override
    protected void loadData() {

    }



    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }


    @OnClick({R.id.info_system_ll, R.id.info_promotion_ll, R.id.info_transportation_ll, R.id.info_coupon_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_system_ll:
                InfoSystemActivity.activityStart(this);
                break;
            case R.id.info_promotion_ll:
                InfoPromotionActivity.activityStart(this);
                break;
            case R.id.info_transportation_ll:
                InfoTransportionActivity.activityStart(this);
                break;
            case R.id.info_coupon_ll:
                InfoCouponActivity.activityStart(this);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }
}
