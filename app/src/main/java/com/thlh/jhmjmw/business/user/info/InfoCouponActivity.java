package com.thlh.jhmjmw.business.user.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.LogsOrder;
import com.thlh.baselib.model.response.InfoTransportListResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.order.trace.OrderTraceActivity;
import com.thlh.jhmjmw.business.user.info.adapter.InfoTransportAdapter;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogSimple;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 物流消息页
 */
public class InfoCouponActivity extends BaseViewActivity {
    private final String TAG = "InfoTransportionActivity";
    @BindView(R.id.info_ptpr_rv)
    PullToRefreshRecyclerView infoPtprRv;
    @BindView(R.id.info_noinfoview)
    NoInfoView infoNoinfoview;
    @BindView(R.id.info_header)
    HeaderNormal infoHeader;


    private List<LogsOrder> infoList = new ArrayList<>();
    private InfoTransportAdapter infoAdapter;
    private EasyRecyclerView infoRv;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = false;
    private int current_page = 1;
    private int total_page = 1;
    private BaseObserver<InfoTransportListResponse> infoObserver;

    private BaseObserver<BaseResponse> delCollectObserver;

    private DialogSimple.Builder dialog;
    private int deletePositon = -1;


    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, InfoCouponActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_info_transportion);
        ButterKnife.bind(this);
        infoHeader.setTitle(getResources().getString(R.string.coupon_message));
        dialog = new DialogSimple.Builder(this);
        infoNoinfoview.setTitle(getResources().getString(R.string.no_message));
        infoNoinfoview.setTitleIv(R.drawable.noinfo_info);
        infoNoinfoview.setVisibility(View.VISIBLE);
        infoPtprRv.setVisibility(View.GONE);

        infoPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        infoRv = infoPtprRv.getRefreshableView();


        infoAdapter = new InfoTransportAdapter(this);
        infoAdapter.setOnClickEvent(new InfoTransportAdapter.OnClickEvent() {
            @Override
            public void onDelete(View view, int position) {
                final LogsOrder logsOrder = infoAdapter.getItem(position);
                showDialog(logsOrder.getId(), position);
            }

        });

        infoAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                OrderTraceActivity.activityStart(InfoCouponActivity.this, ((LogsOrder) infoAdapter.getItem(position)).getOrder_id());
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        infoRv.setLayoutManager(mLayoutManager);
        infoRv.setAdapter(infoAdapter);
        infoPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        infoPtprRv.setHeaderLayout(new PtorHeaderLayout(this));
        infoPtprRv.setFooterLayout(new PtorFooterLayout(this));
        infoPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                infoList.clear();
                infoAdapter.notifyDataSetChanged();
                infoAdapter.closeOpenedSwipeItemLayoutWithAnim();
                current_page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                infoPtprRv.onRefreshComplete();
            }
        });

        infoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " dy" + dy);
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载， dy>0 表示向下滑动
                if (totalItemCount >= Constants.PageDataCount && lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    isLoadingMore = true;
                    current_page++;
                    L.e("加载更多数据 current_page" + current_page);
                    loadCollectData();
                }
            }
        });


        infoObserver = new BaseObserver<InfoTransportListResponse>() {
            @Override
            public void onErrorResponse(InfoTransportListResponse response) {
                progressMaterial.dismiss();
                infoPtprRv.onRefreshComplete();
                isLoadingMore = false;
                showErrorDialog( response.getErr_msg());
            }

            @Override
            public void onNextResponse(InfoTransportListResponse response) {
                progressMaterial.dismiss();
                total_page = response.getData().getTotal_page();
                infoPtprRv.onRefreshComplete();
                List<LogsOrder> tempList = response.getData().getLogs();
                if (isLoadingMore) {
                    isLoadingMore = false;
                    infoAdapter.setList(tempList, true);
                } else {
                    if (tempList == null || tempList.size() == 0) {
                        infoNoinfoview.setVisibility(View.VISIBLE);
                    } else {
                        infoNoinfoview.setVisibility(View.GONE);
                    }
                    infoAdapter.setList(tempList);
                }
            }
        };

        delCollectObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                progressMaterial.dismiss();
                showErrorDialog( baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                progressMaterial.dismiss();
                L.i(TAG + " 删除成功");
                infoList.remove(deletePositon);
                infoAdapter.setList(infoList);
                infoAdapter.closeOpenedSwipeItemLayoutWithAnim();
                if (infoList.size() == 0) {
                    infoNoinfoview.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    @Override
    protected void loadData() {
//        progressMaterial.show();
//        loadCollectData();
    }

    private void loadCollectData() {
        subscription = NetworkManager.getMessageApi()
                .orderInfo(SPUtils.getToken(),current_page,Constants.PageDataCount,"0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(infoObserver);
    }


    private void postDelCollect(String goods_id) {
//        progressMaterial.show();
//        subscription = NetworkManager.getCollectApi()
//                .deleteCollect(SPUtils.getToken(), goods_id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(delCollectObserver);
    }


    public void showDialog(final String goodsId, final int position) {
        dialog.setTitle(getResources().getString(R.string.delete_message)).setLeftBtnStr(getResources().getString(R.string.trues)).setRightBtnStr(getResources().getString(R.string.collect_cannal))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setLeftClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePositon = position;
                        postDelCollect(goodsId);
                    }
                }).create().show();
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
