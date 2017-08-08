package com.thlh.jhmjmw.business.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.response.VersionResponse;
import com.thlh.baselib.utils.ActivityUtils;
import com.thlh.baselib.utils.AppUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.other.AboutUsActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.UpdateManager;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;
import com.thlh.viewlib.togglebutton.ToggleButton;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  设置页
 */
public class SettingActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "SettingActivity";
    @BindView(R.id.setting_btn)
    ToggleButton settingBtn;
    @BindView(R.id.logout_normal_rip)
    RippleRelativeLayout logoutNormalRip;
    @BindView(R.id.setting_version_tv)
    TextView settingVersionTv;
    @BindView(R.id.setting_update_tv)
    TextView settingUpdateTv;
    @BindView(R.id.setting_aboutus_tv)
    TextView settingAboutUsTv;
    @BindView(R.id.setting_chmemeber_tv)
    TextView chmemberTv;


    private BaseObserver<BaseResponse> logoutObserver;
    private BaseObserver<VersionResponse> versionobserver;

    private int versioncode;
    private String updataUrl;
    private String updataContent;
    private DialogNormal.Builder updateDialog;


    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        updateDialog = new DialogNormal.Builder(this);

        String  pushFlag = (String) SPUtils.get("user_push_flag","1").toString();
        if(pushFlag.equals("1")){
            settingBtn.setToggleOn();
        }else {
            settingBtn.setToggleOff();
        }

        settingBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    SPUtils.put("user_push_flag","1");
                }else {
                    SPUtils.put("user_push_flag","0");
                }
            }
        });

        logoutNormalRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                logout();
            }
        });

        int ch = Integer.valueOf(SPUtils.get("user_isch",0).toString());
        String storeid = (String)SPUtils.get("user_storeid","").toString();
        if(ch==1 ||! storeid.equals("0")){
            chmemberTv.setVisibility(View.VISIBLE);
            if(ch==1 && storeid.equals("0")){
                chmemberTv.setText(getResources().getString(R.string.setting_user));
            }
            if(ch==0 && !storeid.equals("0")){
                chmemberTv.setText(getResources().getString(R.string.setting_bing_shop));
            }
            if(ch==1 && !storeid.equals("0")){
                chmemberTv.setText(getResources().getString(R.string.setting_user_bing_shop));
            }
        }

        String version = AppUtils.getVersionName(this) ;
        settingVersionTv.setText("V" + version);
        settingVersionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logoutObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                setResult(Activity.RESULT_OK);
                ActivityUtils.popAllActivityUntilSpecify(IndexActivity.class);
                clearUserInfo();
                finish();
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                setResult(Activity.RESULT_OK);
                ActivityUtils.popAllActivityUntilSpecify(IndexActivity.class);
                SPUtils.put("user_agency_id","0");
                clearUserInfo();
            }
        };

        versionobserver = new BaseObserver<VersionResponse>() {



            @Override
            public void onNextResponse(VersionResponse versionResponse) {
                versioncode = Integer.parseInt(versionResponse.getData().getVer());
                updataContent = versionResponse.getData().getContent();
                updataUrl = versionResponse.getData().getUrl();

                int currentCode = AppUtils.getVersionCode(SettingActivity.this);
                L.e(TAG +" 更新版本  updataCode" + versioncode + " currentCode" +currentCode );
                if(versioncode > currentCode ){
                    new UpdateManager(SettingActivity.this, updataContent, updataUrl, versioncode).showDialog();
                }else {

                    updateDialog.setSubTitle(getResources().getString(R.string.setting_updata_)).setRightBtnStr(getResources().getString(R.string.back))
                            .setRightClickListener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();

                }
            }

            @Override
            public void onErrorResponse(VersionResponse baseResponse) {
            }
        };


    }


    @Override
    protected void loadData() {

    }

    private void loadVersion(){
        Subscription subscription = NetworkManager.getGoodsDataApi()
                .getVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(versionobserver);
        subscriptionList.add(subscription);
    }

    private void logout() {
        Subscription subscription = NetworkManager.getUserDataApi()
                .logout(SPUtils.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logoutObserver);
        subscriptionList.add(subscription);
    }


    private void clearUserInfo() {
        SPUtils.clearUserInfo();
        BaseApplication.getInstance().getmShareAPI().deleteOauth(SettingActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }


    @OnClick({R.id.setting_update_tv,R.id.setting_aboutus_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_update_tv:
                loadVersion();
                break;
            case R.id.setting_aboutus_tv:
                AboutUsActivity.activityStart(SettingActivity.this);
                break;
        }
    }

}
