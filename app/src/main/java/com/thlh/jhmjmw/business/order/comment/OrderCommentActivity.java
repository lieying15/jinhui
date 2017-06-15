package com.thlh.jhmjmw.business.order.comment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 用户订单评价界面
 */
public class OrderCommentActivity extends BaseViewActivity {
    private static final String TAG = "OrderCommentActivity";
    public static final int COMMENT_TYPE_WAIT = 0; //全部订单
    public static final int COMMENT_TYPE_COMPLETE = 1;


    @BindView(R.id.comment_tablayout)
    CommonTabLayout commentTablayout;
    @BindView(R.id.comment_vp)
    ViewPager commentVp;

    private OrderCommentFragment waitFragment, completeFragment;
    private int cotent_type;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mSegmentTLTitles = {getResources().getString(R.string.shop_to_evaluation), getResources().getString(R.string.shop_have_evaluation)};
    private int[] mIconUnselectIds = {R.drawable.bg_null, R.drawable.bg_null};
    private int[] mIconSelectIds = {R.drawable.bg_null, R.drawable.bg_null};

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, OrderCommentActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        waitFragment = new OrderCommentFragment();
        completeFragment = new OrderCommentFragment();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(waitFragment);
        fragments.add(completeFragment);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);


        PagerAdapter PagerAdapter = new PagerAdapter(getSupportFragmentManager());
        commentVp.setAdapter(PagerAdapter);

        for (int i = 0; i < mSegmentTLTitles.length; i++) {
            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        commentTablayout.setTabData(mTabEntities);
        commentTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                commentVp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        commentVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commentTablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        commentVp.setCurrentItem(cotent_type);
    }


    @Override
    protected void loadData() {
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
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
                    args.putInt("commentType", COMMENT_TYPE_WAIT);
                    waitFragment.setArguments(args);
                    return waitFragment;
                case 1:
                    args.putInt("commentType", COMMENT_TYPE_COMPLETE);
                    completeFragment.setArguments(args);
                    return completeFragment;
                default:
                    args.putInt("commentType", COMMENT_TYPE_WAIT);
                    waitFragment.setArguments(args);
                    return waitFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public void reLoadComment(){
        waitFragment.reLoadComment();
        completeFragment.reLoadComment();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateOrderList();
    }

    public void updateOrderList(){
        String updateType = (String) SPUtils.get("order_need_update","0").toString();//0无变化，1付款，2确认收货，3取消订单，4评论
        if(updateType.equals("4")){
            waitFragment.reLoadComment();
            completeFragment.reLoadComment();
            SPUtils.put("order_need_update","0");//0无变化，1付款，2确认收货，3取消订单，4评论
        }
    }
}
