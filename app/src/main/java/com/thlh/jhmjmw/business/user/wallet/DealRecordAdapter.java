package com.thlh.jhmjmw.business.user.wallet;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.model.DealRecord;
import com.thlh.baselib.utils.TimeUtils;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 交易记录
 */
public class DealRecordAdapter extends EasyRecyclerViewAdapter {
    private Context context ;
    public DealRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_deal_record
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        DealRecord dealRecord = getItem(position);
        TextView timeTv = viewHolder.findViewById(R.id.deal_record_time_tv);
        TextView describeTv = viewHolder.findViewById(R.id.deal_record_describe_tv);
        TextView priceTv = viewHolder.findViewById(R.id.deal_record_price_tv);
        TextView remarkTv = viewHolder.findViewById(R.id.deal_record_remark_tv);

        String total = dealRecord.getTotal();
        String mjz = dealRecord.getAmount();

        StringBuilder stringBuilder = new StringBuilder();

        if(dealRecord.getType().equals("R")){
            stringBuilder.append(context.getResources().getString(R.string.mjz_top_up));
            priceTv.setTextColor(context.getResources().getColor(R.color.wine_light));
            /*加*/
            priceTv.setText("+"+dealRecord.getAmount());
            describeTv.setText(stringBuilder.toString());
            remarkTv.setVisibility(View.GONE);
        }
        if(dealRecord.getType().equals("E")){
            stringBuilder.append(context.getResources().getString(R.string.mjz_consumer));
            priceTv.setTextColor(context.getResources().getColor(R.color.blue_light));
            /*减*/
            priceTv.setText("-"+dealRecord.getAmount());
            describeTv.setText(stringBuilder.toString());
            remarkTv.setText(context.getResources().getString(R.string.order_num) + dealRecord.getRemark());
        }

        String time = TimeUtils.stringToDateString(dealRecord.getInputtime());
        time = time.replace(" ","\n");
        timeTv.setText(time);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



}