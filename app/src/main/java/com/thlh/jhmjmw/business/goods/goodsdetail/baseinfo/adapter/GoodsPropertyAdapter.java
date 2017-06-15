package com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo.adapter;

import android.widget.TextView;

import com.thlh.baselib.model.GoodsDetailProperty;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsPropertyAdapter extends EasyRecyclerViewAdapter {


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goodsdetail_property
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        GoodsDetailProperty property = this.getItem(position);
        TextView keyTv = viewHolder.findViewById(R.id.goodsdetail_property_key);
        TextView valueTv = viewHolder.findViewById(R.id.goodsdetail_property_value);

        keyTv.setText(property.getName() + ":");
        valueTv.setText(property.getValue());
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}




