package com.thlh.jhmjmw.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.Goods;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.search.adapter.SearchRecommendAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.List;

/**
 * Created by HQ on 2016/3/30.
 */
public class NoInfoView extends LinearLayout {
    private Context context;
    private ImageView noinfoTitleIv;
    private TextView noinfoTitleTv,nextactionTv;
    private FrameLayout nextactionFl;
    private EasyRecyclerView noinforv;
    private SearchRecommendAdapter noinfoAdapter;
    private LinearLayout recommandLl;


    public NoInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public NoInfoView(Context context) {
        super(context);
        this.context = context;
        initView();

    }

    public NoInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }




    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_noinfo_layout, this, true);

        noinfoTitleIv = ((ImageView) view.findViewById(R.id.noinfo_title_iv));
        noinfoTitleTv = ((TextView) view.findViewById(R.id.noinfo_content_tv));
        nextactionTv = ((TextView) view.findViewById(R.id.noinfo_nextaction_tv));
        nextactionFl = ((FrameLayout) view.findViewById(R.id.noinfo_nextaction_fl));
        noinforv = ((EasyRecyclerView) view.findViewById(R.id.noinfo_goods_rv));
        recommandLl = ((LinearLayout) view.findViewById(R.id.noinfo_recommand_ll));

        GridLayoutManager hotsaleLM = new GridLayoutManager(context, 3);
        noinforv.setLayoutManager(hotsaleLM);

        noinfoAdapter = new SearchRecommendAdapter(context);
        noinfoAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart((Activity) context, ((Goods) noinfoAdapter.getItem(position)).getItem_id());
            }
        });
        noinforv.setAdapter(noinfoAdapter);

    }





    public void setTitle(String title){
        noinfoTitleTv.setText(title);
    }

    public void setTitleIv(int titleIv){
        noinfoTitleIv.setImageResource(titleIv);
    }

    public void setNextactionStr(String str){
        nextactionTv.setText(str);
    }

    public void setNextactionListener(OnClickListener listener){
        nextactionFl.setVisibility(VISIBLE);
        nextactionFl.setOnClickListener(listener);
    }

    public void setList(List<Goods> list){
        recommandLl.setVisibility(VISIBLE);
        noinfoAdapter.setList(list);
    }

}
