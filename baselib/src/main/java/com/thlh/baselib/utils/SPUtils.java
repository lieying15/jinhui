package com.thlh.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.thlh.baselib.base.BaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by HQ on 2016/3/18.
 */
public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    public static boolean getIsLogin() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", (Boolean) false);
    }

    public static void  setLogin(boolean islogin) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", islogin);
        SharedPreferencesCompat.apply(editor);
    }

    public static void  setPayParam(String pay_price,String pay_type,String pay_purpose,String pay_no) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pay_price", pay_price);
        editor.putString("pay_type", pay_type); //付款方式|分割 1:美家币支付 2:余额支付 3:微信 4:支付宝 5:银联 6:货到付款 7:积分换购
        editor.putString("pay_purpose", pay_purpose);  // 1下单支付 2钱包充值 3美家币充值
        editor.putString("pay_no", pay_no);
        SharedPreferencesCompat.apply(editor);
    }

    public static String getToken() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    public static void  setToken(String token) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        SharedPreferencesCompat.apply(editor);
    }

    public static Double getLatitude() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return Double.parseDouble(sp.getString("latitude", "0"));
    }

    public static void  setLatitude(String latitude) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("latitude", latitude);
        SharedPreferencesCompat.apply(editor);
    }

    public static Double getLongitude() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return  Double.parseDouble(sp.getString("longitude", "0"));
    }

    public static void  setLongitude(String token) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("longitude", token);
        SharedPreferencesCompat.apply(editor);
    }

    public static String getUsername() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString("username",BaseApplication.DEFAULT_USER);
    }

    public static String getDefaultUsername() {
        return BaseApplication.DEFAULT_USER;
    }

    public static String getPassword() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString("password",BaseApplication.DEFAULT_USER);
    }

    public static void  setPassword(String password) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", password);
        SharedPreferencesCompat.apply(editor);
    }

    public static void  setUsername(String username) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {

        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 存储LIST string
     * @return
     */
    public static void saveStringList(String name,ArrayList<String> list)  {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // 还原数据
        int size = sp.getInt(name + "_size", 0);
        editor.putInt(name + "_size",0);
        for(int i=0;i< size;i++) {
            editor.remove(name + "_" + i);
        }
        editor.commit();
        // 添加数据
        editor.putInt(name + "_size",list.size());
        for(int i=0;i<list.size();i++) {
            editor.remove(name + "_" + i);
            editor.putString(name + "_" + i, list.get(i));
        }
        editor.commit();
    }
    /**
     * 取LIST string
     * @return
     */
    public static ArrayList<String>  getStringList(String name) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        int size = sp.getInt(name + "_size", 0);
        ArrayList<String> templist = new ArrayList<>();
        for(int i=0;i< size;i++) {
            templist.add(sp.getString(name + "_" + i, ""));
        }
        return templist;
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     * @author zhy
     *
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();
        /**
         * 反射查找apply的方法
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {

            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
            editor.commit();
        }
    }


    public static void clearUserInfo() {
        remove("token");
        setLogin(false);
        setUsername(BaseApplication.DEFAULT_USER);
        put("needupdate_userinfo", true);
        put("user_inner_member", "");
        put("user_nickname", "");
        put("user_avatar", "");
        put("user_ispaypass", 0);
        put("user_ch","");
        put("user_isch",0);/**是否扫码用户*/
        put("user_paypass_hint", false);
        put("user_storeid", "");
        put("user_bind_mobile", "");
        put("user_equipmentid", "");/**智能设备的扫码*/
        put("user_address_id", "");
        put("user_address_name", "");
        put("user_address_address", "");
        put("user_address_phone", "");
        put("user_address_is_on", "");
        put("user_address_province", "");
        put("user_address_city", "");
        put("user_address_district", "");
        put("user_wait_pay", "");
        put("user_wait_deliver", "");
        put("user_wait_get", "");
        put("user_return_goods", "");
        put("user_wait_comment", "");
        put("user_systeminfo_amount", "");
        put("user_hadchange_icebox", false);
        put("agreeRechargeProtocol", false);
        put("recharge_mjb_amount", "0");
        put("user_agree_recharge_protocol", false);
    }
}
