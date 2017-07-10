package com.thlh.baselib.utils;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;

import com.thlh.baselib.R;
import com.thlh.baselib.view.NormalDialogFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by huqiang on 2017/1/4.
 */

public class DialogUtils {
    public static final int TYPE_NORMAL_SUCCESS = 0;
    public static final int TYPE_NORMAL_ERROR = 1;
    public static final int TYPE_NORMAL_WARNING = 2;

    public static void showNormal(RxAppCompatActivity activity, int type, String contentStr) {
        final NormalDialogFragment normalFrgDialog = new NormalDialogFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        normalFrgDialog.setContentStr(contentStr);
        normalFrgDialog.setContentType(type);
        normalFrgDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalFrgDialog.dismiss();
            }
        });
        normalFrgDialog.show(ft, "normalFrgDialog");
    }

    public static void showNormal(RxAppCompatActivity activity,String contentStr) {
        showNormal(activity,DialogUtils.TYPE_NORMAL_SUCCESS,contentStr);
    }
    public static  void showCuxiao(RxAppCompatActivity activity,CharSequence contentChar) {
        final NormalDialogFragment expressDialog = new NormalDialogFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        expressDialog.setContentStr(contentChar);
        expressDialog.setTitleIvRes(R.drawable.icon_dialog_warning);
        expressDialog.setTitleStr("金惠币促销规则");
        expressDialog.setFinalBtnStr("我知道了");
        expressDialog.setContentGravity(Gravity.LEFT);
        expressDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressDialog.dismiss();
            }
        });
        expressDialog.show(ft, "expressDialog");
    }


    public static  void showExprnse(RxAppCompatActivity activity,CharSequence contentChar) {
        final NormalDialogFragment expressDialog = new NormalDialogFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        expressDialog.setContentStr(contentChar);
        expressDialog.setTitleIvRes(R.drawable.img_dialog_car);
        expressDialog.setFinalBtnStr("关闭");
        expressDialog.setContentGravity(Gravity.LEFT);
        expressDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressDialog.dismiss();
            }
        });
        expressDialog.show(ft, "expressDialog");
    }

    public static  void showPhone(final RxAppCompatActivity activity, String contentStr) {
        Spannable ariveSpan = new SpannableString(contentStr+"4001688521");
        ariveSpan.setSpan(activity.getResources().getColor(R.color.wine_light), contentStr.length()-1, ariveSpan.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final NormalDialogFragment expressDialog = new NormalDialogFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        expressDialog.setContentStr(ariveSpan);
        expressDialog.setTitleIvRes(R.drawable.img_dialog_call);
        expressDialog.setFinalBtnStr("立即拨打");
        expressDialog.setContentGravity(Gravity.LEFT);
        expressDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent();
                phoneIntent.setAction(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:4001688521"));
                activity.startActivity(phoneIntent);
                expressDialog.dismiss();
            }
        });
        expressDialog.setCancelVisible(View.VISIBLE);
        expressDialog.show(ft, "phoneDialog");
    }




}
