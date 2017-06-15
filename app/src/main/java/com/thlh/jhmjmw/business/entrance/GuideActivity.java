package com.thlh.jhmjmw.business.entrance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 引导页
 */
public class GuideActivity extends BaseViewActivity {
    private final String TAG = "GuideActivity";

    @BindView(R.id.guide_viewpager)
    ViewPager guideViewpager;
    @BindView(R.id.guide_tablayout)
    CommonTabLayout guideTablayout;

    private GuideFragment guideNumOne, guideNumTwo, guideNumThere, guideNumFour;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mSegmentTLTitles = {"", "", "", ""};
    private int[] mIconUnselectIds = {
            R.drawable.icon_guide_m, R.drawable.icon_guide_j, R.drawable.icon_guide_m,
            R.drawable.icon_guide_w};
    private int[] mIconSelectIds = {
            R.drawable.icon_guide_select_m, R.drawable.icon_guide_select_j, R.drawable.icon_guide_select_m,
            R.drawable.icon_guide_select_w};


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, GuideActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

        if (Boolean.valueOf(SPUtils.get("frist_use", false).toString())) {
            IndexActivity.activityStart(this);
            finish();
        } else {
            SPUtils.put("frist_use", true);
        }
        guideNumOne = new GuideFragment();
        guideNumTwo = new GuideFragment();
        guideNumThere = new GuideFragment();
        guideNumFour = new GuideFragment();


    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        OrderPagerAdapter orderPagerAdapter = new OrderPagerAdapter(getSupportFragmentManager());
        if (orderPagerAdapter != null) {
            guideViewpager.setAdapter(orderPagerAdapter);
        }
        guideViewpager.setOffscreenPageLimit(4);
        for (int i = 0; i < mSegmentTLTitles.length; i++) {
            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        guideTablayout.setTabData(mTabEntities);
        guideTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                guideViewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });


        guideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                guideTablayout.setCurrentTab(position);
                /**第四页直接到首页界面*/
                if (position == 3) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(getApplication(), IndexActivity.class);
                            startActivity(intent);
                        }
                    }, 500);


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        guideViewpager.setCurrentItem(0);
    }


    @Override
    protected void loadData() {

    }


    //ViewPager适配器
    private class OrderPagerAdapter extends FragmentPagerAdapter {
        public OrderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();

            switch (position) {
                case 0:
                    args.putInt("guide_num", 1);
                    guideNumOne.setArguments(args);
                    return guideNumOne;
                case 1:
                    args.putInt("guide_num", 2);
                    guideNumTwo.setArguments(args);
                    return guideNumTwo;
                case 2:
                    args.putInt("guide_num", 3);
                    guideNumThere.setArguments(args);
                    return guideNumThere;
                case 3:
                    args.putInt("guide_num", 4);
                    guideNumFour.setArguments(args);
                    return guideNumFour;
                default:
                    args.putInt("guide_num", 1);
                    guideNumOne.setArguments(args);
                    return guideNumOne;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
                default:
                    return "";
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
