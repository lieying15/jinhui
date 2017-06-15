package com.thlh.jhmjmw.business.goods.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsNormalGridAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private OnClickListener listener;
    public GoodsNormalGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_grid_normal_goods
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {

        Goods goods = (Goods)this.getItem(position);
        ImageView cartIv = viewHolder.findViewById(R.id.normal_goods_cart_iv);
        ImageView tagIv = viewHolder.findViewById(R.id.normal_goods_tag_iv);
        TextView cartTv = viewHolder.findViewById(R.id.normal_goods_cart_tv);
        ImageView goodsIv = viewHolder.findViewById(R.id.normal_goods_grid_iv);
        TextView goodsNameTv = viewHolder.findViewById(R.id.normal_goods_grid_name_tv);
        TextView goodsPriceTv = viewHolder.findViewById(R.id.normal_goods_grid_price_tv);
        TextView goodsCouponTv = viewHolder.findViewById(R.id.normal_goods_grid_coupon_tv);

        goodsNameTv.setText(goods.getItem_name());
        ImageLoader.display(goods.getItem_img_thumb(),goodsIv);


        if(goods.getStorage()==0 || goods.getIs_prepare().equals("1")) {
            cartIv.setBackgroundResource(R.drawable.icon_shopcart_gray);
            cartIv.setOnClickListener(null);
        }else {
            cartIv.setBackgroundResource(R.drawable.icon_shopcart_wine);
            cartIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onClickCart(position);
                    }
                }
            });
        }
        if(goods.getIs_limit().equals("1")&&goods.getLimit_icon().equals("1")){
            tagIv.setVisibility(View.VISIBLE);
        }else {
            tagIv.setVisibility(View.GONE);
        }

        cartTv.setVisibility(View.GONE);
        cartIv.setVisibility(View.VISIBLE);

        if(goods.getItem_id().equals("1")){
            goodsPriceTv.setVisibility(View.GONE);
            cartIv.setVisibility(View.GONE);
            goodsCouponTv.setVisibility(View.VISIBLE);
        }else {
            goodsPriceTv.setVisibility(View.VISIBLE);
            cartIv.setVisibility(View.VISIBLE);
            goodsCouponTv.setVisibility(View.GONE);
            /*
            * R.string.money
            * ￥
            * questions----->
            * place---》no
            * */
            goodsPriceTv.setText(context.getResources().getString(R.string.money)+goods.getItem_price());
        }

        if(goods.getIs_prepare().equals("1")){
            cartTv.setVisibility(View.VISIBLE);
            cartIv.setVisibility(View.GONE);
            cartTv.setText(context.getResources().getString(R.string.stock));
            cartIv.setOnClickListener(null);
            return;
        }

        if(goods.getIs_shelves().equals("0")){
            cartTv.setVisibility(View.VISIBLE);
            cartIv.setVisibility(View.GONE);
            cartTv.setText(context.getResources().getString(R.string.shelves));
            cartIv.setOnClickListener(null);
            return;
        }

        if(goods.getStorage()==0){
            cartTv.setVisibility(View.VISIBLE);
            cartIv.setVisibility(View.GONE);
            cartTv.setText(context.getResources().getString(R.string.gone));
            cartIv.setOnClickListener(null);
            return;
        }

        cartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onClickCart(position);
                }
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setOnClickEvent(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onClickCart(int position);
    }

}