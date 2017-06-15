package com.thlh.jhmjmw.business.buy.buyconfirm.express;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.ExpressSupplier;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送信息适配器
 */
public class BuyExpressListAdapter extends EasyRecyclerViewAdapter {

    private Activity context;
    private BuyExpressImgAdapter imgAdapter;
    private List<String> freeList = new ArrayList<>();
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


        TextView supplierNameTv = viewHolder.findViewById(R.id.express_name_tv);
        TextView freeTv = viewHolder.findViewById(R.id.express_free_tv);
        TextView infoTv = viewHolder.findViewById(R.id.express_info_tv);
        EasyRecyclerView expressRv = viewHolder.findViewById(R.id.express_goods_rv);

        supplierNameTv.setText(expressSupplier.getStore_name());
        infoTv.setText(context.getResources().getString(R.string.you) + expressSupplier.getStore_name() + context.getResources().getString(R.string.distribution));
        double total_price = expressSupplier.getExpress_fee();
        if(total_price == 0){
            freeTv.setText(context.getResources().getString(R.string.mail));
        }else {
            freeTv.setText( (context.getResources().getString(R.string.freight) + total_price));
        }

        imgAdapter = new BuyExpressImgAdapter();
        expressRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        expressRv.setMinimumWidth(BaseApplication.width);
        imgAdapter.setList(expressSupplier.getItem());
        expressRv.addItemDecoration(new HorizontaltemDecoration((int) context.getResources().getDimension(R.dimen.x10)));
        expressRv.setAdapter(imgAdapter);

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }




}