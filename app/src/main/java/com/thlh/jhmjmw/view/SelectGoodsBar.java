package com.thlh.jhmjmw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.jhmjmw.R;
import com.thlh.viewlib.expandableLayout.ExpandableLinearLayout;

/**
 * Created by HQ on 2016/3/30.
 */
public class SelectGoodsBar extends RelativeLayout {
    public static final int POP_SORTTYPE_COMPSITE = 0;
    public static final int POP_SORTTYPE_HTOL = 1;
    public static final int POP_SORTTYPE_LTOH = 2;
    public static final int POP_SORTTYPE_CREDIT = 3;
    private LinearLayout selectGoodsBarSortLl, selectGoodsBarPriorityLl, selectGoodsBarFilterLl;
    private TextView selectGoodsBarSortTv;
    private ImageView selectGoodsBarSortIv;
    private Context context;

    //pop
    private View poprootview;
    private LinearLayout sortll,htolll,ltohll,creditll;
    private PopupWindow sortPopWindow;
    private PopListener mPopListener;

    private  int paddingleft = 27;
    private FrameLayout sortSynthesizeFl;
    private FrameLayout sortHtolFl;
    private FrameLayout sortLtoHFl;
    private FrameLayout sortCreditFl;
    private ExpandableLinearLayout sortExpandable;

    public SelectGoodsBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initListener();
    }

    public SelectGoodsBar(Context context) {
        super(context);
        this.context = context;
        initView();
        initListener();

    }

    public SelectGoodsBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initListener();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_searchresult_sortbar, this, true);
        selectGoodsBarSortLl = (LinearLayout) view.findViewById(R.id.sortbar_title_synthesize_ll);
        selectGoodsBarPriorityLl = (LinearLayout) view.findViewById(R.id.sortbar_title_priority_ll);
        selectGoodsBarFilterLl = (LinearLayout) view.findViewById(R.id.sortbar_title_filter_ll);
//        selectGoodsBarSortTv = (TextView) view.findViewById(R.id.sortbar_title_synthesize_tv);
//        selectGoodsBarSortIv = (ImageView) view.findViewById(R.id.sortbar_title_synthesize_iv);

        sortExpandable = (ExpandableLinearLayout) view.findViewById(R.id.searchresult_synthesize_expandableLayout);
        sortSynthesizeFl = (FrameLayout) view.findViewById(R.id.explayout_synthesize_fl);
        sortHtolFl = (FrameLayout) view.findViewById(R.id.explayout_synthesize_htol_fl);
        sortLtoHFl = (FrameLayout) view.findViewById(R.id.explayout_synthesize_ltoh_fl);
        sortCreditFl = (FrameLayout) view.findViewById(R.id.explayout_synthesize_credit_fl);

        sortExpandable.expand();
        sortSynthesizeFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sortExpandable.move(0);
            }
        });
        //更多 POP


        poprootview = LayoutInflater.from(context).inflate(R.layout.popup_select_goods,null);
        sortll = (LinearLayout) poprootview.findViewById(R.id.pop_seletgoods_composite_sort_ll);
        htolll = (LinearLayout) poprootview.findViewById(R.id.pop_seletgoods_hightolow_ll);
        ltohll = (LinearLayout) poprootview.findViewById(R.id.pop_seletgoods_lowtohigh_ll);
        creditll = (LinearLayout) poprootview.findViewById(R.id.pop_seletgoods_credit_ll);
        paddingleft = (int)getResources().getDimension(R.dimen.goods_select_popwindow_offsetx);
        int tempWidh = BaseApplication.width/4 - paddingleft -1;
        sortPopWindow = new PopupWindow(poprootview, tempWidh, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        sortPopWindow.setContentView(poprootview);
        sortPopWindow.setOutsideTouchable(true);
        sortPopWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_null));
//        sortPopWindow.setAnimationStyle(R.style.popwin_anim_left_style);
        sortPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                recoverTabStyle();
            }
        });
        sortPopWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    recoverTabStyle();
                    sortPopWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
    }


    private void initListener() {
        if (!isInEditMode()) {
            selectGoodsBarSortLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectGoodsBarSortLl.setBackgroundResource(R.color.app_subtheme_press);
                    selectGoodsBarSortIv.setImageResource(R.drawable.icon_category_wine);
                    selectGoodsBarSortTv.setTextColor(context.getResources().getColor(R.color.app_theme));
                    sortPopWindow.showAsDropDown(selectGoodsBarSortLl, paddingleft, -DisplayUtil.dp2px(context, 8));
                }
            });

            sortll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGoodsBarSortTv.setText("综合排序");
                    mPopListener.setPopListener(POP_SORTTYPE_COMPSITE);
                    sortPopWindow.dismiss();
                }
            });

            htolll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGoodsBarSortTv.setText("从高到低");
                    mPopListener.setPopListener(POP_SORTTYPE_HTOL);
                    sortPopWindow.dismiss();
                }
            });

            ltohll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGoodsBarSortTv.setText("从低到高");
                    mPopListener.setPopListener(POP_SORTTYPE_LTOH);
                    sortPopWindow.dismiss();
                }
            });
            creditll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGoodsBarSortTv.setText("信用排序");
                    mPopListener.setPopListener(POP_SORTTYPE_CREDIT);
                    sortPopWindow.dismiss();
                }
            });

        }
    }

    private void recoverTabStyle(){
        selectGoodsBarSortLl.setBackgroundResource(R.drawable.selector_subtheme_white);
        selectGoodsBarSortIv.setImageResource(R.drawable.icon_category_black);
        selectGoodsBarSortTv.setTextColor(context.getResources().getColor(R.color.black));
    }

    public interface PopListener {
        void setPopListener(int position);

    }

    public void setOnPopListener(PopListener popListener) {
        mPopListener = popListener;
    }

    public void setSortTypeText(String sortTypeText){
        selectGoodsBarSortTv.setText(sortTypeText);
    }

}
