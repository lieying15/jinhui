package com.thlh.jhmjmw.business.order.trace;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.OrderItem;
import com.thlh.baselib.model.OrderPack;
import com.thlh.baselib.model.TrackData;
import com.thlh.baselib.utils.TimeUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TraceFragment extends BaseFragment {
    private static final String TAG = "TraceFragment";

    @BindView(R.id.trace_info_topll)
    RelativeLayout traceInfoTopLl;
    @BindView(R.id.trace_image_iv)
    ImageView traceImageIv;
    @BindView(R.id.trace_goodsnum_tv)
    TextView traceGoodsnumTv;
    @BindView(R.id.order_trace_company_name_tv)
    TextView orderTraceCompanyNameTv;
    @BindView(R.id.order_trace_no_tv)
    TextView orderTraceNoTv;
    @BindView(R.id.order_trace_expree_tv)
    TextView orderTraceExpreeTv;
    @BindView(R.id.trace_info_ll)
    RelativeLayout traceInfoLl;
    @BindView(R.id.order_trace_rv)
    EasyRecyclerView orderTraceRv;
    @BindView(R.id.trace_supplier_tv)
    TextView traceSupplierTv;
    //运费
    @BindView(R.id.order_trace_phone_tv)
    TextView orderTracePhoneTv;
    @BindView(R.id.goods_iv_rv)
    RecyclerView goodsIvRv;
    Unbinder unbinder;
    @BindView(R.id.goods_details_tv)
    TextView goodsDetailsTv;

    private OrderTraceAdapter orderTraceAdapter;
    private OrderTracePopItemAdapter popItemAdapter;

    private int position;
    private OrderPack orderPack;
    private ArrayList<GoodsOrder> orderGoodsList;
    private String supplierName, expressFree;
    private boolean isNull;
    private Order order;
    private String ordertime, paytime;

    //pop
    private View poprootview;
    private EasyRecyclerView popItemRv;
    private PopupWindow popupWindow;
    private int is_fridge;
    private String express_fee;
    private String item_id;
    private String url;
    private String imageurl;
    private TraceImgAdapter buyConfirmImgAdapter;
    private boolean flag = false;

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        orderPack = bundle.getParcelable("orderPack");
        orderGoodsList = bundle.getParcelableArrayList("orderGoods");

        supplierName = bundle.getString("supplier_name", "");
        /**
         * 运费：到货后物流公司收取
         * */
        expressFree = bundle.getString("expressfree", getResources().getString(R.string.goods_arrive_info));
        isNull = bundle.getBoolean("isNull", false);
        if (isNull) {
            order = bundle.getParcelable("order");
        }
        String tempOrderTime = bundle.getString("ordertime", "");
        String tempPayTime = bundle.getString("paytime", "");

        paytime = TimeUtils.stringToDateString(tempPayTime);
        ordertime = TimeUtils.stringToDateString(tempOrderTime);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_trace;
    }

    @Override
    protected void initView() {
        goodsIvRv.setVisibility(View.GONE);
        buyConfirmImgAdapter = new TraceImgAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        goodsIvRv.setAdapter(buyConfirmImgAdapter);
        goodsIvRv.setLayoutManager(linearLayoutManager);
        buyConfirmImgAdapter.setList(orderGoodsList);
        orderTraceCompanyNameTv.setText(getResources().getString(R.string.store) + orderGoodsList.get(0).getSupplier().getName());
        // orderTraceNoTv.setText(getResources().getString(R.string.send) +  orderGoodsList.get(0).getSupplier().getName() +getResources().getString(R.string.distribution));
        orderTraceNoTv.setText(getResources().getString(R.string.send) + orderGoodsList.get(0).getSupplier_name() + getResources().getString(R.string.distribution));
        traceGoodsnumTv.setText(getResources().getString(R.string.gong) + getGoodsNum(orderGoodsList) + getResources().getString(R.string.ones));
        if (getGoodsNum(orderGoodsList) > 1) {
            goodsDetailsTv.setVisibility(View.VISIBLE);
        }
        //
        if (isNull && order != null) {

            if (order.getOrder_items().get(0).getItem().get(0).getItem_img_thumb().contains("http")) {
                imageurl = order.getOrder_items().get(0).getItem().get(0).getItem_img_thumb();
            } else {
                imageurl = Deployment.IMAGE_PATH + order.getOrder_items().get(0).getItem().get(0).getItem_img_thumb();
            }
            ImageLoader.displayRoundImg(imageurl, traceImageIv);


            String supplier_id = order.getSupplier_id();
            item_id = order.getOrder_items().get(0).getItem().get(0).getItem_id();
//            express_fee = order.getOrder_items().get(0).getExpress_fee();

            String yunfei = null;

            if (!(order.getExpress_fee().equals("0.00"))) {
                yunfei = getResources().getString(R.string.money) + order.getExpress_fee();
            }

            for (OrderItem item : order.getOrder_items()) {
                if (yunfei != null) {
                    if (!yunfei.contains(getResources().getString(R.string.mail))
                            && !yunfei.contains(getResources().getString(R.string.expressfree_pay))) {
                        if (item.getSupplier_id().equals("1")) {
                            yunfei = yunfei.concat("+" + getResources().getString(R.string.mail));
                        } else if (item.getSupplier_id().equals("48")) {
                            yunfei = yunfei.concat("+" + getResources().getString(R.string.expressfree_pay));
                        } else if (!yunfei.contains(getResources().getString(R.string.mail))) {
                            if (item.getExpress_fee().equals("0.00")) {
                                yunfei = yunfei.concat("+" + getResources().getString(R.string.mail));
                            }
                        }
                    } else if (yunfei.contains(getResources().getString(R.string.mail))
                            && !yunfei.contains(getResources().getString(R.string.expressfree_pay))) {
                        if (item.getSupplier_id().equals("48")) {
                            yunfei = yunfei.concat("+" + getResources().getString(R.string.expressfree_pay));
                        } else if (!yunfei.contains(getResources().getString(R.string.mail))) {
                            if (item.getExpress_fee().equals("0.00")) {
                                yunfei = yunfei.concat("+" + getResources().getString(R.string.mail));
                            }
                        }
                    } else if (!yunfei.contains(getResources().getString(R.string.mail))
                            && yunfei.contains(getResources().getString(R.string.expressfree_pay))) {
                        if (item.getSupplier_id().equals("1")) {
                            yunfei = yunfei.concat("+" + getResources().getString(R.string.goods_receive));
                        } else if (!yunfei.contains(getResources().getString(R.string.mail))) {
                            if (item.getExpress_fee().equals("0.00")) {
                                yunfei = yunfei.concat("+" + getResources().getString(R.string.mail));
                            }
                        }
                    } else if (!yunfei.contains(getResources().getString(R.string.mail))) {
                        if (!item.getExpress_fee().equals("0.00")) {
                            yunfei = getResources().getString(R.string.money) + item.getExpress_fee();
                        } else {
                            yunfei = getResources().getString(R.string.mail);
                        }
                    }
                } else {
                    if (item.getSupplier_id().equals("1")) {
                        yunfei = getResources().getString(R.string.mail);
                    } else if (item.getSupplier_id().equals("48")) {
                        yunfei = getResources().getString(R.string.expressfree_pay);
                    } else {
                        yunfei = getResources().getString(R.string.mail);
                    }
                }
            }
            orderTraceExpreeTv.setText(yunfei);
        } else {

            if (orderGoodsList.get(0).getItem_img_thumb().contains("http")) {
                url = orderGoodsList.get(0).getItem_img_thumb();
            } else {
                url = Deployment.IMAGE_PATH + orderGoodsList.get(0).getItem_img_thumb();
            }
            ImageLoader.display(url, traceImageIv);

        }

        traceSupplierTv.setText(supplierName);
        traceImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) return;
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(traceInfoTopLl, 0, 0);
                }
            }
        });

        orderTraceAdapter = new OrderTraceAdapter(getActivity());
        orderTraceRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderTraceRv.setAdapter(orderTraceAdapter);

        if (orderPack.getTrack().getError_code() == 0) {
            List<TrackData> trackDataList = orderPack.getTrack().getResult().getData();
            if (!isNull) {
                TrackData trackData1 = new TrackData(ordertime, getResources().getString(R.string.order_commit_wait_pay));
                TrackData trackData2 = new TrackData(paytime, getResources().getString(R.string.pay_success_wait_goods));
                trackDataList.add(trackData2);
                trackDataList.add(trackData1);
            }
            orderTraceAdapter.setList(trackDataList);
        }

        initPop();
    }

    @OnClick(R.id.goods_details_tv)
    public void onClick() {
        if (flag){
            goodsIvRv.setVisibility(View.GONE);
            flag = false;
            goodsDetailsTv.setText(getResources().getString(R.string.shouqi));
        }else {
            goodsIvRv.setVisibility(View.VISIBLE);
            flag = true;
            goodsDetailsTv.setText(getResources().getString(R.string.chakan));
        }
    }

    private void initPop() {
        poprootview = LayoutInflater.from(getActivity()).inflate(R.layout.popup_ordertrack_item, null);
        popItemAdapter = new OrderTracePopItemAdapter();
        if (isNull) {
            popItemAdapter.setList(order.getOrder_items().get(0).getItem());
        } else {
            popItemAdapter.setList(orderGoodsList);
        }
        popItemRv = (EasyRecyclerView) poprootview.findViewById(R.id.order_trace_pop_rv);
        popItemRv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        popItemRv.setAdapter(popItemAdapter);
        popupWindow = new PopupWindow(poprootview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(poprootview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
    }

    private int getGoodsNum(List<GoodsOrder> orderList) {
        int num = 0;
        for (GoodsOrder goodsOrder : orderList) {
            num += Integer.parseInt(goodsOrder.getItem_num());
        }
        return num;
    }

   /* private List<> getGoods(List<GoodsOrder> orderList) {
        int num = 0;
        for (GoodsOrder goodsOrder : orderList) {
            num += Integer.parseInt(goodsOrder.getItem_num());
        }
        return num;
    }*/


    private int getGoodsNumByPack(GoodsOrder goodsOrder) {
        return Integer.parseInt(goodsOrder.getItem_num());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
