package com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.Comment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.order.comment.CommentPicsAdapter;
import com.thlh.jhmjmw.business.other.PhotoPagerActivity;
import com.thlh.jhmjmw.view.PoiRedStar;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;

import java.util.ArrayList;

/**
 * Created by HQ on 2016/3/30.
 */
public class GoodsCommentAdapter extends EasyRecyclerViewAdapter {

    private Activity context;

    public GoodsCommentAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_comment_ingoodsdetail
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        final Comment comment = getItem(position);
        LinearLayout topLl = viewHolder.findViewById(R.id.comment_ingoodsdetail_topll);
        topLl.setMinimumWidth((int) context.getResources().getDimension(R.dimen.x730));
        TextView commentNameTv = viewHolder.findViewById(R.id.comment_ingoodsdetail_name_tv);
        PoiRedStar commentRankView = viewHolder.findViewById(R.id.comment_ingoodsdetail_rank_view);
        TextView commentContentTv = viewHolder.findViewById(R.id.comment_ingoodsdetail_content_tv);
        EasyRecyclerView commentRv = viewHolder.findViewById(R.id.comment_ingoodsdetail_rv);

        commentNameTv.setText(comment.getName());
        commentContentTv.setText(comment.getComment());
        commentRankView.setStar(Integer.decode(comment.getRate()));
        CommentPicsAdapter adapter = new CommentPicsAdapter(context,3);
        commentRv.setLayoutManager(new GridLayoutManager(context,3));
        commentRv.setAdapter(adapter);
        int space = (int) context.getResources().getDimension(R.dimen.x10);
        commentRv.addItemDecoration( new HorizontaltemDecoration(space));
        adapter.setList(comment.getPic());
        adapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                ArrayList<String> temppic = new ArrayList<String>();
                for(String pic :comment.getPic()){
                    temppic.add(pic);
                }
                PhotoPagerActivity.activityStart(context,temppic,position);
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return  Math.min(mList.size(),3) ;
    }

}