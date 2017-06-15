package com.thlh.jhmjmw.business.order.orderdetail;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.model.OrderItem;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

/**
 * Created by HQ on 2016/3/30.
 */
public class OrderDetailAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private OrderDetailItemAdapter itemAdapter;
    private OrderDetailItemAdapter.OnClickEvent listener;
    private boolean showReturn;

    public OrderDetailAdapter(Context context,boolean showReturn) {
        this.context = context;
        this.showReturn = showReturn;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_order_detail
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        final OrderItem orderItem = (OrderItem)this.getItem(position);

        TextView supplierNameTv = (TextView) viewHolder.findViewById(R.id.order_detail_supliername_tv);
        EasyRecyclerView orderDetailRv = (EasyRecyclerView) viewHolder.findViewById(R.id.order_detail_rv);

        orderDetailRv.setLayoutManager(new LinearLayoutManager(context));
        itemAdapter = new  OrderDetailItemAdapter(context,showReturn);
        itemAdapter.setList(orderItem.getItem());

        itemAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int itemposition) {
                GoodsDetailV3Activity.activityStart(context, orderItem.getItem().get(itemposition).getItem_id());
            }
        });
        itemAdapter.setOnClickEvent(new OrderDetailItemAdapter.OnClickEvent() {
            @Override
            public void onReturn(int position) {
                if(OrderDetailAdapter.this.listener != null) {
                    OrderDetailAdapter.this.listener.onReturn(position);
                }
            }
        });
        orderDetailRv.setAdapter(itemAdapter);
        supplierNameTv.setText(orderItem.getStore_name());

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setOnClickEvent(OrderDetailItemAdapter.OnClickEvent listener) {
        this.listener = listener;
    }



    public interface OnClickEvent {
        void onReturn(int position);
    }

    public void setShowReturn(boolean showReturn) {
        this.showReturn = showReturn;
        if(itemAdapter != null){
            itemAdapter.notifyDataSetChanged();
        }
    }

}