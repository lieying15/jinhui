package com.thlh.jhmjmw.business.goods.suit.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsSuitListItemAdapter extends EasyRecyclerViewAdapter {


    private Context context;

    public GoodsSuitListItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goodssuit_listitem
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {

        GoodsBundlingItem goodsitem = (GoodsBundlingItem) this.getItem(position);
        ImageView goodsIv = viewHolder.findViewById(R.id.goodssuit_goods_iv);
        ImageView itemArrowIv = viewHolder.findViewById(R.id.goodssuit_item_arrow_iv);
        TextView goodsNameTv = viewHolder.findViewById(R.id.goodssuit_goods_name_tv);
        ImageLoader.display(goodsitem.getItem_img_thumb(),goodsIv);
        goodsNameTv.setText(goodsitem.getItem_name());

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}