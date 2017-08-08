package com.thlh.jhmjmw.business.buy.buyconfirm.express;

import android.widget.ImageView;

import com.thlh.baselib.model.ExpressItem;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class BuyExpressImgAdapter extends EasyRecyclerViewAdapter {


    private String url;

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_buy_confirm_img
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        ExpressItem goods = (ExpressItem)this.getItem(position);
        ImageView orderConfirmGoodsIv = (ImageView) viewHolder.findViewById(R.id.order_confirm_goods_iv);

        if (goods.getItem_img_thumb().contains("http")){
            url = goods.getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + goods.getItem_img_thumb();
        }
        ImageLoader.displayRoundImg(url,orderConfirmGoodsIv);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}