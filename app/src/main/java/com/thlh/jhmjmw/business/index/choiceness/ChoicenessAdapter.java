package com.thlh.jhmjmw.business.index.choiceness;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.ChoiceGoods;
import com.thlh.baselib.utils.GlideRoundTransform;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 商城精选页适配
 */
public class ChoicenessAdapter extends EasyRecyclerViewAdapter {
    private String url;

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_choiceness
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        ChoiceGoods choiceness = getItem(position);
        ImageView choiceIv = viewHolder.findViewById(R.id.item_choiceness_title_iv);
        LinearLayout choicLL = viewHolder.findViewById(R.id.item_choiceness_topll);
        choicLL.setMinimumWidth(BaseApplication.width);
        TextView titleTv = viewHolder.findViewById(R.id.item_choiceness_title_tv);
        TextView subTitleTv = viewHolder.findViewById(R.id.item_choiceness_subtitle_tv);
        titleTv.setText(choiceness.getTitle());
        subTitleTv.setText(choiceness.getSubtile());
        if (choiceness.getPic().contains("http")){
            url = choiceness.getPic();
        }else {
            url =Deployment.IMAGE_PATH + choiceness.getPic();
        }
            Glide.with(BaseApplication.getInstance())
                    .load(url)
                    .error(com.thlh.baselib.R.drawable.img_empty)//加载失败时显示的图片
                    .transform(new GlideRoundTransform(BaseApplication.getInstance().getBaseContext(), 5))
                    .into(choiceIv);

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}