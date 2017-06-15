package com.thlh.jhmjmw.business.user.password;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 密码相关页  找回,设置修改等
 */
public class PasswordSetActivity extends BaseActivity {
    private static final String TAG = "PhoneVerifyCodeActivity";


    @BindView(R.id.password_reset_header)
    HeaderNormal passwordResetHeader;
    @BindView(R.id.password_reset_input_newpw_et)
    EditText passwordResetInputNewpwEt;
    @BindView(R.id.password_reset_input_newpw_ll)
    LinearLayout passwordResetInputNewpwLl;
    @BindView(R.id.password_reset_confirm_newpw_et)
    EditText passwordResetConfirmNewpwEt;
    @BindView(R.id.password_reset_hint_tv)
    TextView passwordResethintTv;
    @BindView(R.id.password_reset_confirm_newpw_ll)
    LinearLayout passwordResetConfirmNewpwLl;
    @BindView(R.id.password_reset_next_rip)
    RippleRelativeLayout passwordResetNextRip;

    private BaseObserver<BaseResponse> resetObserver;
    private String verifyCode;
    private String phone;
    private int setType = 0;


    private String newpw ;
    private String confirmpw;

    public static void activityStart(Context context,int setType) {
        Intent intent = new Intent();
        intent.putExtra("setType", setType);
        intent.setClass(context, PasswordSetActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Context context,int setType, String verifyCode, String phone) {
        Intent intent = new Intent();
        intent.putExtra("setType", setType);
        intent.putExtra("verifyCode", verifyCode);
        intent.putExtra("phone", phone);
        intent.setClass(context, PasswordSetActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {
        setType = getIntent().getIntExtra("setType", Constants.PHONECODE_TYPE_PWRESET);
        if(setType != Constants.PAYPW_TYPE_NEW){
            verifyCode = getIntent().getStringExtra("verifyCode");
            phone = getIntent().getStringExtra("phone");
        }
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password_reset);
        ButterKnife.bind(this);
        switch (setType){
            case Constants.PHONECODE_TYPE_PWRESET:
                passwordResetHeader.setTitle(getResources().getString(R.string.reset_password));
                passwordResethintTv.setText(getResources().getString(R.string.reset_password_lengh));break;
            case Constants.PHONECODE_TYPE_BINDPHONE:
                passwordResetHeader.setTitle(getResources().getString(R.string.set_password));
                passwordResethintTv.setText(getResources().getString(R.string.reset_password_lengh));break;
            case Constants.PAYPW_TYPE_NEW:
                passwordResetHeader.setTitle(getResources().getString(R.string.set_pay_password));
                passwordResetInputNewpwEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                passwordResetConfirmNewpwEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                passwordResetConfirmNewpwEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                passwordResetInputNewpwEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                passwordResethintTv.setText(getResources().getString(R.string.password_num));break;
            case Constants.PHONECODE_TYPE_RESET:
                passwordResetHeader.setTitle(getResources().getString(R.string.reset_pay_password));
                passwordResetInputNewpwEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                passwordResetConfirmNewpwEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                passwordResetConfirmNewpwEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                passwordResetInputNewpwEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                passwordResethintTv.setText(getResources().getString(R.string.password_num));break;
        }
        
        passwordResetNextRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passwordResetInputNewpwEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(passwordResetConfirmNewpwEt.getWindowToken(), 0);
                if(judgePost()){
                    switch (setType){
                        case Constants.PHONECODE_TYPE_PWRESET:
                            postRetrievePw();
                        case Constants.PHONECODE_TYPE_BINDPHONE:
                            postBindPhone();break;
                        case Constants.PAYPW_TYPE_NEW:
                            postNewPayPw();break;
                        case Constants.PHONECODE_TYPE_RESET:
                            postReSetPayPw();break;
                    }
                }
            }
        });

        passwordResetInputNewpwEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    passwordResetInputNewpwLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    passwordResetInputNewpwLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });

        passwordResetConfirmNewpwEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    passwordResetConfirmNewpwLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    passwordResetConfirmNewpwLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });

        resetObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                progressMaterial.dismiss();
                ActionResponse response = new ActionResponse();
                String responseTitle ="";
                String responseContent =getResources().getString(R.string.password_set_success);
                switch (setType){
                    case Constants.PHONECODE_TYPE_PWRESET:
                        responseTitle = getResources().getString(R.string.set_new_password);
                        break;
                    case Constants.PHONECODE_TYPE_BINDPHONE:
                        responseTitle = getResources().getString(R.string.bind_phone);
                        responseContent = getResources().getString(R.string.bind_phone_success);
                        break;
                    case Constants.PAYPW_TYPE_NEW:
                        responseTitle = getResources().getString(R.string.set_pay_password);
                        SPUtils.put("user_ispaypass", 1);//是否设定支付密码
                        break;
                    case Constants.PHONECODE_TYPE_RESET:
                        responseTitle = getResources().getString(R.string.reset_pay_password);
                        response.setBackType(Constants.RESPONSE_BACK_TYPE_PAYPW);
                        response.setBackStr(getResources().getString(R.string.back));
                        break;
                }
                response.setHeadertitle(responseTitle);
                response.setTitle(getResources().getString(R.string.congratulation));
                response.setContent(responseContent);
                ResponseActivity.activityStart(PasswordSetActivity.this,response);
                finish();
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                progressMaterial.dismiss();
                showErrorDialog(baseResponse.getErr_msg());
            }
        };
    }

    @Override
    protected void loadData() {}

    private boolean judgePost(){
        newpw = passwordResetInputNewpwEt.getText().toString().trim();
        confirmpw = passwordResetConfirmNewpwEt.getText().toString().trim();
        if (newpw.length() < 6) {
            passwordResethintTv.setTextColor(getResources().getColor(R.color.red));
            showWaringDialog(getResources().getString(R.string.re_password_success_num));
            return false;
        }
        if (!newpw.equals(confirmpw)) {
            showWaringDialog(getResources().getString(R.string.input_password_diff));
            return false;
        }
        return true;
    }

    private void postRetrievePw() {
        progressMaterial.show();
        Subscription subscriptionset = NetworkManager.getUserDataApi()
                .retrievePw("3", phone, verifyCode,newpw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resetObserver);
        subscriptionList.add(subscriptionset);
    }

    private void postBindPhone() {
        progressMaterial.show();
        Subscription subscriptionbind = NetworkManager.getUserDataApi()
                .bindPhone(SPUtils.getToken(),"3",verifyCode, phone,newpw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resetObserver);
        subscriptionList.add(subscriptionbind);
    }

    private void postNewPayPw() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .setPayPw(SPUtils.getToken(),newpw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resetObserver);
        subscriptionList.add(subscription);
    }

    private void postReSetPayPw() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserDataApi()
                .retrievePayPw(SPUtils.getToken(), "3", phone,verifyCode,newpw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resetObserver);
        subscriptionList.add(subscription);
    }


}
