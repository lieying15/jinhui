package com.thlh.jhmjmw.business.buy.buyconfirm.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.adapter.ShopcartSuitItemAdapter;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物清单 套装适配.
 */
public class BuyConfirmListSuitAdapter extends EasyRecyclerViewAdapter {
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    private Activity context;

    private ShopcartSuitItemAdapter suitItemAdapter;

    public BuyConfirmListSuitAdapter(Activity context){
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_orderconfirm_suit
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final Cartgoods cartgoods = (Cartgoods)this.getItem(position);
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        TextView suitNameTv = viewHolder.findViewById(R.id.shopcart_suit_name_tv);
        TextView suitPriceTv = viewHolder.findViewById(R.id.shopcart_suit_price_tv);
        TextView suitNumTv = viewHolder.findViewById(R.id.shopcart_suit_num_tv);
        //解析套装数据
        GsonBuilder builder  =new GsonBuilder();
        Gson gson = builder.create();
        String bundinginfo = cartgoods.getBunding_info();
        final GoodsBundling goodsBundling =gson.fromJson(bundinginfo, GoodsBundling.class);
        /*
        * R.string.money
        * ￥
        *
        * */
        suitPriceTv.setText(context.getResources().getString(R.string.money) + goodsBundling.getPrice());
        suitNumTv.setText(cartgoods.getGoods_num()+"");

        EasyRecyclerView suitItemRv = viewHolder.findViewById(R.id.shopcart_suit_item_rv);
        suitItemAdapter = new ShopcartSuitItemAdapter(context,cartgoods.getGoods_num());
        suitItemRv.setLayoutManager(new LinearLayoutManager(context));
        suitItemAdapter.setList(goodsBundling.getItem());
//        suitItemAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
//            @Override
//            public void onItemClick(View convertView, int suitposition) {
//                String tempid =  goodsBundling.getItem().get(suitposition).getItem_id();
//                GoodsDetailV3Activity.activityStart(context, tempid);
//            }
//        });


        swipeRoot.setSwipeAble(false);

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

        suitItemRv.setAdapter(suitItemAdapter);
        suitItemRv.addItemDecoration(new EasyDividerItemDecoration(
                context,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        ));
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