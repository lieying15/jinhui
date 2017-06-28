package com.thlh.jhmjmw.business.devices;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.fragment.IceboxScreenFragment;
import com.thlh.jhmjmw.other.L;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by HQ on 2015/12/17.
 */
public class IceboxActivity extends BaseViewActivity {
    private static final String TAG = "IceboxActivity";
    public static final int ICOBOX_TYPE_SETTING = 0;
    public static final int ICOBOX_TYPE_SCREEN = 1;
    public int CHECK_STATUS = 1;



    @BindView(R.id.icebox_viewpager)
    ViewPager iceboxViewpager;
    @BindView(R.id.icon_icebox_temperature_iv)
    ImageView iconIceboxTemperatureIv;
    @BindView(R.id.icon_icebox_temperature_tv)
    TextView iconIceboxTemperatureTv;
    @BindView(R.id.icon_icebox_temperature_ll)
    LinearLayout iconIceboxTemperatureLl;
    @BindView(R.id.icon_icebox_screen_iv)
    ImageView iconIceboxScreenIv;
    @BindView(R.id.icon_icebox_screen_tv)
    TextView iconIceboxScreenTv;
    @BindView(R.id.icon_icebox_screen_ll)
    LinearLayout iconIceboxScreenLl;

    private List<Fragment> fragmentList;
    private IceboxScreenFragment screenFrgment;
    private IceboxSettingFragment settingFrgment;

    private int initPosition;
    private Uri cameraURI, imageUri;//上传头像用
    private int equipmentid = 0 ;


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, IceboxActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }


    @Override
    protected void initVariables() {
        fragmentList = new ArrayList<Fragment>();
        screenFrgment = new IceboxScreenFragment();
        settingFrgment = new IceboxSettingFragment();
        fragmentList.add(settingFrgment);
        fragmentList.add(screenFrgment);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_icebox);
        ButterKnife.bind(this);

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        iceboxViewpager.setAdapter(myFragmentPagerAdapter);
        iceboxViewpager.setCurrentItem(ICOBOX_TYPE_SCREEN);
        iceboxViewpager.addOnPageChangeListener(new MyPagerChangeListener());
        iceboxViewpager.setOffscreenPageLimit(2);
        changeTabView(ICOBOX_TYPE_SCREEN);

    }

    @OnClick({R.id.icon_icebox_temperature_ll, R.id.icon_icebox_screen_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_icebox_temperature_ll:
                changeTabView(ICOBOX_TYPE_SETTING);
                break;

            case R.id.icon_icebox_screen_ll:
                changeTabView(ICOBOX_TYPE_SCREEN);
                break;
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> myList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            myList = list;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case ICOBOX_TYPE_SCREEN:
                    return screenFrgment;

                case ICOBOX_TYPE_SETTING:
                    return settingFrgment;

                default:
                    return screenFrgment;
            }
        }

        @Override
        public int getCount() {
            return myList.size();
        }
    }

    class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == CHECK_STATUS) {

            } else {
                CHECK_STATUS = position;
                changeTabView(CHECK_STATUS);
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private void changeTabView(int position) {
        switch (position) {
            case ICOBOX_TYPE_SETTING:
                iconIceboxScreenTv.setTextColor(getResources().getColor(R.color.text_nomal));
                iconIceboxTemperatureTv.setTextColor(getResources().getColor(R.color.app_theme));
                iconIceboxScreenIv.setImageResource(R.drawable.icon_icebox_screen);
                iconIceboxTemperatureIv.setImageResource(R.drawable.icon_icebox_temperature_select);
                break;

            case ICOBOX_TYPE_SCREEN:
                iconIceboxScreenTv.setTextColor(getResources().getColor(R.color.app_theme));
                iconIceboxTemperatureTv.setTextColor(getResources().getColor(R.color.text_nomal));
                iconIceboxScreenIv.setImageResource(R.drawable.icon_icebox_screen_select);
                iconIceboxTemperatureIv.setImageResource(R.drawable.icon_icebox_temperature);
                break;
        }
        iceboxViewpager.setCurrentItem(position, false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            L.i(TAG, "onActivityResult-->RESULT_OK");
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            L.e(TAG, "OnActivityResult-->" + resultCode + "用户取消了操作");
        } else {
            // 获取结果失败
            L.e(TAG, "OnActivityResult-->" + resultCode);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        if (UCrop.getOutput(result) == null) return;
        imageUri = null;
        imageUri = UCrop.getOutput(result);
        L.e("测试图片裁剪返回--> imageUri" + imageUri);
        if (imageUri != null && screenFrgment != null) {
            screenFrgment.picsUrl.clear();
            screenFrgment.picsUrl.add(imageUri);
            screenFrgment.postAddPhoto();
        }
    }


}
