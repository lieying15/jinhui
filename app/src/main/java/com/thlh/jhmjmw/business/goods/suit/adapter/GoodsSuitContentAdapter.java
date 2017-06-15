package com.thlh.jhmjmw.business.goods.suit.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 套装内容适配
 */
public class GoodsSuitContentAdapter extends EasyRecyclerViewAdapter {


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_choiceness
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        GoodsBundlingItem bundlingItem = getItem(position);
        ImageView choiceIv = viewHolder.findViewById(R.id.item_choiceness_title_iv);
        LinearLayout choicLL = viewHolder.findViewById(R.id.item_choiceness_topll);
        choicLL.setMinimumWidth(BaseApplication.width);
        choiceIv.setMinimumHeight(BaseApplication.width *2/5);

        ImageLoader.display(bundlingItem.getItem_img(),choiceIv);
        TextView titleTv = viewHolder.findViewById(R.id.item_choiceness_title_tv);
        titleTv.setText(bundlingItem.getItem_name());
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}