package com.thlh.jhmjmw.business.user.address;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.function.rxcache.RxCacheResult;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.Area;
import com.thlh.baselib.model.City;
import com.thlh.baselib.model.District;
import com.thlh.baselib.model.response.AddrAddResponse;
import com.thlh.baselib.model.response.AddrCodeResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;
import com.thlh.viewlib.togglebutton.ToggleButton;
import com.thlh.viewlib.wheel.OnWheelChangedListener;
import com.thlh.viewlib.wheel.WheelView;
import com.thlh.viewlib.wheel.adapters.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑收货地址页
 */
public class AddrEditActivity extends BaseActivity implements  OnWheelChangedListener {
    private final String TAG = "AddrEditActivity";
    public static final int ADDR_EDITTYPE_ADD = 0;
    public static final int ADDR_EDITTYPE_EDIT = 1;
    private final int ACTIVITY_CODE_BAIDUMAP = 1;

    @BindView(R.id.addr_edit_name_et)
    EditText addrEditNameEt;
    @BindView(R.id.addr_edit_addr_et)
    EditText addrEditAddrEt;
    @BindView(R.id.addr_edit_phone_et)
    EditText addrEditPhoneEt;
    @BindView(R.id.addr_edit_states_btn)
    ToggleButton addrEditStatesBtn;
    @BindView(R.id.addr_edit_submit_tv)
    TextView addrEditSubmitTv;
    @BindView(R.id.addr_edit_rip)
    RippleRelativeLayout addrEditRip;
    @BindView(R.id.addr_edit_header)
    HeaderNormal addrEditHeader;
    @BindView(R.id.addr_edit_location_ll)
    LinearLayout addrEditLocationLl;
    @BindView(R.id.addr_edit_map_ll)
    LinearLayout addrMapLl;
    @BindView(R.id.addr_edit_map_iv)
    ImageView addrMapIv;
    @BindView(R.id.addr_edit_location_tv)
    TextView addrLocationTv;
    @BindView(R.id.addr_edit_states_ll)
    LinearLayout addrStatesLl;

    private int editType;
    private Address address;
    private BaseObserver<BaseResponse>  editObserver;
    private BaseObserver<AddrAddResponse> addObserver;
    private Observer<RxCacheResult<AddrCodeResponse>> areaObserver;

    private View parent;
    private View poprootview;
    private PopupWindow popupWindow;
    private WheelView mViewArea;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView cancelTv,completeTv;
    /**  所有省*/
    protected String[] mProvinceDatas;
    /**  key - 省 value - 市*/
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**  key - 市 values - 区*/
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    /**  key - 区 values - 邮编*/
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    /**  当前省的名称*/
    protected String mCurrentProviceName = "北京";
    /**  当前市的名称*/
    protected String mCurrentCityName = "北京";
    /**  当前区的名称*/
    protected String mCurrentDistrictName = "东城区";
    /**  当前区的邮政编码*/
    protected String mCurrentZipCode ="";

    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private String province ;
    private String city ;
    private String district ;


    public static void activityStart(Context context, int editType) {
        Intent intent = new Intent();
        intent.putExtra("editType", editType);
        intent.setClass(context, AddrEditActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Context context, int editType, Address address) {
        Intent intent = new Intent();
        intent.putExtra("editType", editType);
        intent.putExtra("address", address);
        intent.setClass(context, AddrEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        editType = getIntent().getIntExtra("editType", ADDR_EDITTYPE_ADD);
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addr_edit);
        ButterKnife.bind(this);
        //6.0 获取权限
        performCodeWithPermission(getResources().getString(R.string.App_dw_qx),new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        //执行打开相机相关代码
                    }
                    @Override
                    public void noPermission() {

                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        initPopWindow();
        initLocation();
        switch (editType) {
            case ADDR_EDITTYPE_EDIT:
                address = getIntent().getParcelableExtra("address");
                if (address == null) return;
                addrEditNameEt.setText(address.getName());
                addrEditAddrEt.setText(address.getAddress());
                addrEditPhoneEt.setText(address.getPhone());
                province  = address.getProvince();
                city = address.getCity();
                district = address.getDistrict();
                if(province.equals(city)){
                    addrLocationTv.setText(province + district);
                }else {
                    addrLocationTv.setText(province + city + district);

                }
                if (address.getIs_on().equals("1")) {
                    addrEditStatesBtn.setToggleOn();
                }
                break;
            case ADDR_EDITTYPE_ADD:
                addrEditHeader.setTitle(getResources().getString(R.string.address_add_new));
                break;
        }

        addrEditRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                if (jugePostCondition()) {
                    getEditData();
                    if (editType == ADDR_EDITTYPE_EDIT) {
                        postEditAddress();
                    } else if (editType == ADDR_EDITTYPE_ADD) {
                        postAddAddress();
                    }
                }
            }
        });

        addObserver = new BaseObserver<AddrAddResponse>() {
            @Override
            public void onNextResponse(AddrAddResponse baseResponse) {
                SPUtils.put("user_address_id", baseResponse.getData().getAddress_id());
                SPUtils.put("user_address_name", address.getName());
                SPUtils.put("user_address_address", address.getAddress());
                SPUtils.put("user_address_phone", address.getPhone());
                SPUtils.put("user_address_is_on", address.getIs_on());
                SPUtils.put("user_address_province", address.getProvince());
                SPUtils.put("user_address_city", address.getCity());
                SPUtils.put("user_address_district", address.getDistrict());
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onErrorResponse(AddrAddResponse baseResponse) {
            }
        };

        editObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
            }
        };

        areaObserver  = new Observer<RxCacheResult<AddrCodeResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressMaterial.dismiss();
            }

            @Override
            public void onNext(RxCacheResult<AddrCodeResponse> codeResponse) {
                initAreaData(codeResponse.getResultModel());
                progressMaterial.dismiss();
            }
        };
    }

    @Override
    protected void loadData() {
        getAreaInfo();
    }

    private void getEditData() {
        if (address == null) {
            address = new Address();
        }
        address.setAddress(addrEditAddrEt.getText().toString().trim());
        address.setName(addrEditNameEt.getText().toString().trim());
        address.setPhone(addrEditPhoneEt.getText().toString().trim());
        address.setProvince(mCurrentProviceName);
        address.setCity(mCurrentCityName);
        address.setDistrict(mCurrentDistrictName);
        if (addrEditStatesBtn.isToggleOn()) {
            address.setIs_on("1");
        } else {
            address.setIs_on("0");
        }
    }


    private void getAreaInfo() {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getUserCacheApi(this)
                .getArea()
                .subscribeOn(Schedulers.io())
                .materialize()
                .observeOn(AndroidSchedulers.mainThread())
                .<RxCacheResult<AddrCodeResponse>>dematerialize() // 地址缓存
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(areaObserver);
        subscriptionList.add(subscription);
    }

    private void postAddAddress() {
        Subscription subscription = NetworkManager.getUserDataApi()
                .addAddr(SPUtils.getToken(), address.getName(), address.getAddress(), address.getPhone(),
                        address.getIs_on(),mCurrentProviceName,mCurrentCityName,mCurrentDistrictName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addObserver);
        subscriptionList.add(subscription);
    }


    private void postEditAddress() {
        Subscription subscription = NetworkManager.getUserDataApi()
                .editAddr(SPUtils.getToken(), address.getId(), address.getName(), address.getAddress(),
                        address.getPhone(), address.getIs_on(),mCurrentProviceName,mCurrentCityName,mCurrentDistrictName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(editObserver);
        subscriptionList.add(subscription);
    }


    private boolean jugePostCondition(){
        String nameStr = addrEditAddrEt.getText().toString().trim();
        if (nameStr.equals("")) {
            showWaringDialog(getResources().getString(R.string.address_person_none));
            return false;
        }
        String addrStr = addrLocationTv.getText().toString().trim();
        if (addrStr.equals("")) {
            showWaringDialog(getResources().getString(R.string.address_pro_shi));
            return false;
        }
        String phoneStr = addrEditPhoneEt.getText().toString().trim();
        if (phoneStr.equals("")) {
            showWaringDialog(getResources().getString(R.string.address_phone_none));
            return false;
        }

        if (! TextUtils.isPhone(phoneStr)) {
            showWaringDialog(getResources().getString(R.string.address_phone_no_true));
            return false;
        }
        return true;
    }


    @OnClick({R.id.addr_edit_location_ll,R.id.addr_edit_map_ll})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addr_edit_location_ll:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addrEditNameEt.getWindowToken(), 0);
                popupWindow.showAtLocation(addrStatesLl, Gravity.BOTTOM,0,0);
                break;
            case R.id.addr_edit_map_ll:
                AddressMapActivity.activityStart(this, ACTIVITY_CODE_BAIDUMAP);
                break;
        }
    }


    private void  initPopWindow(){
        parent = findViewById(R.id.addr_edit_rl);
        poprootview = LayoutInflater.from(this).inflate(R.layout.popup_select_addr, null);
        cancelTv = (TextView) poprootview.findViewById(R.id.pop_seletaddr_cancel);
        completeTv = (TextView) poprootview.findViewById(R.id.pop_seletaddr_complete);
        mViewArea = (WheelView) poprootview.findViewById(R.id.id_province);
        mViewCity = (WheelView) poprootview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) poprootview.findViewById(R.id.id_district);
        mViewArea.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        completeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(mCurrentProviceName.equals(mCurrentCityName)){
                    addrLocationTv.setText(mCurrentProviceName+mCurrentDistrictName);
                }else{
                    addrLocationTv.setText(mCurrentProviceName+mCurrentCityName +mCurrentDistrictName);
                }
            }
        });
        popupWindow = new PopupWindow(poprootview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(poprootview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_null));
        popupWindow.setAnimationStyle(R.style.popwin_anim_bottom_style);
    }



    private void  initAreaData(AddrCodeResponse codeResponse){
        initProvinceDatas(codeResponse);
        mViewArea.setViewAdapter(new ArrayWheelAdapter<String>(AddrEditActivity.this, mProvinceDatas));
        mViewArea.setCurrentItem(0);
        // 设置可见条目数量
        mViewArea.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateDistrict();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewArea) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateDistrict();
        } else if (wheel == mViewDistrict) {
            if(mDistrictDatasMap != null) {
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
            }
        }
    }


    protected void initProvinceDatas(AddrCodeResponse codeResponse) {
        List<Area> areaList = codeResponse.getData().getArea();
        int areaListSize = areaList.size();
        mProvinceDatas = new String[areaListSize];
        for (int i=0; i< areaListSize; i++) {
            mProvinceDatas[i] = areaList.get(i).getName();
            List<City> cityList = areaList.get(i).getCity();
            String[] cityNames = new String[cityList.size()];
            for (int j=0; j< cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityNames[j] = cityList.get(j).getName();
                List<District> districtList = cityList.get(j).getDistrict();
                String[] distrinctNameArray = new String[districtList.size()];
                District[] distrinctArray = new District[districtList.size()];
                for (int k = 0; k<districtList.size(); k++) {
                    // 遍历市下面所有区/县的数据
                    District district = new District(districtList.get(k).getId(), districtList.get(k).getName());
                    distrinctArray[k] = district;
                    distrinctNameArray[k] = district.getName();
                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
            }
            // 省-市的数据，保存到mCitisDatasMap
            mCitisDatasMap.put(areaList.get(i).getName(), cityNames);
        }


    }
    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateDistrict() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewArea.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateDistrict();
    }


    private void initLocation(){
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
    }



    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null ) {
                return;
            }
            L.i(TAG + " 定位  " +  location.getAddrStr()+ "  getLatitude "  +location.getLatitude() +"  getLongitude "  +location.getLongitude() +"  getProvince "  + location.getProvince() +"  getCity " + location.getCity()+"  getDistrict "  +  location.getDistrict() );
            mCurrentProviceName = location.getProvince();
            mCurrentCityName = location.getCity();
            mCurrentDistrictName = location.getDistrict();
            mLocClient.stop();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_BAIDUMAP:
                    String tempaddress = data.getStringExtra("address");
                    addrEditAddrEt.setText(tempaddress);
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        mLocClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        super.onDestroy();
    }
}
