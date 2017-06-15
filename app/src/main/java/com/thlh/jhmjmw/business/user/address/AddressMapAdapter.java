package com.thlh.jhmjmw.business.user.address;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

public class AddressMapAdapter extends EasyRecyclerViewAdapter {
    private OnClickListener listener;
    private SparseArray<Boolean> checkStates;

    public AddressMapAdapter(SparseArray<Boolean> checkStates){
        this.checkStates = checkStates;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_baidumap_around
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        PoiInfo poiInfo = (PoiInfo)this.getItem(position);
        RelativeLayout mapAroundRl = viewHolder.findViewById(R.id.item_baidumap_around_rl);
//        CheckBox mapAroundCb = viewHolder.findViewById(R.id.item_baidumap_around_cb);
        ImageView mapAroundSelectIv = viewHolder.findViewById(R.id.item_baidumap_around_select_iv);
//        RippleLinearLayout mapAroundbRipLl = viewHolder.findViewById(R.id.item_baidumap_around_ripll);
        TextView mapAroundTv = viewHolder.findViewById(R.id.item_baidumap_around_tv);
        TextView mapAroundTitle = viewHolder.findViewById(R.id.item_baidumap_around_title);
        ImageView mapAroundIv = viewHolder.findViewById(R.id.item_baidumap_around_iv);
//        mapAroundbRipLl.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
//            @Override
//            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
//                AddressMapAdapter.this.notifyDataSetChanged();
//            }
//        });
        mapAroundRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddressMapAdapter.this.listener != null){
                    AddressMapAdapter.this.listener.onSelect(v, position);
                    notifyDataSetChanged();
                }
            }
        });

        if(position == 0){
            mapAroundTitle.setVisibility(View.VISIBLE);
        }else {
            mapAroundTitle.setVisibility(View.GONE);
        }
        mapAroundTv.setText(poiInfo.name);
        if(checkStates.get(position)){
//            mapAroundCb.setChecked(true);
            mapAroundSelectIv.setBackgroundResource(R.drawable.icon_check_wine_select);
            mapAroundTv.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.wine_light));
            mapAroundTitle.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.wine_light));
            if(position == 0){
                mapAroundIv.setImageResource(R.drawable.icon_address_gps);
            }else {
                mapAroundIv.setImageResource(R.drawable.icon_address_info);
            }
        }else {
//            mapAroundCb.setChecked(false);
            mapAroundSelectIv.setBackgroundResource(R.drawable.icon_check_gray);
            mapAroundTv.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.text_nomal));
            mapAroundTitle.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.text_nomal));
            if(position == 0){
                mapAroundIv.setImageResource(R.drawable.icon_address_loction);
            }else {
                mapAroundIv.setImageResource(R.drawable.icon_address_info);
            }
        }



    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setSelectListener(OnClickListener listener) {
        this.listener = listener;
    }



    public interface OnClickListener {
        void onSelect( View view, int position);
    }
}