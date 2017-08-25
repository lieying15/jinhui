package com.thlh.jhmjmw.business.buy.shopcart.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.CartSupplierCheck;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;

import java.util.List;
import java.util.Map;

/**
 * 购物车列表
 */
public class ShopcartAdapter extends EasyRecyclerViewAdapter {
    private int contentType  = 0;
    private final  int  CONTENT_TYPE_NORMAL = 0;
    private final  int  CONTENT_TYPE_SUIT = 1;

    private OnClickListener listener;
    private List<CartSupplierCheck> cartSupplierCheckList; //数据选则状态
    private Activity context;
    public ShopcartItemAdapter itemAdapter;
    private ShopcarSuitAdapter shopcartSuitAdapter;
    private Map<String,String> itemStatusMap;

    public ShopcartAdapter(Activity context, List<CartSupplierCheck> cartSupplierCheckList, Map<String,String> itemStatusMap){
        this.context = context;
        this.cartSupplierCheckList = cartSupplierCheckList;
        this.itemStatusMap = itemStatusMap;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_shopcart, R.layout.item_shopcart_suit_top
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final CartSupplier cartSupplier = (CartSupplier)this.getItem(position);
        if(contentType == CONTENT_TYPE_NORMAL){
            FrameLayout shopcartCbFl = (FrameLayout)viewHolder.findViewById(R.id.shopcart_supplier_cbfl);
            ImageView shopcartCb = viewHolder.findViewById(R.id.shopcart_supplier_cb);
            TextView shopcartNameTv = viewHolder.findViewById(R.id.shopcart_supplier_name_tv);
            EasyRecyclerView shopcartRv = viewHolder.findViewById(R.id.shopcart_goods_rv);

            if(cartSupplierCheckList.get(position)==null){
                notifyDataSetChanged();
                return;
            }
            L.e("dingdansahgndian===" + cartSupplier.getCartgoods().get(0).getGoodsdb().getSupplier().getStore_name());
            L.e("dingdansahgndian===" + cartSupplier.getCartgoods().get(0).getGoodsdb().getSupplier().getName());
            if(cartSupplier.getCartgoods()!=null &&cartSupplier.getCartgoods().size()>0){
                if(cartSupplier.getCartgoods().get(0).getGoodsdb().getSupplier().getStore_name()== null
                        || cartSupplier.getCartgoods().get(0).getGoodsdb().getSupplier().getStore_name().equals("")){
                    shopcartNameTv.setText(cartSupplier.getCartgoods().get(0).getGoodsdb().getSupplier().getName());
                }else {
                    shopcartNameTv.setText(cartSupplier.getCartgoods().get(0).getGoodsdb().getSupplier().getStore_name());
                }
            }

            //选择全部供应商下产品
            shopcartCbFl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ShopcartAdapter.this.listener != null) {
//                        boolean select = cartSupplierCheckList.get(position).getSelect();
//                        cartSupplierCheckList.get(position).setSelect(!select);
//                        for (int i = 0; i < cartSupplierCheckList.get(position).getGoodsCheckStates().size(); i++) {
//                            cartSupplierCheckList.get(position).getGoodsCheckStates().set(i,!select);
//                        }
//                        DbManager.getInstance().setSupplierGoodsSelect(cartSupplier.getSupplier_id(),!select);
                        ShopcartAdapter.this.listener.onClickSelectSupplier(position);
//                        notifyDataSetChanged();
                    }
                }
            });


            if(cartSupplierCheckList.get(position).getSelect()){
                shopcartCb.setImageResource(R.drawable.icon_check_wine_select);
            }else {
                shopcartCb.setImageResource(R.drawable.icon_check_wine);
            }

            itemAdapter = new ShopcartItemAdapter(context,cartSupplier.getCartgoods(), cartSupplierCheckList.get(position).getGoodsCheckStates(),itemStatusMap);

            shopcartRv.setLayoutManager(new LinearLayoutManager(context));

            itemAdapter.setList(cartSupplier.getCartgoods());
            itemAdapter.notifyDataSetChanged();
            itemAdapter.setOnClickEvent(new ShopcartItemAdapter.OnClickListener() {
                @Override
                public void onClickSelect(int itemposition) {
                    if (ShopcartAdapter.this.listener != null){
                        ShopcartAdapter.this.listener.onClickSelectGoods(position,itemposition);
                    }
                }

                @Override
                public void onClickAdd(View view, int itemposition) {
                    if (ShopcartAdapter.this.listener != null)
                        ShopcartAdapter.this.listener.onClickAdd(view, position,itemposition);
                }

                @Override
                public void onClickSub(View view, int itemposition) {
                    if (ShopcartAdapter.this.listener != null)
                        ShopcartAdapter.this.listener.onClickSub(view, position,itemposition);
                }

                @Override
                public void onClickDelete(View view, int itemposition) {
                    if (ShopcartAdapter.this.listener != null)
                        ShopcartAdapter.this.listener.onClickDelete(view, position,itemposition);
                }
            });

            itemAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
                @Override
                public void onItemClick(View convertView, int itemposition) {
                    String tempid =((Cartgoods)cartSupplier.getCartgoods().get(itemposition)).getGoodsdb().getItem_id();
                    GoodsDetailV3Activity.activityStart(context, tempid);
                }
            });
            shopcartRv.setAdapter(itemAdapter);
        }else {
            EasyRecyclerView suitTopRv = viewHolder.findViewById(R.id.shopcart_suit_top_rv);
            shopcartSuitAdapter = new ShopcarSuitAdapter(context, cartSupplierCheckList.get(position).getGoodsCheckStates(),itemStatusMap);
            shopcartSuitAdapter.setList(cartSupplier.getCartgoods());
            shopcartSuitAdapter.setOnClickEvent(new ShopcarSuitAdapter.OnClickListener() {
                @Override
                public void onClickSelect(int itemposition) {
                    if (ShopcartAdapter.this.listener != null){
                        ShopcartAdapter.this.listener.onClickSelectGoods(position,itemposition);
                    }
                }

                @Override
                public void onClickAdd(View view, int itemposition) {
                    if (ShopcartAdapter.this.listener != null)
                        ShopcartAdapter.this.listener.onClickAdd(view, position,itemposition);
                }

                @Override
                public void onClickSub(View view, int itemposition) {
                    if (ShopcartAdapter.this.listener != null)
                        ShopcartAdapter.this.listener.onClickSub(view, position,itemposition);
                }

                @Override
                public void onClickDelete(View view, int itemposition) {
                    if (ShopcartAdapter.this.listener != null)
                        ShopcartAdapter.this.listener.onClickDelete(view, position,itemposition);
                }
            });
            suitTopRv.setLayoutManager(new LinearLayoutManager(context));
            suitTopRv.setAdapter(shopcartSuitAdapter);
            suitTopRv.addItemDecoration(new VerticalltemDecoration((int) context.getResources().getDimension(R.dimen.y10)));
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        CartSupplier cartSupplier = (CartSupplier)this.getItem(position);
        if(cartSupplier.getSupplier_id().equals("-1")){
            contentType  = CONTENT_TYPE_SUIT;
        }else {
            contentType  = CONTENT_TYPE_NORMAL;

        }
        return contentType;

    }


    public void setOnClickEvent(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onClickSelectSupplier(int position);
        void onClickSelectGoods(int position, int itemposition);
        void onClickAdd(View view, int position,int itemposition);
        void onClickSub(View view, int position,int itemposition);
        void onClickDelete(View view, int position,int itemposition);
    }




}