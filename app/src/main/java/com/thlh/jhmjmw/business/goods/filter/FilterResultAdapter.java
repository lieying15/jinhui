package com.thlh.jhmjmw.business.goods.filter;

import android.widget.TextView;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.Brand;
import com.thlh.baselib.model.FilterSupplier;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 筛选结果适配
 */
public class FilterResultAdapter extends EasyRecyclerViewAdapter{
    private  int type ;
    public FilterResultAdapter(int type) {
        this.type = type;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_filter_result
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        TextView searchIndexTv = viewHolder.findViewById(R.id.filter_result_tv);
        switch (type){
            case Constants.FILTER_TYPE_SUPPLIER:
                    FilterSupplier filterSupplier = (FilterSupplier)getItem(position);
                    searchIndexTv.setText(filterSupplier.getStore_name());
                break;
            case Constants.FILTER_TYPE_BRAND:
                    Brand brand = (Brand)getItem(position);
                    searchIndexTv.setText(brand.getBrand());
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}