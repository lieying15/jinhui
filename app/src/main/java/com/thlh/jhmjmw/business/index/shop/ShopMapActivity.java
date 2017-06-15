package com.thlh.jhmjmw.business.index.shop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.function.mapoverlayutil.WalkingRouteOverlay;
import com.thlh.baselib.model.StoreNear;
import com.thlh.baselib.model.response.StoreNearResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogNormal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 附近小店页
 */
public class ShopMapActivity extends BaseActivity implements OnGetRoutePlanResultListener {
    private final String TAG = "ShopMapActivity";
    @BindView(R.id.store_baidumap_mapview)
    MapView mMapView;
    @BindView(R.id.store_name_iv)
    ImageView storeNameIv;
    @BindView(R.id.store_name_tv)
    TextView storeNameTv;
    @BindView(R.id.store_addr_tv)
    TextView storeAddrTv;
    @BindView(R.id.store_phone_tv)
    TextView storePhoneTv;
    @BindView(R.id.shop_bottom_route_tv)
    TextView bottomRouteTv;


    /**
     * 百度地图控件
     */
    private BaiduMap mBaiduMap;
    private MapStatus mMapStatus;

    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private LatLng userLatlng;
    private double userLat, userLng;

    boolean isFirstLoc = true; // 是否首次定位
    private View mapMarkWindow;
    private TextView markerNameTv, markerAddrTv, markerGuideTv;

    private BaseObserver<StoreNearResponse> storeNearObserver;
    private List<StoreNear> stores;

    private InfoWindow mInfoWindow;
    private DialogNormal.Builder bindinfoNormal;
    private String storeid;
    private StoreNear store;
    private LatLng markerll;
    private RouteLine route = null;
    private boolean hasLoadData = false;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ShopMapActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context, int code) {
        Intent intent = new Intent();
        intent.setClass(context, ShopMapActivity.class);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopmap);
        ButterKnife.bind(this);
        performCodeWithPermission(getResources().getString(R.string.App_dw_qx), new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        //执行打开相机相关代码
                    }

                    @Override
                    public void noPermission() {

                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // 地图初始化
        initBaiduMap();
        // 定位初始化
        initLocation();
        // 初始地图小店
        initMarkerWindow();

        bindinfoNormal = new DialogNormal.Builder(this);
        //地图标记的点击事件
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if (marker == null || marker.getExtraInfo() == null) {
//                    L.e(TAG + " marker 为空");
//                    return false;
//                }
//                storeid = marker.getExtraInfo().getString("id");
//                String name = marker.getExtraInfo().getString("store_name");
//                String address = marker.getExtraInfo().getString("address");
//                String phone = marker.getExtraInfo().getString("phone");
//                L.e(TAG + " maker storeid:" + storeid + " name:" + name + " address：" + address + " phone：" + phone);
//                markerNameTv.setText(name);
//                markerAddrTv.setText(address);
//                final LatLng markerll = marker.getPosition();
//                Point point = mBaiduMap.getProjection().toScreenLocation(markerll);
//                int infowindow_distance = 10 * (int) BaseApplication.density;
//                point.y -= infowindow_distance;
//                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(point);
//                mInfoWindow = new InfoWindow(mapMarkWindow, llInfo, -infowindow_distance);
//                mBaiduMap.showInfoWindow(mInfoWindow);
//                bottomRouteTv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        initRoute(markerll);
//                    }
//                });
//                return true;
//            }
//        });


        storeNearObserver = new BaseObserver<StoreNearResponse>() {
            @Override
            public void onErrorResponse(StoreNearResponse storeNearResponse) {
            }

            @Override
            public void onNextResponse(StoreNearResponse storeNearResponse) {
                stores = storeNearResponse.getData().getStoreNear();
                hasLoadData = true;
                if (stores == null || stores.size() == 0) {
                    return;
                }
                store = calculateDistance(stores);
                LatLng geoPoint = new LatLng(Double.parseDouble(store.getLat()), Double.parseDouble(store.getLng()));
                BitmapDescriptor marker = BitmapDescriptorFactory.fromResource(R.drawable.bg_null);
                Bundle storeBundle = new Bundle();
                storeBundle.putString("id", store.getId());
                storeBundle.putString("store_name", store.getStore_name());
                storeBundle.putString("phone", store.getPhone());
                storeBundle.putString("logo", store.getLogo());
                storeBundle.putString("province", store.getProvince());
                storeBundle.putString("city", store.getCity());
                storeBundle.putString("district", store.getDistrict());
                storeBundle.putString("address", store.getAddress());
                storeBundle.putString("lat", store.getLat());
                storeBundle.putString("lng", store.getLng());
                storeBundle.putString("rating", store.getRating());



                OverlayOptions item = new MarkerOptions().position(geoPoint)
                        .icon(marker).zIndex(9).draggable(true).title(store.getStore_name())
                        .extraInfo(storeBundle);
                mBaiduMap.addOverlay(item);
                showMarkWindow(store);
                bottomRouteTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initRoute(markerll);
                    }
                });
                mapMoveToStore();
            }
        };

    }


    @Override
    protected void loadData() {

    }


    private void initBaiduMap() {
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        userLat = SPUtils.getLatitude();
        userLng = SPUtils.getLongitude();
        userLatlng = new LatLng(userLat, userLng);
        mMapStatus = new MapStatus.Builder().target(userLatlng).zoom(17).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.bg_null);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, false, mCurrentMarker));


//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, false, mCurrentMarker));
//        // 注册移动地图监听
//        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
//            @Override
//            public void onMapStatusChangeStart(MapStatus status) {}
//
//            @Override
//            public void onMapStatusChangeFinish(MapStatus status) {
//                updateMapState(status);
//            }
//
//            @Override
//            public void onMapStatusChange(MapStatus status) {
//            }
//        });
    }

    private void initLocation() {
        mBaiduMap.setMyLocationEnabled(true);
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

    private void initRoute(LatLng markerll) {
        progressMaterial.show();
        mBaiduMap.hideInfoWindow();
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        PlanNode stNode = PlanNode.withLocation(userLatlng);
        PlanNode enNode = PlanNode.withLocation(markerll);
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
    }

    private void initMarkerWindow() {
        //地图标记点
        mapMarkWindow = LayoutInflater.from(ShopMapActivity.this).inflate(R.layout.view_map_marker, null);
        markerNameTv = (TextView) mapMarkWindow.findViewById(R.id.map_store_name_tv);
        markerAddrTv = (TextView) mapMarkWindow.findViewById(R.id.map_store_addr_tv);
        markerGuideTv = (TextView) mapMarkWindow.findViewById(R.id.map_store_guide_tv);
    }


    private void showMarkWindow(StoreNear store) {
//        storeid = marker.getExtraInfo().getString("id");
        String name = store.getStore_name();
        String address = store.getAddress();
        String phone = store.getPhone();
        L.e(TAG + " maker storeid:" + storeid + " name:" + store.getStore_name() + " address：" + store.getAddress() + " phone：" + store.getPhone());

        storeNameTv.setText(name);
        storeAddrTv.setText(address);
        storePhoneTv.setText(phone);
        markerNameTv.setText(name);
        markerAddrTv.setText(address);
        markerll = new LatLng(Double.parseDouble(store.getLat()), Double.parseDouble(store.getLng()));
        Point point = mBaiduMap.getProjection().toScreenLocation(markerll);
//        int infowindow_distance = 5 *  (int) BaseApplication.density;
//        point.y -= infowindow_distance;
        LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(point);
        markerGuideTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                initRoute(markerll);
            }
        });
        mInfoWindow = new InfoWindow(mapMarkWindow, llInfo, 0);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    private void mapMoveToStore() {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(markerll).zoom(17.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    public void loadNearStore() {
        L.e(TAG + " loadNearStore lat " + SPUtils.getLatitude() + " lng " + SPUtils.getLongitude());
        NetworkManager.getStoreApi()
                .getNearStore(SPUtils.getToken(), SPUtils.getLatitude(), SPUtils.getLongitude())
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(storeNearObserver);
    }

    //计算距离，取最近小店
    private StoreNear calculateDistance(List<StoreNear> stores) {
        if (stores.size() == 1) {
            return stores.get(0);
        }
        StoreNear store = stores.get(0);
        double minDistance = 0;
        for (StoreNear storeNear : stores) {
            Double lat = Double.parseDouble(storeNear.getLat());
            Double lng = Double.parseDouble(storeNear.getLng());

            double _x = Math.abs(userLat - lat);
            double _y = Math.abs(userLng - lng);
            double distance = Math.sqrt(_x * _x + _y * _y);
            if (distance < minDistance) {
                minDistance = distance;
                store = storeNear;
            }
        }
        return store;
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
            L.e(TAG + " 定位  " + location.getAddrStr() + "  getLatitude " + location.getLatitude() + "  getLongitude " + location.getLongitude() + "  getProvince " + location.getProvince() + "  getCity " + location.getCity() + "  getDistrict " + location.getDistrict());
            SPUtils.setLatitude(location.getLatitude() + "");
            SPUtils.setLongitude(location.getLongitude() + "");
            MyLocationData locData = new MyLocationData.Builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc && !hasLoadData) {
                isFirstLoc = false;
                userLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(userLatlng).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.img_map_gps);
                OverlayOptions ooD = new MarkerOptions().position(userLatlng).icon(bdA).perspective(false);
                mBaiduMap.addOverlay(ooD);
            }
            loadNearStore();
        }
    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        progressMaterial.dismiss();
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ShopMapActivity.this, getResources().getString(R.string.sorry_no_find_result), Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            try {
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);

                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

//    @OnClick({R.id.shop_bottom_route_tv})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.shop_bottom_route_tv:
//                ShareUtils shareUtils = new ShareUtils(ShopActivity.this,"每家美物","万元冰箱免费送","http://v2.mjmw365.com",R.mipmap.ic_launcher);
//                shareUtils.showShareWindow();
//                break;
//        }
//    }


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
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


}
