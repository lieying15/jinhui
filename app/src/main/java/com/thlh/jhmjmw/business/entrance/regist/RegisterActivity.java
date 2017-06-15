package com.thlh.jhmjmw.business.entrance.regist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.response.RegistVerifyNumberResponse;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Func1;

import static android.R.attr.type;


/**
 * 注册页面
 */

public class RegisterActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "RegisterActivity";
    private final int VERIFYNUMBER_TIME = 60; //重新获取验证码等待时间
    private final String REGISTER_STEP_REQUEST_VERIFYCODE = "1";
    private final String REGISTER_STEP_CONFIRM_VERIFYCODE = "2";
    private final String REGISTER_STEP_FINAL = "3";
    @BindView(R.id.regist_topnum_line)
    View registStepLine;
    @BindView(R.id.regist_topnumone_tv)
    TextView registTopnumoneTv;
    @BindView(R.id.regist_toptextone_tv)
    TextView registToptextoneTv;
    @BindView(R.id.regist_topnumtwo_tv)
    TextView registTopnumtwoTv;
    @BindView(R.id.regist_toptexttwo_tv)
    TextView registToptexttwoTv;
    @BindView(R.id.regist_topnumthree_tv)
    TextView registTopnumthreeTv;
    @BindView(R.id.regist_toptextthree_tv)
    TextView registToptextthreeTv;
    @BindView(R.id.regist_topnumfour_tv)
    TextView registTopnumfourTv;
    @BindView(R.id.regist_toptextfour_tv)
    TextView registToptextfourTv;
    @BindView(R.id.regist_header)
    HeaderNormal registHeader;


    private BaseObserver<BaseResponse> verifyCodeObserver,confrimCodeObserver;
    private BaseObserver<RegistVerifyNumberResponse> postDataObserver;


    private String registerAccountStr, verifyCode, passwordStr, passwordComfirmStr;
    private RegistDialogFragment rigistDialog;
    private FragmentTransaction fragmentTransaction ;
    private int registStep = 1 ; //1 输手机号 2输验证吗 3填密码  4完成

    private int lineWith, lineStepWith;


    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        registHeader.setLeftListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rigistDialog.dismiss();
                switch (registStep) {
                    case 1:
                        finish();
                        break;
                    case 2:
                        rigistDialog.setRegistStep(1);
                        rigistDialog.show(getFragmentManager(), "registDialog");
                        startTabAnimation(2,false);
                        break;
                    case 3:
                        rigistDialog.setRegistStep(2);
                        startTabAnimation(3,false);
                        rigistDialog.show(getFragmentManager(), "registDialog");
                        break;
                    case 4:
                        finish();
                        break;
                }
            }
        });


        rigistDialog = new RegistDialogFragment();
        lineWith = registStepLine.getWidth();
        lineStepWith = lineWith / 3;
        rigistDialog.setRegistStep(registStep);
        rigistDialog.setNextEvent(new RegistDialogFragment.onStepListener() {
            @Override
            public void next(int step) {
                rigistDialog.dismiss();
                L.e(" RegisterDialog  next step:" + step);
                if (judgeLoginInfo(step)) {
                    switch (step) {
                        case 1:
                            requestVerifyNumber();
                            break;
                        case 2:
                            verifyCode = rigistDialog.getVerifycodeEt().getText().toString().trim();
                            confirmVerifyNumber();
                            break;
                        case 3:
                            registFianlPostData();
                            break;
                        case 4:
                            LoginActivity.activityStart(RegisterActivity.this);
                            finish();
                            break;

                    }
                }
            }

            @Override
            public void cancel(int step) {
                rigistDialog.dismiss();
                switch (step) {
                    case 1:
                        finish();
                        break;
                    case 2:
                        rigistDialog.setRegistStep(1);
                        rigistDialog.show(getFragmentManager(), "registDialog");
                        startTabAnimation(2,false);
                        break;
                    case 3:
                        rigistDialog.setRegistStep(2);
                        startTabAnimation(3,false);
                        rigistDialog.show(getFragmentManager(), "registDialog");
                        break;
                    case 4:
                        finish();
                        break;
                }
            }

            @Override
            public void getVerifyCode() {
                rigistDialog.dismiss();
                requestVerifyNumber();
            }
        });
        rigistDialog.show(getFragmentManager(), "registDialog");


        verifyCodeObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showRegisterErrorDialog(registStep, baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse registVerifyNumberResponse) {
                registStep = 2;
                showStepDialog(registStep,true);
                startVerifyNumberCount(VERIFYNUMBER_TIME);
            }
        };

        confrimCodeObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showRegisterErrorDialog(registStep, baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse registVerifyNumberResponse) {
                registStep = 3;
                showStepDialog(registStep,true);

            }
        };

        postDataObserver = new BaseObserver<RegistVerifyNumberResponse>() {
            @Override
            public void onNextResponse(RegistVerifyNumberResponse registVerifyNumberBaseResponse) {
                registStep = 4;
                showStepDialog(registStep,true);
            }

            @Override
            public void onErrorResponse(RegistVerifyNumberResponse registVerifyNumberBaseResponse) {
                showErrorDialog(registVerifyNumberBaseResponse.getErr_msg());
                rigistDialog.setRegistStep(3);
                rigistDialog.show(getFragmentManager(), "registDialog");
            }
        };

        startTabAnimation(1,true);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View view) {

    }




    private boolean judgeLoginInfo(int step) {
        switch (step) {
            case 1:
                if (!rigistDialog.isProtocoSelect()) {
                    showWaringDialog(getResources().getString(R.string.register_thlh_agree));
                    return false;
                }
                registerAccountStr = rigistDialog.getPhoneEt().getText().toString().trim();
                if (registerAccountStr.equals("")) {
                    showWaringDialog(getResources().getString(R.string.name_no));
                    return false;
                }
                if (!TextUtils.isPhone(registerAccountStr)) {
                    showWaringDialog(getResources().getString(R.string.input_no));
                    return false;
                }
                return true;
            case 2:
                if (rigistDialog.getVerifycodeEt().getText().toString().trim().equals("")) {
                    showWaringDialog(getResources().getString(R.string.code));
                    return false;
                }
                return true;
            case 3:
                passwordStr = rigistDialog.getPasswordEt().getText().toString().trim();
                passwordComfirmStr = rigistDialog.getPasswordReEt().getText().toString().trim();
                if (passwordStr.equals("") || passwordComfirmStr.equals("")) {
                    showWaringDialog(getResources().getString(R.string.password_null));
                    return false;
                }

                if (passwordStr.length() < 6 || passwordComfirmStr.length() < 6) {
                    showWaringDialog(getResources().getString(R.string.re_password_success_num));
                    return false;
                }

                if (!passwordStr.equals(passwordComfirmStr)) {
                    showWaringDialog(getResources().getString(R.string.input_password_diff));
                    return false;
                }
                return true;
            case 4:
                return true;
            default:
                return false;
        }
    }

    private void requestVerifyNumber() {
        NetworkManager.getUserDataApi()
                .register(REGISTER_STEP_REQUEST_VERIFYCODE, registerAccountStr)
                .compose(RxUtils.androidSchedulers(RegisterActivity.this))
                .subscribe(verifyCodeObserver);
    }

    private void confirmVerifyNumber() {
        NetworkManager.getUserDataApi()
                .register(REGISTER_STEP_CONFIRM_VERIFYCODE, registerAccountStr,verifyCode)
                .compose(RxUtils.androidSchedulers(RegisterActivity.this))
                .subscribe(confrimCodeObserver);
    }


    private void registFianlPostData() {
        String ch = (String) SPUtils.get("user_ch", "").toString();
        L.i(TAG + " 注册 ch" + ch);
        NetworkManager.getUserDataApi()
                .register(REGISTER_STEP_FINAL, registerAccountStr, verifyCode, passwordStr, ch)
                .compose(RxUtils.androidSchedulers(RegisterActivity.this))
                .subscribe(postDataObserver);
    }

    public void startVerifyNumberCount(final int countTime) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(countTime + 1)
                .compose(RxUtils.androidSchedulers(RegisterActivity.this))
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        rigistDialog.getTimeLl().setClickable(false);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        rigistDialog.getTimeLl().setClickable(true);
                        rigistDialog.getTimeTv().setText(getResources().getString(R.string.re_send_code));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                        L.i("当前计时：" + integer);
                        rigistDialog.getTimeTv().setText(integer + getResources().getString(R.string.second_send));
                    }
                });
    }




    private void startTabAnimation(int step,boolean isNext) {
        float startpostion = 0;
        float endpostion = 0;
        if(step >1){
            if(isNext){
                startpostion = 0.33f * (step - 2);
                endpostion = 0.33f * (step - 1);
            }else {
                startpostion = 0.33f * (step - 1);
                endpostion = 0.33f * (step - 2);
            }

        }

        ScaleAnimation animation = new ScaleAnimation(startpostion, endpostion, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(500);
        registStepLine.startAnimation(animation);
    }

    private void showStepDialog(int step , boolean isNext){
        rigistDialog.setRegistStep(step);
        startTabAnimation(step,isNext);
        rigistDialog.show(getFragmentManager(), "registDialog");
    }

    //访问网络错误窗口
    private void showRegisterErrorDialog(int registStep, String contentStr) {
        final NormalDialogFragment normalFrgDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        normalFrgDialog.setContentStr(contentStr);
        normalFrgDialog.setContentType(type);
        normalFrgDialog.setFinalBtnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                normalFrgDialog.dismiss();
                showStepDialog(registStep,false);
            }
        });
        normalFrgDialog.show(ft, "netErrorDialog");
    }

    //下一步条件错误窗口
    @Override
    public void showWaringDialog(String msg) {
        final NormalDialogFragment normalFrgDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        normalFrgDialog.setContentStr(msg);
        normalFrgDialog.setContentType(DialogUtils.TYPE_NORMAL_WARNING);
        normalFrgDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalFrgDialog.dismiss();
                showStepDialog(registStep,false);
            }
        });
        normalFrgDialog.show(ft, "nextErrorDialog");

    }
}
