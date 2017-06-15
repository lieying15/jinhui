package com.thlh.jhmjmw.business.goods.suit.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsSuitGridItemAdapter extends EasyRecyclerViewAdapter {


    private Context context;

    public GoodsSuitGridItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goodssuit_griditem
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {

        GoodsBundlingItem goodsitem = (GoodsBundlingItem) this.getItem(position);

        ImageView goodsIv = viewHolder.findViewById(R.id.goodssuit_goods_iv);

//        Glide.with(context)
//                .load(Deployment.IMAGE_PATH + goodsitem.getItem_img_thumb())
//                .into( goodsIv);
        ImageLoader.display(goodsitem.getItem_img_thumb(),goodsIv);


    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}