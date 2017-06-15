package com.thlh.jhmjmw.business.user.coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.config.Constants;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户优惠券列表页
 */
public class CouponActivity extends BaseActivity {
    private static final int ACTIVITY_CODE_COUPON_FINISH = 0;
    private final String TAG = "CouponActivity";
    private static final int ACTIVITY_CODE_COUPON_EXCHANGE = 1;

    @BindView(R.id.coupon_tablayout)
    CommonTabLayout couponTablayout;
    @BindView(R.id.coupon_vp)
    ViewPager couponVp;
    @BindView(R.id.coupon_headerNormal)
    HeaderNormal couponHeaderNormal;

    private CouponFragment unuseFragment, usedFragment, neartimeFragment;
    private int cotent_type;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //    private String[] mSegmentTLTitles = {getResources().getString(R.string.coupon_no_use), getResources().getString(R.string.coupon_About_expired), getResources().getString(R.string.coupon_used),};
    private int[] mIconUnselectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null,};
    private int[] mIconSelectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null,};
    private CouponActivity couponActivity;


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, CouponActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    public static void activityStart(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, CouponActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);

    }

    @Override
    protected void initVariables() {
        unuseFragment = new CouponFragment();
        neartimeFragment = new CouponFragment();
        usedFragment = new CouponFragment();

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(unuseFragment);
        fragments.add(neartimeFragment);
        fragments.add(usedFragment);

        PagerAdapter PagerAdapter = new PagerAdapter(getSupportFragmentManager());
        couponVp.setAdapter(PagerAdapter);
        couponVp.setOffscreenPageLimit(3);
        String[] mSegmentTLTitles = getResources().getStringArray(R.array.titles);
        for (int i = 0; i < mSegmentTLTitles.length; i++) {
            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        couponTablayout.setTabData(mTabEntities);
        couponTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                couponVp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        couponVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                couponTablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        couponVp.setCurrentItem(cotent_type);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    //ViewPager适配器
    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    args.putString("couponFlag", Constants.COUPON_FLAG_UNUSE);
                    unuseFragment.setArguments(args);
                    return unuseFragment;
                case 1:
                    args.putString("couponFlag", Constants.COUPON_FLAG_NEARTIME);
                    neartimeFragment.setArguments(args);
                    return neartimeFragment;
                case 2:
                    args.putString("couponFlag", Constants.COUPON_FLAG_USED);
                    usedFragment.setArguments(args);
                    return usedFragment;
                default:
                    args.putString("couponFlag", Constants.COUPON_FLAG_UNUSE);
                    unuseFragment.setArguments(args);
                    return unuseFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.coupon_no_use);
                case 1:
                    return getResources().getString(R.string.coupon_About_expired);
                case 2:
                    return getResources().getString(R.string.coupon_used);
                default:
                    return getResources().getString(R.string.coupon_no_use);
            }
        }
    }


    public CommonTabLayout getTapTopLayout() {
        return couponTablayout;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i(TAG + "onActivityResult -> RESULT_OK");
            if (data.getStringExtra("res").equals("back")){
                finish();
            }
            switch (requestCode) {
                case ACTIVITY_CODE_COUPON_EXCHANGE:
//                    unuseFragment.reLoadData();
//                    usedFragment.reLoadData();
//                    neartimeFragment.reLoadData();
                    IndexActivity.activityStart(this);
                    break;
                case ACTIVITY_CODE_COUPON_FINISH:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
