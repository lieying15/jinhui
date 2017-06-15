package com.thlh.jhmjmw.business.goods.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.Seengood;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class SearchSeenAdapter extends EasyRecyclerViewAdapter {
;    private Context context;

    public SearchSeenAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_search_goods_hot_sale
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        Seengood seengood = (Seengood)this.getItem(position);
        LinearLayout goodsLL = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_ll);
        ImageView goodsIv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_iv);
        TextView goodsNameTv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_name_tv);
        TextView goodsPriceTv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_price_tv);
        ImageView tagIv = viewHolder.findViewById(R.id.searchgoods_tag_iv);
        goodsNameTv.setText(seengood.getGoodsdb().getItem_name());
        TextView goodsMjzTv = viewHolder.findViewById(R.id.searchgoods_hotsale_goods_mjz_tv);

        GoodsDb goods = seengood.getGoodsdb();
        goodsLL.setMinimumWidth(R.dimen.x240);
        goodsNameTv.setText(goods.getItem_name());

        if(goods.getItem_id().equals("1")){
            goodsPriceTv.setText(context.getResources().getString(R.string.ice_voucher_use));
            goodsMjzTv.setVisibility(View.GONE);
        }else {
            String priceStr = goods.getItem_price();
             /*
            *R.string.money
            * ¥
            * place--->搜索--->看过的商品
            * questions--->已解决
             */
            goodsPriceTv.setText(context.getResources().getString(R.string.money)+priceStr);
            if(goods.getIs_mjb().equals("0")){
                goodsMjzTv.setVisibility(View.GONE);
            }else {
                goodsMjzTv.setVisibility(View.VISIBLE);
                String mjzStr = goods.getItem_price();
                if(goods.getIs_mjb().equals("2"))
                    mjzStr = goods.getMjb_value();
                goodsMjzTv.setText(TextUtils.showMjz(context,mjzStr));
            }
        }
        
        ImageLoader.display(seengood.getGoodsdb().getItem_img_thumb(),goodsIv);
        if(seengood.getGoodsdb().getIs_limit().equals("1")&&seengood.getGoodsdb().getLimit_icon().equals("1")){
            tagIv.setVisibility(View.VISIBLE);
        }else {
            tagIv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }




}