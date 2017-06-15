package com.thlh.jhmjmw.business.user.wallet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.DealRecord;
import com.thlh.baselib.model.response.DealRecordResponse;
import com.thlh.baselib.utils.S;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 交易记录页
 */
public class DealRecordActivity extends BaseViewActivity {
    private static final String TAG = "DealRecordActivity";

    @BindView(R.id.deal_record_type_tv)
    TextView dealRecordTypeTv;
    @BindView(R.id.deal_record_type_ll)
    LinearLayout dealRecordTypeLl;
    @BindView(R.id.deal_record_ptpr_rv)
    PullToRefreshRecyclerView dealRecordPtprRv;
    @BindView(R.id.deal_record_cl)
    CoordinatorLayout recordCl;
    @BindView(R.id.deal_record_noinfoview)
    NoInfoView recordNoinfoview;

    private String paraFrom;
    private String paraType;
    private int current_page = 1;
    private int total_page = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = false;
    private DealRecordAdapter dealRecordAdapter;
    private EasyRecyclerView recordRv;

    private View poprootview;
    private PopupWindow popupWindow;
    private LinearLayout rechargell,consumell,mjbRechargell,mjbConsumell,allrecordll;
    private BaseObserver<DealRecordResponse> recordObserver;
    private List<DealRecord> recordList = new ArrayList<>();

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, DealRecordActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dealrecord);
        ButterKnife.bind(this);
        initPopwindow();
        dealRecordPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        recordRv = dealRecordPtprRv.getRefreshableView();
        dealRecordAdapter = new DealRecordAdapter(this);
        recordNoinfoview.setTitle(getResources().getString(R.string.none_record));
        recordNoinfoview.setTitleIv(R.drawable.noinfo_order);
        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );

        recordRv.addItemDecoration(dataDecoration);
        mLayoutManager  = new LinearLayoutManager(this);
        recordRv.setLayoutManager(mLayoutManager);
        recordRv.setAdapter(dealRecordAdapter);
        dealRecordPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        dealRecordPtprRv.setHeaderLayout(new PtorHeaderLayout(this));
        dealRecordPtprRv.setFooterLayout(new PtorFooterLayout(this));
        dealRecordPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                dealRecordPtprRv.onRefreshComplete();
            }
        });

        recordRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        recordObserver   = new BaseObserver<DealRecordResponse>() {
            @Override
            public void onErrorResponse(DealRecordResponse response) {
                dealRecordPtprRv.onRefreshComplete();
                new S.Builder( recordCl, response.getErr_msg()).create().show();
//                new S.Builder(DealRecordActivity.this, recordCl, response.getErr_msg()).create().show();
                isLoadingMore = false;
            }

            @Override
            public void onNextResponse(DealRecordResponse response) {
                dealRecordPtprRv.onRefreshComplete();
                total_page = response.getData().getTotal_page();
                recordList = response.getData().getWallet();
                if (isLoadingMore) {
                    dealRecordAdapter.setList(recordList,true);
                    isLoadingMore = false;
                } else {
                    if(recordList == null || recordList.size() ==0){
                        recordNoinfoview.setVisibility(View.VISIBLE);
                        dealRecordPtprRv.setVisibility(View.GONE);
                    }else {
                        recordNoinfoview.setVisibility(View.GONE);
                        dealRecordPtprRv.setVisibility(View.VISIBLE);
                        dealRecordAdapter.setList(recordList);
                    }
                }

            }
        };
    }


    @Override
    protected void loadData() {
        loadRecordData(Constants.RECORD_FROM_ALL,Constants.RECORD_TYPE_All);
    }

    private void loadRecordData(String paraFrom ,String paraType){
        subscription = NetworkManager.getWalletApi()
                .recordIndex(SPUtils.getToken(),current_page, Constants.PageDataCount ,paraFrom,paraType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordObserver);
    }

    private void initPopwindow(){
        poprootview = LayoutInflater.from(this).inflate(R.layout.popup_deal_record, null);
        rechargell = (LinearLayout) poprootview.findViewById(R.id.pop_dealrecord_type_recharge_ll);
        consumell = (LinearLayout) poprootview.findViewById(R.id.pop_dealrecord_type_consume_ll);
        mjbRechargell = (LinearLayout) poprootview.findViewById(R.id.pop_dealrecord_type_mjb_recharge_ll);
        mjbConsumell = (LinearLayout) poprootview.findViewById(R.id.pop_dealrecord_type_mjb_consume_ll);
        allrecordll = (LinearLayout) poprootview.findViewById(R.id.pop_dealrecord_type_all_ll);
        rechargell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealRecordTypeTv.setText(getResources().getString(R.string.balance_top_up));
                recordList.clear();
                paraFrom = Constants.RECORD_FROM_BAlANCE ;
                paraType = Constants.RECORD_TYPE_RECHARGE;
                loadRecordData(paraFrom,paraType);
                popupWindow.dismiss();
            }
        });
        consumell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealRecordTypeTv.setText(getResources().getString(R.string.balance_consumer));
                recordList.clear();
                paraFrom =  Constants.RECORD_FROM_BAlANCE ;
                paraType = Constants.RECORD_TYPE_CONSUME;
                loadRecordData(paraFrom,paraType);
                popupWindow.dismiss();
            }
        });
        mjbRechargell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealRecordTypeTv.setText(getResources().getString(R.string.mjz_Obtain));
                recordList.clear();
                paraFrom =  Constants.RECORD_FROM_MJB ;
                paraType = Constants.RECORD_TYPE_RECHARGE;
                loadRecordData(paraFrom,paraType);
                popupWindow.dismiss();
            }
        });
        mjbConsumell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealRecordTypeTv.setText(getResources().getString(R.string.mjz_consumer));
                recordList.clear();
                paraFrom = Constants.RECORD_FROM_MJB ;
                paraType = Constants.RECORD_TYPE_CONSUME;
                loadRecordData(paraFrom,paraType);
                popupWindow.dismiss();
            }
        });
        allrecordll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dealRecordTypeTv.setText(getResources().getString(R.string.all_transaction));
                        recordList.clear();
                        paraFrom = Constants.RECORD_FROM_ALL ;
                        paraType = Constants.RECORD_TYPE_All;
                        loadRecordData(paraFrom,paraType);
                        popupWindow.dismiss();
                    }
                });
        popupWindow = new PopupWindow(poprootview,  BaseApplication.width/3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(poprootview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_null));
        popupWindow.setAnimationStyle(R.style.popwin_anim_left_style);
    }



    @OnClick({R.id.deal_record_type_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deal_record_type_ll:
                popupWindow.showAsDropDown(dealRecordTypeLl, 0,0);
                break;

        }
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
