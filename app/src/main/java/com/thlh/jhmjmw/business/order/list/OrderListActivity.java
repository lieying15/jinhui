package com.thlh.jhmjmw.business.order.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单页
 */
public class OrderListActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "OrderListActivity";
    private final int ACTIVITY_CODE_PAY = 1;

    @BindView(R.id.order_viewpager)
    ViewPager orderViewpager;
    @BindView(R.id.order_tabtop)
    CommonTabLayout tapTopLayout;
    @BindView(R.id.order_tab_complete)
    CommonTabLayout tapUnCompleteLayout;
    @BindView(R.id.order_tab_uncomplete)
    CommonTabLayout tapCompleteLayout;
    @BindView(R.id.order_noinfoview)
    NoInfoView orderNoinfoview;
    @BindView(R.id.order_header)
    HeaderNormal orderHeader;
    @BindView(R.id.order_tab_line)
    View tabLine;

    private OrderFragment orderAllFragment,unCompeletFragment,waitPayFragment, waitSendFragment, waitGainFragment,
            compeletFragment,waitCommentFragment,hadCommentFragment,cancelFragment;
    private int content_type;

    private ArrayList<CustomTabEntity> mTabTopEntities = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabCompleteEntities = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabUnCompleteEntities = new ArrayList<>();

    public static void activityStart(Activity context, int content_type) {
        Intent intent = new Intent();
        intent.putExtra("content_type", content_type);
        intent.setClass(context, OrderListActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        content_type = getIntent().getIntExtra("content_type", 0);
        L.e(TAG + " content_type:"+content_type);
        orderAllFragment = new OrderFragment();
        unCompeletFragment = new OrderFragment();
        waitPayFragment = new OrderFragment();
        waitSendFragment = new OrderFragment();
        waitGainFragment = new OrderFragment();
        compeletFragment = new OrderFragment();
        waitCommentFragment = new OrderFragment();
        hadCommentFragment = new OrderFragment();
        cancelFragment = new OrderFragment();

        String[] mTabTopTitles = {getResources().getString(R.string.order_no_finish), getResources().getString(R.string.order_finish), getResources().getString(R.string.ordercaanal)};
        for (int i = 0; i < mTabTopTitles.length; i++) {
            mTabTopEntities.add(new TabEntity(mTabTopTitles[i], R.drawable.bg_null, R.drawable.bg_null));
        }

        String[] mTabCompleteTitles = {getResources().getString(R.string.pay), getResources().getString(R.string.send_goods), getResources().getString(R.string.goods)};
        for (int i = 0; i < mTabCompleteTitles.length; i++) {
            mTabCompleteEntities.add(new TabEntity(mTabCompleteTitles[i], R.drawable.bg_null, R.drawable.bg_null));
        }

        String[] mTabUnCompleteTitles = {getResources().getString(R.string.shop_to_evaluation), getResources().getString(R.string.shop_have_evaluation)};
        for (int i = 0; i < mTabUnCompleteTitles.length; i++) {
            mTabUnCompleteEntities.add(new TabEntity(mTabUnCompleteTitles[i], R.drawable.bg_null, R.drawable.bg_null));
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_base);
        ButterKnife.bind(this);
        orderNoinfoview.setTitle(getResources().getString(R.string.order_no));
        orderNoinfoview.setTitleIv(R.drawable.noinfo_order);
        orderNoinfoview.setNextactionStr(getResources().getString(R.string.shopcart_total_golook));
        orderNoinfoview.setNextactionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexActivity.activityStart(OrderListActivity.this, IndexActivity.POSITON_HOMEPAGE);

            }
        });

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(orderAllFragment);
        fragments.add(unCompeletFragment); //1
        fragments.add(waitPayFragment);
        fragments.add(waitSendFragment);
        fragments.add(waitGainFragment);
        fragments.add(compeletFragment);  //5
        fragments.add(waitCommentFragment);
        fragments.add(hadCommentFragment);
        fragments.add(cancelFragment);

        OrderPagerAdapter orderPagerAdapter = new OrderPagerAdapter(getSupportFragmentManager());
        orderViewpager.setAdapter(orderPagerAdapter);
        orderViewpager.setOffscreenPageLimit(9);
  
        tapTopLayout.setTabData(mTabTopEntities);
        tapTopLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0 ://未完成订单
                        orderViewpager.setCurrentItem(1);
                        tapUnCompleteLayout.setVisibility(View.VISIBLE);
                        tapUnCompleteLayout.hideIndicatorRect();
                        tapCompleteLayout.setVisibility(View.GONE);
                        tabLine.setVisibility(View.VISIBLE);
                        break;
                    case 1: //已完成订单
                        orderViewpager.setCurrentItem(5);
                        tapUnCompleteLayout.setVisibility(View.GONE);
                        tapCompleteLayout.setVisibility(View.VISIBLE);
                        tapCompleteLayout.hideIndicatorRect();
                        tabLine.setVisibility(View.VISIBLE);
                        break;
                    case 2: //已取消订单
                        orderViewpager.setCurrentItem(8);
                        tapUnCompleteLayout.setVisibility(View.GONE);
                        tapCompleteLayout.setVisibility(View.GONE);
                        tabLine.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
                switch (position){
                    case 0 ://未完成订单
                        orderViewpager.setCurrentItem(1);
                        tapTopLayout.showIndicatorRect();
                        tapUnCompleteLayout.setVisibility(View.VISIBLE);
                        tapUnCompleteLayout.hideIndicatorRect();
                        tapCompleteLayout.setVisibility(View.GONE);
                        tabLine.setVisibility(View.VISIBLE);
                        break;
                    case 1: //已完成订单
                        orderViewpager.setCurrentItem(5);
                        tapTopLayout.showIndicatorRect();
                        tapUnCompleteLayout.setVisibility(View.GONE);
                        tapCompleteLayout.setVisibility(View.VISIBLE);
                        tapCompleteLayout.hideIndicatorRect();
                        tabLine.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        tapUnCompleteLayout.setTabData(mTabCompleteEntities);
        tapUnCompleteLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0 ://待付款
                        orderViewpager.setCurrentItem(2);
                        tapTopLayout.setCurrentTab(0);
                        break;
                    case 1: //待发货
                        orderViewpager.setCurrentItem(3);
                        tapTopLayout.setCurrentTab(0);
                        break;
                    case 2: //待收货
                        orderViewpager.setCurrentItem(4);
                        tapTopLayout.setCurrentTab(0);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
                switch (position){
                    case 0 ://待付款
                        orderViewpager.setCurrentItem(2);
                        tapUnCompleteLayout.showIndicatorRect();
                        break;
                    case 1: //待发货
                        orderViewpager.setCurrentItem(3);
                        tapUnCompleteLayout.showIndicatorRect();
                        break;
                    case 2: //待收货
                        orderViewpager.setCurrentItem(4);
                        tapUnCompleteLayout.showIndicatorRect();
                        break;
                }
            }
        });

        tapCompleteLayout.setTabData(mTabUnCompleteEntities);
        tapCompleteLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0 :
                        orderViewpager.setCurrentItem(6);
                        tapTopLayout.setCurrentTab(1);
                        break;
                    case 1:
                        orderViewpager.setCurrentItem(7);
                        tapTopLayout.setCurrentTab(1);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
                switch (position){
                    case 0 :
                        orderViewpager.setCurrentItem(6);
                        tapCompleteLayout.showIndicatorRect();
                        break;
                    case 1:
                        orderViewpager.setCurrentItem(7);
                        tapCompleteLayout.showIndicatorRect();
                        break;
                }
            }
        });

        orderViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabView(position);
                changeHeaderTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        orderViewpager.setCurrentItem(changeTypeToPosition(content_type));
        fitTabView(content_type);
        setHeaderTitle(content_type);
    }

    //将内容类型转为位置
    private int changeTypeToPosition(int content_type){
        switch (content_type) {
            case Constants.ORDER_TYPE_ALL:
                return 0;
            case Constants.ORDER_TYPE_UNCOMPLETE:
                return 1;
            case Constants.ORDER_TYPE_WAIT_PAY:
                return 2;
            case Constants.ORDER_TYPE_WAIT_SENDOUT:
                return 3;
            case Constants.ORDER_TYPE_WAIT_GAIN:
                return 4;
            case Constants.ORDER_TYPE_COMPLETE:
                return 5;
            case Constants.ORDER_TYPE_WAIT_COMMENT:
                return 6;
            case Constants.ORDER_TYPE_HAS_COMMENT:
                return 7;
            case Constants.ORDER_TYPE_CANCEL:
                return 8;
            default:
                return 0;
        }
    };

    //将位置转为内容类型
    private int changePositionToType(int position){
        switch (position) {
            case 0:return Constants.ORDER_TYPE_ALL;
            case 1:return Constants.ORDER_TYPE_UNCOMPLETE;
            case 2:return Constants.ORDER_TYPE_WAIT_PAY;
            case 3:return Constants.ORDER_TYPE_WAIT_SENDOUT;
            case 4:return Constants.ORDER_TYPE_WAIT_GAIN;
            case 5:return Constants.ORDER_TYPE_COMPLETE;
            case 6:return Constants.ORDER_TYPE_WAIT_COMMENT;
            case 7:return Constants.ORDER_TYPE_HAS_COMMENT;
            case 8:return Constants.ORDER_TYPE_CANCEL;
            default:return Constants.ORDER_TYPE_ALL;
        }
    };

    private void changeTabView(int position){
        tapTopLayout.showIndicatorRect();
        tapUnCompleteLayout.showIndicatorRect();
        tapCompleteLayout.showIndicatorRect();
        switch (position){
            case 0 :
                tapTopLayout.hideIndicatorRect();
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.VISIBLE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                tapTopLayout.setCurrentTab(0);
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.VISIBLE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                tapTopLayout.setCurrentTab(0);
                tapUnCompleteLayout.setCurrentTab(0);
                tapUnCompleteLayout.setVisibility(View.VISIBLE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 3:
                tapTopLayout.setCurrentTab(0);
                tapUnCompleteLayout.setCurrentTab(1);
                tapUnCompleteLayout.setVisibility(View.VISIBLE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 4:
                tapTopLayout.setCurrentTab(0);
                tapUnCompleteLayout.setCurrentTab(2);
                tapUnCompleteLayout.setVisibility(View.VISIBLE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 5:
                tapTopLayout.setCurrentTab(1);
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.GONE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.VISIBLE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 6:
                tapTopLayout.setCurrentTab(1);
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.GONE);
                tapCompleteLayout.setCurrentTab(0);
                tapCompleteLayout.setVisibility(View.VISIBLE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 7:
                tapTopLayout.setCurrentTab(1);
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.GONE);
                tapCompleteLayout.setCurrentTab(1);
                tapCompleteLayout.setVisibility(View.VISIBLE);
                tabLine.setVisibility(View.VISIBLE);
                break;
            case 8:
                tapTopLayout.setCurrentTab(2);
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.GONE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.GONE);
                break;
            default:
                tapTopLayout.hideIndicatorRect();
                tapUnCompleteLayout.hideIndicatorRect();
                tapUnCompleteLayout.setVisibility(View.VISIBLE);
                tapCompleteLayout.hideIndicatorRect();
                tapCompleteLayout.setVisibility(View.GONE);
                tabLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void loadData() {
    }


    @Override
    public void onClick(View v) {
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
                    args.putInt("orderType", Constants.ORDER_TYPE_ALL);
                    orderAllFragment.setArguments(args);
                    return orderAllFragment;
                case 1:
                    args.putInt("orderType",Constants.ORDER_TYPE_UNCOMPLETE);
                    unCompeletFragment.setArguments(args);
                    return unCompeletFragment;
                case 2:
                    args.putInt("orderType", Constants.ORDER_TYPE_WAIT_PAY);
                    waitPayFragment.setArguments(args);
                    return waitPayFragment;
                case 3:
                    args.putInt("orderType", Constants.ORDER_TYPE_WAIT_SENDOUT);
                    waitSendFragment.setArguments(args);
                    return waitSendFragment;
                case 4:
                    args.putInt("orderType", Constants.ORDER_TYPE_WAIT_GAIN);
                    waitGainFragment.setArguments(args);
                    return waitGainFragment;
                case 5:
                    args.putInt("orderType", Constants.ORDER_TYPE_COMPLETE);
                    compeletFragment.setArguments(args);
                    return compeletFragment;
                case 6:
                    args.putInt("orderType", Constants.ORDER_TYPE_WAIT_COMMENT);
                    waitCommentFragment.setArguments(args);
                    return waitCommentFragment;
                case 7:
                    args.putInt("orderType", Constants.ORDER_TYPE_HAS_COMMENT);
                    hadCommentFragment.setArguments(args);
                    return hadCommentFragment;
                case 8:
                    args.putInt("orderType", Constants.ORDER_TYPE_CANCEL);
                    cancelFragment.setArguments(args);
                    return cancelFragment;
                default:
                    args.putInt("orderType", Constants.ORDER_TYPE_ALL);
                    orderAllFragment.setArguments(args);
                    return orderAllFragment;
            }

        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "全部订单";
                case 1:
                    return "未完成订单";
                case 2:
                    return "待发货";
                case 3:
                    return "待发货";
                case 4:
                    return "待收货";
                case 5:
                    return "已完成订单";
                case 6:
                    return "待评论";
                case 7:
                    return "已评论";
                case 8:
                    return "已取消订单";
                default:
                    return "全部订单";
            }
        }
    }

    private void setHeaderTitle(int content_type){
        switch (content_type){
            case Constants.ORDER_TYPE_ALL: orderHeader.setTitle(getResources().getString(R.string.all_order));break;
            case Constants.ORDER_TYPE_WAIT_PAY: orderHeader.setTitle(getResources().getString(R.string.pay));break;
            case Constants.ORDER_TYPE_WAIT_SENDOUT: orderHeader.setTitle(getResources().getString(R.string.send_goods));break;
            case Constants.ORDER_TYPE_WAIT_GAIN: orderHeader.setTitle(getResources().getString(R.string.goods));break;
            case Constants.ORDER_TYPE_WAIT_COMMENT: orderHeader.setTitle(getResources().getString(R.string.comment));break;
            case Constants.ORDER_TYPE_HAS_COMMENT: orderHeader.setTitle(getResources().getString(R.string.comment_ok));break;
            case Constants.ORDER_TYPE_COMPLETE: orderHeader.setTitle(getResources().getString(R.string.order_finish));break;
            case Constants.ORDER_TYPE_UNCOMPLETE: orderHeader.setTitle(getResources().getString(R.string.order_no_finish));break;
            case Constants.ORDER_TYPE_CANCEL: orderHeader.setTitle(getResources().getString(R.string.ordercaanal));break;
            default:orderHeader.setTitle(getResources().getString(R.string.all_order));break;
        }
    }

    private void changeHeaderTitle(int position){
        int type = changePositionToType(position);
        setHeaderTitle(type);
    }

    private void fitTabView(int content_type){
        switch (content_type){
            case Constants.ORDER_TYPE_ALL :
                tapTopLayout.setNeedHideIndicator(true);
                tapUnCompleteLayout.setNeedHideIndicator(true);
                tapCompleteLayout.setNeedHideIndicator(true);
                changeTabView(0);
                break;
            case Constants.ORDER_TYPE_UNCOMPLETE:
                tapUnCompleteLayout.setNeedHideIndicator(true);
                tapCompleteLayout.setNeedHideIndicator(true);
                changeTabView(1);
                break;
            case Constants.ORDER_TYPE_WAIT_PAY:
                changeTabView(2);
                break;
            case Constants.ORDER_TYPE_WAIT_SENDOUT:
                changeTabView(3);
                break;
            case Constants.ORDER_TYPE_WAIT_GAIN:
                changeTabView(4);
                break;
            case Constants.ORDER_TYPE_COMPLETE:
                tapUnCompleteLayout.setNeedHideIndicator(true);
                tapCompleteLayout.setNeedHideIndicator(true);
                changeTabView(5);
                break;
            case Constants.ORDER_TYPE_WAIT_COMMENT:
                changeTabView(6);
                break;
            case Constants.ORDER_TYPE_HAS_COMMENT:
                changeTabView(7);
                break;
            case Constants.ORDER_TYPE_CANCEL:
                changeTabView(8);
                break;
        }
    }

    public void showOrderList() {
        orderNoinfoview.setVisibility(View.GONE);
        orderViewpager.setVisibility(View.VISIBLE);
    }

    public void showNoinfo() {
//        orderNoinfoview.setVisibility(View.VISIBLE);
//        orderViewpager.setVisibility(View.GONE);
//        tapTopLayout.setVisibility(View.GONE);
    }

    public CommonTabLayout getTapTopLayout() {
        return tapTopLayout;
    }

    public void setTapTopLayout(CommonTabLayout tapTopLayout) {
        this.tapTopLayout = tapTopLayout;
    }

    public CommonTabLayout getTapCompleteLayout() {
        return tapUnCompleteLayout;
    }

    public void setTapCompleteLayout(CommonTabLayout tapUnCompleteLayout) {
        this.tapUnCompleteLayout = tapUnCompleteLayout;
    }

    public CommonTabLayout getTapUnCompleteLayout() {
        return tapCompleteLayout;
    }

    public void setTapUnCompleteLayout(CommonTabLayout tapCompleteLayout) {
        this.tapCompleteLayout = tapCompleteLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i(TAG + "onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_PAY:
                    waitPayFragment.reLoadData();
                    waitSendFragment.reLoadData();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        updateOrderList();
    }

    public void updateOrderList(){
        String updateType = (String) SPUtils.get("order_need_update","0");//0无变化，1付款，2确认收货，3取消订单，4评论
        if(!updateType.equals("0")){
            switch (updateType){
                case  "1":
                    orderAllFragment.reLoadData();
                    unCompeletFragment.reLoadData();
                    waitPayFragment.reLoadData();
                    break;
                case "2":
                    orderAllFragment.reLoadData();
                    waitGainFragment.reLoadData();
                    compeletFragment.reLoadData();
                    waitCommentFragment.reLoadData();
                    break;
                case "3":
                    orderAllFragment.reLoadData();
                    waitPayFragment.reLoadData();
                    cancelFragment.reLoadData();
                    break;
            }
            SPUtils.put("order_need_update","0");
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
