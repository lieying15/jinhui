package com.thlh.jhmjmw.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 商品列表基本适配
 */
public abstract class BaseGoodsLIstAdapter extends EasyRecyclerViewAdapter {

    public Context context;
    private boolean showCart = false;
    private String url;

    protected abstract void onViewHolder(EasyRecyclerViewHolder viewHolder, final int position);

    public BaseGoodsLIstAdapter(Context context) {
        this.context = context;
    }

    public BaseGoodsLIstAdapter(Context context, boolean showCart) {
        this.context = context;
        this.showCart = showCart;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goods_base
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        Goods goods = (Goods) this.getItem(position);


        ImageView baseIv = viewHolder.findViewById(R.id.item_goods_base_iv);
        ImageView baseTagIv = viewHolder.findViewById(R.id.item_goods_base_tag_iv);
        ImageView baseStatusBgIv = viewHolder.findViewById(R.id.item_goods_base_statubg_iv);
        TextView baseNameTv = viewHolder.findViewById(R.id.item_goods_base_name_tv);
        TextView baseStatusTv = viewHolder.findViewById(R.id.item_goods_base_status_tv);
        TextView basePriceTv = viewHolder.findViewById(R.id.item_goods_base_price_tv);
        TextView baseMjzTv = viewHolder.findViewById(R.id.item_goods_base_mjz_tv);
        TextView baseNumTv = viewHolder.findViewById(R.id.item_goods_base_num_tv);

        FrameLayout basecartFl = viewHolder.findViewById(R.id.item_goods_base_cart_fl);
        ImageView baseCartIv = viewHolder.findViewById(R.id.item_goods_base_cart_iv);
        baseCartIv.setImageResource(R.drawable.icon_homepage_cart);


        baseTagIv.setVisibility(View.GONE);
        baseStatusTv.setVisibility(View.GONE);
        baseStatusBgIv.setVisibility(View.GONE);
        baseMjzTv.setVisibility(View.GONE);

        baseNameTv.setText(goods.getItem_name());
        if (goods.getItem_img_thumb().contains("http")){
            url = goods.getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + goods.getItem_img_thumb();
        }
        ImageLoader.displayRoundImg(url, baseIv);
        String priceStr = goods.getItem_price();

        /*
        *
        * 筛选页
        * R.string.money_
        * */
        basePriceTv.setText(context.getResources().getString(R.string.money_) + TextUtils.showPrice(priceStr));


        String ismjb = goods.getIs_mjb();
        if (ismjb.equals("0")) {
            baseMjzTv.setVisibility(View.GONE);
        } else {
            baseMjzTv.setVisibility(View.VISIBLE);
            String mjzStr = priceStr;
            if (ismjb.equals("2"))
                mjzStr = goods.getMjb_value();
            baseMjzTv.setText(TextUtils.showMjz(context, mjzStr));
        }

        if (goods.getIs_limit().equals("1") && goods.getLimit_icon().equals("1")) {
            baseTagIv.setVisibility(View.VISIBLE);
        } else {
            baseTagIv.setVisibility(View.GONE);
        }

        if (goods.getIs_prepare().equals("1")) {
            baseStatusTv.setVisibility(View.VISIBLE);
            baseStatusBgIv.setVisibility(View.VISIBLE);
            baseCartIv.setImageResource(R.drawable.icon_shopcart_gray);
            baseStatusTv.setText(context.getResources().getString(R.string.stock));
        }

        if (goods.getIs_shelves().equals("0")) {
            baseStatusTv.setVisibility(View.VISIBLE);
            baseStatusBgIv.setVisibility(View.VISIBLE);
            baseCartIv.setImageResource(R.drawable.icon_shopcart_gray);
            baseStatusTv.setText(context.getResources().getString(R.string.shelves));
        }

        if (goods.getStorage() == 0) {
            baseStatusTv.setVisibility(View.VISIBLE);
            baseStatusBgIv.setVisibility(View.VISIBLE);
            baseCartIv.setImageResource(R.drawable.icon_shopcart_gray);
            baseStatusTv.setText(context.getResources().getString(R.string.gone));
        }

        if (showCart) {
            basecartFl.setVisibility(View.VISIBLE);
        } else {
            basecartFl.setVisibility(View.GONE);
        }

        onViewHolder(viewHolder, position);

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public boolean isShowCart() {
        return showCart;
    }

    public void setShowCart(boolean showCart) {
        this.showCart = showCart;
    }
}