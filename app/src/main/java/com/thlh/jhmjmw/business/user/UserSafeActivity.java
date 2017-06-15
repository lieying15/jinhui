package com.thlh.jhmjmw.business.user;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.user.password.PasswordChangeActivity;
import com.thlh.jhmjmw.business.user.password.PasswordSetActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleLinearLayout;
import com.thlh.viewlib.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 注册页面
 */
public class UserSafeActivity extends BaseViewActivity {

    private static final String TAG = "UserSafeActivity";
    @BindView(R.id.changepw_header)
    HeaderNormal changepwHeader;
    @BindView(R.id.account_login_password_ll)
    RippleLinearLayout accountLoginPasswordLl;
    @BindView(R.id.account_bind_phone_ll)
    RippleLinearLayout accountBindPhoneLl;
    @BindView(R.id.account_paypw_ll)
    RippleLinearLayout accountPaypwLl;
    @BindView(R.id.account_paypw_tv)
    TextView accountPaypwTv;
    @BindView(R.id.usersafe_bindphone_tv)
    TextView accountBindPhoneTv;
    @BindView(R.id.account_nopaypw_btn)
    ToggleButton accountNopaypwBtn;
    @BindView(R.id.account_nopaypw_ll)
    LinearLayout accountNopaypwLl;
    @BindView(R.id.usersafe_bindphone_title_tv)
    TextView phoneTitleTv;


    private int isSetPayPw;
    private String bindMobile;

    private DialogNormal.Builder errorDialog;
    private BaseObserver<BaseResponse> setPayPassSwitch;
    
    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserSafeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_usersafe);
        ButterKnife.bind(this);
        errorDialog = new DialogNormal.Builder(this);
        accountLoginPasswordLl.setLLRippleCompleteListener(
                new RippleLinearLayout.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                        PasswordChangeActivity.activityStart(UserSafeActivity.this);
                    }
                }
        );


        accountBindPhoneLl.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                UserChangeBindPhoneMangeActivity.activityStart(UserSafeActivity.this);
            }
        });


        isSetPayPw = Integer.parseInt(SPUtils.get("user_ispaypass", "0").toString());
        bindMobile = (String) SPUtils.get("user_bind_mobile", "").toString();
        if(!bindMobile.equals("")){
            StringBuilder builder = new StringBuilder();
            builder.append(bindMobile);
            builder.replace(builder.length() -4 ,builder.length(),"****");
            accountBindPhoneTv.setText(builder.toString());
            phoneTitleTv.setText(getResources().getString(R.string.user_bind_phone));
        }else {
            accountBindPhoneTv.setText("");
            phoneTitleTv.setText(getResources().getString(R.string.user_change_bind_phone));
        }
        if (isSetPayPw == 0) {
            accountPaypwTv.setText(getResources().getString(R.string.user_once_set));
            accountNopaypwBtn.setCanToggle(false);
        } else {
            accountPaypwTv.setText(getResources().getString(R.string.user_change_password));
            accountNopaypwBtn.setCanToggle(true);
        }


        accountPaypwLl.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                if (bindMobile.equals("")) {//未绑定手机
                    PhoneVerifyCodeActivity.activityStart(UserSafeActivity.this, Constants.PHONECODE_TYPE_BINDPHONE);
                } else {
                    if (isSetPayPw == 0) { //未设置密码  立即设置
                        PasswordSetActivity.activityStart(UserSafeActivity.this, Constants.PAYPW_TYPE_NEW, "", "");
                    } else {
                        //已设置密码  修改密码
                        PhoneVerifyCodeActivity.activityStart(UserSafeActivity.this, Constants.PHONECODE_TYPE_RESET);
                    }
                }
            }
        });

        int user_ispaypass = Integer.parseInt(SPUtils.get("user_ispaypass", "0").toString());

//        int user_ispaypass = (int) SPUtils.get("user_ispaypass", 0);//支付密码  -1免密开启不使用支付密码 0未设置 1使用支付密码
        switch (user_ispaypass){
            case -1 : accountNopaypwBtn.setToggleOn();break;
            case 0 : accountNopaypwBtn.setCanToggle(false);break;
            case 1 : accountNopaypwBtn.setToggleOff();break;
        }

        accountNopaypwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountNopaypwBtn.isToggleOn()){
                    accountNopaypwBtn.setToggleOff();
                    postOpenNoPayPw();
                }else {
                    PhoneVerifyCodeActivity.activityStart(UserSafeActivity.this,Constants.PHONECODE_TYPE_CLOSE_PAYPW);
                }
            }
        });

        setPayPassSwitch  = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse generateResponse) {
                errorDialog.setSubTitle(generateResponse.getErr_msg())
                        .setRightBtnStr(getResources().getString(R.string.back))
                        .setCancelOutside(false)
                        .setRightClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).create().show();
            }

            @Override
            public void onNextResponse(BaseResponse generateResponse) {
                SPUtils.put("user_ispaypass", 1);
            }
        };
    }

    @Override
    protected void loadData() {
    }

    private void postOpenNoPayPw() {
        L.e(TAG + " 打开免密支付");
        Subscription subscription = NetworkManager.getUserDataApi()
                .setPayPassSwitch(SPUtils.getToken(), "1")//1打开免密支付，不要密码  0关闭免密支付，要密码，需认证
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(setPayPassSwitch);
        subscriptionList.add(subscription);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        int user_ispaypass = (int) SPUtils.get("user_ispaypass", 0);//支付密码  -1免密开启不使用支付密码 0未设置 1使用支付密码
        int user_ispaypass = Integer.parseInt(SPUtils.get("user_ispaypass", "0").toString());
        switch (user_ispaypass){
            case -1 : accountNopaypwBtn.setToggleOn();break;
            case 0 : accountNopaypwBtn.setCanToggle(false);break;
            case 1 : accountNopaypwBtn.setToggleOff();break;
        }
    }



}
