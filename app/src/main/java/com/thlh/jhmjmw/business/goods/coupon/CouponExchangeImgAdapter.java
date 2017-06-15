package com.thlh.jhmjmw.business.goods.coupon;

import android.content.Context;
import android.widget.ImageView;

import com.thlh.baselib.model.Goods;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 兑换券图片适配
 */
public class CouponExchangeImgAdapter extends EasyRecyclerViewAdapter {
    private Context context;

    public CouponExchangeImgAdapter(Context context){
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_buy_confirm_img
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        Goods goods = (Goods)this.getItem(position);
        ImageView orderConfirmGoodsIv = (ImageView) viewHolder.findViewById(R.id.order_confirm_goods_iv);

        orderConfirmGoodsIv.setBackgroundResource(R.drawable.img_fridge);
//        ImageLoader.display(goods.getItem_img_thumb(),orderConfirmGoodsIv);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}