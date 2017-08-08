package com.thlh.jhmjmw.business.order.trace;

import android.widget.ImageView;

import com.thlh.baselib.model.GoodsOrder;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 */
public class OrderTracePopItemAdapter extends EasyRecyclerViewAdapter {

    private String url;

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

        if (goods.getItem_img_thumb().contains("http")){
            url = goods.getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + goods.getItem_img_thumb();
        }
        ImageLoader.display(url,orderConfirmGoodsIv);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}