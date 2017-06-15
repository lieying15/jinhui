package com.thlh.jhmjmw.business.goods.filter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.Brand;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * Created by HQ on 2016/3/30.
 */
public class FilterBrandAdapter extends EasyRecyclerViewAdapter   implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private List<Boolean> checkStates ;

    public FilterBrandAdapter(List<Boolean> checkStates){
        this.checkStates = checkStates;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_filter
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        Brand brand = (Brand)this.getItem(position);

        TextView filterTv = viewHolder.findViewById(R.id.filter_item_tv);
        LinearLayout filterLl = viewHolder.findViewById(R.id.filter_item_ll);
//        View matchview = viewHolder.findViewById(R.id.filter_item_ll);
        filterLl.setMinimumWidth(BaseApplication.width);
        filterTv.setText(brand.getBrand());
        if(checkStates.get(position)){
            filterLl.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(R.color.app_mainback));
        }else {
            filterLl.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public long getHeaderId(int position) {
        return ((Brand)getItem(position)).getSortLetters().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fliter_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(((Brand)getItem(position)).getSortLetters());
        holder.itemView.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(R.color.app_mainback));
    }

}