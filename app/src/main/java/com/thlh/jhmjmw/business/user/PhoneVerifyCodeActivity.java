package com.thlh.jhmjmw.business.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.RegistVerifyNumberResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.user.password.PasswordSetActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 密码找回界面  重置支付密码  绑定手机
 */
public class PhoneVerifyCodeActivity extends BaseActivity {
    private static final String TAG = "PhoneVerifyCodeActivity";

    private final int VERIFYNUMBER_TIME = 60;
    private  int contentType = 0;
    @BindView(R.id.changepw_header)
    HeaderNormal codeHeader;
    @BindView(R.id.pwretrieve_input_phone_et)
    EditText pwretrieveInputPhoneEt;
    @BindView(R.id.pwretrieve_input_phone_ll)
    LinearLayout pwretrieveInputPhoneLl;
    @BindView(R.id.pwretrieve_input_verifysms_et)
    EditText pwretrieveInputVerifysmsEt;
    @BindView(R.id.pwretrieve_input_verifysms_ll)
    LinearLayout pwretrieveInputVerifysmsLl;
    @BindView(R.id.pwretrieve_verifysms_rip)
    RippleRelativeLayout pwretrieveVerifysmsRipLl;
    @BindView(R.id.pwretrieve_resend_tv)
    TextView pwretrieveResendTv;
    @BindView(R.id.pwretrieve_verifysms_tv)
    TextView pwretrieveVerifysmsTv;



    private String verifyCode;
    private String phone;
    private BaseObserver<BaseResponse> retrieveObserver ,verifyPayPwObserver,getBaseObserver;
    private BaseObserver<RegistVerifyNumberResponse> getSMSObserver;
    private boolean isCounting = false;
    private int setType = Constants.PHONECODE_TYPE_PWRESET;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PhoneVerifyCodeActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Context context,int setType) {
        Intent intent = new Intent();
        intent.putExtra("setType", setType);
        intent.setClass(context, PhoneVerifyCodeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {
        setType = getIntent().getIntExtra("setType",Constants.PHONECODE_TYPE_PWRESET);
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password_retrieve);
        ButterKnife.bind(this);

        switch (setType){
            case Constants.PHONECODE_TYPE_PWRESET:
                pwretrieveInputPhoneEt.setText((String) SPUtils.get("user_bind_mobile","").toString());
                pwretrieveInputPhoneEt.setMaxLines(11);
                codeHeader.setTitle(getResources().getString(R.string.pwretrieve_header_name));break;
            case Constants.PHONECODE_TYPE_BINDPHONE:
                codeHeader.setTitle(getResources().getString(R.string.bind_phone));break;
            case Constants.PHONECODE_TYPE_RESET:
                pwretrieveInputPhoneEt.setText((String) SPUtils.get("user_bind_mobile","").toString());
            case Constants.PHONECODE_TYPE_CLOSE_PAYPW:
                pwretrieveInputPhoneEt.setText((String) SPUtils.get("user_bind_mobile","").toString());
                codeHeader.setTitle(getResources().getString(R.string.safe_));
                pwretrieveVerifysmsTv.setText(getResources().getString(R.string.trues));break;
                
        }

        pwretrieveResendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pwretrieveInputPhoneEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwretrieveInputVerifysmsEt.getWindowToken(), 0);
                if(judgeCodeCondition()){
                    switch (setType){
                        case Constants.PHONECODE_TYPE_PWRESET:
                            getPWVerifyCode();break;
                        case Constants.PHONECODE_TYPE_BINDPHONE:
                            getBindPhoneCode();break;
                        case Constants.PHONECODE_TYPE_RESET:
                            getPayPWVerifyCode();break;
                        case Constants.PHONECODE_TYPE_CLOSE_PAYPW:
                            getClosePayPwVerifyCode();break;
                    }
                }
            }});



        pwretrieveVerifysmsRipLl.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pwretrieveInputPhoneEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwretrieveInputVerifysmsEt.getWindowToken(), 0);
                if(judgePostCondition()){
                    switch (setType){
                        case Constants.PHONECODE_TYPE_PWRESET:
                            postPwRetrieve();break;
                        case Constants.PHONECODE_TYPE_BINDPHONE:
                            postBindPhone();break;
                        case Constants.PHONECODE_TYPE_RESET:
                            postPayPwRetrieve();break;
                        case Constants.PHONECODE_TYPE_CLOSE_PAYPW:
                            postClosePayPwVerifyCode();break;
                    }
                }
            }
        });


        retrieveObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                PasswordSetActivity.activityStart(PhoneVerifyCodeActivity.this,setType,verifyCode,phone);
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };

        verifyPayPwObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                SPUtils.put("user_ispaypass", -1);//支付密码  -1免密开启不使用支付密码 0未设置 1使用支付密码
                finish();
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };

        getBaseObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                getProgressMaterial().dismiss();
                startVerifyNumberCount(VERIFYNUMBER_TIME);
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };


        getSMSObserver = new BaseObserver<RegistVerifyNumberResponse>() {
            @Override
            public void onNextResponse(RegistVerifyNumberResponse baseResponse) {
                getProgressMaterial().dismiss();
                startVerifyNumberCount(VERIFYNUMBER_TIME);
            }

            @Override
            public void onErrorResponse(RegistVerifyNumberResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };

    }

    @Override
    protected void loadData() {
    }



    private boolean judgeCodeCondition(){
        if(isCounting){
            L.e(TAG + " judgeCodeCondition isCounting:" + isCounting);
            return false;
        }
        phone = pwretrieveInputPhoneEt.getText().toString().trim();
        L.i(TAG + " judgeCodeCondition phone:" + phone);
        if (! TextUtils.isPhone(phone)) {
            showErrorDialog(getResources().getString(R.string.input_phone_no_ture));
            return false;
        }
        return true;
    }

    private void getPWVerifyCode() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .retrievePw( "1", phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSMSObserver);
        subscriptionList.add(subscription);
    }

    private void getPayPWVerifyCode() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .retrievePayPw(SPUtils.getToken(), "1", phone,"","")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSMSObserver);
        subscriptionList.add(subscription);
    }

    private void getBindPhoneCode() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .bindPhone(SPUtils.getToken(), "1", phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSMSObserver);
        subscriptionList.add(subscription);
    }

    private void getClosePayPwVerifyCode() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .setPayPassSwitch(SPUtils.getToken(), "0","1","",phone)//1打开免密支付，不要密码  0关闭免密支付，要密码，需认证
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getBaseObserver);
        subscriptionList.add(subscription);
    }

    private boolean judgePostCondition(){
        phone = pwretrieveInputPhoneEt.getText().toString().trim();
        verifyCode = pwretrieveInputVerifysmsEt.getText().toString().trim();
        if (verifyCode.equals("")) {
            showErrorDialog(getResources().getString(R.string.code));
            return false;
        }
        L.i(TAG +" postPwRetrieve phone" + phone + " verifyCode"+verifyCode);
        return true;
    }

    private void postPwRetrieve() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .retrievePw("2", phone, verifyCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retrieveObserver);
        subscriptionList.add(subscription);
    }

    private void postPayPwRetrieve() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .retrievePayPw(SPUtils.getToken(), "2", phone,verifyCode,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retrieveObserver);
        subscriptionList.add(subscription);
    }

    private void postClosePayPwVerifyCode() {
        Subscription subscription = NetworkManager.getUserDataApi()
                .setPayPassSwitch(SPUtils.getToken(), "0","3",verifyCode,phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(verifyPayPwObserver);
        subscriptionList.add(subscription);
    }

    private void postBindPhone() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .bindPhone(SPUtils.getToken(),"2", verifyCode,phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retrieveObserver);
        subscriptionList.add(subscription);
    }

    public void startVerifyNumberCount(final int countTime) {
        L.i(TAG +" startVerifyNumberCount");
        Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
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
                        isCounting = true;
                        pwretrieveResendTv.setTextColor(getResources().getColor(R.color.text_tips));
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        isCounting = false;
                        pwretrieveResendTv.setTextColor(getResources().getColor(R.color.blue_light));
                        pwretrieveResendTv.setText(getResources().getString(R.string.re_send_code));
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Integer integer) {
                        L.e("当前计时：" + integer );
                        pwretrieveResendTv.setText(integer + getResources().getString(R.string.second_send));
                    }
                });
        subscriptionList.add(subscription);
    }


}
