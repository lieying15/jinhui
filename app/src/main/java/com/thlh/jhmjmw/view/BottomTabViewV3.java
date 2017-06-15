package com.thlh.jhmjmw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.tablayout.MsgView;

/**
 * Created by HQ on 2016/3/30.
 */
public class BottomTabViewV3 extends RelativeLayout  {
    private Context context;
    public int currIndex = -1; //当前
    private int offset = BaseApplication.width / 5; //屏幕宽度/5

    private ChangeFragmentListener mChangeFragmentListener;
    public static final int POSITON_HOMEPAGE = 0;
    public static final int POSITON_CATEGORY = 1;
    public static final int POSITON_SHOP = 2;
    public static final int POSITON_SHOPCART =3;
    public static final int POSITON_MINE = 4;
    public static final int POSITON_NOCONNECTION = 5;
    public  final int ANIMATION_TIME = 300;

//    private ImageView ivHomepageTabLine;

    private ImageView hpIv;
    private TextView hpTv;
    private LinearLayout hpLl;
    private ImageView choiceIv;
    private  TextView choiceTv;
    private LinearLayout choiceLl;
    private ImageView cartIv;
    private   TextView cartTv;
    private   MsgView cartNumTv;
    private RelativeLayout cartLl;
    private ImageView mineIv,shopIv;
    private TextView mineTv;
    private LinearLayout mineLl;

    private TextView shopTv;
    private  LinearLayout shopLl;

    private boolean needChangeTabView = true;

    public BottomTabViewV3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initListener();

    }

    public BottomTabViewV3(Context context) {
        super(context);
        this.context = context;
        initView();
        initListener();

    }

    public BottomTabViewV3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initListener();
    }


    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_homepage_bottom_tab_v3, this, true);

        hpIv = (ImageView) view.findViewById(R.id.homepage_btab_hp_iv);
        hpTv = (TextView) view.findViewById(R.id.homepage_btab_hp_tv);
        hpLl = (LinearLayout) view.findViewById(R.id.homepage_btab_hp_ll);

        choiceIv = (ImageView) view.findViewById(R.id.homepage_btab_choice_iv);
        choiceTv = (TextView) view.findViewById(R.id.homepage_btab_choice_tv);
        choiceLl = (LinearLayout) view.findViewById(R.id.homepage_btab_choice_ll);

        cartIv = (ImageView) view.findViewById(R.id.homepage_btab_shopcart_iv);
        cartTv = (TextView) view.findViewById(R.id.homepage_btab_shopcart_tv);
        cartNumTv = (MsgView) view.findViewById(R.id.homepage_btab_shopcart_num_msg);
        cartLl = (RelativeLayout) view.findViewById(R.id.homepage_btab_shopcart_ll);

        mineIv = (ImageView) view.findViewById(R.id.homepage_btab_mine_iv);
        mineTv = (TextView) view.findViewById(R.id.homepage_btab_mine_tv);
        mineLl = (LinearLayout) view.findViewById(R.id.homepage_btab_mine_ll);

        shopIv = (ImageView) view.findViewById(R.id.homepage_btab_shop_iv);
        shopTv = (TextView) view.findViewById(R.id.homepage_btab_shop_tv);
        shopLl = (LinearLayout) view.findViewById(R.id.homepage_btab_shop_ll);

        //控件初始化
        hpIv.setImageResource(R.drawable.icon_home_wine);
        hpTv.setTextColor(getResources().getColor(R.color.app_theme));

    }

    public void initListener(){
        setViewClickable(true);
    }



    public interface ChangeFragmentListener {
         void changeFragment(int position);

    }

    public void setChangeeFragmentListener(ChangeFragmentListener onClickListener) {
        mChangeFragmentListener = onClickListener;
    }


    public void onclickEvent(int position){
//        if(currIndex == position){
//            return;
//        }
        if(position !=  POSITON_SHOP ){
//            recoverColor(currIndex);
            recoverColor();
        }
        mChangeFragmentListener.changeFragment(position);
        if(needChangeTabView) {
            changeTabView(position);
        }
    }

    public void changeTabView(int position, int duration){
        switch (position) {
            case POSITON_HOMEPAGE:
                currIndex = 0;
                hpIv.setImageResource(R.drawable.icon_home_wine);
                hpTv.setTextColor(getResources().getColor(R.color.app_theme));
                break;
            case POSITON_CATEGORY:
                currIndex = 1;
                choiceIv.setImageResource(R.drawable.icon_choice_wine);
                choiceTv.setTextColor(getResources().getColor(R.color.app_theme));
                break;

            case POSITON_SHOP: //点击小店跳转页面，这里不触动方法。
//                startTabAnimation(currIndex * offset, offset * 2 , duration);
//                currIndex = 2;
//                ivHomepageTabShop.setImageResource(R.drawable.main_tab_shop_select);
//                shopTv.setTextColor(getResources().getColor(R.color.app_theme));
                break;

            case POSITON_SHOPCART:
                currIndex = 3;
                cartIv.setImageResource(R.drawable.icon_shopcart_wine);
                cartTv.setTextColor(getResources().getColor(R.color.app_theme));
                break;

            case POSITON_MINE:
                currIndex = 4;
                mineIv.setImageResource(R.drawable.icon_mine_wine);
                mineTv.setTextColor(getResources().getColor(R.color.app_theme));
                break;
            case POSITON_NOCONNECTION :
                currIndex = 5;
                break;
        }
    }



    public void changeTabView(int position){
        changeTabView(position,ANIMATION_TIME);
    }



    //恢复之前选中按钮样式
    public void recoverColor(int index) {
        switch (index) {
            case POSITON_HOMEPAGE:
                hpIv.setImageResource(R.drawable.icon_home_gray);
                hpTv.setTextColor(getResources().getColor(R.color.text_tips));
                break;
            case POSITON_CATEGORY:
                choiceIv.setImageResource(R.drawable.icon_choice_gray);
                choiceTv.setTextColor(getResources().getColor(R.color.text_tips));
                break;
            case POSITON_SHOP: //点击小店跳转页面，这里不触动方法。

//                shopTv.setTextColor(getResources().getColor(R.color.text_tips));
                break;
            case POSITON_SHOPCART:
                cartIv.setImageResource(R.drawable.icon_shopcart_gray);
                cartTv.setTextColor(getResources().getColor(R.color.text_tips));
//                cartIv.setImageResource(R.drawable.icon_shopcart_gray);
//                cartTv.setTextColor(getResources().getColor(R.color.text_tips));
                break;
            case POSITON_MINE:
                mineIv.setImageResource(R.drawable.icon_mine_gray);
                mineTv.setTextColor(getResources().getColor(R.color.text_tips));
                break;
        }
    }

    public void recoverColor() {
        hpIv.setImageResource(R.drawable.icon_home_gray);
        hpTv.setTextColor(getResources().getColor(R.color.text_tips));
        choiceIv.setImageResource(R.drawable.icon_choice_gray);
        choiceTv.setTextColor(getResources().getColor(R.color.text_tips));
        shopTv.setTextColor(getResources().getColor(R.color.text_tips));
        cartIv.setImageResource(R.drawable.icon_shopcart_gray);
        cartTv.setTextColor(getResources().getColor(R.color.text_tips));
        mineIv.setImageResource(R.drawable.icon_mine_gray);
        mineTv.setTextColor(getResources().getColor(R.color.text_tips));
    }

    public void setViewClickable(Boolean canClick){
        if(canClick){
            hpLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickEvent(POSITON_HOMEPAGE);
                }
            });

            choiceLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickEvent(POSITON_CATEGORY);
                }
            });

            shopLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickEvent(POSITON_SHOP);
                }
            });
            cartLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickEvent(POSITON_SHOPCART);
                }
            });
            mineLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickEvent(POSITON_MINE);
                }
            });
        }else{
            hpLl.setOnClickListener(null);
            choiceLl.setOnClickListener(null);
            shopLl.setOnClickListener(null);
            cartLl.setOnClickListener(null);
            mineLl.setOnClickListener(null);
        }
    }

    public boolean isNeedChangeTabView() {
        return needChangeTabView;
    }

    public void setNeedChangeTabView(boolean needChangeTabView) {
        this.needChangeTabView = needChangeTabView;
    }


    public ImageView getTabShopcartIv() {
        return cartIv;
    }

    public TextView getTabShopcartTv() {
        return cartTv;
    }

    public MsgView getTabShopcartNumTv() {
        return cartNumTv;
    }

    public TextView getTabShopTv() {
        return shopTv;
    }



}
