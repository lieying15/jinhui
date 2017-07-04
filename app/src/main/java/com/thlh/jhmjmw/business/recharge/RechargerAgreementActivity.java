package com.thlh.jhmjmw.business.recharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.view.HeaderNormal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargerAgreementActivity extends BaseActivity {


    @BindView(R.id.recharge_agreement_header)
    HeaderNormal rechargeAgreementHeader;
    @BindView(R.id.beijingortianjin_tv)
    TextView beijingortianjinTv;
    @BindView(R.id.shengzhen_tv)
    TextView shengzhenTv;
    @BindView(R.id.hadRechargeBox_tv)
    TextView hadRechargeBoxTv;
    @BindView(R.id.activity_recharger_agreement)
    RelativeLayout activityRechargerAgreement;
    private int agency_id;
    private boolean hadRechargeBox;

    @Override
    protected void initVariables() {
        agency_id = Integer.parseInt(SPUtils.get("user_agency_id", "0").toString());
        hadRechargeBox = Boolean.valueOf(SPUtils.get("user_hadchange_icebox", false).toString());
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharger_agreement);
        ButterKnife.bind(this);

        if (hadRechargeBox){
            beijingortianjinTv.setVisibility(View.GONE);
            shengzhenTv.setVisibility(View.GONE);
            hadRechargeBoxTv.setVisibility(View.VISIBLE);
        }  else {
            if (agency_id == 10 || agency_id == 11) {
                beijingortianjinTv.setVisibility(View.VISIBLE);
                shengzhenTv.setVisibility(View.GONE);
                hadRechargeBoxTv.setVisibility(View.GONE);
            } else if (agency_id == 37) {
                beijingortianjinTv.setVisibility(View.GONE);
                shengzhenTv.setVisibility(View.VISIBLE);
                hadRechargeBoxTv.setVisibility(View.GONE);
            }else {
                beijingortianjinTv.setVisibility(View.GONE);
                shengzhenTv.setVisibility(View.VISIBLE);
                hadRechargeBoxTv.setVisibility(View.GONE);
            }
        }

        beijingortianjinTv.setText(getResources().getString(R.string.bjtj));
        shengzhenTv.setText(getResources().getString(R.string.bjtj));
        hadRechargeBoxTv.setText(getResources().getString(R.string.bjtj));
    }

    @Override
    protected void loadData() {

    }

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, RechargerAgreementActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
