package com.thlh.jhmjmw.business.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑收货地址页
 */
public class AddressMapActivity extends BaseViewActivity implements OnGetGeoCoderResultListener {
    private final String TAG = "AddressMapActivity";
    @BindView(R.id.store_baidumap_mapview)
    MapView mMapView;
    @BindView(R.id.baidumap_rv)
    EasyRecyclerView baidumapRv;
    @BindView(R.id.address_baidumap_bottom_rl)
    RelativeLayout bottomRl;

    /**
     * 百度地图控件
     */
    private BaiduMap mBaiduMap;
    private MapStatus mMapStatus;

    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private GeoCoder mSearch = null; // 搜索模块
    boolean isFirstLoc = true; // 是否首次定位


    private AddressMapAdapter mapAroundAdapter;
    private List<PoiInfo> nearList = new ArrayList<PoiInfo>();
    private SparseArray<Boolean> checkStates = new SparseArray<>();

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddressMapActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context,int code) {
        Intent intent = new Intent();
        intent.setClass(context, AddressMapActivity.class);
        context.startActivityForResult(intent,code);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_baidumap);
        ButterKnife.bind(this);
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        mMapStatus = new MapStatus.Builder().zoom(18).build();

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.bg_null);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, false, mCurrentMarker));

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
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

        // 注册移动地图监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {}

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState(status);
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);


        mapAroundAdapter = new AddressMapAdapter(checkStates);

        mapAroundAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                boolean tempStates = checkStates.get(position);
                if (tempStates) {
                    checkStates.setValueAt(position, false);
                } else {
                    //单选，关闭其他
                    for (int i = 0; i < checkStates.size(); i++) {
                        if (checkStates.get(i)) {
                            checkStates.setValueAt(i, false);
                        }
                    }
                    checkStates.setValueAt(position, true);
                }
                mapAroundAdapter.notifyDataSetChanged();
            }
        });

        mapAroundAdapter.setList(nearList);
        baidumapRv.setAdapter(mapAroundAdapter);
        baidumapRv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void loadData() {

    }



    private void updateMapState(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        if (mCenterLatLng == null) {
            return;
        }
        /**获取经纬度*/
        double lat = mCenterLatLng.latitude;
        double lng = mCenterLatLng.longitude;
        // 坐标转换为地址
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mCenterLatLng));
    }


    @OnClick(R.id.address_baidumap_bottom_rl)
    public void onClick() {
        String selectAddress ="";
        for (int i = 0; i < checkStates.size(); i++) {
            if (checkStates.get(i)) {
                selectAddress = ((PoiInfo)mapAroundAdapter.getItem(i)).name;
                break;
            }
        }
        if(selectAddress.equals("")){
            showWaringDialog(getResources().getString(R.string.address_please_choose));
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("address",selectAddress);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            L.e(TAG + " onReceiveLocation   AddrStr " +  location.getAddrStr()+ "  getLatitude "  +location.getLatitude() +"  getAltitude "  +location.getAltitude() +"  getProvince "  + location.getProvince() +"  getCity " + location.getCity()+"  getDistrict "  +  location.getDistrict() );
            MyLocationData locData = new MyLocationData.Builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
                mLocClient.stop();
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {}
    }

    //根据经纬度返回地址
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            L.e(TAG + " 未能找到结果");
            return;
        }

        if (reverseGeoCodeResult.getPoiList() != null && reverseGeoCodeResult.getPoiList().size() > 0) {
            nearList.clear();
            nearList.addAll(reverseGeoCodeResult.getPoiList());
            nearList.get(0).address = reverseGeoCodeResult.getAddress();
            checkStates.clear();
            for (int i = 0; i < nearList.size(); i++) {
                checkStates.put(i, false);
            }
            checkStates.setValueAt(0, true);
            mapAroundAdapter.setList(nearList);
        }

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult.getLocation()));
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


}
