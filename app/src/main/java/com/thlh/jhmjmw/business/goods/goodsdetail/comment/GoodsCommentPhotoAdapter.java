package com.thlh.jhmjmw.business.goods.goodsdetail.comment;

import android.content.Context;
import android.widget.ImageView;

import com.thlh.baselib.model.response.Pic;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsCommentPhotoAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private int showCount = 5;
    private String url;

    public GoodsCommentPhotoAdapter(Context context) {
        this.context = context;
    }

    public GoodsCommentPhotoAdapter(Context context, int showCount) {
        this.context = context;
        this.showCount = showCount;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_goodscomment_grid_pic
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        ImageView goodsIv = viewHolder.findViewById(R.id.goods_pic_iv);
        Pic goodspath = (Pic)this.getItem(position);
        if (goodspath.getUrl().contains("http")){
            url = goodspath.getUrl();
        }else {
            url = Deployment.IMAGE_PATH + goodspath.getUrl();
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