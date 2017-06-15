package com.thlh.jhmjmw.business.order.trace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.TrackData;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 */
public class OrderTraceAdapter extends EasyRecyclerViewAdapter {
    private Context context;

    public OrderTraceAdapter(Context context){
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_order_trace
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        TrackData trackData = (TrackData)this.getItem(position);
        ImageView pointIv = (ImageView) viewHolder.findViewById(R.id.order_trace_point_iv);
        View topline = (View) viewHolder.findViewById(R.id.order_trace_topline_iv);
        View bottompline = (View) viewHolder.findViewById(R.id.order_trace_bottompline_iv);
        TextView dateTv = (TextView) viewHolder.findViewById(R.id.order_trace_date_tv);
        TextView timeTv = (TextView) viewHolder.findViewById(R.id.order_trace_time_tv);
        TextView contentTv = (TextView) viewHolder.findViewById(R.id.order_trace_content_tv);

        String[] strs = trackData.getTime().split(" ");
        dateTv.setText(strs[0]);
        timeTv.setText(strs[1]);
        contentTv.setText(trackData.getContext());
        ViewGroup.LayoutParams para;
        para = pointIv.getLayoutParams();
        if (position ==0){
            pointIv.setImageResource(R.drawable.img_ordertrace_positon);
            para.height = (int) context.getResources().getDimension(R.dimen.x40);
            para.width =(int) context.getResources().getDimension(R.dimen.y40);
            pointIv.setLayoutParams(para);

            dateTv.setTextColor(context.getResources().getColor(R.color.wine_light));
            timeTv.setTextColor(context.getResources().getColor(R.color.wine_light));
//            topline.setVisibility(View.GONE);
            contentTv.setTextColor(context.getResources().getColor(R.color.white));
            contentTv.setBackgroundResource(R.drawable.order_trace_info_winlight);
        }else {
            pointIv.setImageResource(R.drawable.shap_radius_gray_shallow);
            para.width = (int) context.getResources().getDimension(R.dimen.x16);
            para.height = (int) context.getResources().getDimension(R.dimen.y16);
            pointIv.setLayoutParams(para);
            dateTv.setTextColor(context.getResources().getColor(R.color.text_nomal));
            timeTv.setTextColor(context.getResources().getColor(R.color.text_nomal));
            contentTv.setTextColor(context.getResources().getColor(R.color.text_nomal));
            contentTv.setBackgroundResource(R.drawable.order_trace_info_gray);


        }

//        if (position == (mList.size()-1)){
//            bottompline.setVisibility(View.INVISIBLE);
//        }else {
//            bottompline.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}