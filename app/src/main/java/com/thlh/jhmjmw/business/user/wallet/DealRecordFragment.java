package com.thlh.jhmjmw.business.user.wallet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.DealRecord;
import com.thlh.baselib.model.response.DealRecordResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.List;

import butterknife.BindView;

public class DealRecordFragment extends BaseFragment {
    private static final String TAG = "DealRecordFragment";

    @BindView(R.id.record_ptpr_rv)
    PullToRefreshRecyclerView balancePtprRv;

    @BindView(R.id.balance_noinfoview)
    NoInfoView balanceNoinfoview;

    private EasyRecyclerView collectGoodsRv;
    private int current_page = 1;
    private int total_page = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = false;

    private DealRecordAdapter dealRecordAdapter;
    private String cotent_type;
    private String cotent_form;
    private BaseObserver<DealRecordResponse> recordObserver;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_record;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        cotent_type = bundle.getString("recordType");
        cotent_form = bundle.getString("recordForm");

//        int out_money = bundle.getInt("out_money");

      /*  int mjz_money = bundle.getInt("mjz_money");
        int out_money = bundle.getInt("out_money");*/


        L.e("DealRecordFragment cotent_type:" +cotent_type + "  cotent_form:"+cotent_form);
    }

    @Override
    protected void initView() {
        balanceNoinfoview.setTitle(getResources().getString(R.string.none_record));
        balanceNoinfoview.setTitleIv(R.drawable.img_dialog_mjz);

        balancePtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        collectGoodsRv = balancePtprRv.getRefreshableView();
        dealRecordAdapter = new DealRecordAdapter(getActivity());
        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                getActivity(),
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );

        collectGoodsRv.addItemDecoration(dataDecoration);
        mLayoutManager = new LinearLayoutManager(getActivity());
        collectGoodsRv.setLayoutManager(mLayoutManager);
        collectGoodsRv.setAdapter(dealRecordAdapter);

        balancePtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        balancePtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));
        balancePtprRv.setFooterLayout(new PtorFooterLayout(getActivity()));
        balancePtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadDealRecord();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                balancePtprRv.onRefreshComplete();
            }
        });

        collectGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    loadDealRecord();//这里多线程也要手动控制isLoadingMore
                }
            }
        });
    }

    @Override
    protected void initData() {
        recordObserver   = new BaseObserver<DealRecordResponse>() {
            @Override
            public void onErrorResponse(DealRecordResponse response) {
                balancePtprRv.onRefreshComplete();
                isLoadingMore = false;
            }

            @Override
            public void onNextResponse(DealRecordResponse response) {

                total_page = response.getData().getTotal_page();
                balancePtprRv.onRefreshComplete();
                List<DealRecord> recordList = response.getData().getWallet();
                if (isLoadingMore) {
                    dealRecordAdapter.setList(recordList, true);
                    isLoadingMore = false;
                } else {
                    if(recordList == null || recordList.size() ==0){
                        balanceNoinfoview.setVisibility(View.VISIBLE);
                        balancePtprRv.setVisibility(View.GONE);
                    }else {
                        balanceNoinfoview.setVisibility(View.GONE);
                        balancePtprRv.setVisibility(View.VISIBLE);
                        dealRecordAdapter.setList(recordList);

                    }
                }
            }
        };
    }
    @Override
    protected void loadData() {
        loadDealRecord();
    }

    public void loadDealRecord(){
        NetworkManager.getWalletApi()
                .recordIndex(SPUtils.getToken(),current_page , Constants.PageDataCount ,cotent_form,cotent_type)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(recordObserver);
    }


}
