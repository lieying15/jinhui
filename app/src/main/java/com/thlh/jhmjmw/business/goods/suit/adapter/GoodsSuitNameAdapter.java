package com.thlh.jhmjmw.business.goods.suit.adapter;

import android.content.Context;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsSuitNameAdapter extends EasyRecyclerViewAdapter {

    private Context context;
    public GoodsSuitNameAdapter(Context context){
        this.context=context;
    }
    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goodssuit_name
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        GoodsBundlingItem bundlingItem = this.getItem(position);
        TextView nameTv = viewHolder.findViewById(R.id.goodssuit_name_tv);
        String tempinfo = bundlingItem.getItem_name() + context.getResources().getString(R.string.multiplys) + bundlingItem.getItem_num();
        nameTv.setText(tempinfo);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}




