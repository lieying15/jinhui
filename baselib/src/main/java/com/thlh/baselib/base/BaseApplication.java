package com.thlh.baselib.base;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.kingsoft.media.httpcache.KSYProxyService;
import com.thlh.baselib.db.DaoMaster;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.utils.AES;
import com.thlh.baselib.utils.BaseLog;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * 在application中为整个应用程序绑定了验证是否登录的服务,可以通过getLoginService获取服务对象,调用服务里的方法
 * @author many people
 */
public class BaseApplication extends MultiDexApplication {
	private static final String TAG = "BaseApplication";
	public static final String DEFAULT_USER  = "100000";//默认用户名

	/** 当前手机的高度 */
	public static int height;
	/** 当前手机的宽度 */
	public static int width;
    /** 当前手机的像素密度 */
    public static float density;
    /** 当前手机的像素密度DPI */
    public static int densityDIP;

	//运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();
	/** 当前应用的实例 */
	private static BaseApplication mInstance;

	/** 登录用户的缓存数据 */
	private SharedPreferences userinfoSP;
	/** 临时用户的缓存数据 */
	private SharedPreferences tempUserinfoSP;

	/** 友盟-微信登录 */
	private UMShareAPI mShareAPI;
	/**
	 * 获取当前应用程序的实例
	 * @return 应用程序的实例
	 */
	public static BaseApplication getInstance() {
		return mInstance;
	}



	public void onCreate() {
		// 如果不加这一句,使用百度推送会导致崩溃
		super.onCreate();
		// 给实例赋值
		mInstance = this;
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
        density = displayMetrics.density;
        densityDIP = displayMetrics.densityDpi;
		BaseLog.i(TAG, "手机尺寸-->高:" + height + "  ;宽:" + width + "  密度：" + density + "  密度DIP：" + densityDIP);
		//LeakCanary
//		if (LeakCanary.isInAnalyzerProcess(this)) {
//
//			return;
//		}
//		LeakCanary.install(this);


		//百度地图初始化
		SDKInitializer.initialize(this);
		initUMengShare();
		initDb();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		// 启用EventBus3.0加速功能
//		EventBus.builder().addIndex(new SubscriberInfoIndex() {
//			@Override
//			public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
//				return null;
//			}
//		}).installDefaultEventBus();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	private void initDb() {
		DbManager.getInstance().initialize(BaseApplication.this);
		DaoMaster daoMaster = DbManager.getInstance().getDaoMaster();
	}



	//关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity:mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	/**
	 * 返回用户的sp数据
	 *
	 * @return
	 */
	public SharedPreferences getUserinfoSharedPreferences() {
		if (userinfoSP == null) {
			userinfoSP = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		}
		return userinfoSP;
	}

	public SharedPreferences getTempUserInfoSharedPreferences() {
		if (tempUserinfoSP == null) {
			tempUserinfoSP = getSharedPreferences("tempUserinfoSP", Context.MODE_PRIVATE);
		}
		return tempUserinfoSP;
	}


	//初始化分享
	private void initUMengShare() {
		//微信 appid appsecret
		PlatformConfig.setWeixin("wx9fea5d86e2148924", "7d0254d9252dc6778f342d5f69bf9b97");
		//新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");

		mShareAPI =  UMShareAPI.get(this);

	}

	public UMShareAPI getmShareAPI() {
		return mShareAPI;
	}





	//获去登录的 Device字段  （AES加密 ）
	public String getPhoneDevice(){
		//字串组合顺序(设备ID|设备MAC地址|设备类型|设备型号)：device_id|device_mac|device_type(1:iphone 2:android手机 3:冰箱屏)|device_model（设备型号，如：iPhone 6s, Sony M35t等)|IP
		String device = BaseApplication.getInstance().getPhoneIMEI() + "|"+ BaseApplication.getInstance().getPhoneMac()+"|2|"+  BaseApplication.getInstance().getPhoneType()+"|"+ BaseApplication.getInstance().getPhoneIP();
		String encryptResult = "";
		try {
			encryptResult = AES.encrypt(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseLog.i("BaseApplication PhoneDevice: "+encryptResult);
		return encryptResult;
	}

	public String getPhoneIMEI(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmPhone;
		tmDevice = "" + tm.getDeviceId();
		BaseLog.i("BaseApplication 获取手机型号：" + android.os.Build.MODEL + ", IMEI " + tmDevice);
		return tmDevice;
	}

	public String getPhoneType(){
		return android.os.Build.MODEL;
	}

	public String getPhoneMac(){
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		BaseLog.i("BaseApplication 获取手机Mac：" + info.getMacAddress());
		return info.getMacAddress();
	}

	public String getPhoneIP(){
		WifiManager wifiManager = (WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
//		try {
//			String tempip = InetAddress.getByName(String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff))).toString();
//			tempip = tempip.replace("/","");
			BaseLog.i("BaseApplication 获取手机IP：" + ipAddress);
			return  intToIp( ipAddress) ;
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}
//=======================


	private KSYProxyService proxy;

	public static KSYProxyService getKSYProxy(Context context) {
//		App app = (App) context.getApplicationContext();
		return mInstance.proxy == null ? (mInstance.proxy = mInstance.newProxy()) : mInstance.proxy;
	}

	private KSYProxyService newProxy() {
		return new KSYProxyService(this);
	}


}
