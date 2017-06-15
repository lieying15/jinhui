package com.thlh.jhmjmw.business.user.info.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.LogsOrder;
import com.thlh.baselib.utils.TimeUtils;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流消息适配器
 */
public class InfoTransportAdapter extends EasyRecyclerViewAdapter {
    private OnClickEvent listener;
    private Context context;
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public InfoTransportAdapter(Context context){
        this.context = context;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_info_transport
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        ImageView transportIv = (ImageView) viewHolder.findViewById(R.id.info_transport_goods_iv);
        TextView infoTimeTv = (TextView) viewHolder.findViewById(R.id.info_transport_time_tv);
        TextView infoStatusTv = (TextView) viewHolder.findViewById(R.id.info_transport_status_tv);
        TextView titleTv = (TextView) viewHolder.findViewById(R.id.info_transport_title_tv);
        TextView orderNoTv = (TextView) viewHolder.findViewById(R.id.info_transport_orderno_tv);
        TextView nameTV = (TextView) viewHolder.findViewById(R.id.info_transport_name_tv);


        LogsOrder logsOrder = getItem(position);
//        Glide.with(context)
//                .load(Deployment.IMAGE_PATH + logsOrder.get)
//                .into(transportIv);
        infoTimeTv.setText(TimeUtils.stringToDateString(logsOrder.getInputtime(),"yyyy年M月d日 HH:mm"));
        titleTv.setText(logsOrder.getMessage());
//        orderNoTv.setText("快递单号：" + logsOrder.getOrder_no());

//        infoStatusTv.setText(getStatus(logsOrder.getAction()));

        swipeRoot.setSwipeAble(true);
        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InfoTransportAdapter.this.listener != null){
                    InfoTransportAdapter.this.listener.onDelete(v,position);
                }
            }
        });


    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



    public void setOnClickEvent(OnClickEvent listener) {
        this.listener = listener;
    }



    public interface OnClickEvent {
        void onDelete(View view, int position);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }


    private String getStatus(String  action){
        switch (action){
            case "1":
                return "下单";
            case "2":
                return "付款";
            case "3":
                return "供应商确认订单";
            case "4":
                return "供应商发货";
            case "5":
                return "小店签收";
            case "6":
                return "小店发货";
            case "7":
                return "用户签收";
            case "8":
                return "取消订单";
        }
        return "下单";
    }
}