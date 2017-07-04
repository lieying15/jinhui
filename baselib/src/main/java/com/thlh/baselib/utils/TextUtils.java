package com.thlh.baselib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.thlh.baselib.R;
import com.thlh.viewlib.tablayout.MsgView;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/5/31.
 */
public class TextUtils {

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    public static String showPrice(String price) {
        if (null == price)
            return "0";
        if (price.equals(""))
            return "0";
//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        return decimalFormat.format(price);
        return price;
    }

    public static String showPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(price);
    }

    public static SpannableString showMjz(Context context,String mjz) {
        return  showMjz(context,mjz,(int)context.getResources().getDimension(R.dimen.icon_mjz_x), (int)context.getResources().getDimension(R.dimen.icon_mjz_y));
    }
    public static SpannableString showOnlyMjz(Context context,String mjz) {

        SpannableString spannableString = new SpannableString(" " + mjz);
        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_mjz);
        drawable.setBounds(0, 0, (int)context.getResources().getDimension(R.dimen.x28),
                (int)context.getResources().getDimension(R.dimen.x28));
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        return spannableString;
    }

    public static SpannableString showMjz(Context context,String mjz,int textx,int texty) {
        SpannableString spannableString = new SpannableString("金惠币 可抵¥"+ mjz);
        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_mjz);
        drawable.setBounds(0, 0, textx, texty);
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 3,4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static SpannableString showHadMjz(Context context,String mjz,int textx,int texty) {
        SpannableString spannableString = new SpannableString("金惠币 已抵¥"+ mjz);
        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_mjz);
        drawable.setBounds(0, 0, textx, texty);
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 3,4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static SpannableString showSimpleMjz(Context context,String mjz) {
        SpannableString spannableString = new SpannableString("金惠币"+ mjz);
        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_mjz);
        drawable.setBounds(0, 0, (int)context.getResources().getDimension(R.dimen.icon_mjz_x), (int)context.getResources().getDimension(R.dimen.icon_mjz_y));
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static String showString(String string) {
        if (null == string)
            return "string";
        return string;
    }


    public static boolean isPhone( String phonestr) {
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (phonestr.matches(telRegex)) {
            return true;
        }else {
            return false;
        }
    }

    public static String getSPAddress(){
        StringBuilder tempaddress =  new StringBuilder();
        String province = (String) SPUtils.get("user_address_province", "");
        String city = (String) SPUtils.get("user_address_city", "");
        tempaddress.append( province);
        if(! province.equals(city)){
            tempaddress.append( city);
        }
        tempaddress.append(  (String) SPUtils.get("user_address_district", "") );
        return  tempaddress.toString();
    }

    public static void showNum(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        int radius = 14;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");
//            lp.width = (int) (5 * dm.density);
//            lp.height = (int) (5 * dm.density);
            lp.height = 0;
            lp.height = 0;
            msgView.setLayoutParams(lp);
        } else {
            lp.height = (int) (radius * dm.density);
            if (num > 0 && num < 10) {//圆
                lp.width = (int) (radius * dm.density);
                msgView.setText(num + "");
            } else if (num > 9 && num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (3 * dm.density), 0, (int) (3 * dm.density), 0);
                msgView.setText(num + "");
            } else {//数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (3 * dm.density), 0, (int) (3 * dm.density), 0);
                msgView.setText("…");
            }
            msgView.setLayoutParams(lp);
        }
    }
}
