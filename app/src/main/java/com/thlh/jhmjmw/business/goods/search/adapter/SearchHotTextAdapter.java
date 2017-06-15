package com.thlh.jhmjmw.business.goods.search.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class SearchHotTextAdapter extends EasyRecyclerViewAdapter {

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_search_goods_hot_search
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        String index = (String)this.getItem(position);
        LinearLayout textLl = viewHolder.findViewById(R.id.searchgoods_item_text_ll);
//        textLl.setMinimumHeight(R.dimen.x150);
        TextView searchIndexTv = viewHolder.findViewById(R.id.searchgoods_item_text_tv);
        searchIndexTv.setText(index);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }




}