package com.thlh.jhmjmw.business.buy.shopcart.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class ShopcartSuitItemAdapter extends EasyRecyclerViewAdapter {

    private Context context;
    private int  num;
    private String itemStatus = "normal";
    private String url;

    public ShopcartSuitItemAdapter(Context context,int num){
        this.context = context;
        this.num = num;
    }

    public ShopcartSuitItemAdapter(Context context,int num,String itemStatus){
        this.context = context;
        this.num = num;
        this.itemStatus = itemStatus;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_shopcart_suit_item
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        GoodsBundlingItem cartgoods = (GoodsBundlingItem)this.getItem(position);

        ImageView shopcartIv  = viewHolder.findViewById(R.id.shopcart_iv);
        ImageView shopcartBackIv  = viewHolder.findViewById(R.id.shopcart_back_iv);
        TextView shopcartStatusTv = viewHolder.findViewById(R.id.shopcart_status_tv);
        TextView shopcartNameTv = viewHolder.findViewById(R.id.shopcart_name_tv);
        TextView shopcartPriceTv = viewHolder.findViewById(R.id.shopcart_price_tv);

        TextView shopcartNumTv = viewHolder.findViewById(R.id.shopcart_num_tv);
        switch (itemStatus){
            case "onShelves" :
                shopcartBackIv.setVisibility(View.VISIBLE);
                shopcartStatusTv.setVisibility(View.VISIBLE);
                shopcartStatusTv.setText(context.getResources().getString(R.string.shelves));
                break;
            case "storage" :
                shopcartBackIv.setVisibility(View.VISIBLE);
                shopcartStatusTv.setVisibility(View.VISIBLE);
                shopcartStatusTv.setText(context.getResources().getString(R.string.gone));
                break;
            default: shopcartBackIv.setVisibility(View.GONE);
                shopcartStatusTv.setVisibility(View.GONE);
                shopcartStatusTv.setText("");
                break;
        }
        if (cartgoods.getItem_img_thumb().contains("http")){
            url = cartgoods.getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + cartgoods.getItem_img_thumb();
        }
        ImageLoader.displayRoundImg(url,shopcartIv);
        shopcartNameTv.setText(cartgoods.getItem_name());
//        shopcartPriceTv.setText(cartgoods.getItem_price());
        shopcartNumTv.setText( cartgoods.getItem_num()+context.getResources().getString(R.string.one) + num);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}