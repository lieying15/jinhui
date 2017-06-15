package com.thlh.jhmjmw.business.order.list;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.response.OrderResponse;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.view.DialogFrgCancelOrder;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.ShopCartActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.order.comment.OrderCommentActivity;
import com.thlh.jhmjmw.business.order.orderdetail.OrderDetailActivity;
import com.thlh.jhmjmw.business.pay.PayOrderActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderFragment extends BaseFragment {
    private static final String TAG = "OrderFragment";
    private final int ACTIVITY_CODE_PAY = 1;
    @BindView(R.id.order_base_ptpr_rv)
    PullToRefreshRecyclerView orderBasePtprRv;
    @BindView(R.id.order_noinfoview)
    NoInfoView noinfoview;

    private EasyRecyclerView orderBaseRv;
    private LinearLayoutManager mLayoutManager;
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    
    private int total_page;
    private int current_page = 1;
    private boolean isLoadingMore = false;
    private int cotent_type;
    private BaseObserver<OrderResponse> orderObserver;
    private BaseObserver<BaseResponse> confrimOrderObserver,cancelOrderObserver;

    private SweetAlertDialog sweetDialog;
    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        cotent_type = bundle.getInt("orderType");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        noinfoview.setTitle(getResources().getString(R.string.order_no));
        noinfoview.setTitleIv(R.drawable.img_dialog_pen);
        noinfoview.setNextactionStr(getResources().getString(R.string.shopcart_total_golook));
        noinfoview.setNextactionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexActivity.activityStart(getActivity(), IndexActivity.POSITON_HOMEPAGE);

            }
        });
        orderBasePtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        orderBaseRv = orderBasePtprRv.getRefreshableView();

        orderAdapter  = new OrderAdapter(getActivity(),cotent_type);
        orderAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                OrderDetailActivity.activityStart(getActivity(),orderList.get(position));
            }
        });

        orderAdapter.setActionEvent(new OrderAdapter.OnClickListener() {

            @Override
            public void cancel(String orderid) {
                showCancelOrderDialog(orderid);
            }

            @Override
            public void onRebuy(int position,Order order) {
                addShopcart(order);
                ShopCartActivity.activityStart(getActivity());

            }

            @Override
            public void onConfiromGetGoods(String orderid) {
                showConfrimGainDialog(orderid);
            }

            @Override
            public void gotoPay(Order order) {
                PayOrderActivity.activityStart(getActivity(),ACTIVITY_CODE_PAY,order);
            }

        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        orderBaseRv.setLayoutManager(mLayoutManager);
        orderBaseRv.setAdapter(orderAdapter);
        orderBaseRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
        orderBasePtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        orderBasePtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));
        orderBasePtprRv.setFooterLayout(new PtorFooterLayout(getActivity()));
        orderBasePtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                orderBasePtprRv.onRefreshComplete();
            }
        });

        orderBaseRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
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
        orderObserver = new BaseObserver<OrderResponse>() {
            @Override
            public void onErrorResponse(OrderResponse orderResponse) {
                orderBasePtprRv.onRefreshComplete();
                isLoadingMore = false;
            }

            @Override
            public void onNextResponse(OrderResponse orderResponse) {
                orderBasePtprRv.onRefreshComplete();
                orderList = orderResponse.getData().getOrders();
                total_page = orderResponse.getData().getTotal_page();
                L.e("加载更多数据 cotent_type "+cotent_type+" isLoadingMore" + "isLoadingMore " + isLoadingMore);
                if (isLoadingMore) {
                    orderAdapter.setList(orderList, true);
                    isLoadingMore = false;
                } else {
                    if(orderList == null || orderList.size() ==0){
                        L.e(TAG + "orderList == null || orderList.size() ==0");
                        if(cotent_type == Constants.ORDER_TYPE_ALL){
                            ((OrderListActivity)getActivity()).showNoinfo();
                        }else {
                            orderBasePtprRv.setVisibility(View.GONE);
                            noinfoview.setVisibility(View.VISIBLE);
                        }
                    }else {
                        L.e(TAG + "orderList != null");
                        ((OrderListActivity)getActivity()).showOrderList();
                        orderBasePtprRv.setVisibility(View.VISIBLE);
                        noinfoview.setVisibility(View.GONE);
                    }
                    orderAdapter.setList(orderList);
                    showMsgNum(orderResponse);
                }
            }
        };

        confrimOrderObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse orderResponse) {
                showErrorDialog(orderResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse orderResponse) {
                SPUtils.put("order_need_update","2");//刷新0无变化，1付款，2确认收货，3取消订单
                OrderCommentActivity.activityStart(getActivity());
            }
        };

        cancelOrderObserver  = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse orderResponse) {
                showErrorDialog(orderResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse orderResponse) {
                SPUtils.put("order_need_update","3");//刷新0无变化，1付款，2确认收货，3取消订单
                ((OrderListActivity)getActivity()).updateOrderList();
            }
        };

    }

    @Override
    protected void loadData() {
        NetworkManager.getOrderApi()
                .index(SPUtils.getToken(), cotent_type + "", current_page , Constants.PageDataCount )
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(orderObserver);
    }

    public void reLoadData(){
        current_page = 1;
        orderList.clear();
        orderAdapter.setList(orderList);
        loadData();
    }


    public void postCancelOrder(String orderid,String reason) {
        NetworkManager.getOrderApi()
                .cancelOrder(SPUtils.getToken(), orderid,reason)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(cancelOrderObserver);

    }

    public void postConfrimOrder(String orderid) {
        NetworkManager.getOrderApi()
                .confrimOrder(SPUtils.getToken(), orderid)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(confrimOrderObserver);
    }


    public void showCancelOrderDialog(final String orderid){
        final DialogFrgCancelOrder cancelDialog = new DialogFrgCancelOrder();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        cancelDialog.setTitleIvRes(com.thlh.baselib.R.drawable.img_dialog_trash);
        cancelDialog.setMiddleBtnVisible(View.VISIBLE);
        cancelDialog.setMiddleBtnStr(getResources().getString(R.string.think_about));
        cancelDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog.dismiss();
            }
        });
        cancelDialog.setFinalBtnStr(getResources().getString(R.string.order_cannal));
        cancelDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCancelOrder(orderid,cancelDialog.getRadioButtonStr());
                cancelDialog.dismiss();
            }
        });
        cancelDialog.show(ft,"cancelDialog");
    }

    public void showConfrimGainDialog(final  String orderid){
        final NormalDialogFragment confirmDialog = new NormalDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        confirmDialog.setTitleIvRes(R.drawable.icon_dialog_warning);
        confirmDialog.setContentStr(getResources().getString(R.string.yes_no_goods));
        confirmDialog.setMiddleBtnVisible(View.VISIBLE);
        confirmDialog.setMiddleBtnStr(getResources().getString(R.string.shopcart_total_cannal));
        confirmDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        confirmDialog.setFinalBtnStr(getResources().getString(R.string.shopcart_total_confirm));
        confirmDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postConfrimOrder(orderid);
                confirmDialog.dismiss();
            }
        });
        confirmDialog.show(ft,"confirmDialog");
    }

    public void showMsgNum(OrderResponse orderResponse ){
        ((OrderListActivity)getActivity()).getTapTopLayout().showMsg(0,orderResponse.getData().getProcessing_num());
        ((OrderListActivity)getActivity()).getTapTopLayout().showMsg(1,orderResponse.getData().getDone_num());
        ((OrderListActivity)getActivity()).getTapTopLayout().showMsg(2,orderResponse.getData().getCancel_num());
        ((OrderListActivity)getActivity()).getTapCompleteLayout().showMsg(0,orderResponse.getData().getUnpay_num());
        ((OrderListActivity)getActivity()).getTapCompleteLayout().showMsg(1,orderResponse.getData().getUndeliv_num());
        ((OrderListActivity)getActivity()).getTapCompleteLayout().showMsg(2,orderResponse.getData().getUnget_num());
        ((OrderListActivity)getActivity()).getTapUnCompleteLayout().showMsg(0,orderResponse.getData().getUncomm_num());
        ((OrderListActivity)getActivity()).getTapUnCompleteLayout().showMsg(1,orderResponse.getData().getComm_num());
    }

    private void addShopcart(Order order){
        List<GoodsOrder> goodsorder = order.getOrder_items().get(0).getItem();
        boolean canReBuy = true;
        for(GoodsOrder goods : goodsorder){
            try {
                Goods tempgoods = GoodsChangeUtils.changeGoods(goods);
                if(tempgoods.getStorage()==0){
                    showErrorDialog(getResources().getString(R.string.shop)+tempgoods.getItem_name() +getResources().getString(R.string.gone_));
                    canReBuy = false;
                    return;
                }
                // 单品套装
                if(goods.getIs_part().equals("1")){
                    if(goods.getPart_is_bundling().equals("1")){
                        GoodsSuitDetailActivity.activityStart(getActivity(),goods.getPart_of_id(),0);
                    }else {
                        GoodsDetailV3Activity.activityStart(getActivity(),goods.getPart_of_id());
                    }
                    canReBuy = false;
                    return;
                }

                // 整箱套装
                int size = Integer.parseInt(goods.getItem_num());
                for (int i = 0; i <size ; i++) {
                    if(tempgoods.getIs_pack().equals("1")){
                        int packNum  = tempgoods.getPack_num()==null?0:Integer.parseInt(tempgoods.getPack_num());
                        for (int n = 0; n < packNum; n++) {
                            DbManager.getInstance().insertCart(tempgoods);
                        }
                    }else {
                        DbManager.getInstance().insertCart(tempgoods);
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
//        if(canReBuy){
//            sweetDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
//            sweetDialog.setTitleText("已加入购物车").show();
//        }
    }


}
