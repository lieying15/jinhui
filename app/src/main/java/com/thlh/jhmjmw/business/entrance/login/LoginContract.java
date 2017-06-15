package com.thlh.jhmjmw.business.entrance.login;

import android.app.Activity;

import com.thlh.baselib.base.BaseView;

/**
 * Created by huqiang on 2016/12/30.
 */

public interface LoginContract {

    interface View extends BaseView{
        void hideKeyBoard();

        String getUserName();

        String getPassWord();

        Activity getActivityView();

        void setHistoryUserName();

        void saveHistroyUserName(String name);

        void showHintDialog(String message);


        void startHomePageActivity();

        void startRegisterActivity();

        void startFogetPwActivity();

        void finish();


    }


    interface Presenter {

        void loginNormalTask();

        void loginWechatTask();

        void getWechatUserInfo();


    }

}
