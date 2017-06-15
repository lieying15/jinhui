package com.thlh.jhmjmw.other;

import com.thlh.baselib.utils.BaseLog;
import com.thlh.jhmjmw.BuildConfig;

/**
 * Log 统一管理类
 */
public class L extends BaseLog {

    public static boolean isDebug = BuildConfig.SHOWLOG;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 下面四个是默认tag的函数
    public static void i(String msg)
    {
        if (isDebug)
            BaseLog.i(msg);
    }

    public static void d(String msg)
    {
        if (isDebug)
            BaseLog.d(msg);
    }

    public static void e(String msg)
    {
        if (isDebug)
            BaseLog.e(msg);
    }

    public static void v(String msg)
    {
        if (isDebug)
            BaseLog.v(msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            BaseLog.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            BaseLog.d(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            BaseLog.e(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            BaseLog.v(tag, msg);
    }
}