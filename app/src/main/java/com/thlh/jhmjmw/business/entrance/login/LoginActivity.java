package com.thlh.jhmjmw.business.entrance.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.R;

import butterknife.ButterKnife;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    public static final int START_TYPE_LANCHER = 0;
    public static final int START_TYPE_HOMEPAGE = 1;
    public static final int START_TYPE_GOODSDETAIL = 2;

    private LoginFragment loginFragment;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initVariables() {
//        startType = getIntent().getIntExtra("startType", START_TYPE_LANCHER);
//        BaseApplication.getInstance().getmShareAPI().isInstall(LoginActivity.this, SHARE_MEDIA.WEIXIN);

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);

        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.activity_baseview);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.activity_baseview, loginFragment);
            transaction.commit();
        }

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(loginFragment.getLoginBackIv()!= null){
            loginFragment.getLoginBackIv().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseApplication.getInstance().getmShareAPI().onActivityResult(requestCode, resultCode, data);
    }





}