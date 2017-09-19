package com.thlh.jhmjmw.business.goods.suit.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsSuitAdapter extends EasyRecyclerViewAdapter {
    private ArrayList<Boolean > checkList = new ArrayList<>();

    private GoodsSuitGridItemAdapter itemGridAdapter;
    private OnClickListener listener;
    private Activity context;
    private EasyDividerItemDecoration dataDecoration;

    public GoodsSuitAdapter(Activity context, ArrayList<Boolean > checkList) {
        this.context = context;
        this.checkList = checkList;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goodssuit_group
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {

        final GoodsBundling goodsBundling = (GoodsBundling) this.getItem(position);

        LinearLayout topLl = viewHolder.findViewById(R.id.goodssuit_top_ll);
        ImageView titleSelectIv = viewHolder.findViewById(R.id.goodssuit_select_iv);
        TextView nameTv = viewHolder.findViewById(R.id.goodssuit_title_name_tv);
        TextView numTv = viewHolder.findViewById(R.id.goodssuit_num_tv);
        EasyRecyclerView itemRv = viewHolder.findViewById(R.id.goodssuit_item_rv);
        TextView finalpriceTv = viewHolder.findViewById(R.id.goodssuit_group_finalprice_tv);
        ImageView actionIv = viewHolder.findViewById(R.id.goodssuit_group_shopcart_iv);

        nameTv.setText(goodsBundling.getItem_name());
        List<GoodsBundlingItem> item = goodsBundling.getItem();
        numTv.setText(context.getResources().getString(R.string.gong) + item.size() + context.getResources().getString(R.string.ones));
        Spannable ariveSpan = new SpannableString(context.getResources().getString(R.string.group_price) + goodsBundling.getPrice());
        ariveSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.app_theme)), 4, ariveSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finalpriceTv.setText(ariveSpan);
        itemGridAdapter = new GoodsSuitGridItemAdapter(context);
        itemRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        itemRv.setAdapter(itemGridAdapter);
      /*  dataDecoration = new EasyDividerItemDecoration(
                context,
                EasyDividerItemDecoration.HORIZONTAL_LIST,
                R.drawable.divider_white
        );*/
//        itemRv.addItemDecoration(dataDecoration);
        itemGridAdapter.setList(goodsBundling.getItem());
        itemGridAdapter.notifyDataSetChanged();

        actionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.addShopCart(position);
                }
            }
        });
        titleSelectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCheck(position);
                }
            }
        });
        if(checkList.get(position)){
            titleSelectIv.setImageResource(R.drawable.icon_check_wine_select);
        }else {
            titleSelectIv.setImageResource(R.drawable.icon_check_wine);
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setEventListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void addShopCart(int position);
        void onCheck(int position);
    }

    private String getDiscountPrice( GoodsBundling goodsBundling ,String initprice){
        double init = Double.parseDouble(initprice);
        double price = Double.parseDouble(   goodsBundling.getPrice());
        DecimalFormat df = new DecimalFormat("###.0");
        return df.format(init - price);
    }

}

