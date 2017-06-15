package com.thlh.jhmjmw.business.goods.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.adapter.BaseGoodsLIstAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsNormalListAdapter extends BaseGoodsLIstAdapter {
    private  final int ITEM_TYPE_NORMAL = 0;
    private  final int ITEM_TYPE_BOTTOM = 1;
    private OnClickListener listener;
    private boolean isLoadOver = false;
    private Context context;
    public GoodsNormalListAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_normal_goods,R.layout.item_normal_goods_bottom
        };
    }

    @Override
    protected void onViewHolder(EasyRecyclerViewHolder viewHolder, int position) {
        int itemType = this.getRecycleViewItemType(position);

        if(itemType == ITEM_TYPE_BOTTOM){
            ProgressBar bottomBar = viewHolder.findViewById(R.id.item_bottom_progressbar);
            TextView bottomTv = viewHolder.findViewById(R.id.item_bottom_tv);
            if(isLoadOver){
                bottomBar.setVisibility(View.GONE);
                bottomTv.setText(context.getResources().getString(R.string.all_shops));
            }else {
                bottomBar.setVisibility(View.VISIBLE);
                bottomTv.setText(context.getResources().getString(R.string.going));
            }
        }

    }

    @Override
    public int getRecycleViewItemType(int position) {
        if(position == this.mList.size() -1) return ITEM_TYPE_BOTTOM;
        return ITEM_TYPE_NORMAL;
    }


    public void setOnClickEvent(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onClickCart(int position);
    }

    //加载更多的样式
    public void setLoadOver(boolean loadOver) {
        isLoadOver = loadOver;
    }

    public boolean isLoadOver() {
        return isLoadOver;
    }
}