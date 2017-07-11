package com.thlh.jhmjmw.business.buy.buyconfirm.express;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.ExpressSupplier;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;

import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * 配送信息适配器
 */
public class BuyExpressListAdapter extends EasyRecyclerViewAdapter {

    private Activity context;
    private BuyExpressImgAdapter imgAdapter;
    private List<String> freeList = new ArrayList<>();
    private boolean flag = false;

    public BuyExpressListAdapter(Activity context){
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_expressinfo_list
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        
        ExpressSupplier expressSupplier = (ExpressSupplier)this.getItem(position);

        ImageView supplierGoodsIv = viewHolder.findViewById(R.id.express_goods_iv);
        TextView supplierNameTv = viewHolder.findViewById(R.id.express_name_tv);
        TextView freeTv = viewHolder.findViewById(R.id.express_free_tv);
        TextView infoTv = viewHolder.findViewById(R.id.express_info_tv);
        EasyRecyclerView expressRv = viewHolder.findViewById(R.id.express_goods_rv);
        TextView goodsNumTv = viewHolder.findViewById(R.id.express_goods_num_tv);
        TextView goodsDetailsBt = viewHolder.findViewById(R.id.express_goods_details_tv);
        View view = viewHolder.findViewById(R.id.view);
        Glide.with(context)
                .load(Deployment.IMAGE_PATH + expressSupplier.getItem().get(0).getItem_img_thumb())
                .into(supplierGoodsIv);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.app_theme));
        ForegroundColorSpan graySpan = new ForegroundColorSpan(context.getResources().getColor(R.color.text_tips));

        SpannableStringBuilder supplierName = new SpannableStringBuilder(context.getResources().getString(R.string.store) + expressSupplier.getStore_name());
        supplierName.setSpan(graySpan,0,3,SPAN_EXCLUSIVE_EXCLUSIVE);
        supplierNameTv.setText(supplierName);
        SpannableStringBuilder info = new SpannableStringBuilder(context.getResources().getString(R.string.you) + expressSupplier.getStore_name() + context.getResources().getString(R.string.distribution));
        info.setSpan(graySpan,0,3,SPAN_EXCLUSIVE_EXCLUSIVE);
        infoTv.setText(info);
        int size = expressSupplier.getItem().size();
        goodsNumTv.setText(context.getResources().getText(R.string.gong) + String.valueOf(size) + context.getResources().getText(R.string.ones));
        if (size>1){
            goodsDetailsBt.setVisibility(View.VISIBLE);
        }

        double total_price = expressSupplier.getExpress_fee();

        if (expressSupplier.getSupplier_id().equals("48")){
            SpannableStringBuilder builder = new SpannableStringBuilder(context.getResources().getString(R.string.yunfei_money)
                    + context.getResources().getString(R.string.goods_pay));
            builder.setSpan(redSpan,3, builder.length(),SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(graySpan,0,3,SPAN_EXCLUSIVE_EXCLUSIVE);
            freeTv.setText(builder);
        }else {
            if(total_price <= 0.0d){
                SpannableStringBuilder builder = new SpannableStringBuilder(context.getResources().getString(R.string.yunfei_money)
                        + context.getResources().getString(R.string.mail));
                builder.setSpan(redSpan,3, builder.length(),SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(graySpan,0,3,SPAN_EXCLUSIVE_EXCLUSIVE);
                freeTv.setText(builder);
            }else {
                SpannableStringBuilder builder = new SpannableStringBuilder((context.getResources().getString(R.string.freight) + total_price));
                builder.setSpan(redSpan,3, builder.length(),SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(graySpan,0,3,SPAN_EXCLUSIVE_EXCLUSIVE);
                freeTv.setText(builder);
            }
        }


        imgAdapter = new BuyExpressImgAdapter();
        expressRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        expressRv.setMinimumWidth(BaseApplication.width);
        imgAdapter.setList(expressSupplier.getItem());
        expressRv.addItemDecoration(new HorizontaltemDecoration((int) context.getResources().getDimension(R.dimen.x10)));
        expressRv.setAdapter(imgAdapter);
        goodsDetailsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){
                    goodsDetailsBt.setText(context.getResources().getString(R.string.shouqi));
                    goodsDetailsBt.setTextColor(context.getResources().getColor(R.color.text_tips));
                    expressRv.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    flag = false;
                }else {
                    goodsDetailsBt.setText(context.getResources().getString(R.string.chakan));
                    goodsDetailsBt.setTextColor(context.getResources().getColor(R.color.app_theme));
                    expressRv.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}