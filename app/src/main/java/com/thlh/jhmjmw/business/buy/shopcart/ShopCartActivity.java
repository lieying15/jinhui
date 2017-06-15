package com.thlh.jhmjmw.business.buy.shopcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.jhmjmw.R;

import butterknife.ButterKnife;

/**
 * 购物车界面
 */
public class ShopCartActivity extends BaseActivity  {
    private final String TAG = "ShopCartActivity";
    private ShopcartFragment shopcartFragment;

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, ShopCartActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);

        shopcartFragment = (ShopcartFragment) getSupportFragmentManager().findFragmentById(R.id.activity_baseview);
        if (shopcartFragment == null) {
            shopcartFragment = new ShopcartFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.activity_baseview, shopcartFragment);
            transaction.commit();
        }

    }

    @Override
    protected void loadData() {
            
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }



}
