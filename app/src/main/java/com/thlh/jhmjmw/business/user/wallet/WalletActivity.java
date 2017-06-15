package com.thlh.jhmjmw.business.user.wallet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 我的钱包
 */
public class WalletActivity extends BaseViewActivity {
    private static final String TAG = "WalletActivity";
    private final int ACTIVITY_CODE_RECHARGE = 0;

    @BindView(R.id.wallet_header)
    HeaderNormal walletHeader;

    @BindView(R.id.wallet_mjz_ripll)
    RippleLinearLayout mjzRipll;

    @BindView(R.id.wallet_record_ripll)
    RippleLinearLayout recordRipll;




    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, WalletActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        mjzRipll.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                MjzActivity.activityStart(WalletActivity.this);
            }
        });

        recordRipll.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                MjzActivity.activityStart(WalletActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void loadData() {}


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }


}
