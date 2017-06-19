package com.thlh.jhmjmw.business.index.homepage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页商品适配
 */
public class HomePageGoodsAdapter extends RecyclerView.Adapter<HomePageGoodsAdapter.MyViewHolder> {


    private Context context;
    private boolean isLoadOver = false;
    private OnClickListener listener;
    private LayoutInflater inflater;

    List<Goods> goodsList = new ArrayList<>();
    private EasyRecyclerViewHolder.OnItemClickListener onItemClickListener;

    public HomePageGoodsAdapter(Context context, List<Goods> goodsList){
        this.context = context;
        this.goodsList = goodsList;
    }


    public void setBuyListener(HomePageGoodsAdapter.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public HomePageGoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homepage_goods, null);
//        View view = inflater.inflate(R.layout.item_homepage_goods, parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (position==getItemCount()-1){
            listener.loadMore();
        }
        Goods goods = goodsList.get(position);

        holder.topRl.setMinimumWidth((int)context.getResources().getDimension(R.dimen.x360));
        holder.topRl.setMinimumHeight((int)context.getResources().getDimension(R.dimen.y460));


        ImageLoader.display(goods.getItem_img_thumb(), holder.goodsIv);
        holder.goodsNameTv.setText(goods.getItem_name());
        holder.goodsStatusTv.setVisibility(View.GONE);
        holder.goodsBackIv.setVisibility(View.GONE);
        holder. cartIv.setImageResource(R.drawable.icon_homepage_cart);

        if(goods.getItem_id().equals("1")){
            holder.goodsPriceTv.setVisibility(View.VISIBLE);
            holder.goodsMjzTv.setVisibility(View.GONE);
            holder.goodsPriceTv.setText(context.getResources().getString(R.string.ice_exchage));
        }else {

            String priceStr = goods.getItem_price();
            holder.goodsPriceTv.setText(context.getResources().getString(R.string.money_)+priceStr);
            if(goods.getIs_mjb().equals("0")){
                holder.goodsMjzTv.setVisibility(View.GONE);
            }else {
                holder.goodsMjzTv.setVisibility(View.VISIBLE);
                String mjzStr = goods.getItem_price();
                if(goods.getIs_mjb().equals("2"))
                    mjzStr = goods.getMjb_value();
                holder.goodsMjzTv.setText(TextUtils.showMjz(context,mjzStr));
            }

            holder.goodsDescribeTv.setText(goods.getItem_subtitle());
        }
        holder.goodsDescribeTv.setText(goods.getItem_subtitle());

//        if(position==0){
//            goodsTagIv.setVisibility(View.VISIBLE);
//            goodsTagIv.setImageResource(R.drawable.homepag_goodstag_today);
//        }else if(position==1){
//            goodsTagIv.setVisibility(View.VISIBLE);
//            goodsTagIv.setImageResource(R.drawable.homepage_goodstag_hot);
//        }else {
//            goodsTagIv.setVisibility(View.GONE);
//        }
        if (goods.getIs_shelves() != null) {
            if (goods.getIs_shelves().equals("0")) {
                holder.goodsBackIv.setVisibility(View.VISIBLE);
                holder.goodsStatusTv.setText(R.string.shelves);
                holder.goodsStatusTv.setVisibility(View.VISIBLE);
                holder.cartIv.setImageResource(R.drawable.icon_shopcart_gray);
                holder.cartFl.setOnClickListener(null);
                return;
            }
        }
        if (goods.getIs_prepare() != null) {
            if (goods.getIs_prepare().equals("1")) {
                holder.goodsBackIv.setVisibility(View.VISIBLE);
                holder.goodsStatusTv.setText(R.string.stock);
                holder.goodsStatusTv.setVisibility(View.VISIBLE);
                holder.cartIv.setImageResource(R.drawable.icon_shopcart_gray);
                holder.cartFl.setOnClickListener(null);
                return;
            }
        }

        if (goods.getStorage() == 0) {
            holder.goodsBackIv.setVisibility(View.VISIBLE);
            holder.goodsStatusTv.setText(R.string.gone);
            holder.goodsStatusTv.setVisibility(View.VISIBLE);
            holder.cartIv.setImageResource(R.drawable.icon_shopcart_gray);
            holder.cartFl.setOnClickListener(null);
            return;
        }

        holder.cartFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomePageGoodsAdapter.this.listener != null){
                    int[] item_location = new int[2];
                    holder.goodsIv.getLocationInWindow(item_location);//获取点击商品图片的位置
                    Drawable drawable =  holder.goodsIv.getDrawable();//复制一个新的商品图标
                    HomePageGoodsAdapter.this.listener.onClickBuy(v, position,item_location,drawable);
                }
            }
        });

        holder.goodsIv.setEnabled(true);
        holder.goodsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDetails(goods);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public void setOnItemClickListener(EasyRecyclerViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnClickListener {
        void onClickBuy(View view, int position, int[] item_location, Drawable drawable);
        void onClickDetails(Goods goods);
        void loadMore();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout topRl;
        public TextView goodsNameTv;
        public TextView goodsDescribeTv;
        public TextView goodsPriceTv;
        public TextView goodsMjzTv;
        public TextView goodsStatusTv;
        public ImageView goodsIv;
        public ImageView goodsTagIv;
        public ImageView goodsBackIv;
        public ImageView cartIv;
        public FrameLayout cartFl;

        public MyViewHolder(View view) {
            super(view);
            topRl = (RelativeLayout) view.findViewById(R.id.item_homgpage_goods_toprl);
            goodsNameTv = (TextView) view.findViewById(R.id.item_homgpage_goods_name_tv);
            goodsDescribeTv = (TextView) view.findViewById(R.id.item_homgpage_goods_describe_tv);
            goodsPriceTv = (TextView) view.findViewById(R.id.item_homgpage_goods_price_tv);
            goodsMjzTv = (TextView) view.findViewById(R.id.item_homgpage_goods_mjz_tv);
            goodsStatusTv = (TextView) view.findViewById(R.id.item_homgpage_goods_status_tv);

            goodsIv = (ImageView) view.findViewById(R.id.item_homgpage_goods_iv);
            goodsTagIv = (ImageView) view.findViewById(R.id.item_homgpage_goods_tagiv);
            goodsBackIv = (ImageView) view.findViewById(R.id.item_homgpage_goods_back_iv);
            cartIv = (ImageView) view.findViewById(R.id.item_homgpage_cart_iv);
            cartFl = (FrameLayout) view.findViewById(R.id.item_homgpage_cart_fl);
        }




    }
}