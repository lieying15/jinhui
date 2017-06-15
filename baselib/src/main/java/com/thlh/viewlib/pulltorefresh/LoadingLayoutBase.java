package com.thlh.viewlib.pulltorefresh;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by zwenkai on 2015/12/19.
 */
public abstract class LoadingLayoutBase extends FrameLayout implements ILoadingLayout{

    public LoadingLayoutBase(Context context) {
        super(context);
    }

    public LoadingLayoutBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingLayoutBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {

    }

    @Override
    public void setLoadingDrawable(Drawable drawable) {

    }

    @Override
    public void setTextTypeface(Typeface tf) {

    }

    /**
     * get the LoadingLayout height or width
     * 获取加载头部高度
     * @return size
     */
    public abstract int getContentSize();

    /**
     * Call when the widget begins to slide
     * 开始下拉时的回调
     */
    public abstract void pullToRefresh();

    /**
     * Call when the LoadingLayout is fully displayed
     * “加载头部”完全显示时的回调
     */
    public abstract void releaseToRefresh();

    /**
     * Call when the LoadingLayout is sliding
     * 拖动时的回调，参数拖动距离与加载头部高度成正比
     * @param scaleOfLayout scaleOfLayout
     */
    public abstract void onPull(float scaleOfLayout);

    /**
     * Call when the LoadingLayout is fully displayed and the widget has released.
     * Used to prompt the user loading data
     * 释放后刷新时的回调
     */
    public abstract void refreshing();

    /**
     * Call when the data has loaded
     * 初始化未刷新状态
     */
    public abstract void reset();

    public void hideAllViews(){
        hideAllViews(this);
    }

    public void showInvisibleViews() {
        showAllViews(this);
    }

    private void hideAllViews(View view) {
        if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++) {
                hideAllViews(((ViewGroup)view).getChildAt(i));
            }
        } else {
            if(View.VISIBLE == view.getVisibility()) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showAllViews(View view) {
        if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++) {
                showAllViews(((ViewGroup) view).getChildAt(i));
            }
        } else {
            if(View.INVISIBLE == view.getVisibility()) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

}
