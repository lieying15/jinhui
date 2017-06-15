package com.thlh.jhmjmw.business.entrance.regist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.model.response.LoginResponse;
import com.thlh.baselib.model.response.RegistVerifyNumberResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.thlh.baselib.utils.SPUtils.put;


/**
 * 注册页面
 */
public class RegisterPasswordActivity extends BaseActivity {
    private static final String TAG = "RegisterPasswordActivity";
    private final int VERIFYNUMBER_TIME = 60; //重新获取验证码等待时间
    private final String REGISTER_STEP_REQUEST_VERIFYNUMBER = "1";
    private final String REGISTER_STEP_COMFRIM_VERIFYNUMBER = "2";
    private final String REGISTER_STEP_FINAL = "3";

    @BindView(R.id.regist_verify_regetsms_ll)
    LinearLayout registVerifyRegetsmsLl;
    @BindView(R.id.regist_verify_regetsms_tv)
    TextView registVerifyRegetsmsTv;
    @BindView(R.id.regist_password_et)
    EditText registPasswordEt;
    @BindView(R.id.regist_password_confirm_et)
    EditText registPasswordConfirmEt;
    @BindView(R.id.regist_password_next_rip)
    RippleRelativeLayout registPasswordNextRip;
//    @BindView(R.id.regist_password_snack_cl)
//    CoordinatorLayout registPasswordSnackCl;
    @BindView(R.id.regist_phone_et)
    EditText registPhoneEt;
    @BindView(R.id.regist_password_hint)
    TextView registPWHint;
    @BindView(R.id.regist_password_header)
    HeaderNormal registHeader;

    private String phoneNumber;
    private String verifyCode;
    private String password;
    private BaseObserver<RegistVerifyNumberResponse> postDataObserver;
    private BaseObserver<BaseResponse> verifyNumberObserver;
    private BaseObserver<LoginResponse> loginNormalObserver;

    public static void activityStart(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.putExtra("phoneNumber", phoneNumber);
//        intent.putExtra("verifyCode", verifyCode);
        intent.setClass(context, RegisterPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        phoneNumber = getIntent().getStringExtra("phoneNumber");
//        verifyCode = getIntent().getStringExtra("verifyCode");
        L.i(TAG + " : phoneNumber " + phoneNumber + " verifyCode " + verifyCode);
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regist_password);
        ButterKnife.bind(this);
//        new S.Builder(registPasswordSnackCl, "已经发送验证码").create().show();
//        new S.Builder(RegisterPasswordActivity.this, registPasswordSnackCl, "已经发送验证码").create().show();
//        registPasswordSnackCl.setMinimumHeight(BaseApplication.height);

        registPasswordNextRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(registPasswordEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(registPasswordConfirmEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(registPhoneEt.getWindowToken(), 0);

                String passwordStr = registPasswordEt.getText().toString().trim();
                String passwordComfirmStr = registPasswordConfirmEt.getText().toString().trim();
                if (registPhoneEt.getText().toString().trim().equals("")) {
                    showWaringDialog(getResources().getString(R.string.code));
                    return;
                }

                if (passwordStr.length() <  6) {
                    registPWHint.setTextColor(getResources().getColor(R.color.red));
                    showWaringDialog(getResources().getString(R.string.re_password_success_num));
                    return;
                }

                if (passwordStr.equals(passwordComfirmStr)) {
                    password = passwordStr;
                    verifyCode = registPhoneEt.getText().toString().trim();
                    registFianlPostData();
                } else {
                    showWaringDialog(getResources().getString(R.string.input_password_diff));
                    return;
                }
            }
        });


        //step 3最终上传信息
        postDataObserver = new BaseObserver<RegistVerifyNumberResponse>() {
            @Override
            public void onNextResponse(RegistVerifyNumberResponse registVerifyNumberBaseResponse) {
                normalLogin();
            }

            @Override
            public void onErrorResponse(RegistVerifyNumberResponse registVerifyNumberBaseResponse) {
                showFailedDialog();
            }
        };

        verifyNumberObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showWaringDialog(baseResponse.getErr_msg());
                progressMaterial.dismiss();
            }

            @Override
            public void onNextResponse(BaseResponse registVerifyNumberResponse) {
                progressMaterial.dismiss();
                showWaringDialog(getResources().getString(R.string.already_send_yzm));
                startVerifyNumberCount(VERIFYNUMBER_TIME);
            }
        };

        loginNormalObserver = new BaseObserver<LoginResponse>() {
            @Override
            public void onNextResponse(LoginResponse loginBaseResponse) {
                L.i(TAG + " loginNormalObserver onNextResponse");
                progressMaterial.dismiss();
                if (loginBaseResponse.getData() != null) {
                    //存储用户数据
                    saveUserInfo(loginBaseResponse);
                    L.i(TAG + " saveAccountInfo  存储完毕!");
                    showSucessDialog();
                }
            }

            @Override
            public void onErrorResponse(LoginResponse loginBaseResponse) {
                L.i(TAG + " onErrorResponse ");
                progressMaterial.dismiss();
                showWaringDialog(loginBaseResponse.getErr_msg());
            }
        };

    }

    @Override
    protected void loadData() {
        startVerifyNumberCount(VERIFYNUMBER_TIME);
    }


    private void registFianlPostData() {
        progressMaterial.show();
        String ch = (String) SPUtils.get("user_ch","").toString();
        L.i(TAG + " 注册 ch" + ch);
        Subscription subscription = NetworkManager.getUserDataApi()
                .register(REGISTER_STEP_FINAL, phoneNumber,verifyCode,password,ch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postDataObserver);
        subscriptionList.add(subscription);
    }

    protected void normalLogin() {
        L.e(TAG + " normalLogin");
        Subscription subscription = NetworkManager.getUserDataApi()
                .login(phoneNumber, password,BaseApplication.getInstance().getPhoneDevice())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginNormalObserver);
        subscriptionList.add(subscription);
    }



    public void showSucessDialog(){
        ActionResponse response = new ActionResponse();
        response.setHeadertitle(getResources().getString(R.string.regist_header_name));
        response.setTitle(getResources().getString(R.string.congratulation));
        response.setContent(getResources().getString(R.string.regist_header_name_success));
        ResponseActivity.activityStart(RegisterPasswordActivity.this,response);
        finish();
    }

    public void showFailedDialog(){
        ActionResponse response = new ActionResponse();
        response.setHeadertitle(getResources().getString(R.string.regist_header_name));
        response.setTitle(getResources().getString(R.string.sorry));
        response.setContent(getResources().getString(R.string.regist_fail));
        response.setSuccess(false);
        ResponseActivity.activityStart(RegisterPasswordActivity.this,response);
        finish();
    }


    public void startVerifyNumberCount(final int countTime) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(countTime + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        registVerifyRegetsmsTv.setClickable(false);
                        registVerifyRegetsmsLl.setBackgroundResource(R.drawable.shap_radius_mainback);
                        registVerifyRegetsmsTv.setTextColor(getResources().getColor(R.color.text_tips));
                        registVerifyRegetsmsLl.setBackgroundResource(R.drawable.shap_radius_mainback);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        registVerifyRegetsmsTv.setClickable(true);
                        registVerifyRegetsmsTv.setText(getResources().getString(R.string.re_send_code));
                        registVerifyRegetsmsTv.setTextColor(getResources().getColor(R.color.white));
                        registVerifyRegetsmsLl.setBackgroundResource(R.drawable.shap_radius_winelight_r20);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                        L.i("当前计时：" + integer);
                        registVerifyRegetsmsTv.setText( integer + getResources().getString(R.string.second_re_send_code) );
                    }
                });
    }

    @OnClick(R.id.regist_verify_regetsms_tv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist_verify_regetsms_tv:

                requestVerifyNumber();
                break;

        }

    }

    private void requestVerifyNumber() {
        progressMaterial.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(registPasswordEt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(registPasswordConfirmEt.getWindowToken(), 0);

        Subscription subscription = NetworkManager.getUserDataApi()
                .register(REGISTER_STEP_REQUEST_VERIFYNUMBER, phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(verifyNumberObserver);
        subscriptionList.add(subscription);
    }


    private void saveUserInfo(LoginResponse loginBaseResponse){
        L.i(TAG + "login Token : " + loginBaseResponse.getData().getAccount().getToken());
        L.i(TAG + "login userinfo : user_inner_member" + loginBaseResponse.getData().getAccount().getInner_member());
        L.i(TAG + "login userinfo :  user_nickname" + loginBaseResponse.getData().getAccount().getNickname());
        L.i(TAG + "login userinfo :  user_equipmentid" + loginBaseResponse.getData().getAccount().getEquipment_id());
        L.i(TAG + "login userinfo :  user_avatar" + loginBaseResponse.getData().getAccount().getAvatar());
        L.i(TAG + "login userinfo :  user_storeid" + loginBaseResponse.getData().getAccount().getStore_id());
        L.i(TAG + "login userinfo :  user_bind_mobile" + loginBaseResponse.getData().getAccount().getBind_mobile());
        L.i(TAG + "login userinfo :  user_wait_pay" + loginBaseResponse.getData().getOrder_info().getWait_pay());
        L.i(TAG + "login userinfo :  user_wait_deliver" + loginBaseResponse.getData().getOrder_info().getWait_deliver());
        L.i(TAG + "login userinfo :  user_wait_get" + loginBaseResponse.getData().getOrder_info().getWait_get());
        L.i(TAG + "login userinfo :  user_return_goods" + loginBaseResponse.getData().getOrder_info().getReturn_goods());
        L.i(TAG + "login userinfo :  user_wait_comment" + loginBaseResponse.getData().getOrder_info().getWait_comment());
        L.i(TAG + "login userinfo :  user_systeminfo_amount" + loginBaseResponse.getData().getSysteminfo().getInfo_amount());

        SPUtils.setLogin(true);
        put("needupdate_userinfo", (boolean) true);
        put("login_type", Constants.LOGIN_TYPE_NORMAL);
        SPUtils.setToken(loginBaseResponse.getData().getAccount().getToken());

        SPUtils.setPassword(password);
        put("user_nickname", loginBaseResponse.getData().getAccount().getNickname());
        put("user_inner_member", loginBaseResponse.getData().getAccount().getInner_member());
        put("user_avatar", loginBaseResponse.getData().getAccount().getAvatar());
        put("user_storeid", loginBaseResponse.getData().getAccount().getStore_id());
        put("user_bind_mobile", loginBaseResponse.getData().getAccount().getBind_mobile());
        put("user_equipmentid", loginBaseResponse.getData().getAccount().getEquipment_id()+"");
        put("user_wait_pay", loginBaseResponse.getData().getOrder_info().getWait_pay());
        put("user_wait_deliver", loginBaseResponse.getData().getOrder_info().getWait_deliver());
        put("user_wait_get", loginBaseResponse.getData().getOrder_info().getWait_get());
        put("user_return_goods", loginBaseResponse.getData().getOrder_info().getReturn_goods());
        put("user_wait_comment", loginBaseResponse.getData().getOrder_info().getWait_comment());
        put("user_systeminfo_amount", loginBaseResponse.getData().getSysteminfo().getInfo_amount());
    }


}
