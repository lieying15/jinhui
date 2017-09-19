package com.thlh.jhmjmw.business.buy.shopcart.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车 套装适配.
 */

public class ShopcarSuitAdapter extends EasyRecyclerViewAdapter {
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    private OnClickListener listener;
    private List<Boolean> checkStates;
    private Activity context;
    private boolean showCb = true;

    private ShopcartSuitItemAdapter suitItemAdapter;
    private Map<String,String> itemStatusMap;
    private String itemStatus = "normal";

    public ShopcarSuitAdapter(Activity context,  List<Boolean> checkStates,Map<String,String> itemStatusMap){
        this.context = context;
        this.checkStates = checkStates;
        this.itemStatusMap = itemStatusMap;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_shopcart_suit
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final Cartgoods cartgoods = (Cartgoods)this.getItem(position);
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        LinearLayout addandsubLl = (LinearLayout) viewHolder.findViewById(R.id.shopcart_addandsub_ll);

        ImageView suitCb = viewHolder.findViewById(R.id.shopcart_suit_cb);
        TextView suitNameTv = viewHolder.findViewById(R.id.shopcart_suit_name_tv);
        TextView suitPriceTv = viewHolder.findViewById(R.id.shopcart_suit_price_tv);
        TextView suitMjzTv = viewHolder.findViewById(R.id.shopcart_suit_mjz_tv);
        FrameLayout suitSubFl = viewHolder.findViewById(R.id.shopcart_suit_sub_fl);
        FrameLayout suitAddFl = viewHolder.findViewById(R.id.shopcart_suit_add_fl);
        TextView suitNumTv = viewHolder.findViewById(R.id.shopcart_suit_num_tv);

        //解析套装数据
        GsonBuilder builder  =new GsonBuilder();
        Gson gson = builder.create();
        String bundinginfo = cartgoods.getBunding_info();
        final GoodsBundling goodsBundling =gson.fromJson(bundinginfo, GoodsBundling.class);

        int adapterheight = DisplayUtil.dp2px(context,70 + 125 * goodsBundling.getItem().size() );
        mDelete.setMinimumHeight(adapterheight);

        StringBuilder tempGoodsname = new StringBuilder();
        for(GoodsBundlingItem bundlingItem:goodsBundling.getItem()){
            tempGoodsname.append(bundlingItem.getItem_name()+" ");
        }

        suitNameTv.setText(tempGoodsname.toString());
        /*
        *￥
        * R.string.money
        * */
        suitPriceTv.setText(context.getResources().getString(R.string.money) + goodsBundling.getPrice());

        suitNumTv.setText(cartgoods.getGoods_num()+"");

        EasyRecyclerView suitItemRv = viewHolder.findViewById(R.id.shopcart_suit_item_rv);
        if(itemStatusMap.get(cartgoods.getGoodsdb().getItem_id())!=null){
            suitCb.setVisibility(View.INVISIBLE);
            addandsubLl.setVisibility(View.INVISIBLE);
            suitCb.setClickable(false);
            itemStatus = itemStatusMap.get(cartgoods.getGoodsdb().getItem_id());
        }else {
            suitCb.setVisibility(View.VISIBLE);
            addandsubLl.setVisibility(View.VISIBLE);
            swipeRoot.setSwipeAble(true);
        }

        suitItemAdapter = new ShopcartSuitItemAdapter(context,cartgoods.getGoods_num(),itemStatus);
        suitItemRv.setLayoutManager(new LinearLayoutManager(context));
        suitItemAdapter.setList(goodsBundling.getItem());
        suitItemAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int suitposition) {
                String tempid =  goodsBundling.getItem().get(suitposition).getItem_id();
                GoodsDetailV3Activity.activityStart(context, tempid);
            }
        });
        suitItemRv.setAdapter(suitItemAdapter);
        EasyDividerItemDecoration decoration = new EasyDividerItemDecoration(context,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback);
        decoration.bottomDivider = true;
        suitItemRv.addItemDecoration(decoration,1);
        suitCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShopcarSuitAdapter.this.listener != null) {
                    boolean select = checkStates.get(position);
                    checkStates.set(position, !select);
                    DbManager.getInstance().setGoodsSelect(cartgoods.getGoodsdb(),!select);
                    ShopcarSuitAdapter.this.listener.onClickSelect(position);
                    notifyDataSetChanged();
                }
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShopcarSuitAdapter.this.listener != null){
                    ShopcarSuitAdapter.this.listener.onClickDelete(v,position);
                }
            }
        });

        if(checkStates.get(position)){
            suitCb.setImageResource(R.drawable.icon_check_wine_select);
        }else {
            suitCb.setImageResource(R.drawable.icon_check_wine);
        }

        suitAddFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShopcarSuitAdapter.this.listener != null)
                    ShopcarSuitAdapter.this.listener.onClickAdd(v, position);
            }
        });

        suitSubFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShopcarSuitAdapter.this.listener != null)
                    ShopcarSuitAdapter.this.listener.onClickSub(v, position);
            }
        });

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