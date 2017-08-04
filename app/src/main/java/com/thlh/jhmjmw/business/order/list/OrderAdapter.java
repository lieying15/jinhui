package com.thlh.jhmjmw.business.order.list;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.OrderItem;
import com.thlh.baselib.model.OrderPay;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.order.comment.OrderCommentActivity;
import com.thlh.jhmjmw.business.order.orderdetail.OrderDetailActivity;
import com.thlh.jhmjmw.business.order.trace.OrderTraceActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.OrderUtils;
import com.thlh.jhmjmw.view.DialogPhone;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表页适配
 * Created by HQ on 2016/7
 */
public class OrderAdapter extends EasyRecyclerViewAdapter {
    private Activity context;
    private DialogPhone.Builder phoneDialog ;
    private OnClickListener listener;
    private int cotent_type;

    public OrderAdapter(Activity context,int cotent_type){
        this.context = context;
        this.cotent_type = cotent_type;
        phoneDialog = new DialogPhone.Builder(context);
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_order
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final Order order = (Order)this.getItem(position);
        final String orderid = order.getOrder_id();
        final double shoudpay = order.getShould_pay();
        LinearLayout itmeLl = (LinearLayout) viewHolder.findViewById(R.id.order_item_ll);
        itmeLl.setMinimumWidth(BaseApplication.width);
        TextView storenameTv = (TextView) viewHolder.findViewById(R.id.order_item_storename_tv);
        TextView statesTv = (TextView) viewHolder.findViewById(R.id.order_item_states_tv);
        TextView goodsNumTv = (TextView) viewHolder.findViewById(R.id.order_goods_num_tv);
        TextView priceTitleTv = (TextView) viewHolder.findViewById(R.id.order_goods_price_title_tv);
        TextView priceTv = (TextView) viewHolder.findViewById(R.id.order_goods_price_tv);
        TextView actionLeftTv = (TextView) viewHolder.findViewById(R.id.order_goods_nextaction_left_tv);
        TextView actionRightTv = (TextView) viewHolder.findViewById(R.id.order_goods_nextaction_right_tv);
        TextView mjzTv = (TextView) viewHolder.findViewById(R.id.order_goods_mjz_tv);
        EasyRecyclerView goodsRv = (EasyRecyclerView) viewHolder.findViewById(R.id.order_item_rv);

        double finalprice = Double.parseDouble(order.getGoods_amount()) + Double.parseDouble(order.getExpress_fee()) ;
        int tempType = cotent_type;
        if(cotent_type == Constants.ORDER_TYPE_ALL ||cotent_type == Constants.ORDER_TYPE_UNCOMPLETE){
            tempType =  OrderUtils.getOrderStatus(order);
        }

        switch (tempType){
            case Constants.ORDER_TYPE_WAIT_PAY :
                /*
                * 状态*/
                statesTv.setText(context.getResources().getString(R.string.wait_pay));
                priceTitleTv.setText(context.getResources().getString(R.string.need_pay));
                mjzTv.setVisibility(View.VISIBLE);
                finalprice = order.getShould_pay();
                List<OrderPay> tempPaylist = order.getPay();
                if(null!=tempPaylist && tempPaylist.size()>0){
                    L.e("hahaha tempPaylist.size()"+tempPaylist.size() );
                    for (int i = 0; i <tempPaylist.size() ; i++) {
                        int methodid = Integer.parseInt(tempPaylist.get(i).getPayment_method_id());
                        double sum = Double.parseDouble(tempPaylist.get(i).getSum());
                        if(methodid>2){
                            L.e("OrderAdapter Paylist i:"+i +" method_id>2 id:" + methodid +" sum:"+sum);
                            finalprice = sum;
                            break;
                        }
                    }
                }

                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                actionLeftTv.setVisibility(View.VISIBLE);

                actionLeftTv.setText(context.getResources().getString(R.string.cannal_order));
                actionLeftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                            OrderAdapter.this.listener.cancel(orderid);
                        }
                    }
                });
                actionRightTv.setText(context.getResources().getString(R.string.shopcart_clearing));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                            OrderAdapter.this.listener.gotoPay(order);
                        }
                    }
                });
                break;
            case Constants.ORDER_TYPE_WAIT_SENDOUT :
                statesTv.setText(context.getResources().getString(R.string.wait_send));
                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                actionLeftTv.setVisibility(View.GONE);
                mjzTv.setVisibility(View.VISIBLE);
                actionRightTv.setText(context.getResources().getString(R.string.look));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderTraceActivity.activityStart(context,order);
                    }
                });
                priceTitleTv.setText(context.getResources().getString(R.string.zhen_pay));
                break;
            case Constants.ORDER_TYPE_WAIT_GAIN :
                statesTv.setText(context.getResources().getString(R.string.wait_goods));
                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                actionLeftTv.setText(context.getResources().getString(R.string.look));
                actionLeftTv.setVisibility(View.VISIBLE);
                mjzTv.setVisibility(View.VISIBLE);
                actionLeftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderTraceActivity.activityStart(context,order);
                    }
                });
                actionRightTv.setText(context.getResources().getString(R.string.true_goods));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                        OrderAdapter.this.listener.onConfiromGetGoods(orderid);
                        }
                    }
                });
                priceTitleTv.setText(context.getResources().getString(R.string.zhen_pay));
                break;
            case Constants.ORDER_TYPE_WAIT_COMMENT :
                statesTv.setText(context.getResources().getString(R.string.wait_evaluation));
                mjzTv.setVisibility(View.VISIBLE);
                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                if(order.getIs_comment().equals("0")){
                    actionLeftTv.setVisibility(View.VISIBLE);
                    actionLeftTv.setText(context.getResources().getString(R.string.shop_evaluation));
                    actionLeftTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OrderCommentActivity.activityStart(context);
                        }
                    });
                }else {
                    actionLeftTv.setVisibility(View.GONE);
                }
                actionRightTv.setText(context.getResources().getString(R.string.again_buy));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                            OrderAdapter.this.listener.onRebuy(position,order);
                        }
                    }
                });
                priceTitleTv.setText(context.getResources().getString(R.string.zhen_pay));
                break;
            case Constants.ORDER_TYPE_HAS_COMMENT :
                statesTv.setText(context.getResources().getString(R.string.shopcart_total_finish));
                mjzTv.setVisibility(View.VISIBLE);
                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                actionLeftTv.setVisibility(View.GONE);
                actionRightTv.setText(context.getResources().getString(R.string.again_buy));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                            OrderAdapter.this.listener.onRebuy(position,order);
                        }
                    }
                });
                priceTitleTv.setText(context.getResources().getString(R.string.zhen_pay));
                break;
            case  Constants.ORDER_TYPE_CANCEL:
                statesTv.setText(context.getResources().getString(R.string.cannal));
                mjzTv.setVisibility(View.GONE);
                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                actionLeftTv.setVisibility(View.GONE);
                actionRightTv.setText(context.getResources().getString(R.string.again_buy));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                            OrderAdapter.this.listener.onRebuy(position,order);

                        }
                    }
                });
                priceTitleTv.setText(context.getResources().getString(R.string.total_price));
//                priceTitleTv.setTextColor(context.getResources().getColor(R.color.text_tips));
                break;
            case Constants.ORDER_TYPE_COMPLETE :
                statesTv.setText(context.getResources().getString(R.string.finish));
                mjzTv.setVisibility(View.VISIBLE);
                storenameTv.setText(order.getOrder_items().get(0).getStore_name());
                if(order.getIs_comment().equals("0")){
                    actionLeftTv.setVisibility(View.VISIBLE);
                    actionLeftTv.setText(context.getResources().getString(R.string.goodsdetail_comment));
                    actionLeftTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OrderCommentActivity.activityStart(context);
                        }
                    });
                }else {
                    actionLeftTv.setVisibility(View.GONE);
                }
                actionRightTv.setText(context.getResources().getString(R.string.again_buy));
                actionRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OrderAdapter.this.listener != null) {
                            OrderAdapter.this.listener.onRebuy(position,order);
                        }
                    }
                });
                priceTitleTv.setText(context.getResources().getString(R.string.zhen_pay));
                break;
        }


        OrderItemsAdapter orderImgAdapter = new OrderItemsAdapter(context);
        List<OrderItem> orderItems = order.getOrder_items();
        List<GoodsOrder> goodsOrders = new ArrayList<>();
        double mjz = 0.0d;
        for(OrderItem orderItem :orderItems ){
            for(GoodsOrder goodsOrder : orderItem.getItem() ){
                goodsOrders.add(goodsOrder);
                String mjb_value = goodsOrder.getMjb_value();
                mjz = mjz + Double.parseDouble(mjb_value);

            }
        }

        orderImgAdapter.setList(goodsOrders);
        goodsRv.setLayoutManager( new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        goodsRv.setAdapter(orderImgAdapter);
        goodsRv.addItemDecoration(new HorizontaltemDecoration((int) context.getResources().getDimension(R.dimen.x10)));
        orderImgAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                OrderDetailActivity.activityStart(context,order);
            }
        });

        priceTv.setText(OrderUtils.getListTotalPrice(order,context));
        L.e("shouldpay===" + order.getShould_pay()  +"=====" + OrderUtils.getListTotalPrice(order,context));
        SpannableStringBuilder builder = new SpannableStringBuilder(context.getResources().getString(R.string.gong) +  goodsOrders.size() + context.getResources().getString(R.string.shops));
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.app_theme));
        builder.setSpan(redSpan, 1,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsNumTv.setText(builder);

        /**
         * 1,如果不支持金惠币显示   金惠币可抵 零元
         * 2，如果自己的金惠币不足，显示自己金惠币的数量
         * 3，如果自己金惠币大于需要抵消的。直接显示需要抵消的
         *
         */
        String user_mjb = (String) SPUtils.get("user_mjb", "0");
        double jhb = Double.parseDouble(user_mjb);

        if (order.getIs_pay().equals("0")){

        }else if (order.getIs_pay().equals("2")){

        }
        if (jhb > 0){
            if (jhb < mjz){
                mjzTv.setText(TextUtils.showHadMjz(context,String.valueOf(jhb),
                        (int)context.getResources().getDimension(R.dimen.icon_mjz_x),
                        (int)context.getResources().getDimension(R.dimen.icon_mjz_y)));
            }else{
                mjzTv.setText(TextUtils.showHadMjz(context,String.valueOf(mjz),
                        (int)context.getResources().getDimension(R.dimen.icon_mjz_x),
                        (int)context.getResources().getDimension(R.dimen.icon_mjz_y)));
            }
        }else {
            mjzTv.setText(TextUtils.showHadMjz(context,String.valueOf(0),
                    (int)context.getResources().getDimension(R.dimen.icon_mjz_x),
                    (int)context.getResources().getDimension(R.dimen.icon_mjz_y)));
        }

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setActionEvent(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onRebuy(int position,Order order);
        void onConfiromGetGoods( String  orderid);
        void gotoPay( Order order);
        void cancel(String orderid);
    }

    public void showPhoneDialog(String suppler_name,String suppler_tel){
        phoneDialog.setTitle(suppler_name + context.getResources().getString(R.string.Customer_service)).setContent(suppler_tel).setButtonListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Intent phoneIntent = new Intent();
                    phoneIntent.setAction(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + 51078668));
                    context.startActivity(phoneIntent);
                } catch (Exception e) {
                    L.e("call phone error", e.getLocalizedMessage());
                }
            }
        }).create().show();
    }







    public void setCotent_type(int cotent_type) {
        this.cotent_type = cotent_type;
    }
}