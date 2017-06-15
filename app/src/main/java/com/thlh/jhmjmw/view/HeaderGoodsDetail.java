package com.thlh.jhmjmw.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.jhmjmw.other.ShareUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.AboatUsActivity;
import com.thlh.jhmjmw.business.user.info.InfoManageActivity;
import com.thlh.jhmjmw.business.goods.search.SearchActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;

/**
 * Created by HQ on 2016/3/30.
 */
public class HeaderGoodsDetail extends RelativeLayout {

    private ImageView headerNomalLeftIv;
    private FrameLayout headerNomalLeftFl;
    private TextView headerNomalTv;
    private FrameLayout headerNomalShareFl,headerNomalMoreFl;
    private Context context;
    private Activity activitycontext;
    private String mTitleText;
    //pop
    private View poprootview;
    private LinearLayout infoll,homepagell,searchll,aboutusll;
    private PopupWindow popupWindow;

    private Activity activity;

    public HeaderGoodsDetail(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initListener();
    }

    public HeaderGoodsDetail(Context context) {
        super(context);
        this.context = context;
        initView();
        initListener();
        initDate();
    }

    public HeaderGoodsDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initListener();
        initDate();
    }

    public  void initDate(){

    }



    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_header_goods, this, true);
        if (isInEditMode()) { return; }
        headerNomalLeftIv = (ImageView) view.findViewById(R.id.header_goods_left_iv);
        headerNomalLeftFl = (FrameLayout) view.findViewById(R.id.header_goods_left_fl);
        headerNomalTv = (TextView) view.findViewById(R.id.header_goods_tv);
        headerNomalShareFl = (FrameLayout) view.findViewById(R.id.header_goods_share_fl);
        headerNomalMoreFl = (FrameLayout) view.findViewById(R.id.header_goods_more_fl);


        //更多 POP
        poprootview = LayoutInflater.from(context).inflate(R.layout.popup_goodsdetail_more, null);
        infoll = (LinearLayout) poprootview.findViewById(R.id.goods_pop_info_ll);
        homepagell = (LinearLayout) poprootview.findViewById(R.id.goods_pop_homepage_ll);
        searchll = (LinearLayout) poprootview.findViewById(R.id.goods_pop_search_ll);
        aboutusll = (LinearLayout) poprootview.findViewById(R.id.goods_pop_aboutus_ll);
        popupWindow = new PopupWindow(poprootview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(poprootview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_right_style);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_null));
    }

    private void initListener() {
        if (!isInEditMode()) {
            headerNomalLeftFl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(activity != null){
                        Activity activity = (Activity) HeaderGoodsDetail.this.context;
                        activity.finish();
                    }
                }
            });

            headerNomalMoreFl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int offsetx = (int)getResources().getDimension(R.dimen.goodsdetail_popwindow_offsetx);
                    popupWindow.showAsDropDown(headerNomalMoreFl, -offsetx,-DisplayUtil.dp2px(context, 8));
                }
            });

            infoll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoManageActivity.activityStart(context);
                }
            });

            searchll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activitycontext !=null){
                        SearchActivity.activityStart(activitycontext);
                    }
                }
            });
            aboutusll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activitycontext !=null){
                        AboatUsActivity.activityStart(activitycontext);
                    }
                }
            });
            homepagell.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, IndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                    context.startActivity(intent);
//                    IndexActivity.activityStart(HeaderGoodsDetail.this.context);
                }
            });
        }


    }

    public void setShareInfo(final Activity activity , final String title,final  String content,final String url){
        headerNomalShareFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity != null){
                    ShareUtils shareUtils = new ShareUtils(activity,title,content,url,R.mipmap.ic_launcher);
                    shareUtils.showShareWindow();
                }
            }
        });

        headerNomalLeftFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity != null){
                    activity.finish();
                }
            }
        });
    }


    public void setTitle(String title){
        headerNomalTv.setText(title);
    }

    public void setActivityContext(Activity activity){
        this.activitycontext = activity;
    }


}
