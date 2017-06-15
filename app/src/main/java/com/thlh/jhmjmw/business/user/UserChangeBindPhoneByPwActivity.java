package com.thlh.jhmjmw.business.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.RegistVerifyNumberByPwResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 通过密码修改绑定手机
 */
public class UserChangeBindPhoneByPwActivity extends BaseActivity {

    private static final String TAG = "UserChangeBindPhoneByPwActivity";

    @BindView(R.id.userbind_bypw_password_et)
    EditText userbindBypwPasswordEt;
    @BindView(R.id.userbind_bypw_next_ll)
    LinearLayout userbindBypwNextLl;
    @BindView(R.id.userbind_bypw_verify_ll)
    LinearLayout userbindBypwVerifyLl;
    @BindView(R.id.userbind_bypw_cancel_iv)
    ImageView cancelIv;

    private String inpputpw;
    private BaseObserver<RegistVerifyNumberByPwResponse> changebyPwObserver;
    private String verifyTemp;


    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserChangeBindPhoneByPwActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userbind_bypw);
        ButterKnife.bind(this);
        final String password = SPUtils.getPassword();
        L.e(TAG + "  pw  " + password);

        userbindBypwPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    userbindBypwVerifyLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    userbindBypwVerifyLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });

        changebyPwObserver = new BaseObserver<RegistVerifyNumberByPwResponse>() {
            @Override
            public void onErrorResponse(RegistVerifyNumberByPwResponse response) {
                showWaringDialog(response.getErr_msg());
            }

            @Override
            public void onNextResponse(RegistVerifyNumberByPwResponse registVerifyNumberResponse) {
                verifyTemp = registVerifyNumberResponse.getData().getTmp();
                UserChangeBindPhoneActivity.activityStart(UserChangeBindPhoneByPwActivity.this, Constants.BINDPHONE_TYPE_BYPW, verifyTemp);
            }
        };


    }

    @Override
    protected void loadData() {
    }

    private void postBindPhoneByPW(String password) {
        NetworkManager.getUserDataApi()
                .changeBindPhoneByPW(SPUtils.getToken(), "1", password)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(changebyPwObserver);
    }


    @OnClick({R.id.userbind_bypw_cancel_iv, R.id.userbind_bypw_next_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userbind_bypw_cancel_iv:
                finish();
                break;
            case R.id.userbind_bypw_next_ll:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userbindBypwPasswordEt.getWindowToken(), 0);
                inpputpw = userbindBypwPasswordEt.getText().toString().trim();
                if (inpputpw.equals("")) {
                    showWaringDialog(getResources().getString(R.string.please_input_password));
                    return;
                }
                postBindPhoneByPW(inpputpw);
//                if (inpputpw.equals(password)) {
//                    postBindPhoneByPW(inpputpw);
//                } else {
//                    showWaringDialog("密码不正确");
//                }
                break;
        }
    }
}
