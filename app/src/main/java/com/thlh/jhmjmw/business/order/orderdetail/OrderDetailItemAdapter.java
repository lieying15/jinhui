package com.thlh.jhmjmw.business.order.orderdetail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 订单详情中物品列表
 */
public class OrderDetailItemAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private OnClickEvent listener;
    private boolean showReturn;

    public OrderDetailItemAdapter(Context context, boolean showReturn) {
        this.context = context;
        this.showReturn = showReturn;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_order_detail_item
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, int position) {
        GoodsOrder goods = (GoodsOrder) this.getItem(position);

//        Goods goods = (Goods) this.getItem(position);

        ImageView baseIv = viewHolder.findViewById(R.id.item_goods_base_iv);
        ImageView baseTagIv = viewHolder.findViewById(R.id.item_goods_base_tag_iv);
        ImageView baseStatusBgIv = viewHolder.findViewById(R.id.item_goods_base_statubg_iv);
        TextView baseNameTv = viewHolder.findViewById(R.id.item_goods_base_name_tv);
        TextView baseStatusTv = viewHolder.findViewById(R.id.item_goods_base_status_tv);
        TextView basePriceTv = viewHolder.findViewById(R.id.item_goods_base_price_tv);
        TextView baseMjzTv = viewHolder.findViewById(R.id.item_goods_base_mjz_tv);

        baseTagIv.setVisibility(View.GONE);
        baseStatusTv.setVisibility(View.GONE);
        baseStatusBgIv.setVisibility(View.GONE);
        baseMjzTv.setVisibility(View.GONE);

        baseNameTv.setText(goods.getItem_name());
        ImageLoader.displayRoundImg(goods.getItem_img_thumb(), baseIv);
        String priceStr = goods.getItem_price();

        /*
        * R.string.money_
        * ¥
        * */
        basePriceTv.setText(context.getResources().getString(R.string.money_) + TextUtils.showPrice(priceStr));

        String ismjb = goods.getIs_mjb();
        if (ismjb.equals("0")) {
            baseMjzTv.setVisibility(View.GONE);
        } else {
            baseMjzTv.setVisibility(View.VISIBLE);
            String mjzStr = priceStr;
            if (ismjb.equals("2"))
                mjzStr = goods.getMjb_value();
            baseMjzTv.setText(TextUtils.showMjz(context, mjzStr));
        }

        if (goods.getIs_limit().equals("1") && goods.getLimit_icon().equals("1")) {
            baseTagIv.setVisibility(View.VISIBLE);
        } else {
            baseTagIv.setVisibility(View.GONE);
        }

//        if (goods.getIs_prepare().equals("1")) {
//            baseStatusTv.setVisibility(View.VISIBLE);
//            baseStatusBgIv.setVisibility(View.VISIBLE);
//            baseStatusTv.setText("备货中");
//        }
//
//        if (goods.getIs_shelves().equals("0")) {
//            baseStatusTv.setVisibility(View.VISIBLE);
//            baseStatusBgIv.setVisibility(View.VISIBLE);
//            baseStatusTv.setText("已下架");
//        }
//
//        if (goods.getStorage() == 0) {
//            baseStatusTv.setVisibility(View.VISIBLE);
//            baseStatusBgIv.setVisibility(View.VISIBLE);
//            baseStatusTv.setText("已抢光");
//        }

        TextView goodsNumTv = (TextView) viewHolder.findViewById(R.id.order_goods_num_tv);
        goodsNumTv.setText(context.getResources().getString(R.string.multiplys) + goods.getItem_num());
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }


    public void setOnClickEvent(OnClickEvent listener) {
        this.listener = listener;
    }


    public interface OnClickEvent {
        void onReturn(int position);
    }
}