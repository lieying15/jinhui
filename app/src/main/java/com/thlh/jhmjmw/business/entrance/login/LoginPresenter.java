package com.thlh.jhmjmw.business.entrance.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.response.LoginResponse;
import com.thlh.baselib.utils.AES;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

/**
 * Created by huqiang on 2016/12/30.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final String TAG = "LoginActivity ";
    public static final int START_TYPE_LANCHER = 0;
    public static final int START_TYPE_HOMEPAGE = 1;
    public static final int START_TYPE_GOODSDETAIL = 2;
    Context context;
    private int startType;
    private String userName = "";
    private String userPassWord = "";
    private String weixinNikename = "";

    private int login_type = Constants.LOGIN_TYPE_NORMAL; //登录类型
    private BaseObserver<LoginResponse> loginNormalObserver;

    //
    private final LoginContract.View mLoginView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;

    public LoginPresenter(LoginContract.View mLoginView,Context contexts) {
        this.mLoginView = mLoginView;
        this.context=contexts;
    }

    @Override
    public void loginNormalTask() {
        mLoginView.hideKeyBoard();
        if (judgeLoginInfo()) {
            login_type = Constants.LOGIN_TYPE_NORMAL;
            postNormalLgoin();
        }
    }

    @Override
    public void loginWechatTask() {
//        mLoginView.showProgressBar();
        login_type = Constants.LOGIN_TYPE_WECHAT;
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        BaseApplication.getInstance().getmShareAPI().doOauthVerify(mLoginView.getActivityView(), platform, umAuthListener);
    }

    private boolean judgeLoginInfo() {
        userName = mLoginView.getUserName();
        if (userName.equals("")) {

            mLoginView.showHintDialog(context.getResources().getString(R.string.name_no));
            return false;
        }

        if (!TextUtils.isPhone(userName)) {

            mLoginView.showHintDialog(context.getResources().getString(R.string.input_no));
            return false;
        }
        userPassWord = mLoginView.getPassWord();
        if (userPassWord.equals("")) {

            mLoginView.showHintDialog(context.getResources().getString(R.string.password_null));
            return false;
        }
        return true;
    }

    protected void postNormalLgoin() {
        String tempUserName = mLoginView.getUserName();
        String tempPassWord = mLoginView.getPassWord();
        initLoginObserver();
        NetworkManager.getUserDataApi()
                .login(tempUserName, tempPassWord, BaseApplication.getInstance().getPhoneDevice())
                .compose(RxUtils.androidSchedulers(mLoginView))
                .subscribe(loginNormalObserver);
    }

    private void initLoginObserver(){
        loginNormalObserver = new BaseObserver<LoginResponse>() {
            @Override
            public void onNextResponse(LoginResponse loginBaseResponse) {
//                mLoginView.hideProgressBar();
                if (loginBaseResponse.getData() != null) {
                    //存储用户数据
                    saveUserInfo(loginBaseResponse);
                    L.i(TAG + " saveUserInfo  存储完毕!");
                    //保存用户账号
                    mLoginView.saveHistroyUserName(userName);
                    L.i(TAG + " saveAccountInfo  存储完毕!");
                    if (startType == START_TYPE_HOMEPAGE) {
                        mLoginView.finish();
                    } else {
                        mLoginView.startHomePageActivity();
                    }
                }
            }

            @Override
            public void onErrorResponse(LoginResponse loginBaseResponse) {
                L.i(TAG + " onErrorResponse ");
//                mLoginView.hideProgressBar();
                mLoginView.showHintDialog(loginBaseResponse.getErr_msg());
            }
        };
    }

    private void saveUserInfo(LoginResponse loginBaseResponse) {
//        L.i(TAG + "login Token : " + loginBaseResponse.getData().getAccount().getToken());
//        L.i(TAG + "login userinfo : user_inner_member" + loginBaseResponse.getData().getAccount().getInner_member());
//        L.i(TAG + "login userinfo :  user_nickname" + loginBaseResponse.getData().getAccount().getNickname());
//        L.i(TAG + "login userinfo :  user_equipmentid" + loginBaseResponse.getData().getAccount().getEquipment_id());
//        L.i(TAG + "login userinfo :  user_avatar" + loginBaseResponse.getData().getAccount().getAvatar());
//        L.i(TAG + "login userinfo :  user_ispaypass" + loginBaseResponse.getData().getAccount().getIs_paypass());
//        L.i(TAG + "login userinfo :  userstore_id" + loginBaseResponse.getData().getAccount().getStore_id());
//        L.i(TAG + "login userinfo :  user_bind_mobile" + loginBaseResponse.getData().getAccount().getBind_mobile());
//        L.i(TAG + "login userinfo :  user_wait_pay" + loginBaseResponse.getData().getOrder_info().getWait_pay());
//        L.i(TAG + "login userinfo :  user_wait_deliver" + loginBaseResponse.getData().getOrder_info().getWait_deliver());
//        L.i(TAG + "login userinfo :  user_wait_get" + loginBaseResponse.getData().getOrder_info().getWait_get());
//        L.i(TAG + "login userinfo :  user_return_goods" + loginBaseResponse.getData().getOrder_info().getReturn_goods());
//        L.i(TAG + "login userinfo :  user_wait_comment" + loginBaseResponse.getData().getOrder_info().getWait_comment());
//        L.i(TAG + "login userinfo :  user_systeminfo_amount" + loginBaseResponse.getData().getSysteminfo().getInfo_amount());

        SPUtils.setLogin(true);
        SPUtils.put("needupdate_userinfo", (boolean) true);
        SPUtils.put("login_type", login_type);
        SPUtils.setToken(loginBaseResponse.getData().getAccount().getToken());
        switch (login_type) {
            case Constants.LOGIN_TYPE_NORMAL:
                if (userName.equals("")) {
                    SPUtils.setUsername(BaseApplication.DEFAULT_USER);
                } else {
                    SPUtils.setUsername(userName);
                }
                break;
            case Constants.LOGIN_TYPE_WECHAT:
                SPUtils.put("username", weixinNikename);
                break;
        }
        SPUtils.setPassword(userPassWord);
        SPUtils.put("user_nickname", loginBaseResponse.getData().getAccount().getNickname());
        SPUtils.put("user_inner_member", loginBaseResponse.getData().getAccount().getInner_member());
        SPUtils.put("user_avatar", loginBaseResponse.getData().getAccount().getAvatar());
        SPUtils.put("user_isch", loginBaseResponse.getData().getAccount().getIs_ch());//是否是扫码用户
        SPUtils.put("user_storeid", loginBaseResponse.getData().getAccount().getStore_id());
        SPUtils.put("user_bind_mobile", loginBaseResponse.getData().getAccount().getBind_mobile());
        SPUtils.put("user_equipmentid", loginBaseResponse.getData().getAccount().getEquipment_id() + "");
        saveUserAddress(loginBaseResponse.getData().getAddress());
        SPUtils.put("user_wait_pay", loginBaseResponse.getData().getOrder_info().getWait_pay());
        SPUtils.put("user_wait_deliver", loginBaseResponse.getData().getOrder_info().getWait_deliver());
        SPUtils.put("user_wait_get", loginBaseResponse.getData().getOrder_info().getWait_get());
        SPUtils.put("user_return_goods", loginBaseResponse.getData().getOrder_info().getReturn_goods());
        SPUtils.put("user_wait_comment", loginBaseResponse.getData().getOrder_info().getWait_comment());
        SPUtils.put("user_systeminfo_amount", loginBaseResponse.getData().getSysteminfo().getInfo_amount());
        SPUtils.put("user_ispaypass", Integer.parseInt(loginBaseResponse.getData().getAccount().getIs_paypass()));//支付密码  -1免密开启不使用支付密码 0未设置 1使用支付密码
    }

    private void saveUserAddress(List<Address> addressList) {
        if (addressList == null || addressList.size() == 0)
            return;
        for (Address address : addressList) {
            if (address.getIs_on().equals("1")) {
                saveAddress(address);
                return;
            }
        }
        for (Address address : addressList) {
            if (address.getId().equals("0")) {
                saveAddress(address);
                return;
            }
        }
        saveAddress(addressList.get(0));
    }

    private void saveAddress(Address address) {
        SPUtils.put("user_address_id", address.getId());
        SPUtils.put("user_address_name", address.getName());
        SPUtils.put("user_address_address", address.getAddress());
        SPUtils.put("user_address_phone", address.getPhone());
        SPUtils.put("user_address_is_on", address.getIs_on());
        SPUtils.put("user_address_province", address.getProvince());
        SPUtils.put("user_address_city", address.getCity());
        SPUtils.put("user_address_district", address.getDistrict());
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String infoStr = data.toString();
            getUserInfo();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            L.e("hq doOauthVerify onError");
//            mLoginView.showProgressBar();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            L.e("hq doOauthVerify onCancel");
//            mLoginView.showProgressBar();

        }
    };


    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        L.i(TAG + "请求用户信息");
        BaseApplication.getInstance().getmShareAPI().getPlatformInfo(mLoginView.getActivityView(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (map != null) {
                    String headimgurl = map.get("headimgurl");
                    String unionid = map.get("unionid");
                    String nickname = map.get("nickname");
                    weixinNikename = nickname;
                    String sex = map.get("sex");
                    String openid = map.get("openid");
                   // L.e(TAG + "  得到的用户信息:" + headimgurl + "   unionid:" + unionid + "  nickname:" + nickname + "  sex" + sex + "  openid:" + openid);
                    StringBuilder userinfo = new StringBuilder();
                    userinfo.append(unionid + "|");
                    userinfo.append(headimgurl + "|");
                    userinfo.append(sex + "|");
                    userinfo.append(nickname);
                    weixinLoginPost(userinfo.toString());
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                L.e(TAG,"weixinlogin getUserInfo onError");
//                mLoginView.showProgressBar();

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                L.e(TAG,"weixinlogin getUserInfo onCancel");
//                mLoginView.showProgressBar();
            }
        });
    }

    @Override
    public void getWechatUserInfo() {

    }

    protected void weixinLoginPost(String content) {
        String encryptResult = "";
        try {
            encryptResult = AES.encrypt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        L.e(TAG + " weixinLoginPost 加密结果" + encryptResult);
        String ch = (String) SPUtils.get("user_ch", "").toString();
        initLoginObserver();
        NetworkManager.getUserDataApi()
                .weixinlogin(encryptResult, BaseApplication.getInstance().getPhoneDevice(), ch)
                .compose(RxUtils.androidSchedulers(mLoginView))
                .subscribe(loginNormalObserver);
    }
}