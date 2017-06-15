package com.thlh.jhmjmw.business.order.comment;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.Comment;
import com.thlh.baselib.utils.TimeUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.PhotoPagerActivity;
import com.thlh.jhmjmw.view.PoiRedStar;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.ArrayList;

/**
 * 商品详情-更多评价 页
 */
public class CommentAdapter extends EasyRecyclerViewAdapter {

    private Activity context;

    public CommentAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_comment
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {

        final Comment comment = (Comment) this.getItem(position);
        ImageView commentAvatarIv = viewHolder.findViewById(R.id.comment_avatar_iv);
        TextView commentNameTv = viewHolder.findViewById(R.id.comment_name_tv);
        TextView commentTimeTv = viewHolder.findViewById(R.id.comment_time_tv);
        PoiRedStar commentRankView = viewHolder.findViewById(R.id.comment_rank_view);
        TextView commentContentTv = viewHolder.findViewById(R.id.comment_content_tv);
        TextView commentBuytimeTv = viewHolder.findViewById(R.id.comment_buytime_tv);
        EasyRecyclerView commentPicRv = viewHolder.findViewById(R.id.comment_pic_rv);

        commentNameTv.setText(comment.getName());
        commentContentTv.setText(comment.getComment());
        commentRankView.setStar(Integer.parseInt(comment.getRate()));
        commentTimeTv.setText(  TimeUtils.stringToDateString(comment.getInputtime()));
        /*
        * */
        commentBuytimeTv.setText(context.getResources().getString(R.string.buy_date)+TimeUtils.stringToDateString(comment.getOrder_time()));
        if(comment.getPic() != null && comment.getPic().size()>0){
            CommentPicsAdapter adapter =  new CommentPicsAdapter(context,5);
            commentPicRv.setLayoutManager(new GridLayoutManager(context,5));
            commentPicRv.setAdapter(adapter);
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
        }else {
            commentPicRv.setVisibility(View.GONE);
        }


    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }



}