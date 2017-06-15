package com.thlh.jhmjmw.business.order.trace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.OrderPack;
import com.thlh.baselib.model.Track;
import com.thlh.baselib.model.TrackData;
import com.thlh.baselib.model.TrackResult;
import com.thlh.baselib.model.response.OrderTraceResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TimeUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单配送页
 */
public class OrderTraceActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "OrderTraceActivity";

    @BindView(R.id.trace_packege_ll)
    LinearLayout traceTagLl;
    @BindView(R.id.trace_viewpager)
    ViewPager traceViewpager;
    @BindView(R.id.trace_tagarrow_left_iv)
    ImageView traceTagarrowLeftIv;
    @BindView(R.id.trace_tagbtn_left_tv)
    TextView traceTagbtnLeftTv;
    @BindView(R.id.trace_tagbtn_right_tv)
    TextView traceTagbtnRightTv;
    @BindView(R.id.trace_tagarrow_right_iv)
    ImageView traceTagarrowRightIv;



    private BaseObserver<OrderTraceResponse> traceObserver;
    private List<Fragment> fragmentList;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private int currentPosition = 0;
    private int packSize = 0;

    private Order order;
    private String order_id;
    private String  supplierName = "";

    public static void activityStart(Activity context, String order_id) {
        Intent intent = new Intent();
        intent.putExtra("order_id", order_id);
        intent.setClass(context, OrderTraceActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context, String order_id,String supplierName) {
        Intent intent = new Intent();
        intent.putExtra("order_id", order_id);
        intent.putExtra("supplier_name", supplierName);
        intent.setClass(context, OrderTraceActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context, Order order) {
        Intent intent = new Intent();
        intent.putExtra("order", order);
        intent.setClass(context, OrderTraceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        order = getIntent().getParcelableExtra("order");
        if(order==null){
            order_id = getIntent().getStringExtra("order_id");
            supplierName = getIntent().getStringExtra("supplier_name");
        }else {
            order_id = order.getOrder_id();
            supplierName = order.getOrder_items().get(0).getStore_name();
        }
        L.e(TAG + " order_id:" + order_id +" supplierName:" + supplierName);
        fragmentList = new ArrayList<Fragment>();
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_trace);
        ButterKnife.bind(this);

        traceTagbtnLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = 0;
                traceViewpager.setCurrentItem(currentPosition);
                traceTagbtnLeftTv.setBackgroundResource(R.drawable.shap_radius_half_top_white);
                traceTagbtnRightTv.setBackgroundResource(R.drawable.shap_radius_half_top_gray);
            }
        });

        traceTagbtnRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = 1;
                traceViewpager.setCurrentItem(currentPosition);
                traceTagbtnRightTv.setBackgroundResource(R.drawable.shap_radius_half_top_white);
                traceTagbtnLeftTv.setBackgroundResource(R.drawable.shap_radius_half_top_gray);
            }
        });


        traceTagarrowLeftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition <2) return;
                currentPosition -- ;
                traceViewpager.setCurrentItem(currentPosition);

            }
        });

        traceTagarrowRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition <packSize -1 ){
                    currentPosition ++;
                    traceViewpager.setCurrentItem(currentPosition);

                }
            }
        });
        traceObserver = new BaseObserver<OrderTraceResponse>() {
            @Override
            public void onErrorResponse(OrderTraceResponse traceResponse) {
                showErrorDialog(traceResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(OrderTraceResponse traceResponse) {
                List<OrderPack> orderPacks = traceResponse.getData().getPack();
                ArrayList<GoodsOrder> orderGoods = new ArrayList<>();

                for(GoodsOrder goods: traceResponse.getData().getItem()){
                    orderGoods.add(goods);
                }

                packSize = orderPacks.size();
                if (packSize == 0 ) {
                    insertDate(traceResponse.getData().getOrder_time(),traceResponse.getData().getPay_time(),orderGoods);
                    return;
                }
                if (packSize < 2) {
                    traceTagLl.setVisibility(View.GONE);
                } else {
                    traceTagLl.setVisibility(View.VISIBLE);
                }
                if(packSize>2){
                    traceTagarrowRightIv.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < packSize; i++) {
                    TraceFragment traceFragment = new TraceFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", i);
                    args.putString("supplier_name", supplierName);
                    args.putParcelable("orderPack", orderPacks.get(i));
                    args.putParcelableArrayList("orderGoods", orderGoods);
                    args.putString("ordertime", traceResponse.getData().getOrder_time());
                    args.putString("paytime",traceResponse.getData().getPay_time());
                    args.putString("expressfree",traceResponse.getData().getExpress_fee());
                    traceFragment.setArguments(args);
                    fragmentList.add(traceFragment);
                }
                myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
                traceViewpager.setAdapter(myFragmentPagerAdapter);
                traceViewpager.setCurrentItem(currentPosition);

                traceViewpager.addOnPageChangeListener(new MyPagerChangeListener());
                traceViewpager.setOffscreenPageLimit(3);

                changeTabView(0);
            }
        };
    }


    @Override
    protected void loadData() {
        L.e(TAG +" loadtrace order_id:"+order_id );
        NetworkManager.getOrderApi()
                .trace(SPUtils.getToken(), order_id)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(traceObserver);
    }


    @Override
    public void onClick(View v) {

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> myList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            myList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
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
            if (currentPosition == position) {
                return;
            } else {
                currentPosition = position;
                changeTabView(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private void changeTabView(int position) {
        switch (position) {
            case 0:
                traceTagbtnLeftTv.setBackgroundResource(R.drawable.shap_radius_half_top_white);
                traceTagbtnRightTv.setBackgroundResource(R.drawable.shap_radius_half_top_gray);
                break;
            case 1:
                traceTagbtnRightTv.setBackgroundResource(R.drawable.shap_radius_half_top_white);
                traceTagbtnLeftTv.setBackgroundResource(R.drawable.shap_radius_half_top_gray);
                break;
        }
    }

    //无数据时显示的内容
    private void insertDate(String ordertime ,String paytiem,ArrayList<GoodsOrder> orderGoods){
        L.e("");
        String newOrdertime =  TimeUtils.stringToDateString(ordertime);
        String newPaytime =  TimeUtils.stringToDateString(paytiem);

        TrackData trackData1 = new TrackData(newOrdertime,getResources().getString(R.string.order_commit_wait_pay));
        TrackData trackData2 = new TrackData(newPaytime,getResources().getString(R.string.pay_success_wait_goods));
        List<TrackData> trackDataList = new ArrayList<>();
        trackDataList.add(trackData2);
        trackDataList.add(trackData1);
        TrackResult trackResult = new TrackResult();
        trackResult.setData(trackDataList);
        Track track = new Track();
        track.setError_code(0);
        track.setResult(trackResult);
        OrderPack orderPacks = new OrderPack();
        orderPacks.setTrack(track);

        TraceFragment traceFragment = new TraceFragment();
        Bundle args = new Bundle();
        args.putInt("position", 0);
        args.putString("supplier_name", supplierName);
        args.putParcelable("orderPack", orderPacks);
        args.putBoolean("isNull", true); //是否有数据。
        args.putParcelable("order", order);
        args.putParcelableArrayList("orderGoods", orderGoods);
        traceFragment.setArguments(args);

        fragmentList.add(traceFragment);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        traceViewpager.setAdapter(myFragmentPagerAdapter);
        traceViewpager.setCurrentItem(currentPosition);
        traceViewpager.addOnPageChangeListener(new MyPagerChangeListener());

        changeTabView(0);
    }

}
