package com.thlh.jhmjmw.business.entrance.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.entrance.regist.RegisterActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.user.PhoneVerifyCodeActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by huqiang on 2016/12/30.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View, View.OnClickListener {


    @BindView(R.id.login_back_iv)
    ImageView loginBackIv;
    @BindView(R.id.login_account_et)
    AutoCompleteTextView loginAccountEt;
    @BindView(R.id.login_account_arrow_iv)
    ImageView loginAccountArrowIv;
    @BindView(R.id.login_account_ll)
    LinearLayout loginAccountLl;
    @BindView(R.id.login_password_et)
    EditText loginPasswordEt;
    @BindView(R.id.login_password_ll)
    LinearLayout loginPasswordLl;
    @BindView(R.id.login_normal_tv)
    TextView loginNormalTv;
    @BindView(R.id.login_normal_ll)
    LinearLayout loginNormalLl;
    @BindView(R.id.login_wechat_iv)
    ImageView loginWechatIv;
    @BindView(R.id.login_forget_password_tv)
    TextView loginForgetPasswordTv;
    @BindView(R.id.login_register_tv)
    TextView loginRegisterTv;



    public LoginContract.Presenter mPresenter;

    // 用户名保存
    private JSONArray accounts;
    private SharedPreferences userAccounts;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_longin;
    }

    @Override
    protected void initVariables() {
        mPresenter =  new LoginPresenter(this,getActivity());
    }

    @Override
    protected void initView() {
        loginBackIv.setOnClickListener(this);
        loginNormalLl.setOnClickListener(this);
        loginWechatIv.setOnClickListener(this);
        loginRegisterTv.setOnClickListener(this);
        loginForgetPasswordTv.setOnClickListener(this);

        loginAccountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(loginAccountLl ==null) return;
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    loginAccountLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    loginAccountLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });

        loginPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(loginPasswordLl ==null) return;
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    loginPasswordLl.setBackgroundResource(R.drawable.shap_stroke_blackshallow_r50);
                } else {
                    // 此处为失去焦点时的处理内容
                    loginPasswordLl.setBackgroundResource(R.drawable.shap_stroke_grayshallow_r50);
                }
            }
        });
    }

    @Override
    protected void initData() {
        setHistoryUserName();
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back_iv:
                finish();
                break;
            /**登录-----到首页*/
            case R.id.login_normal_ll:

                mPresenter.loginNormalTask();

                break;
            case R.id.login_forget_password_tv:
                startFogetPwActivity();
                break;
            case R.id.login_register_tv:
                startRegisterActivity();
                break;
            case R.id.login_wechat_iv:
                mPresenter.loginWechatTask();
                break;
        }
    }

    @Override
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginAccountEt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(loginPasswordEt.getWindowToken(), 0);
    }


    @Override
    public String getUserName() {
        return loginAccountEt.getText().toString().trim();
    }

    @Override
    public String getPassWord() {
        return loginPasswordEt.getText().toString().trim();
    }

    @Override
    public Activity getActivityView() {
        return getActivity();
    }


    @Override
    public void showHintDialog(String message) {
        DialogUtils.showNormal((RxAppCompatActivity) getActivity(), DialogUtils.TYPE_NORMAL_WARNING, message);
    }

    @Override
    public void setHistoryUserName() {
        try {
            userAccounts = getActivity().getSharedPreferences("acounts_history", Context.MODE_PRIVATE);
            accounts = new JSONArray(userAccounts.getString("accountsJArray", new JSONArray().toString()));
            // 若用户名SP中有数据，则将保存的用户名信息添加到AutoCompleteTextView上
            if (accounts.length() > 0) {
                ArrayList<String> accountslist = new ArrayList<String>();
                for (int i = 0; i < accounts.length(); i++){
                    accountslist.add(accounts.getString(i));
                }
                loginAccountEt.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.autocompletetv_dropdown, accountslist));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveHistroyUserName(String userName) {
        // 如果登录成功，则将用户名加入到用户名SP中，供以后AutoCompleteTextView使用
        boolean hasaccount = false;
        for (int i = 0; i < accounts.length(); i++) {
            try {
                if (userName.equals(accounts.getString(i))) {
                    hasaccount = true;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!hasaccount) {
            accounts.put(userName);
            userAccounts.edit().putString("accountsJArray", accounts.toString()).commit();
        }
    }

    @Override
    public void startHomePageActivity() {
        IndexActivity.activityStart(getActivity());
        getActivity().finish();
    }


    @Override
    public void startRegisterActivity() {
        RegisterActivity.activityStart(getActivity());
    }

    @Override
    public void startFogetPwActivity() {
        PhoneVerifyCodeActivity.activityStart(getActivity());
    }


    public void setLoginBackIv(ImageView loginBackIv) {
        this.loginBackIv = loginBackIv;
    }

    public ImageView getLoginBackIv() {
        return loginBackIv;
    }

    @Override
    public void finish() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

}
