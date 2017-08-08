package com.thlh.jhmjmw.business.order.comment;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class CommentPicsAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private int showCount = 5;
    private String url;

    public CommentPicsAdapter(Context context) {
        this.context = context;
    }

    public CommentPicsAdapter(Context context, int showCount) {
        this.context = context;
        this.showCount = showCount;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goods_grid_pic
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {

        ImageView goodsIv = viewHolder.findViewById(R.id.goods_pic_iv);
        LinearLayout goodsLl = viewHolder.findViewById(R.id.goods_pic_ll);
        ViewGroup.LayoutParams layoutParams = goodsIv.getLayoutParams();

        String goodspath = (String)this.getItem(position);
        if (goodspath.contains("http")){
            url = goodspath;
        }else {
            url = Deployment.IMAGE_PATH + goodspath;
        }
        ImageLoader.display(url,goodsIv);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return  Math.min(mList.size(),showCount) ;
    }
}