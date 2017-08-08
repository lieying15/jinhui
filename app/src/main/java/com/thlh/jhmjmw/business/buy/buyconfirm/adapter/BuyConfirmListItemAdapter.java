package com.thlh.jhmjmw.business.buy.buyconfirm.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品清单
 */
public class BuyConfirmListItemAdapter extends EasyRecyclerViewAdapter {

    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    private Activity context;
    private String url;

    public BuyConfirmListItemAdapter(Activity context){
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_orderconfirm_normal
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final Cartgoods cartgoods = (Cartgoods)this.getItem(position);
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        ImageView shopcartIv  = viewHolder.findViewById(R.id.shopcart_iv);
        TextView shopcartNameTv = viewHolder.findViewById(R.id.shopcart_name_tv);
        TextView shopcartPriceTv = viewHolder.findViewById(R.id.shopcart_price_tv);
        TextView shopcartMjzTv = viewHolder.findViewById(R.id.shopcart_mjz_tv);
        TextView shopcartNumTv = viewHolder.findViewById(R.id.shopcart_num_tv);

        if (cartgoods.getGoodsdb().getItem_img_thumb().contains("http")){
            url = cartgoods.getGoodsdb().getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + cartgoods.getGoodsdb().getItem_img_thumb();
        }
        ImageLoader.display(url,shopcartIv);

        swipeRoot.setSwipeAble(false);
        shopcartNameTv.setText(cartgoods.getGoodsdb().getItem_name());
        shopcartNumTv.setText(context.getResources().getString(R.string.multiplys) + cartgoods.getGoods_num());
        String priceStr = cartgoods.getGoodsdb().getItem_price();

        /*
        * R.string.money_
        *  ¥
        *
        */
        shopcartPriceTv.setText(context.getResources().getString(R.string.money_) + TextUtils.showPrice(priceStr));

        String isMjz = cartgoods.getGoodsdb().getIs_mjb();
        if(isMjz.equals("0")){
            shopcartMjzTv.setVisibility(View.GONE);
        }else {
            shopcartMjzTv.setVisibility(View.VISIBLE);
            String mjzStr = priceStr;
            if(isMjz.equals("2"))
                mjzStr = cartgoods.getGoodsdb().getMjb_value();
            shopcartMjzTv.setText(TextUtils.showMjz(context,mjzStr));
        }

        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }


}