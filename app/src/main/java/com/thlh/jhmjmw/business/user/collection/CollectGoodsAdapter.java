package com.thlh.jhmjmw.business.user.collection;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.thlh.baselib.model.Goods;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.adapter.BaseGoodsLIstAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.swipview.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏适配器
 */
public class CollectGoodsAdapter extends BaseGoodsLIstAdapter {
    private OnClickEvent listener;
    private Context context;
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();



    public CollectGoodsAdapter(Context context) {
        super(context,true);
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_collect_goods
        };
    }


    @Override
    protected void onViewHolder(EasyRecyclerViewHolder viewHolder, int position) {

        SwipeItemLayout swipeRoot = (SwipeItemLayout) viewHolder.findViewById(R.id.item_contact_swipe_root);
        LinearLayout mDelete = (LinearLayout) viewHolder.findViewById(R.id.item_delete_ll);
        FrameLayout basecartFl = viewHolder.findViewById(R.id.item_goods_base_cart_fl);

        Goods goods = getItem(position);
        if (!goods.getIs_shelves().equals("0")){

        }

        swipeRoot.setSwipeAble(true);
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
                if(CollectGoodsAdapter.this.listener != null){
                    CollectGoodsAdapter.this.listener.onDelete(v,position);
                }
            }
        });

        basecartFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CollectGoodsAdapter.this.listener != null){
                    CollectGoodsAdapter.this.listener.onAddCart(position);
                }
            }
        });


    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



    public void setOnClickEvent(OnClickEvent listener) {
        this.listener = listener;
    }



    public interface OnClickEvent {
        void onDelete(View view,int position);
        void onAddCart(int position);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }
}