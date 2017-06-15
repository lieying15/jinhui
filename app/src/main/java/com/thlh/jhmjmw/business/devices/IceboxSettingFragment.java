package com.thlh.jhmjmw.business.devices;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.response.FridgeSettingResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.view.HeaderNormal;

import butterknife.BindView;
import butterknife.OnClick;

public class IceboxSettingFragment extends BaseFragment {
    private static final String TAG = "IceboxSettingFragment";
    @BindView(R.id.icebox_header)
    HeaderNormal iceboxHeader;
    @BindView(R.id.icebox_setting_temperature_top_iv)
    ImageView iceboxSettingTemperatureTopIv;
    @BindView(R.id.icebox_setting_temperature_top_tv)
    TextView topTemperatureTv;
    @BindView(R.id.icebox_setting_top_sub_iv)
    ImageView iceboxSettingTopSubIv;
    @BindView(R.id.icebox_setting_top_add_iv)
    ImageView iceboxSettingTopAddIv;
    @BindView(R.id.icebox_setting_temperature_bottom_iv)
    ImageView iceboxSettingTemperatureBottomIv;
    @BindView(R.id.icebox_setting_temperature_bottom_tv)
    TextView bottomTemperatureTv;
    @BindView(R.id.icebox_setting_bottom_sub_iv)
    ImageView iceboxSettingBottomSubIv;
    @BindView(R.id.icebox_setting_bottom_add_iv)
    ImageView iceboxSettingBottomAddIv;
    @BindView(R.id.icebox_setting_topleft_tv)
    TextView settingTLTv;
    @BindView(R.id.icebox_setting_bottomleft_tv)
    TextView settingBLTv;
    @BindView(R.id.icebox_setting_topright_tv)
    TextView settingTRTv;
    @BindView(R.id.icebox_setting_bottomright_tv)
    TextView settingBRTv;
    @BindView(R.id.icebox_setting_ttop_iv)
    ImageView iceboxSettingTtopIv;
    @BindView(R.id.icebox_setting_tbottom_iv)
    ImageView iceboxSettingTbottomIv;
    @BindView(R.id.icebox_setting_ttop_tv)
    TextView iceboxSettingTtopTv;

    private BaseObserver<FridgeSettingResponse> getSetInfoObserver;
    private BaseObserver<BaseResponse> setObserver;
    private String temperatureUpper = "5";
    private String temperatureLower = "-18";
    private String coolroom_kg = "0"; //冷藏室开关
    private String fastcool_kg = "0"; //速冷开关
    private String fastfrozen_kg = "0";//速冻开关
    private String smart_kg = "0"; //智能开关
    private String lock_kg = "0"; //锁定开关

    @Override
    protected void initVariables() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_icebox_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getSetInfoObserver = new BaseObserver<FridgeSettingResponse>() {
            @Override
            public void onErrorResponse(FridgeSettingResponse settingResponse) {

            }

            @Override
            public void onNextResponse(FridgeSettingResponse settingResponse) {
                temperatureUpper = settingResponse.getData().getUpper();
                temperatureLower = settingResponse.getData().getLower();
                coolroom_kg = settingResponse.getData().getCoolroom_kg(); //冷藏室开关
                fastcool_kg = settingResponse.getData().getFastcool_kg(); //速冷开关
                fastfrozen_kg = settingResponse.getData().getFastfrozen_kg();//速冻开关
                smart_kg = settingResponse.getData().getSmart_kg(); //智能开关
                lock_kg = settingResponse.getData().getLock_kg(); //锁定开关
                changeSettingView();
            }
        };

        setObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {

            }
        };

    }


    @Override
    protected void loadData() {
        loadSetting();
    }

    public void loadSetting() {
        NetworkManager.getFridgeApi()
                .getSettingInfo(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(getSetInfoObserver);
    }


    public void postSetting() {
        NetworkManager.getFridgeApi()
                .setParameter(SPUtils.getToken(), temperatureUpper, temperatureLower, coolroom_kg, fastcool_kg, fastfrozen_kg, smart_kg, lock_kg)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(setObserver);
    }

    @OnClick({R.id.icebox_setting_top_sub_iv, R.id.icebox_setting_top_add_iv, R.id.icebox_setting_bottom_sub_iv,
            R.id.icebox_setting_bottom_add_iv, R.id.icebox_setting_topleft_tv, R.id.icebox_setting_bottomleft_tv,
            R.id.icebox_setting_topright_tv, R.id.icebox_setting_bottomright_tv, R.id.icebox_setting_ttop_iv, R.id.icebox_setting_tbottom_iv})
    public void onClick(View view) {
        int viewid = view.getId();
        if (lock_kg.equals("1") && viewid != R.id.icebox_setting_bottomright_tv) {
            showWaringDialog(getResources().getString(R.string.ice_lock_please_unlock));
            return;
        }
        switch (view.getId()) {
            case R.id.icebox_setting_top_sub_iv:
                if(!changeTemperature("up", -1))return;
                break;
            case R.id.icebox_setting_top_add_iv:
                if(!changeTemperature("up", 1)) return;
                break;
            case R.id.icebox_setting_bottom_sub_iv:
                if(!changeTemperature("bottom", -1))return;
                break;
            case R.id.icebox_setting_bottom_add_iv:
                if(!changeTemperature("bottom", 1))return;
                break;
            case R.id.icebox_setting_topleft_tv://速冷
                if(coolroom_kg.equals("0")){
                    showWaringDialog(getResources().getString(R.string.cool_room_close_no_));
                    return;
                }
                if (fastcool_kg.equals("1")) {
                    fastcool_kg = "0";
                    temperatureUpper = (String) SPUtils.get("icebox_setting_upT","5");
                } else {
                    if(smart_kg.equals("1")){
                        smart_kg = "0";
                        temperatureLower = (String) SPUtils.get("icebox_setting_lowT","-18");
                    }else {
                        SPUtils.put("icebox_setting_upT",temperatureUpper);
                    }
                    fastcool_kg = "1";
                    temperatureUpper = "2";
                }
                break;
            case R.id.icebox_setting_bottomleft_tv: //智能
                if (smart_kg.equals("1")) {
                    smart_kg = "0";
                    temperatureUpper = (String) SPUtils.get("icebox_setting_upT","5");
                    temperatureLower = (String) SPUtils.get("icebox_setting_lowT","-18");
                } else {
                    //缓存点击智能前的温度
                    if(fastcool_kg.equals("0"))
                        SPUtils.put("icebox_setting_upT",temperatureUpper);
                    if(fastfrozen_kg.equals("0"))
                        SPUtils.put("icebox_setting_lowT",temperatureLower);
                    smart_kg = "1";
                    fastcool_kg = "0";
                    fastfrozen_kg = "0";
                    temperatureUpper = "5";
                    temperatureLower = "-18";
                }
                break;
            case R.id.icebox_setting_topright_tv: //速冻
                if (fastfrozen_kg.equals("1")) {
                    fastfrozen_kg = "0";
                    temperatureLower = (String) SPUtils.get("icebox_setting_lowT","-18");
                } else {
                    if(smart_kg.equals("1")){
                        smart_kg = "0";
                        temperatureUpper = (String) SPUtils.get("icebox_setting_upT","5");
                    }else {
                        SPUtils.put("icebox_setting_lowT",temperatureLower);
                    }
                    fastfrozen_kg = "1";
                    temperatureLower = "-24";
                }
                break;
            case R.id.icebox_setting_bottomright_tv: //锁定
                if (lock_kg.equals("1")) {
                    lock_kg = "0";
                } else {
                    lock_kg = "1";
                }
                break;
            case R.id.icebox_setting_ttop_iv: //冷藏
                if (coolroom_kg.equals("1")) {
                    coolroom_kg = "0";
                    temperatureUpper = "5";
                    iceboxSettingTtopIv.setBackgroundResource(R.drawable.img_icebox_temp_top);
                    iceboxSettingTtopTv.setTextColor(getActivity().getResources().getColor(R.color.text_nomal));
                } else {
                    fastcool_kg = "0";
                    coolroom_kg = "1";
                    iceboxSettingTtopIv.setBackgroundResource(R.drawable.img_icebox_temp_top_red);
                    iceboxSettingTtopTv.setTextColor(getActivity().getResources().getColor(R.color.wine));
                }
                break;
            case R.id.icebox_setting_tbottom_iv:
                showWaringDialog(getResources().getString(R.string.cool_room_no_close));
                return;
        }
        changeSettingView();
        postSetting();
    }

    private boolean changeTemperature(String location, int changeT) {
        int tempT;
        switch (location) {
            case "up":
                if (coolroom_kg.equals("0")) {
                    showWaringDialog(getResources().getString(R.string.cool_room_close_no_));
                    return false;
                }
                tempT = Integer.parseInt(topTemperatureTv.getText().toString()) + changeT;
                if (tempT > 8 || tempT < 2) {
                    showWaringDialog(getResources().getString(R.string.cool_cang));
                    return false;
                }
                temperatureUpper = tempT + "";
                fastcool_kg = "0";
                break;
            case "bottom":
                tempT = Integer.parseInt(bottomTemperatureTv.getText().toString()) + changeT;
                if (tempT > -16 || tempT < -24) {
                    showWaringDialog(getResources().getString(R.string.cool_dong));
                    return false;
                }
                temperatureLower = tempT + "";
                fastfrozen_kg = "0";
                break;
        }
        smart_kg = "0";
        return true;
    }

    private void changeSettingView() {
        settingTLTv.setBackgroundResource(R.drawable.shap_radius_quarter_topleft_gray);
        settingBLTv.setBackgroundResource(R.drawable.shap_radius_quarter_bottomleft_gray);
        settingTRTv.setBackgroundResource(R.drawable.shap_radius_quarter_topright_gray);
        settingBRTv.setBackgroundResource(R.drawable.shap_radius_quarter_bottomright_gray);
        if (coolroom_kg.equals("1")){ //冷藏室开关
            iceboxSettingTtopIv.setBackgroundResource(R.drawable.img_icebox_temp_top_red);
            iceboxSettingTtopTv.setTextColor(getActivity().getResources().getColor(R.color.wine));
        }else {
            iceboxSettingTtopIv.setBackgroundResource(R.drawable.img_icebox_temp_top);
            iceboxSettingTtopTv.setTextColor(getActivity().getResources().getColor(R.color.text_nomal));
        }

        if (fastcool_kg.equals("1")) //速冷开关
                settingTLTv.setBackgroundResource(R.drawable.shap_radius_quarter_topleft_grayshallow);
        if (fastfrozen_kg.equals("1"))//速冻开关
            settingTRTv.setBackgroundResource(R.drawable.shap_radius_quarter_topright_grayshallow);

        if (smart_kg.equals("1")) //智能开关
            settingBLTv.setBackgroundResource(R.drawable.shap_radius_quarter_bottomleft_grayshallow);
        if (lock_kg.equals("1"))
            settingBRTv.setBackgroundResource(R.drawable.shap_radius_quarter_bottomright_grayshallow);
        topTemperatureTv.setText(temperatureUpper);
        bottomTemperatureTv.setText(temperatureLower);
    }


}
