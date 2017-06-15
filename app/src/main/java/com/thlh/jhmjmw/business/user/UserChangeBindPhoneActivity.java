package com.thlh.jhmjmw.business.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 绑定手机
 */
public class UserChangeBindPhoneActivity extends BaseViewActivity {

    private static final String TAG = "UserChangeBindPhoneActivity";
    @BindView(R.id.userbind_phone_number_et)
    EditText userbindPhoneNumberEt;
    @BindView(R.id.userbind_phone_number_ll)
    LinearLayout userbindPhoneNumberLl;
    @BindView(R.id.userbind_phone_next_ll)
    LinearLayout userbindPhoneNextLl;


    private String inpputPhoneNumber;
    private int bindType ;
    private String phoneNumber;
    private String code;

    private BaseObserver<BaseResponse> changebyPhoneObserver;

    public static void activityStart(Context context,int bind_type) {
        Intent intent = new Intent();
        intent.putExtra("bind_type", bind_type);
        intent.setClass(context, UserChangeBindPhoneActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Context context,int bind_type,String code) {
        Intent intent = new Intent();
        intent.putExtra("bind_type", bind_type);
        intent.putExtra("code", code);
        intent.setClass(context, UserChangeBindPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        bindType = getIntent().getIntExtra("bind_type", Constants.BINDPHONE_TYPE_NORMAL);
        code =  getIntent().getStringExtra("code");
        L.e(TAG + " code:"+code);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userbind_phone);
        ButterKnife.bind(this);
        final String password = SPUtils.getPassword();

        userbindPhoneNumberEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    userbindPhoneNumberLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    userbindPhoneNumberLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });



        userbindPhoneNextLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userbindPhoneNumberEt.getWindowToken(), 0);
                if(juedgeCondition()){
                    switch (bindType){
                        case  Constants.BINDPHONE_TYPE_NORMAL:
                            break;
                        case  Constants.BINDPHONE_TYPE_BYPHONE:
                            postBindPhoneByPhone(inpputPhoneNumber);
                            break;
                        case  Constants.BINDPHONE_TYPE_BYPW:
                            postBindPhoneByPW(inpputPhoneNumber);
                            break;
                    }
                }
            }
        });

        changebyPhoneObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showWaringDialog(baseResponse.getErr_msg());

            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                ActionResponse response = new ActionResponse();
                response.setHeadertitle(getResources().getString(R.string.bind_phone));
                response.setTitle(getResources().getString(R.string.congratulation));
                response.setContent(getResources().getString(R.string.success_bind_phone));
                ResponseActivity.activityStart(UserChangeBindPhoneActivity.this,response);
                finish();
            }
        };
    }


    @Override
    protected void loadData() {}


    private void postBindPhoneByPhone(String phoneNumber) {
        L.e(TAG + "  postBindPhoneByPhone step:2 code:"+code+" phoneNumber:"+phoneNumber);
        NetworkManager.getUserDataApi()
                .changeBindPhoneByPhone(SPUtils.getToken(),"3", code,phoneNumber)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(changebyPhoneObserver);
    }


    private void postBindPhoneByPW(String phoneNumber) {
        L.e(TAG + "  postBindPhoneByPW step:2 code:"+code+" phoneNumber:"+phoneNumber);
        NetworkManager.getUserDataApi()
                .changeBindPhoneByPW(SPUtils.getToken(),"2", code,phoneNumber)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(changebyPhoneObserver);
    }

    private boolean juedgeCondition(){
        inpputPhoneNumber = userbindPhoneNumberEt.getText().toString().trim();
        if (!TextUtils.isPhone(inpputPhoneNumber)) {
            showWaringDialog(getResources().getString(R.string.input_phone_no_ture));
            return false;
        }
        return true;
    }



}
