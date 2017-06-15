package com.thlh.jhmjmw.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.BottomTabViewV3;

/**
 * Created by huqiang on 2017/3/2.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        L.e("TAG", " 网络变化！");

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            L.i("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }

        L.e("TAG", " ---监听wifi的连接状态即是否连上了一个有效无线路由");

        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                L.i("TAG", "isConnected:" + isConnected);
                if (isConnected) {
                } else {

                }
            }
        }

        L.e("TAG", " -- 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听");

        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    L.e("TAG", " -- 如果当前的网络连接成功并且网络连接可用");

                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        L.i("TAG", getConnectionType(info.getType()) + "连上");
                    }
                } else {
                    L.e("TAG", " -- 当前的网络连接bu可用");

                    L.i("TAG", getConnectionType(info.getType()) + "断开");
                    intent.setClass(context, IndexActivity.class);
                    //在BroadcastReceiver(或者像Service那些没有界面的Android组件)中启动Activity，应该设置FLAG_ACTIVITY_NEW_TASK标记。
                    //清除所有的Activity，应该设置FLAG_ACTIVITY_CLEAR_TOP标记。
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("position", BottomTabViewV3.POSITON_NOCONNECTION);
                    context.startActivity(intent);
                }
            }
        }


//        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        //如果有网络连接中就直接退出
//                        return;
//                    }
//                }
//            }
//        }
//        //如果没有网络连接(网络已经断开)就采取相应的逻辑，跳到相应的界面。
//        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean(Constant.PREFS_NETWORK_ONLINE, false);
//        editor.commit();


    }

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }
}
