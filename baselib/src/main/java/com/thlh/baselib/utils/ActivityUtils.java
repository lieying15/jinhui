package com.thlh.baselib.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个管理所有activity的工具类,调用finishall能直接退出程序
 *
 * @author DE
 */
public class ActivityUtils {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishActivity(String  name) {
        for (int i = 0; i <activities.size() ; i++) {
            if(activities.get(i).getClass().getSimpleName().equals(name)){
                BaseLog.e(" 删除  SimpleName()：" +activities.get(i).getClass().getSimpleName());
                activities.get(i).finish();
                activities.remove(i);
            }
        }
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void popAllActivityUntilSpecify(Class cls) {
        while (activities.size() >= 1) {
            Activity a = activities.get(activities.size() - 1);
            if (a == null) {
                break;
            }
            if (a.getClass().equals(cls)) {
                break;
            }
            removeActivity(a);
            a.finish();
            a = null;

        }
    }

    //MVP模式
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
