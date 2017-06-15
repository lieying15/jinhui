package com.thlh.jhmjmw.business.goods.suit.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsSuitAdapter extends EasyRecyclerViewAdapter {
    private ArrayList<Boolean > checkList = new ArrayList<>();

    private GoodsSuitListItemAdapter itemListAdapter;
    private GoodsSuitGridItemAdapter itemGridAdapter;
    private OnClickListener listener;
    private Activity context;

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
        TextView titlePriceTv = viewHolder.findViewById(R.id.goodssuit_title_price_tv);
        TextView nameTv = viewHolder.findViewById(R.id.goodssuit_title_name_tv);
        ImageView titleSelectIv = viewHolder.findViewById(R.id.goodssuit_select_iv);
        ImageView titleArrowIv = viewHolder.findViewById(R.id.goodssuit_title_arrow_iv);
        EasyRecyclerView itemRv = viewHolder.findViewById(R.id.goodssuit_item_rv);
        itemRv.setMinimumWidth(BaseApplication.width);
        LinearLayout shopcartLl = viewHolder.findViewById(R.id.goodssuit_shopcart_ll);

        TextView initPriceTv = viewHolder.findViewById(R.id.goodssuit_group_initprice_tv);
        TextView discountPriceTv = viewHolder.findViewById(R.id.goodssuit_group_discountprice_tv);
        TextView finalpriceTv = viewHolder.findViewById(R.id.goodssuit_group_finalprice_tv);

        TextView actionTv = viewHolder.findViewById(R.id.goodssuit_group_shopcart_tv);

        topLl.setMinimumWidth(BaseApplication.width);
        nameTv.setText(goodsBundling.getItem_name());
        /*
        * ￥
        * R.string.money
        * */
        titlePriceTv.setText(context.getResources().getString(R.string.money) + goodsBundling.getItem_price());

        String initprice =  getInitPrice(goodsBundling);
        initPriceTv.setText(context.getResources().getString(R.string.mjmw_price) + initprice);
        initPriceTv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);

        discountPriceTv.setText(context.getResources().getString(R.string.sheng) + getDiscountPrice( goodsBundling,initprice));

        Spannable ariveSpan = new SpannableString(context.getResources().getString(R.string.group_price) + goodsBundling.getItem_price());
        ariveSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.app_theme)), 4, ariveSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finalpriceTv.setText(ariveSpan);

        itemListAdapter = new GoodsSuitListItemAdapter(context);
//        itemListAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
//            @Override
//            public void onItemClick(View convertView, int position) {
//                String goodsid = ((GoodsBundlingItem)itemListAdapter.getItem(position)).getItem_id();
//                GoodsDetailV3Activity.activityStart(context,goodsid);
//            }
//        });
        itemListAdapter.setList(goodsBundling.getItem());

        itemGridAdapter = new GoodsSuitGridItemAdapter(context);
        itemGridAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position0) {
//                String goodsid = ((GoodsBundlingItem)itemListAdapter.getItem(position)).getItem_id();
//                GoodsDetailV3Activity.activityStart(context,goodsid);
                GoodsSuitDetailActivity.activityStart(context,goodsBundling.getItem_id(), position);
            }
        });
        itemGridAdapter.setList(goodsBundling.getItem());

        itemRv.setAdapter(itemListAdapter);
        itemRv.setLayoutManager(new LinearLayoutManager(context));

        titleArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GoodsSuitAdapter.this.listener != null) {
                    GoodsSuitAdapter.this.listener.onTitleArrow(position);

                }
            }
        });

        actionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoodsSuitAdapter.this.listener != null) {
                    GoodsSuitAdapter.this.listener.addShopcart(position);

                }
            }
        });
        titleSelectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoodsSuitAdapter.this.listener != null) {
                    GoodsSuitAdapter.this.listener.onCheck(position);

                }
            }
        });
        if(checkList.get(position)){
            titleSelectIv.setImageResource(R.drawable.icon_check_select_black);
        }else {
            titleSelectIv.setImageResource(R.drawable.icon_check_black);
        }
//        if(checkList.get(position)){
//            shopcartLl.setVisibility(View.VISIBLE);
//            titleArrowIv.setImageResource(R.drawable.icon_circle_up_arrow_gray);
//            itemRv.setAdapter(itemListAdapter);
//            itemRv.setLayoutManager(new LinearLayoutManager(context));
//        }else {
            shopcartLl.setVisibility(View.GONE);
            itemRv.setAdapter(itemGridAdapter);
            titleArrowIv.setImageResource(R.drawable.icon_circle_down_arrow);
            itemRv.setLayoutManager(new GridLayoutManager(context,4));
//        }



    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }


    public void setEventListener(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void addShopcart(int position);
        void onCheck( int position);
        void onTitleArrow( int position);
    }

    private String getInitPrice( GoodsBundling goodsBundling){
        double initprice = 0;
        for(GoodsBundlingItem bundlingItem: goodsBundling.getItem()){
            initprice += Double.parseDouble(bundlingItem.getInit_price());
        }
        DecimalFormat df = new DecimalFormat("###.0");
        return df.format(initprice);
    }

    private String getDiscountPrice( GoodsBundling goodsBundling ,String initprice){
        double init = Double.parseDouble(initprice);
        double price = Double.parseDouble(   goodsBundling.getItem_price());
        DecimalFormat df = new DecimalFormat("###.0");
        return df.format(init - price);
    }

}

