package com.thlh.jhmjmw.business.user.coupon;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.Coupon;
import com.thlh.baselib.model.response.CouponListResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.coupon.CouponExchangeActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CouponFragment extends BaseFragment {
    private static final String TAG = "CouponFragment";
    private static final int ACTIVITY_CODE_COUPON_EXCHANGE = 1;
    @BindView(R.id.coupon_ptpr_rv)
    PullToRefreshRecyclerView couponPtprRv;

    @BindView(R.id.coupon_noinfoview)
    NoInfoView couponNoinfoview;

    @BindView(R.id.coupon_cl)
    CoordinatorLayout couponCl;

    private EasyRecyclerView couponsRv;
    private LinearLayoutManager mLayoutManager;
    
    private int total_page = 1;
    private int current_page = 1;
    private boolean isLoadingMore = false;
    private CouponAdapter couponAdapter;
    private String couponFlag;
    private BaseObserver<CouponListResponse> couponObserver;
    private List<Coupon> couponList = new ArrayList<>();
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_coupon;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        couponFlag = bundle.getString("couponFlag");
        L.e("CouponFragment couponFlag:" +couponFlag );
    }

    @Override
    protected void initView() {
        switch (couponFlag){
            case Constants.COUPON_FLAG_UNUSE:
                couponNoinfoview.setTitle(getResources().getString(R.string.no_use_coupon));
                break;
            case Constants.COUPON_FLAG_NEARTIME:
                couponNoinfoview.setTitle(getResources().getString(R.string.About_expired_coupon));
                break;
            case Constants.COUPON_FLAG_USED:
                couponNoinfoview.setTitle(getResources().getString(R.string.no_used_coupon));
                break;
        }
        couponNoinfoview.setTitleIv(R.drawable.img_dialog_coupon);
        // 设置没有上拉阻力
        couponPtprRv.setHasPullUpFriction(false);
        couponsRv = couponPtprRv.getRefreshableView();
        couponsRv.setPadding(20,20,20,0);
        couponAdapter = new CouponAdapter(getActivity(),couponFlag);
        couponAdapter.setActionEvent(new CouponAdapter.OnClickListener() {
            @Override
            public void onExchange(int position) {
                String couponid = ((Coupon)couponAdapter.getItem(position)).getId();
                CouponExchangeActivity.activityStart(getActivity(),ACTIVITY_CODE_COUPON_EXCHANGE,couponid);
            }
        });

        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                getActivity(),
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_wide_white
        );

        couponsRv.addItemDecoration(dataDecoration);
        mLayoutManager = new LinearLayoutManager(getActivity());
        couponsRv.setLayoutManager(mLayoutManager);
        couponsRv.setAdapter(couponAdapter);
        couponPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        couponPtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));
        couponPtprRv.setFooterLayout(new PtorFooterLayout(getActivity()));

        couponPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                reLoadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                couponPtprRv.onRefreshComplete();
            }
        });

        couponsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoadingMore) return;
                if (current_page >= total_page) return;
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载， dy>0 表示向下滑动
                if (totalItemCount >= Constants.PageDataCount && lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    isLoadingMore = true;
                    current_page++;
                    L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " dy" + dy);
                    L.e("加载更多数据 current_page" + current_page);
                    loadData();//这里多线程也要手动控制isLoadingMore
                }
            }
        });
    }

    @Override
    protected void initData() {
        couponObserver   = new BaseObserver<CouponListResponse>() {
            @Override
            public void onErrorResponse(CouponListResponse response) {
                couponPtprRv.onRefreshComplete();
                isLoadingMore = false;
            }

            @Override
            public void onNextResponse(CouponListResponse response) {
                couponPtprRv.onRefreshComplete();

                total_page = response.getData().getTotal_page();

                couponList = response.getData().getCoupons();

                if (isLoadingMore) {
                    couponAdapter.setList(couponList,true);
                    isLoadingMore = false;
                } else {
                    if(couponList == null || couponList.size() ==0){
                        couponNoinfoview.setVisibility(View.VISIBLE);
                        couponPtprRv.setVisibility(View.GONE);
                    }else {
                        couponNoinfoview.setVisibility(View.GONE);
                        couponPtprRv.setVisibility(View.VISIBLE);
                        couponAdapter.setList(couponList);
                    }
                }

                int totalNum = response.getData().getTotal();

                showMsgNum(couponFlag,totalNum);

            }
        };
    }

    public void showMsgNum(String flag ,int totalNum){
        switch (flag){
            case Constants.COUPON_FLAG_UNUSE:
                couponNoinfoview.setTitle(getResources().getString(R.string.no_used_coupon));
                ((CouponActivity)getActivity()).getTapTopLayout().showMsg(0,totalNum);
                break;
            case Constants.COUPON_FLAG_NEARTIME:
                couponNoinfoview.setTitle(getResources().getString(R.string.About_expired_coupon));
                ((CouponActivity)getActivity()).getTapTopLayout().showMsg(1,totalNum);
                break;
            case Constants.COUPON_FLAG_USED:
                couponNoinfoview.setTitle(getResources().getString(R.string.no_use_coupon));
                ((CouponActivity)getActivity()).getTapTopLayout().showMsg(2,totalNum);
                break;
        }
    }

    @Override
    protected void loadData() {
        NetworkManager.getWalletApi()
                .couponIndex(SPUtils.getToken(),current_page , Constants.PageDataCount ,couponFlag)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(couponObserver);
    }

    public void reLoadData(){
        current_page = 1;
        couponList.clear();
        couponAdapter.setList(couponList);
        loadData();
    }

}
