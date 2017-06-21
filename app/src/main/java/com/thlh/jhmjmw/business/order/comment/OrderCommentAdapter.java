package com.thlh.jhmjmw.business.order.comment;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.model.GoodsComment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.PhotoPagerActivity;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.view.PoiRedStar;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

import java.util.ArrayList;

/**
 * 评价中心 
 */
public class OrderCommentAdapter extends EasyRecyclerViewAdapter {
    private OnClickEvent listener;
    private Activity context;
    private int type;

    public OrderCommentAdapter(Activity context, int type) {
        this.type = type;
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_comment_mine
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {

        final GoodsComment goods = (GoodsComment) this.getItem(position);
        ImageView goodsIv = viewHolder.findViewById(R.id.item_goods_base_iv);
        TextView commentNameTv = viewHolder.findViewById(R.id.item_goods_base_name_tv);
        TextView priceTv = viewHolder.findViewById(R.id.item_goods_base_price_tv);
        TextView numTv = viewHolder.findViewById(R.id.item_goods_base_num_tv);
        PoiRedStar commentRankView = (PoiRedStar)viewHolder.findViewById(R.id.comment_rank_view);
        TextView actionRightTv = viewHolder.findViewById(R.id.comment_action_right_tv);
        TextView actionLeftTv = viewHolder.findViewById(R.id.comment_action_left_tv);

        switch (type){
            case OrderCommentActivity.COMMENT_TYPE_WAIT:
                actionLeftTv.setTextColor(context.getResources().getColor(R.color.text_tips));
                actionLeftTv.setBackgroundResource(R.drawable.selector_radius_white_stoke_mainblack_r20);
                actionRightTv.setVisibility(View.VISIBLE);
                commentRankView.setVisibility(View.GONE);
                break;
            case OrderCommentActivity.COMMENT_TYPE_COMPLETE:
                actionRightTv.setVisibility(View.GONE);
                commentRankView.setVisibility(View.VISIBLE);
                actionLeftTv.setTextColor(context.getResources().getColor(R.color.wine_light));
                actionLeftTv.setBackgroundResource(R.drawable.selector_radius_yellow_stoke_theme_r20);
                commentRankView.setStar(Integer.parseInt(goods.getComment().getRate()));
                if(goods.getComment().getPic() !=null && goods.getComment().getPic().size()>0){
                    CommentPicsAdapter adapter = new CommentPicsAdapter(context);
                    adapter.setList(goods.getComment().getPic());
                    adapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
                        @Override
                        public void onItemClick(View convertView, int position) {
                            ArrayList<String> temppic = new ArrayList<String>();
                            for(String pic :goods.getComment().getPic()){
                                temppic.add(pic);
                            }
                            PhotoPagerActivity.activityStart(context,temppic,position);
                        }
                    });
                }
                break;
        }
        commentNameTv.setText(goods.getItem_name());
        /*
        *
        * */
        priceTv.setText(context.getResources().getString(R.string.money)+ goods.getPrice());
        numTv.setText(context.getResources().getString(R.string.multiply)+ goods.getItem_num());
        ImageLoader.displayRoundImg(goods.getItem_img_thumb(),goodsIv);
        actionRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(OrderCommentAdapter.this.listener != null){
                    OrderCommentAdapter.this.listener.onComment(view,position);
                }
            }
        });
        actionLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OrderCommentAdapter.this.listener != null) {
                    OrderCommentAdapter.this.listener.onRebuy(position,goods.getItem_id());
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
        void onComment(View view, int position);
        void onRebuy(int position,String  orderid);

    }

    private void getImgResoures(){

    }
}