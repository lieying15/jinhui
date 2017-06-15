package com.thlh.jhmjmw.business.buy.buyconfirm.selectmjz;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.togglebutton.ToggleButton;

/**
 * 确认支付页-》选择美家币支付页
 */
public class PayMjbAdapter extends EasyRecyclerViewAdapter {

    private OnClickListener listener;

    private Context context;
    public PayMjbAdapter(Context context){
        this.context=context;
    }
    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_paymjb
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        Cartgoods cartgoods = (Cartgoods)this.getItem(position);
        final GoodsDb goods = cartgoods.getGoodsdb();

        ImageView goodsIv = viewHolder.findViewById(R.id.paymjb_goods_iv);
        TextView goodsName = viewHolder.findViewById(R.id.paymjb_goods_name_tv);
        ToggleButton payTbn = viewHolder.findViewById(R.id.paymjb_goods_tbn);
        TextView priceTv = viewHolder.findViewById(R.id.paymjb_goods_price_tv);
        TextView mjbPrice = viewHolder.findViewById(R.id.paymjb_goods_mjbprice_tv);

        ImageLoader.display(goods.getItem_img_thumb(),goodsIv);
        goodsName.setText(goods.getItem_name());
        String priceStr = cartgoods.getGoodsdb().getItem_price();
        /*
        * ￥
        * */
        priceTv.setText(context.getResources().getString(R.string.money)+priceStr + context.getResources().getString(R.string.multiplys) +cartgoods.getGoods_num());

        if(cartgoods.getIsPayMjb()){
            payTbn.setToggleOn();
        }else {
            payTbn.setToggleOff();
        }
        String ismjb = goods.getIs_mjb();
        if(ismjb.equals("0")){
            mjbPrice.setText(context.getResources().getString(R.string.shop_no_mjz));
            mjbPrice.setBackgroundResource(R.drawable.bg_null);
            payTbn.setVisibility(View.GONE);
        }else {
            payTbn.setVisibility(View.VISIBLE);
            String mjzStr ;
            mjzStr = goods.getItem_price();
            if(ismjb.equals("2"))
                mjzStr = cartgoods.getGoodsdb().getMjb_value();
            mjbPrice.setText(context.getResources().getString(R.string.one_yes) + mjzStr+ context.getResources().getString(R.string.ch_mjz));
        }

        payTbn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(listener != null ){
                    listener.changeBotton(on,position);
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
        void changeBotton(boolean on, int position);
    }


}