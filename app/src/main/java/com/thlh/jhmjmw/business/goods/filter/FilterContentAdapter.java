package com.thlh.jhmjmw.business.goods.filter;

import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.Brand;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

import java.util.List;

/**
 * 筛选适配
 */
public class FilterContentAdapter extends EasyRecyclerViewAdapter{
    private  int type ;
    private List<Boolean>  checks;
    private OnClickListener listener;

    public FilterContentAdapter(int type,List<Boolean>  checks ) {
        this.type = type;
        this.checks = checks;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_filter_content
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        TextView searchIndexTv = viewHolder.findViewById(R.id.filter_content_tv);
        searchIndexTv.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.text_tips));
        Brand brand = (Brand)getItem(position);
        searchIndexTv.setText(brand.getBrand());
        searchIndexTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FilterContentAdapter.this.listener != null){
                    FilterContentAdapter.this.listener.onClickSelect(v, position);
                }
            }
        });
        if(checks.get(position)){
            searchIndexTv.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.wine_light));
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return  mList.size();
    }

    public void setEventListener(OnClickListener listener) {
        this.listener = listener;
    }



    public interface OnClickListener {
        void onClickMore(View view, int position);
        void onClickSelect(View view, int position);
    }
}