package com.thlh.jhmjmw.business.order.trace;

import android.widget.ImageView;

import com.thlh.baselib.model.GoodsOrder;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 */
public class OrderTracePopItemAdapter extends EasyRecyclerViewAdapter {

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_order_track_popitem
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        GoodsOrder goods = (GoodsOrder)this.getItem(position);
        ImageView orderConfirmGoodsIv = (ImageView) viewHolder.findViewById(R.id.order_track_popgoods_iv);
        ImageLoader.display(goods.getItem_img_thumb(),orderConfirmGoodsIv);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}