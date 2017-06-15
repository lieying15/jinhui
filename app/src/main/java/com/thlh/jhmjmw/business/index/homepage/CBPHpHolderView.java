package com.thlh.jhmjmw.business.index.homepage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.thlh.baselib.model.HomepageTitleAD;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.GoodsAdActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.search.SearchResultActivity;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.convenientbanner.CBPageAdapter;


/**
 * 首页图片轮播适配
 */
public class CBPHpHolderView implements CBPageAdapter.Holder<HomepageTitleAD> {
    private ImageView imageView;
    private Context context;

    public CBPHpHolderView(Context context) {
        this.context = context;
    }

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return imageView;
    }



    @Override
    public void UpdateUI(final Context context, final int position, final HomepageTitleAD data) {
//        Glide.with(context)
//                .load(Deployment.IMAGE_PATH + data.getPic())
//                .priority(Priority.HIGH)
//                .into(imageView);
        ImageLoader.display(  data.getPic(),imageView,Priority.HIGH);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("点击广告图片");
                switch (data.getType()){
                    case "Search":
//                        Intent intent = new Intent();
//                        intent.setClass(context, SearchResultActivity.class);
//                        intent.putExtra("searchContent", data.getValue());
//                        context.startActivity(intent);
                        if(context instanceof Activity){
                            ((Activity)context).overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
                        }
                        SearchResultActivity.activityStart(context,data.getValue());
                        break;
                    case "Goods":
                        String goodsid = data.getValue();
                        L.e("CBPHpHolderView goodsid" +goodsid );
                        if(goodsid.equals("1")){
                            GoodsAdActivity.activityStart(context,goodsid);
                        }else {
                            GoodsDetailV3Activity.activityStart(context,goodsid);
                        }
                        break;
                }

            }
        });
    }
}
