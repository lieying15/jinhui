package com.thlh.jhmjmw.business.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.viewlib.ripple.RippleLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 个人信息页面
 */
public class UserChangeBindPhoneMangeActivity extends BaseViewActivity {
    private static final String TAG = "UserChangeBindPhoneMangeActivity";
    @BindView(R.id.userbindphone_manage_header)
    HeaderNormal userbindphoneManageHeader;
    @BindView(R.id.userbindphone_manage_cansms_ll)
    RippleLinearLayout cansmsLl;
    @BindView(R.id.userbindphone_manage_cantsms_ll)
    RippleLinearLayout cantsmsLl;
    @BindView(R.id.bindmobile_noinfoview)
    NoInfoView noinfoview;
    @BindView(R.id.userbindphone_manage_ll)
    LinearLayout manageLl;

    
    private String bindMoblie;

    public static void activityStart(Context context ) {
        Intent intent = new Intent();
        intent.setClass(context, UserChangeBindPhoneMangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        bindMoblie = (String) SPUtils.get("user_bind_mobile","").toString();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userinfo_bindphone);
        ButterKnife.bind(this);

        if(bindMoblie.equals("")){
            userbindphoneManageHeader.setTitle(getResources().getString(R.string.person_bind_phone));
            noinfoview.setTitleIv(R.drawable.img_dialog_phone);
            noinfoview.setVisibility(View.VISIBLE);
            manageLl.setVisibility(View.GONE);
            noinfoview.setTitle(getResources().getString(R.string.person_no_bind_phone));
            noinfoview.setTitleIv(R.drawable.noinfo_bindmobile);
            noinfoview.setNextactionStr(getResources().getString(R.string.bind_phone));
            noinfoview.setNextactionListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneVerifyCodeActivity.activityStart(UserChangeBindPhoneMangeActivity.this, Constants.PHONECODE_TYPE_BINDPHONE);

                }
            });
        }else {
            noinfoview.setVisibility(View.GONE);
            manageLl.setVisibility(View.VISIBLE);
        }


        cansmsLl.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                UserChangeBindPhoneByPhoneActivity.activityStart(UserChangeBindPhoneMangeActivity.this);
            }
        });

        cantsmsLl.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                UserChangeBindPhoneByPwActivity.activityStart(UserChangeBindPhoneMangeActivity.this);
            }
        });
    }


    @Override
    protected void loadData() {
    }



}
