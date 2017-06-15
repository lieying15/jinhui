package com.thlh.jhmjmw.business.goods;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.R;
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
        ImageView goodsCartIv = viewHolder.findViewById(R.id.shop_goods_shopcart_iv);
        
        boolean setCartListener = true;
        goodsNameTv.setText(goods.getItem_name());
        ImageLoader.display(goods.getItem_img_thumb(),goodsIv);

        
        if(goods.getStorage()==0 || goods.getIs_prepare().equals("1")){
            goodsCartIv.setBackgroundResource(R.drawable.icon_shopcart_gray);
            setCartListener = false;
        }else {
            goodsCartIv.setBackgroundResource(R.drawable.icon_shopcart_wine);
            setCartListener = true;
        }
        
        if(goods.getIs_limit().equals("1")&&goods.getLimit_icon().equals("1")){
            tagIv.setVisibility(View.VISIBLE);
        }else {
            tagIv.setVisibility(View.GONE);
        }
        if(goods.getItem_id().equals("1")){
            goodsPriceTv.setVisibility(View.GONE);
            goodsCouponTv.setVisibility(View.VISIBLE);
            setCartListener = false;
        }else {
            goodsPriceTv.setVisibility(View.VISIBLE);
            goodsCouponTv.setVisibility(View.GONE);
            /*
            * R.string.money--》数字
            * ￥-->乱码
            * place：购物车---》商铺-----》某某公司
            * questions-->已解决
            * */
            //String dd= String.valueOf(R.string.money);
            goodsPriceTv.setText(context.getResources().getString(R.string.money)+goods.getItem_price());
        }
        
        if(setCartListener){
            goodsCartIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SupplierGoodsAdapter.this.listener != null){
                        int[] item_location = new int[2];
                        goodsIv.getLocationInWindow(item_location);//获取点击商品图片的位置
                        Drawable drawable = goodsIv.getDrawable();//复制一个新的商品图标
                        SupplierGoodsAdapter.this.listener.onClickBuy(view, position,item_location,drawable);
                    }
                }
            });
        }else {
            goodsCartIv.setOnClickListener(null);
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