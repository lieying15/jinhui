package com.thlh.jhmjmw.business.buy.buyconfirm.list;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.thlh.baselib.model.CartSupplier;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.buyconfirm.adapter.BuyConfirmListItemAdapter;
import com.thlh.jhmjmw.business.buy.buyconfirm.adapter.BuyConfirmListSuitAdapter;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

/**
 *商品清单适配器
 */
public class BuyConfirmListAdapter extends EasyRecyclerViewAdapter {
    private int contentType  = 0;
    private final  int  CONTENT_TYPE_NORMAL = 0;
    private final  int  CONTENT_TYPE_SUIT = 1;

    private Activity context;
    public BuyConfirmListItemAdapter itemAdapter;
    private BuyConfirmListSuitAdapter shopcartSuitAdapter;

    public BuyConfirmListAdapter(Activity context){
        this.context = context;
    }



    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_orderconfirm_list, R.layout.item_shopcart_suit_top
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final CartSupplier cartgoodsmap = (CartSupplier)this.getItem(position);
        if(contentType == CONTENT_TYPE_NORMAL){
            TextView shopcartNameTv = viewHolder.findViewById(R.id.shopcart_supplier_name_tv);
            EasyRecyclerView shopcartRv = viewHolder.findViewById(R.id.shopcart_goods_rv);


            if(cartgoodsmap.getCartgoods()!=null &&cartgoodsmap.getCartgoods().size()>0){
                if(cartgoodsmap.getCartgoods().get(0).getGoodsdb().getSupplier().getStore_name().equals("")){
                    shopcartNameTv.setText(cartgoodsmap.getCartgoods().get(0).getGoodsdb().getSupplier().getName());
                }else {
                    shopcartNameTv.setText(cartgoodsmap.getCartgoods().get(0).getGoodsdb().getSupplier().getStore_name());
                }
            }


            itemAdapter = new BuyConfirmListItemAdapter(context);

            shopcartRv.setLayoutManager(new LinearLayoutManager(context));
            itemAdapter.setList(cartgoodsmap.getCartgoods());

//            itemAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
//                @Override
//                public void onItemClick(View convertView, int itemposition) {
//                    String tempid =((Cartgoods)cartgoodsmap.getCartgoods().get(itemposition)).getGoods_id();
//                    GoodsDetailV3Activity.activityStart(context, tempid);
//                }
//            });
            shopcartRv.setAdapter(itemAdapter);
        }else {
            EasyRecyclerView suitTopRv = viewHolder.findViewById(R.id.shopcart_suit_top_rv);
            shopcartSuitAdapter = new BuyConfirmListSuitAdapter(context);
            shopcartSuitAdapter.setList(cartgoodsmap.getCartgoods());

            suitTopRv.setLayoutManager(new LinearLayoutManager(context));
            suitTopRv.setAdapter(shopcartSuitAdapter);
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        CartSupplier cartgoodsmap = (CartSupplier)this.getItem(position);
        if(cartgoodsmap.getSupplier_id().equals("-1")){
            contentType  = CONTENT_TYPE_SUIT;
        }else {
            contentType  = CONTENT_TYPE_NORMAL;

        }
        return contentType;

    }




}