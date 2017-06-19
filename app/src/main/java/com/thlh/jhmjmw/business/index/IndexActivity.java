package com.thlh.jhmjmw.business.index;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.model.response.BindIceBoxResponse;
import com.thlh.baselib.model.response.GetIceBoxInfoResponse;
import com.thlh.baselib.model.response.InfoTransportListResponse;
import com.thlh.baselib.model.response.ScanResponse;
import com.thlh.baselib.model.response.VersionResponse;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.ShopcartFragment;
import com.thlh.jhmjmw.business.devices.BindDevicesDialogFrg;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.index.choiceness.ChoicenessV3Fragment;
import com.thlh.jhmjmw.business.index.homepage.HomePageFragment;
import com.thlh.jhmjmw.business.index.mine.MineFragment;
import com.thlh.jhmjmw.business.index.shop.ShopActivity;
import com.thlh.jhmjmw.business.index.shop.ShopV3Fragment;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.UpdateManager;
import com.thlh.jhmjmw.receiver.ConnectionChangeReceiver;
import com.thlh.jhmjmw.view.BottomTabViewV3;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.viewlib.tablayout.MsgView;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IndexActivity extends BaseActivity {
    private static final String TAG = "IndexActivity";
    public final int ACTIVITY_CODE_LOGIN = 1;
    public final int ACTIVITY_CODE_SHOP = 2;
    public final int ACTIVITY_CODE_SCANNIN = 3;
    public static final int POSITON_HOMEPAGE = 0;
    public static final int POSITON_CATEGORY = 1;
    public static final int POSITON_SHOP = 2;
    public static final int POSITON_SHOPCART = 3;
    public static final int POSITON_MINE = 4;
    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private final String BIND_ICEBOX_TYPE_GETINFO = "0";
    private final String BIND_ICEBOX_TYPE_BIND = "1";
    @BindView(R.id.homepage_fragment_content)
    FrameLayout homepageFragmentContent;
    @BindView(R.id.homepage_btab)
    BottomTabViewV3 homepageBtab;
    private String scan_result;

    private FragmentManager fm;
    private android.support.v4.app.FragmentTransaction transaction;
    private Fragment mCurrentFragment, tofragment;
    private HomePageFragment homePageFragment;
    private ChoicenessV3Fragment choicenessFragment;
    private MineFragment mineFragment;
    private ShopV3Fragment shopFragment;
    private ShopcartFragment shopCartFragment;
    private NoConnectionFragment connectionFragment;

    public TextView shopcartTv;
    public MsgView shopcartNumTv;
    /**
     * 用户信息
     */
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    /**
     * 用户信息
     */
    private boolean isLogin;
    private int position = 0;
    private String jpush_rid;
    /**
     * 判断是否退出程序
     */
    private long mExitTime;

    private BaseObserver<GetIceBoxInfoResponse> geticeboxoinfobserver;
    private BaseObserver<BindIceBoxResponse> bandiceboxobserver;
    private BaseObserver<BaseResponse> unbandiceboxobserver, pushobserver;
    private BaseObserver<ScanResponse> bandstoreobserver;
    private BaseObserver<VersionResponse> versionobserver;
    private BaseObserver<WalletResponse> walletObserver;
    private BaseObserver<InfoTransportListResponse> infoObserver;

    private DialogNormal.Builder bindinfoNormal, bindResultDialog;
    //判断网络广播
    private ConnectionChangeReceiver connectionChangeReceiver;


    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
        intent.setClass(context, IndexActivity.class);
        intent.putExtra("position", 0);
        context.startActivity(intent);
    }

    public static void activityStart(Context context, int position) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
        intent.setClass(context, IndexActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {
        performCodeWithPermission(getResources().getString(R.string.App_dw_qx), new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        //执行打开相机相关代码
                    }

                    @Override
                    public void noPermission() {

                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE);

        jpush_rid = JPushInterface.getRegistrationID(getApplicationContext());
        L.e(TAG + "  Deployment:" + Deployment.BASE_URL + " RegistrationID " + jpush_rid);
        fm = getSupportFragmentManager();
        isLogin = SPUtils.getIsLogin();
        initLocation();
        initBroadcast();
    }



    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        L.e(TAG + " initViews");
        setDefaultFragment();

        homepageBtab.setChangeeFragmentListener( new BottomTabViewV3.ChangeFragmentListener() {
            @Override
            public void changeFragment(int position) {
                switchFragment(position);
            }
        });

        shopcartNumTv = homepageBtab.getTabShopcartNumTv();
        shopcartTv = homepageBtab.getTabShopcartTv();
        updateShopCartTab();

        bindResultDialog = new DialogNormal.Builder(this);
        bindinfoNormal = new DialogNormal.Builder(this);
        geticeboxoinfobserver = new BaseObserver<GetIceBoxInfoResponse>() {
            @Override
            public void onErrorResponse(GetIceBoxInfoResponse bindResponse) {
                L.e(TAG + " 问题：" + bindResponse.getErr_code() + ":   " + bindResponse.getErr_msg());
                showErrorDialog(bindResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(GetIceBoxInfoResponse bindResponse) {
                L.e(TAG + " 获取冰箱信息：user_equipmentid" + bindResponse.getData().getEquipment().getUuid());

                showBindIceBoxDialog(bindResponse);
            }
        };

        bandiceboxobserver = new BaseObserver<BindIceBoxResponse>() {
            @Override
            public void onErrorResponse(BindIceBoxResponse bindResponse) {
                L.e(TAG + " 问题：" + bindResponse.getErr_code() + ":   " + bindResponse.getErr_msg());
                showErrorDialog(bindResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BindIceBoxResponse bindResponse) {
                L.e(TAG + " 绑定信息 user_equipmentid:" + bindResponse.getData().getAccount().getEquipment_id());
                String equipmentid = bindResponse.getData().getAccount().getEquipment_id() + "";
                SPUtils.put("user_equipmentid", equipmentid);
                showBindIceboxSuccessDialog();
            }
        };


        bandstoreobserver = new BaseObserver<ScanResponse>() {
            @Override
            public void onErrorResponse(ScanResponse baseResponse) {
                L.e(TAG + " 问题：" + baseResponse.getErr_code() + ":   " + baseResponse.getErr_msg());
                showErrorDialog(baseResponse.getErr_msg());

            }

            @Override
            public void onNextResponse(ScanResponse scanResponse) {
                showSuccessDialog(scanResponse.getData().getMsg());
            }
        };

        unbandiceboxobserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                L.e(TAG + " 问题：" + baseResponse.getErr_code() + ":   " + baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {

            }
        };

        versionobserver = new BaseObserver<VersionResponse>() {
            @Override
            public void onNextResponse(VersionResponse versionResponse) {
                int versioncode = Integer.parseInt(versionResponse.getData().getVer());
                String url = versionResponse.getData().getUrl();
                String content = versionResponse.getData().getContent();
                L.e(TAG + "  update versioncode " + versioncode + " url:" + url + " content:" + content);
                new UpdateManager(IndexActivity.this, content, url, versioncode).showDialog();
                if (SPUtils.getIsLogin()) {
                    loadWallet();
                }
            }

            @Override
            public void onErrorResponse(VersionResponse baseResponse) {
            }
        };

        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
            }

            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                SPUtils.put("user_balance", walletResponse.getData().getBalance() + "");
                SPUtils.put("user_mjb", walletResponse.getData().getMjb() + "");
            }
        };

        pushobserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                L.e(TAG + " 问题：" + baseResponse.getErr_code() + ":   " + baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
            }
        };

        infoObserver = new BaseObserver<InfoTransportListResponse>() {
            @Override
            public void onErrorResponse(InfoTransportListResponse response) {
                progressMaterial.dismiss();
            }

            @Override
            public void onNextResponse(InfoTransportListResponse response) {
                progressMaterial.dismiss();

            }
        };
    }

    private void setDefaultFragment() {
//        homePageFragment = (HomePageFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        if (homePageFragment == null)
            homePageFragment = HomePageFragment.newInstance();
        choicenessFragment = ChoicenessV3Fragment.newInstance();
        shopCartFragment = ShopcartFragment.newInstance();
        shopFragment = ShopV3Fragment.newInstance();
        mineFragment = MineFragment.newInstance();
        connectionFragment = NoConnectionFragment.newInstance();
        mCurrentFragment = homePageFragment;
        transaction = fm.beginTransaction();
        transaction.add(R.id.homepage_fragment_content, mCurrentFragment);
        transaction.show(mCurrentFragment).commitAllowingStateLoss();
        shopCartFragment.setUpdatelistener(new ShopcartFragment.UpdateCartInfo() {
            @Override
            public void update() {
                updateShopCartTab();
            }
        });
    }


    private void initLocation() {
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

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.setPriority(1000);
        connectionChangeReceiver = new ConnectionChangeReceiver();
        registerReceiver(connectionChangeReceiver, filter);
    }

    @Override
    protected void loadData() {
        loadVersion();
    }

    public void loadWallet() {
        NetworkManager.getUserDataApi()
                .wallet(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(this,false))
                .subscribe(walletObserver);
    }

    public void loadVersion() {
        NetworkManager.getGoodsDataApi()
                .getVersion()
                .compose(RxUtils.androidSchedulers(this,false))
                .subscribe(versionobserver);
    }

    private void postPushData(String pid) {
        String pushflag = (String) SPUtils.get("user_push_flag", "0");
        L.e(TAG + " postPushData  pid:" + pid + " flag:" + pushflag);
        NetworkManager.getUserDataApi()
                .postPushInfo(SPUtils.getToken(), pid, pushflag, "ANDROID")
                .compose(RxUtils.androidSchedulers(this,false))
                .subscribe(pushobserver);
    }


    public BottomTabViewV3 getBottomTabView() {
        return homepageBtab;
    }

    public MineFragment getMineFragment() {
        return mineFragment;
    }

    public ChoicenessV3Fragment getChoicenessFragment() {
        return choicenessFragment;
    }

    public ShopcartFragment getShopCartFragment() {
        return shopCartFragment;
    }

    public FrameLayout getHomepageFragmentContent() {
        return homepageFragmentContent;
    }

    //    替换fragment
    public void switchFragment(int position) {
        if (homepageBtab.currIndex == position && homepageBtab.currIndex == BottomTabViewV3.POSITON_HOMEPAGE) {
            homePageFragment.updateHomePage();
            return;
        }
        if (homepageBtab.currIndex == position) {
            return;
        }
        transaction = fm.beginTransaction();
        switch (position) {
            case BottomTabViewV3.POSITON_HOMEPAGE:
                tofragment = homePageFragment;
                if (homePageFragment != null) {
                    homePageFragment.loadHomePageData(true);
                    homePageFragment.updateHomePage();
                }
                break;
            case BottomTabViewV3.POSITON_CATEGORY:
                tofragment = choicenessFragment;
                break;
            case BottomTabViewV3.POSITON_SHOP:
                ShopActivity.activityStartForResult(IndexActivity.this, ACTIVITY_CODE_SHOP, homepageBtab.currIndex);
                return;
            case BottomTabViewV3.POSITON_SHOPCART:
                tofragment = shopCartFragment;
                if(shopCartFragment.isHasInit()){
                    shopCartFragment.updateCartData();
                }
//                if (isLogin) {
//                    ShopCartActivity.activityStart(IndexActivity.this);
//                } else {
//                    homepageBtab.setNeedChangeTabView(false);
//                    Intent intent = new Intent();
//                    intent.putExtra("startType", LoginActivity.START_TYPE_HOMEPAGE);
//                    intent.setClass(IndexActivity.this, LoginActivity.class);
//                    startActivityForResult(intent, ACTIVITY_CODE_LOGIN);
//                }
                break;
            case BottomTabViewV3.POSITON_MINE:
                if (isLogin) {
                    tofragment = mineFragment;
                } else {
                    homepageBtab.setNeedChangeTabView(false);
                    Intent intent = new Intent();
                    intent.putExtra("startType", LoginActivity.START_TYPE_HOMEPAGE);
                    intent.setClass(IndexActivity.this, LoginActivity.class);
                    startActivityForResult(intent, ACTIVITY_CODE_LOGIN);
                    return;
                }

                break;
            case BottomTabViewV3.POSITON_NOCONNECTION:
                tofragment = connectionFragment;
                break;
            default:
                tofragment = homePageFragment;
                break;
        }
        if (!tofragment.isAdded()) {    // 先判断是否被add过
            transaction.hide(mCurrentFragment).add(R.id.homepage_fragment_content, tofragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(mCurrentFragment).show(tofragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
        }
        mCurrentFragment = tofragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homepageBtab.setNeedChangeTabView(true);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_LOGIN:
                    isLogin = SPUtils.getIsLogin();
                    getBottomTabView().changeTabView(getBottomTabView().currIndex, 0);
                    updateShopCartTab();
                    loadWallet();
                    break;
                case ACTIVITY_CODE_SHOP:
                    int bottom_index = data.getIntExtra("bottom_index", 0);
                    int bottom_before_index = data.getIntExtra("bottom_before_index", 0);
                    getBottomTabView().recoverColor();
                    switchFragment(bottom_index);
//                    if (bottom_index == BottomTabViewV3.POSITON_SHOPCART) {
//                        switchFragment(BottomTabViewV3.POSITON_HOMEPAGE);
//                        getBottomTabView().changeTabView(BottomTabViewV3.POSITON_HOMEPAGE, 0);
//                    } else {
                        switchFragment(bottom_index);
                        getBottomTabView().changeTabView(bottom_index, 0);
//                    }
                    break;
                case Constants.ACTIVITYCODE_SCANNQR:
                    scan_result = data.getStringExtra("scan_result");
                    judgeScanResult(scan_result);
                    break;
                default:
                    break;
            }
        }
    }

    private void judgeScanResult(String scanStr) {
        L.e("扫描结果 " + scanStr);
        //绑定冰箱
        if (scanStr.contains("mjmw365-icebox")) {
            L.e("扫描二维码 绑定冰箱 ");
            scan_result = scanStr.replace("mjmw365-icebox", "");
            if (SPUtils.getIsLogin()) {
                getIceboxInfo(scan_result);//查看
            } else {
                showLoginDialog();
            }
        }

        //绑定小店 v1.1
        if (scanStr.contains("http://v2.mjmw365.com/ch/?ch=")) {
            String ch = scanStr.replace("http://v2.mjmw365.com/ch/?ch=", "").trim();
            L.e(TAG + "绑定小店 judgeScanResult scan_result: " + ch);
            SPUtils.put("user_ch", ch);
            bandStore(ch);
        }
    }

    private void getIceboxInfo(String fridge) {
        L.e("获取冰箱信息  getIceboxInfo");
        NetworkManager.getUserDataApi()
                .getIcoboxInfo(SPUtils.getToken(), fridge)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geticeboxoinfobserver);
    }

    private void bandIcebox(String fridge) {
        L.e(TAG + " bandIcebox fridge" + fridge);
        NetworkManager.getUserDataApi()
                .bindIcobox(SPUtils.getToken(), fridge, "1", SPUtils.getLatitude(), SPUtils.getLongitude())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bandiceboxobserver);
    }


    private void bandStore(String ch) {
        L.e(TAG + " bandStore");
//                绑定小店+代理商
        NetworkManager.getGoodsDataApi()
                .scanQR(SPUtils.getToken(), ch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bandstoreobserver);
    }

    public boolean isLogin() {
        return isLogin;
    }


    @Override
    protected void onStart() {
        super.onStart();
        isLogin = SPUtils.getIsLogin();
        updateShopCartTab();
        if ((Boolean) SPUtils.get("needupdate_userinfo", false) && SPUtils.getIsLogin()) {
            updateUserInfo();
            if (!jpush_rid.equals("") && SPUtils.getIsLogin()) {
                postPushData(jpush_rid);
            }
            loadWallet();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(shopCartFragment.isHasInit()){
            shopCartFragment.updateCartData();
        }
    }

    public void updateShopCartTab() {
        int num = DbManager.getInstance().getAllGoodsNum();
        if (num != 0) {
            shopcartNumTv.setVisibility(View.VISIBLE);
            TextUtils.showNum(shopcartNumTv,num);
            shopcartTv.setTextColor(getResources().getColor(R.color.app_theme));
            double temptotalprice = DbManager.getInstance().getAllGoodsPrice();
            DecimalFormat df = new DecimalFormat("###.0");
            shopcartTv.setText(getResources().getString(R.string.money)+ df.format(temptotalprice));
        } else {
            shopcartNumTv.setVisibility(View.INVISIBLE);
            shopcartTv.setTextColor(getResources().getColor(R.color.text_tips));
            shopcartTv.setText(getResources().getString(R.string.shopcart_header_name));
        }
    }


    private void updateUserInfo() {
        SPUtils.put("needupdate_userinfo", false);
        if (mineFragment != null && mineFragment.isInitView()) {
            mineFragment.updateMineNameTv();
            mineFragment.updateMineAvatarIv();
        }
    }

    private void showBindIceBoxDialog(GetIceBoxInfoResponse bindResponse) {
        final BindDevicesDialogFrg bindDevicesDialog = new BindDevicesDialogFrg();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        bindDevicesDialog.setCode(bindResponse.getData().getEquipment().getUuid());
        bindDevicesDialog.setAddress(bindResponse.getData().getAddress().getAddress());
        bindDevicesDialog.setName(bindResponse.getData().getAddress().getName());
        bindDevicesDialog.setPhone(bindResponse.getData().getAddress().getPhone());
        bindDevicesDialog.setServiceaddress(bindResponse.getData().getStore().getAddress());
        bindDevicesDialog.setServicephone(bindResponse.getData().getStore().getPhone());
        bindDevicesDialog.setBindEvent(new BindDevicesDialogFrg.onBindListener() {
            @Override
            public void binddevices() {
                bandIcebox(scan_result);
            }
        });
        bindDevicesDialog.show(ft, "bindDevicesDialog");
    }

    private void showBindStoreSuccessDialog(String msg) {
        bindResultDialog.setTitle(msg)
                .setRightBtnStr(getResources().getString(R.string.trues))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
    }

    private void showBindIceboxSuccessDialog() {
        ActionResponse response = new ActionResponse();
        response.setHeadertitle(getResources().getString(R.string.binding_ice));
        response.setTitle(getResources().getString(R.string.congratulation));
        response.setContent(getResources().getString(R.string.binding_ice_success));
        ResponseActivity.activityStart(IndexActivity.this, response);
    }


    private void showLoginDialog() {
        bindinfoNormal.setTitle(getResources().getString(R.string.no_loading))
                .setLeftBtnStr(getResources().getString(R.string.back))
                .setRightBtnStr(getResources().getString(R.string.loading))
                .setLeftClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.activityStart(IndexActivity.this);
                    }
                }).create().show();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            L.i(TAG + " 定位  " + location.getAddrStr() + "  getLatitude " + location.getLatitude() + "  getLongitude " + location.getLongitude() + "  getProvince " + location.getProvince() + "  getCity " + location.getCity() + "  getDistrict " + location.getDistrict());
            SPUtils.setLatitude(location.getLatitude() + "");
            SPUtils.setLongitude(location.getLongitude() + "");
            mLocClient.stop();
        }
    }

    // 回退键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            position = intent.getIntExtra("position", 0);
            getBottomTabView().recoverColor();
            switchFragment(position);
            getBottomTabView().changeTabView(position, 0);
        }
        super.onNewIntent(intent);
    }


    // 双击退出
    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.agains_next_out_app), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MobclickAgent.onKillProcess(this);
            BaseApplication.getInstance().exit();
            unregisterReceiver(connectionChangeReceiver);
            finish();
        }
    }


}
