package com.thlh.baselib.utils;

import android.os.Environment;

import com.thlh.baselib.base.BaseApplication;

/**
 * Created by Administrator on 2016/5/12.
 */
public class SystemUtils {
    public static String getDiskCacheDir() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = BaseApplication.getInstance().getExternalCacheDir().getPath();
        } else {
            cachePath = BaseApplication.getInstance().getCacheDir().getPath();
        }
        return cachePath;
    }


}
