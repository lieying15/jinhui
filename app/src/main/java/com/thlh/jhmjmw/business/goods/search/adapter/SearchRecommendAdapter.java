package com.thlh.jhmjmw.business.goods.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class SearchRecommendAdapter extends EasyRecyclerViewAdapter {
    ;
    private Context context;

    public SearchRecommendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_search_goods_hot_sale
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        Goods goods = (Goods) this.getItem(position);
        LinearLayout goodsLL = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_ll);
        ImageView goodsIv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_iv);
        ImageView tagIv = viewHolder.findViewById(R.id.searchgoods_tag_iv);
        TextView goodsNameTv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_name_tv);
        TextView goodsPriceTv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_price_tv);
        TextView goodsMjzTv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_mjz_tv);

        goodsLL.setMinimumWidth(R.dimen.x240);
        goodsNameTv.setText(goods.getItem_name());

        String priceStr = goods.getItem_price();

        goodsPriceTv.setText(context.getResources().getString(R.string.money_) + priceStr);
        if (goods.getIs_mjb().equals("0")) {
            goodsMjzTv.setVisibility(View.GONE);
        } else {
            goodsMjzTv.setVisibility(View.VISIBLE);
            String mjzStr = goods.getItem_price();
            if (goods.getIs_mjb().equals("2"))
                mjzStr = goods.getMjb_value();
            goodsMjzTv.setText(TextUtils.showMjz(context, mjzStr));
        }


        ImageLoader.display(goods.getItem_img_thumb(), goodsIv);
        if (goods.getIs_limit().equals("1") && goods.getLimit_icon().equals("1")) {
            tagIv.setVisibility(View.VISIBLE);
        } else {
            tagIv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }


}