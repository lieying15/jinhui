package com.thlh.jhmjmw.business.goods;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 供应商所有物品的适配 grid
 */
public class SupplierGoodsAdapter extends EasyRecyclerViewAdapter {
    private OnClickListener listener;

    private Context context;
    public SupplierGoodsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_grid_shop_goods
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {

        Goods goods = (Goods)this.getItem(position);
        final ImageView goodsIv = viewHolder.findViewById(R.id.shop_goods_grid_iv);
        ImageView tagIv = viewHolder.findViewById(R.id.shop_goods_tag_iv);
        TextView goodsNameTv = viewHolder.findViewById(R.id.shop_goods_grid_name_tv);
        TextView goodsPriceTv = viewHolder.findViewById(R.id.shop_goods_grid_price_tv);
        TextView goodsCouponTv = viewHolder.findViewById(R.id.shop_goods_grid_coupon_tv);
        TextView goodsmjzTv = viewHolder.findViewById(R.id.shop_goods_grid_mjz_tv);

        boolean setCartListener = true;
        goodsNameTv.setText(goods.getItem_name());
        ImageLoader.display(goods.getItem_img_thumb(),goodsIv);


        if(goods.getIs_limit().equals("1")&&goods.getLimit_icon().equals("1")){
            tagIv.setVisibility(View.VISIBLE);
        }else {
            tagIv.setVisibility(View.GONE);
        }


        if(goods.getItem_id().equals("1")){
            goodsmjzTv.setVisibility(View.GONE);
            goodsCouponTv.setVisibility(View.GONE);
        }else {
            goodsPriceTv.setVisibility(View.VISIBLE);
            goodsmjzTv.setVisibility(View.VISIBLE);
            goodsCouponTv.setVisibility(View.GONE);
            goodsPriceTv.setText(context.getResources().getString(R.string.money)+goods.getItem_price());
            goodsmjzTv.setText(TextUtils.showMjz(context,goods.getMjb_value()));
        }

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



    public void setBuyListener(OnClickListener listener) {
        this.listener = listener;
    }



    public interface OnClickListener {
        void onClickBuy(View view, int position, int[] item_location, Drawable drawable);
    }


}