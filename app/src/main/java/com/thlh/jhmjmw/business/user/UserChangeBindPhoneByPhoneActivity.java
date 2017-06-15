package com.thlh.jhmjmw.business.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;

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


/**
 * 通过手机修改绑定手机
 */
public class UserChangeBindPhoneByPhoneActivity extends BaseActivity {

    private static final String TAG = "UserChangeBindPhoneByPhoneActivity";
    private final int VERIFYNUMBER_TIME = 60; //重新获取验证码等待时间
    private final String BIND_STEP_REQUEST_VERIFYNUMBER = "1";
    private final String BIND_STEP_COMFRIM_VERIFYNUMBER = "2";
    private final String REGISTER_STEP_FINAL = "3";
    @BindView(R.id.userbind_byphone_header)
    HeaderNormal userbindByphoneHeader;
    @BindView(R.id.userbind_byphone_verify_et)
    EditText userbindByphoneVerifyEt;
    @BindView(R.id.userbind_byphone_verify_ll)
    LinearLayout userbindByphoneVerifyLl;
    @BindView(R.id.userbind_byphone_regetsms_tv)
    TextView userbindByphoneRegetsmsTv;
    @BindView(R.id.userbind_byphone_regetsms_ll)
    LinearLayout userbindByphoneRegetsmsLl;
    @BindView(R.id.userbind_byphone_next_ll)
    LinearLayout userbindByphoneNextLl;
    @BindView(R.id.userbind_byphone_cancel_iv)
    ImageView cancelIv;


    private String phoneNumber;
    private String verifyCode;
    private String  step = BIND_STEP_REQUEST_VERIFYNUMBER;
    private BaseObserver<BaseResponse> verifyNumberObserver;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserChangeBindPhoneByPhoneActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userbind_byphone);
        ButterKnife.bind(this);
        phoneNumber =  (String) SPUtils.get("user_bind_mobile","").toString();

        userbindByphoneNextLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userbindByphoneVerifyEt.getWindowToken(), 0);
                if(judgeCondition()){
                    step = BIND_STEP_COMFRIM_VERIFYNUMBER;
                    postConfrimCode();
                }
            }
        });

        userbindByphoneVerifyEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    userbindByphoneVerifyLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    userbindByphoneVerifyLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });


        verifyNumberObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse response) {
                showWaringDialog(response.getErr_msg());

            }

            @Override
            public void onNextResponse(BaseResponse response) {
                switch (step){
                    case BIND_STEP_REQUEST_VERIFYNUMBER:
                        showSuccessDialog(getResources().getString(R.string.already_send_yzm));
                        break;
                    case BIND_STEP_COMFRIM_VERIFYNUMBER:
                        UserChangeBindPhoneActivity.activityStart(UserChangeBindPhoneByPhoneActivity.this, Constants.BINDPHONE_TYPE_BYPHONE,verifyCode);
                        break;
                }
            }
        };
    }

    @Override
    protected void loadData() {
        requestVerifyNumber();
    }


    public void startVerifyNumberCount(final int countTime) {
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
                        userbindByphoneRegetsmsLl.setClickable(false);
                        userbindByphoneRegetsmsTv.setTextColor(getResources().getColor(R.color.text_tips));
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        userbindByphoneRegetsmsLl.setClickable(true);
                        userbindByphoneRegetsmsTv.setText(getResources().getString(R.string.re_send_code));
                        userbindByphoneRegetsmsTv.setTextColor(getResources().getColor(R.color.blue_light));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                        L.i("当前计时：" + integer);
                        userbindByphoneRegetsmsTv.setText(integer + getResources().getString(R.string.second_send));
                    }
                });
        subscriptionList.add(subscription);
    }


    private void requestVerifyNumber() {
        startVerifyNumberCount(VERIFYNUMBER_TIME);
        NetworkManager.getUserDataApi()
                .changeBindPhoneByPhone(SPUtils.getToken(),BIND_STEP_REQUEST_VERIFYNUMBER)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(verifyNumberObserver);
    }

    private void postConfrimCode() {
        NetworkManager.getUserDataApi()
                .changeBindPhoneByPhone(SPUtils.getToken(),BIND_STEP_COMFRIM_VERIFYNUMBER,verifyCode,phoneNumber)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(verifyNumberObserver);
    }


    @OnClick({R.id.userbind_byphone_regetsms_ll,R.id.userbind_byphone_cancel_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userbind_byphone_regetsms_ll:
                requestVerifyNumber();
                break;
            case R.id.userbind_byphone_cancel_iv:
                finish();
                break;
        }
    }

    private boolean judgeCondition(){
        if(userbindByphoneVerifyEt.getText().toString().trim().equals("")){
            showWaringDialog(getResources().getString(R.string.please_input_yzm));
            return false;
        }
        verifyCode = userbindByphoneVerifyEt.getText().toString().trim();
        return  true;
    }

}
