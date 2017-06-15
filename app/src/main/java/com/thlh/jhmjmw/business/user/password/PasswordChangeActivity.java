package com.thlh.jhmjmw.business.user.password;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 密码修改界面
 */
public class PasswordChangeActivity extends BaseViewActivity {
    private static final String TAG = "PasswordChangeActivity";


    @BindView(R.id.changepw_header)
    HeaderNormal changepwHeader;
    @BindView(R.id.changepw_input_newpw_et)
    EditText changepwInputNewpwEt;
    @BindView(R.id.changepw_input_oldpw_et)
    EditText changepwInputOldpwEt;
    @BindView(R.id.changepw_confirm_newpw_et)
    EditText changepwConfirmNewpwEt;
    @BindView(R.id.changepw_hint_tv)
    TextView changepwPwTv;

    @BindView(R.id.changepw_next_rip)
    RippleRelativeLayout changepwNextRip;
    @BindView(R.id.changepw_input_oldpw_ll)
    LinearLayout changepwInputOldpwLl;
    @BindView(R.id.changepw_input_newpw_ll)
    LinearLayout changepwInputNewpwLl;
    @BindView(R.id.changepw_confirm_newpw_ll)
    LinearLayout changepwConfirmNewpwLl;

    private String oldpw,newpw,confirmpw;
    private BaseObserver<BaseResponse> resetObserver;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PasswordChangeActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void initVariables() {
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password_change);
        ButterKnife.bind(this);
        changepwNextRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                if(judgeCondition()){
                    postChangePw();
                }
            }
        });
        changepwHeader.setTitle(getResources().getString(R.string.changepw_login_header_name));

        resetObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                ActionResponse response = new ActionResponse();
                response.setHeadertitle(getResources().getString(R.string.re_password));
                response.setTitle(getResources().getString(R.string.congratulation));
                response.setContent(getResources().getString(R.string.re_password_success));
                ResponseActivity.activityStart(PasswordChangeActivity.this,response);
                finish();

            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };
    }

    @Override
    protected void loadData() {}

    private boolean judgeCondition(){
        oldpw = changepwInputOldpwEt.getText().toString().trim();
        newpw = changepwInputNewpwEt.getText().toString().trim();
        confirmpw = changepwConfirmNewpwEt.getText().toString().trim();
        if(newpw.length()<6){
            changepwPwTv.setTextColor(getResources().getColor(R.color.red));
            showErrorDialog(getResources().getString(R.string.re_password_success_num));
            return false;
        }
        if(!newpw.equals(confirmpw)){
            showErrorDialog(getResources().getString(R.string.input_password_diff));
            return false;
        }
        return true;
    }


    private void postChangePw(){
        NetworkManager.getUserDataApi()
                .resetPw(SPUtils.getToken(), oldpw,newpw)
                .compose(RxUtils.androidSchedulers(PasswordChangeActivity.this))
                .subscribe(resetObserver);
    }


}
