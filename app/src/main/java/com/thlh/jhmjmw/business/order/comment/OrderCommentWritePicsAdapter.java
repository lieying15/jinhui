package com.thlh.jhmjmw.business.order.comment;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class OrderCommentWritePicsAdapter extends EasyRecyclerViewAdapter {
    private Context context;

    public OrderCommentWritePicsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_comment_grid_pic
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        ImageView goodsIv = viewHolder.findViewById(R.id.goods_pic_iv);
        LinearLayout goodsLl = viewHolder.findViewById(R.id.goods_pic_ll);
        ViewGroup.LayoutParams layoutParams = goodsIv.getLayoutParams();
        layoutParams.width = (BaseApplication.width - DisplayUtil.dp2px(context,100))/5;
        layoutParams.height = (BaseApplication.width - DisplayUtil.dp2px(context,120))/5;

//        if(position ==( mList.size()) && (mList.size()!=5)){
//            goodsIv.setImageResource(R.drawable.icon_add_upload);
//            goodsIv.setScaleType(ImageView.ScaleType.CENTER);
//        }else {
        Uri goodspath = (Uri)this.getItem(position);
       /* if (goodspath.toString().contains("http")){
            goodsIv.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.display(goodspath.toString(),goodsIv);
        }else {*/
            goodsIv.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.display(goodspath.toString(),goodsIv);
//        }


//        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
            return  Math.min(mList.size(),5) ;
    }



}