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
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.user.info.adapter.InfoTransportAdapter;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogSimple;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 物流消息页
 */
public class InfoTransportionActivity extends BaseViewActivity {
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
    private int total_page =1;
    private BaseObserver<InfoTransportListResponse> infoObserver;

    private BaseObserver<BaseResponse> delCollectObserver;

    private DialogSimple.Builder dialog;
    private int deletePositon = -1;

    
    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, InfoTransportionActivity.class);
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
        infoHeader.setTitle(getResources().getString(R.string.info_message));
        dialog = new DialogSimple.Builder(this);
        infoNoinfoview.setTitle(getResources().getString(R.string.no_info_message));
        infoNoinfoview.setTitleIv(R.drawable.icon_dialog_warning);

        infoPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        infoRv = infoPtprRv.getRefreshableView();



        infoAdapter = new InfoTransportAdapter(this);
        infoAdapter.setOnClickEvent(new InfoTransportAdapter.OnClickEvent() {
            @Override
            public void onDelete(View view, int position) {
                final LogsOrder logsOrder = infoAdapter.getItem(position);
                showDeleteDialog(logsOrder.getId(),position);
            }

        });

//        infoAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
//            @Override
//            public void onItemClick(View convertView, int position) {
//                OrderTraceActivity.activityStart(InfoTransportionActivity.this, ((LogsOrder) infoAdapter.getItem(position)).getOrder_id());
//            }
//        });

        mLayoutManager = new LinearLayoutManager(this);
        infoRv.setLayoutManager(mLayoutManager);
        infoRv.setAdapter(infoAdapter);
        infoRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
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
//                    current_page++;
                    L.e("加载更多数据 current_page" + current_page);
                    loadLogsData();
                }
            }
        });


        infoObserver = new BaseObserver<InfoTransportListResponse>() {
            @Override
            public void onErrorResponse(InfoTransportListResponse response) {
                infoPtprRv.onRefreshComplete();
                isLoadingMore = false;
                showErrorDialog( response.getErr_msg());
            }

            @Override
            public void onNextResponse(InfoTransportListResponse response) {
                total_page = response.getData().getTotal_page();
                infoPtprRv.onRefreshComplete();
                List<LogsOrder> tempList = response.getData().getLogs();
                if (isLoadingMore) {
                    isLoadingMore = false;
                    infoAdapter.setList(tempList,true);
                } else {
                    if(tempList ==null || tempList.size() ==0){
                        infoNoinfoview.setVisibility(View.VISIBLE);
                    }else {
                        infoNoinfoview.setVisibility(View.GONE);
                    }
                    infoAdapter.setList(tempList);
//                    DbManager.getInstance().insertLogs(tempList);
                }
            }
        };

        delCollectObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog( baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                L.i(TAG + " 删除成功");
                infoList.remove(deletePositon);
                infoAdapter.setList(infoList);
                infoAdapter.closeOpenedSwipeItemLayoutWithAnim();
                if(infoList.size()==0){
                    infoNoinfoview.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    @Override
    protected void loadData() {
        loadLogsData();
    }

    private void loadLogsData(){
//        String lastidStr = DbManager.getInstance().getlastLogsId();
//        L.e(TAG + " loadLogsData lastid:" + lastid);
        NetworkManager.getMessageApi()
                .orderInfo(SPUtils.getToken(),current_page,Constants.PageDataCount,"0")
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(infoObserver);
    }


    private void postDelCollect(String info_id) {
        NetworkManager.getMessageApi()
                .deleteInfo(SPUtils.getToken(), info_id)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(delCollectObserver);
    }


    public void showDeleteDialog(final String goodsId,final int position){
        dialog.setTitle(getResources().getString(R.string.delete_message)).setLeftBtnStr(getResources().getString(R.string.shopcart_total_confirm)).setRightBtnStr(getResources().getString(R.string.shopcart_total_cannal))
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
}
