package com.thlh.jhmjmw.business.user.coupon;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.Coupon;
import com.thlh.baselib.utils.GlideRoundTransform;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 优惠券适配
 */
public class CouponAdapter extends EasyRecyclerViewAdapter {
    private OnClickListener listener;
    private Context context ;
    private String couponFlag ;
    public CouponAdapter(Context context,String couponFlag) {
        this.context = context;
        this.couponFlag = couponFlag;
    }
    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_coupon
        };
    }
    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {

        Coupon coupon = getItem(position);

        CardView couponCardV = viewHolder.findViewById(R.id.coupon_cardview);
        ImageView couponIv = viewHolder.findViewById(R.id.dialog_coupon_iv);
        TextView titleTv = viewHolder.findViewById(R.id.dialog_coupon_title_tv);
//        TextView timeTv = viewHolder.findViewById(R.id.dialog_coupon_time_tv);
        TextView actionTv = viewHolder.findViewById(R.id.dialog_coupon_action_tv);

        titleTv.setText(coupon.getCoupon_name());
        if(coupon.getCoupon_name().equals("水卡换购券")){
            actionTv.setVisibility(View.GONE);
        }

//        String time = TimeUtils.stringToDateString(coupon.getCoupon_end_time(),"yyyy-M-d");
        //有效期间时间
//        timeTv.setText(context.getResources().getString(R.string.coupon_Valid_until)+ time);
        switch (couponFlag){
            case Constants.COUPON_FLAG_UNUSE:
                couponCardV.setCardBackgroundColor(context.getResources().getColor(R.color.pink_shallow));
                couponCardV.setVisibility(View.VISIBLE);
                break;
            case Constants.COUPON_FLAG_NEARTIME:
                couponCardV.setCardBackgroundColor(context.getResources().getColor(R.color.pink_shallow));
                break;
            case Constants.COUPON_FLAG_USED:
                couponCardV.setCardBackgroundColor(context.getResources().getColor(R.color.gray));
                break;
        }
        ImageLoader.display(coupon.getLogo(),couponIv,R.drawable.img_fridge,new GlideRoundTransform(context,5));
        //为 1 状态时---已使用
        if(coupon.getIs_used().equals(Constants.COUPON_USED)){
            actionTv.setVisibility(View.INVISIBLE);
        }
       /* *//*添加的
        * *//*
        //为 0 状态时---未使用
        if(coupon.getIs_used().equals(Constants.COUPON_UNUSE)){
            couponCardV.setVisibility(View.VISIBLE);
        }*/
        actionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CouponAdapter.this.listener != null) {
                    CouponAdapter.this.listener.onExchange(position);
                }
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setActionEvent(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onExchange(int position);
    }

}