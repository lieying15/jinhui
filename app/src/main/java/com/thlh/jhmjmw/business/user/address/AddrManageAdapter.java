package com.thlh.jhmjmw.business.user.address;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.Address;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HQ on 2016/3/30.
 */
public class AddrManageAdapter extends EasyRecyclerViewAdapter {
    private OnClickEvent listener;
    private List<Address> addressList ;
    private Context context;

    private final int START_TYPE_MINE = 0; // 开启前的Activity
    private final int START_TYPE_ORDERCONFIRM = 1;

    private int contentType  = 0;
    private final  int  ADDRESS_TYPE_NORMAL = 1;
    private final  int  ADDRESS_TYPE_SHOP = 0;
    
    private int  type = START_TYPE_MINE;
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public AddrManageAdapter(Context context, List<Address> addressList,int type){
        this.context = context;
        this.addressList = addressList;
        this.type = type;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_addr_manage_shop, R.layout.item_addr_manage_normal
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        TextView addrManageEditTv = (TextView) viewHolder.findViewById(R.id.addr_manage_edit_tv);
        TextView addrManageNameTv = (TextView) viewHolder.findViewById(R.id.addr_manage_name_tv);
        TextView addrManagePhoneTv = (TextView) viewHolder.findViewById(R.id.addr_manage_phone_tv);
        TextView addrManageAddrTv = (TextView) viewHolder.findViewById(R.id.addr_manage_addr_tv);
        CheckBox addrManageCb = (CheckBox)viewHolder.findViewById(R.id.addr_manage_cb);
        FrameLayout addrManageEditFl  = (FrameLayout) viewHolder.findViewById(R.id.addr_manage_edit_fl);
        FrameLayout addrManageDeleteFl  = (FrameLayout) viewHolder.findViewById(R.id.addr_manage_delete_fl);
        Address address = addressList.get(position);

        if(contentType == ADDRESS_TYPE_SHOP){
            addrManageEditTv.setVisibility(View.GONE);
            addrManageEditFl.setVisibility(View.GONE);
            swipeRoot.setSwipeAble(false);

        }else {
            addrManageEditTv.setVisibility(View.VISIBLE);
            addrManageEditFl.setVisibility(View.VISIBLE);
            swipeRoot.setSwipeAble(true);
        }

        switch (type){
            case START_TYPE_MINE:
                addrManageCb.setVisibility(View.GONE);
                break;
            case START_TYPE_ORDERCONFIRM:
                addrManageCb.setVisibility(View.VISIBLE);
                break;
        }
        addrManageNameTv.setText(address.getName());
        addrManagePhoneTv.setText(address.getPhone());
        String province  = address.getProvince();
        String city  = address.getCity();
        if(province.equals(city)){
            addrManageAddrTv.setText(province + address.getDistrict() + address.getAddress());
        }else {
            addrManageAddrTv.setText(province + city +  address.getDistrict() + address.getAddress());
        }

        addrManageCb.setBackgroundResource(R.drawable.icon_check_wine);


        addrManageCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (AddrManageAdapter.this.listener != null) {
                    AddrManageAdapter.this.listener.onClickSelect(position);
                }
            }
        });
        if(address.getIs_on().equals("1")){
            addrManageCb.setBackgroundResource(R.drawable.icon_check_wine_select);
        }else {
            addrManageCb.setBackgroundResource(R.drawable.icon_check_wine);
        }



        addrManageEditFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddrManageAdapter.this.listener != null)
                    AddrManageAdapter.this.listener.onClickEdit(v, position);
            }
        });


        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AddrManageAdapter.this.listener != null){
                    AddrManageAdapter.this.listener.onDelete(v,position);
                }
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        Address address = addressList.get(position);
        if(address.getId().equals("0")){
            contentType  = ADDRESS_TYPE_SHOP;
        }else {
            contentType  = ADDRESS_TYPE_NORMAL;

        }
        return contentType;
    }

    public void setOnClickEvent(OnClickEvent listener) {
        this.listener = listener;
    }

    public interface OnClickEvent {
        void onDelete(View view, int position);
        void onClickSelect(int position);
        void onClickEdit(View view, int position);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }
}