package com.thlh.jhmjmw.business.user.footprint;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.Seengood;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HQ on 2016/3/30.
 */
public class FootprintAdapter extends EasyRecyclerViewAdapter {
    private OnClickEvent listener;
    private List<Seengood> goodsList ;
    private Context context;
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public FootprintAdapter(Context context, List<Seengood> goodsList){
        this.context = context;
        this.goodsList = goodsList;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_collect_goods
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        GoodsDb goods = goodsList.get(position).getGoodsdb();
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        ImageView baseIv = (ImageView) viewHolder.findViewById(R.id.item_goods_base_iv);
        ImageView baseTagIv = (ImageView) viewHolder.findViewById(R.id.item_goods_base_tag_iv);
        ImageView baseStatusBgIv = (ImageView) viewHolder.findViewById(R.id.item_goods_base_statubg_iv);
        TextView baseStatusTv = (TextView) viewHolder.findViewById(R.id.item_goods_base_status_tv);
        TextView baseNameTv = (TextView) viewHolder.findViewById(R.id.item_goods_base_name_tv);
        TextView basePriceTv = (TextView) viewHolder.findViewById(R.id.item_goods_base_price_tv);
        TextView baseMjzTv = (TextView) viewHolder.findViewById(R.id.item_goods_base_mjz_tv);

        baseTagIv.setVisibility(View.GONE);
        baseStatusTv.setVisibility(View.GONE);
        baseStatusBgIv.setVisibility(View.GONE);
        baseMjzTv.setVisibility(View.GONE);

        baseNameTv.setText(goods.getItem_name());
        ImageLoader.displayRoundImg(goods.getItem_img_thumb(), baseIv);
        String priceStr = goods.getItem_price();
        /*
        * R.string.money_--->不可用
        * ¥
        * questions----->已解决
        * 历史足迹
        * */
        basePriceTv.setText(context.getResources().getString(R.string.money_)+ TextUtils.showPrice(priceStr));

        if (goods.getItem_id().equals("1")) {
            basePriceTv.setText(context.getResources().getString(R.string.ice_voucher));
            baseMjzTv.setVisibility(View.GONE);
        }else {
            String ismjb = goods.getIs_mjb();
            if(ismjb.equals("0")){
                baseMjzTv.setVisibility(View.GONE);
            }else {
                baseMjzTv.setVisibility(View.VISIBLE);
                String mjzStr = priceStr;
                if(ismjb.equals("2"))
                    mjzStr = goods.getMjb_value();
                baseMjzTv.setText(TextUtils.showMjz(context,mjzStr));
            }
        }

        if (goods.getIs_limit().equals("1") && goods.getLimit_icon().equals("1")) {
            baseTagIv.setVisibility(View.VISIBLE);
        } else {
            baseTagIv.setVisibility(View.GONE);
        }

        if (goods.getIs_prepare().equals("1")) {
            baseStatusTv.setVisibility(View.VISIBLE);
            baseStatusBgIv.setVisibility(View.VISIBLE);
            baseStatusTv.setText(context.getResources().getString(R.string.stock));
        }

        if (goods.getIs_shelves().equals("0")) {
            baseStatusTv.setVisibility(View.VISIBLE);
            baseStatusBgIv.setVisibility(View.VISIBLE);
            baseStatusTv.setText(context.getResources().getString(R.string.shelves));
        }

        if (goods.getStorage() == 0) {
            baseStatusTv.setVisibility(View.VISIBLE);
            baseStatusBgIv.setVisibility(View.VISIBLE);
            baseStatusTv.setText(context.getResources().getString(R.string.gone));
        }


        swipeRoot.setSwipeAble(true);
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

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FootprintAdapter.this.listener != null){
                    FootprintAdapter.this.listener.onDelete(v,position);
                }
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



    public void setOnClickEvent(OnClickEvent listener) {
        this.listener = listener;
    }



    public interface OnClickEvent {
        void onDelete(View view, int position);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }
}