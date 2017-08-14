package com.thlh.jhmjmw.business.buy.shopcart.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
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
import java.util.Map;

/**
 * Created by HQ on 2016/3/30.
 */
public class ShopcartItemAdapter extends EasyRecyclerViewAdapter {

    private OnClickListener listener;
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    private List<Boolean> goodsCheckStates;
    private List<Cartgoods> cartgoods;
    private Activity context;
    private Map<String,String> itemStatusMap;
    private String url;


    public ShopcartItemAdapter(Activity context, List<Cartgoods> cartgoods, List<Boolean> goodsCheckStates,Map<String,String> itemStatusMap){
        this.context = context;
        this.cartgoods = cartgoods;
        this.goodsCheckStates = goodsCheckStates;
        this.itemStatusMap = itemStatusMap;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_shopcart_swip
        };
    }


    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final Cartgoods cartgoods = (Cartgoods)this.getItem(position);
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        LinearLayout numLl = (LinearLayout) viewHolder.findViewById(R.id.shopcart_num_ll);
        FrameLayout shopcartCbFl = (FrameLayout)viewHolder.findViewById(R.id.shopcart_item_cbfl);
        ImageView shopcartCb = viewHolder.findViewById(R.id.shopcart_cb);
        ImageView shopcartIv  = viewHolder.findViewById(R.id.shopcart_iv);
        ImageView tagIv  = viewHolder.findViewById(R.id.shopcart_tag_iv);
        ImageView shopcartBackIv  = viewHolder.findViewById(R.id.shopcart_back_iv);
        TextView shopcartStatusTv = viewHolder.findViewById(R.id.shopcart_status_tv);
        TextView shopcartNameTv = viewHolder.findViewById(R.id.shopcart_name_tv);
        TextView shopcartPriceTv = viewHolder.findViewById(R.id.shopcart_price_tv);
        TextView mjzTv = viewHolder.findViewById(R.id.shopcart_mjz_tv);
        FrameLayout shopcartSubFl = viewHolder.findViewById(R.id.shopcart_sub_fl);
        FrameLayout shopcartAddFl = viewHolder.findViewById(R.id.shopcart_add_fl);
        TextView shopcartNumTv = viewHolder.findViewById(R.id.shopcart_num_tv);

        if (cartgoods.getGoodsdb().getItem_img_thumb().contains("http")){
            url = cartgoods.getGoodsdb().getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + cartgoods.getGoodsdb().getItem_img_thumb();
        }
        ImageLoader.displayRoundImg(url,shopcartIv);

        shopcartNameTv.setText(cartgoods.getGoodsdb().getItem_name());
        String ismjb = cartgoods.getGoodsdb().getIs_mjb();

        String priceStr = cartgoods.getGoodsdb().getItem_price();
        /*
        * R.string.money_
        * ¥
        *购物车列表
        * */
        shopcartPriceTv.setText(context.getResources().getString(R.string.money_) + TextUtils.showPrice(priceStr));

        if(ismjb.equals("0")){
            mjzTv.setVisibility(View.GONE);
        }else {
            mjzTv.setVisibility(View.VISIBLE);
            String mjzStr = priceStr;
            if (ismjb.equals("2")){
                mjzStr = cartgoods.getGoodsdb().getMjb_value();
            }
            mjzTv.setText(TextUtils.showMjz(context, mjzStr));
        }

        if(cartgoods.getGoodsdb().getIs_limit().equals("1")&&cartgoods.getGoodsdb().getLimit_icon().equals("1")){
            tagIv.setVisibility(View.VISIBLE);
        }else {
            tagIv.setVisibility(View.GONE);
        }
        if(itemStatusMap.get(cartgoods.getGoodsdb().getItem_id())!=null){
            shopcartCbFl.setVisibility(View.INVISIBLE);
            shopcartSubFl.setVisibility(View.INVISIBLE);
            shopcartAddFl.setVisibility(View.INVISIBLE);
            shopcartNumTv.setVisibility(View.INVISIBLE);
            shopcartCbFl.setClickable(false);
            String itemStatus = itemStatusMap.get(cartgoods.getGoodsdb().getItem_id());
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
        }else {
            shopcartCbFl.setVisibility(View.VISIBLE);
            shopcartSubFl.setVisibility(View.VISIBLE);
            shopcartAddFl.setVisibility(View.VISIBLE);
            shopcartNumTv.setVisibility(View.VISIBLE);
            shopcartNumTv.setText(cartgoods.getGoods_num()+"");
        }

        shopcartCbFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShopcartItemAdapter.this.listener != null) {
                    ShopcartItemAdapter.this.listener.onClickSelect(position);
                }
            }
        });

        if(goodsCheckStates.get(position)){
            shopcartCb.setImageResource(R.drawable.icon_check_wine_select);
        }else {
            shopcartCb.setImageResource(R.drawable.icon_check_wine);
        }

        shopcartAddFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShopcartItemAdapter.this.listener != null)
                    ShopcartItemAdapter.this.listener.onClickAdd(v, position);
            }
        });

        shopcartSubFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShopcartItemAdapter.this.listener != null)
                    ShopcartItemAdapter.this.listener.onClickSub(v, position);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShopcartItemAdapter.this.listener != null){
                    ShopcartItemAdapter.this.listener.onClickDelete(v,position);
                }
            }
        });


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


    public void setOnClickEvent(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onClickSelect(int position);
        void onClickAdd(View view, int position);
        void onClickSub(View view, int position);
        void onClickDelete(View view, int position);
    }


    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }


}