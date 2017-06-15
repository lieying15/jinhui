package com.thlh.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/12.
 */
public  class AnimatCartUtils {
    public static final int ANIMA_TYPE_GOODS_LIST = 0;
    public static final int ANIMA_TYPE_GOODS_DETAIL = 1;
    private int animtype ;
    private  Context context;
    private  View endView;
    //动画时间
    private  int AnimationDuration = 1000;
    //正在执行的动画数量
    private  int number = 0;
    //是否完成清理
    private  boolean isClean = false;
    private  FrameLayout animation_viewGroup;
    private OnAnimationEndEvent onAnimationEndEvent;

    public AnimatCartUtils(Context context, View endView,int animtype) {
        this.context = context;
        this.endView = endView;
        this.animtype = animtype;
        animation_viewGroup = createAnimLayout();
    }

    public AnimatCartUtils(Context context, View endView) {
        this.context = context;
        this.endView = endView;
        this.animtype = 0;
        animation_viewGroup = createAnimLayout();
    }

    /**
     * @Description: 创建动画层
     */
    private  FrameLayout createAnimLayout(){
        ViewGroup rootView = (ViewGroup)((Activity)context).getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }
    
    public  void startShopCartAnim(int[] start_location,Drawable drawable){
        if(!isClean){
            setAnim(drawable,start_location);
        }else{
            try{
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable,start_location);
            }catch(Exception e){
                e.printStackTrace();
            }
            finally{
                isClean = true;
            }
        }
    }


    /**
     * @deprecated 将要执行动画的view 添加到动画层
     * @param vg
     *        动画运行的层 这里是frameLayout
     * @param view
     *        要运行动画的View
     * @param location
     *        动画的起始位置
     * @return
     */
    private  View addViewToAnimLayout(ViewGroup vg,View view,int[] location){
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dip2px(context,90),dip2px(context,90));
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);
        return view;
    }
    
    




    /**
     * dip，dp转化成px 用来处理不同分辨路的屏幕
     * @param context
     * @param dpValue
     * @return
     */
    private  int dip2px(Context context,float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale +0.5f);
    }

    /**
     * 动画效果设置
     * @param drawable
     *       将要加入购物车的商品
     * @param start_location
     *        起始位置
     */
    private  void setAnim(Drawable drawable,int[] start_location){
        Animation mScaleAnimation = new ScaleAnimation(1.5f,0.0f,1.5f,0.0f,Animation.RELATIVE_TO_SELF,0.1f,Animation.RELATIVE_TO_SELF,0.1f);
        mScaleAnimation.setDuration(AnimationDuration);
        mScaleAnimation.setFillAfter(true);

        final ImageView iview = new ImageView(context);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview, start_location);
        int[] end_location = new int[2];
        endView.getLocationInWindow(end_location);
        int endX = end_location[0] - start_location[0];
        int endY =  Math.abs(end_location[1] - start_location[1]);

        AnimationSet mAnimationSet = new AnimationSet(true);
        mAnimationSet.setFillAfter(true);
        switch (animtype){
            case ANIMA_TYPE_GOODS_LIST:
                Animation mTranslateAnimation0 = new TranslateAnimation(0,0,0,-200);
                mTranslateAnimation0.setDuration(300);
                Animation mTranslateAnimation1 = new TranslateAnimation(0,endX,0,endY + 200);
                mTranslateAnimation1.setDuration(AnimationDuration);
                mAnimationSet.addAnimation(mScaleAnimation);
                mAnimationSet.addAnimation(mTranslateAnimation0);
                mAnimationSet.addAnimation(mTranslateAnimation1);
                break;
            case ANIMA_TYPE_GOODS_DETAIL:
                Animation mTranslateAnimation2 = new TranslateAnimation(0,endX,0,endY);
                mTranslateAnimation2.setDuration(AnimationDuration);
                mAnimationSet.addAnimation(mScaleAnimation);
                mAnimationSet.addAnimation(mTranslateAnimation2);
                break;
        }




        mAnimationSet.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                number--;
                if(number==0){
                    isClean = true;
                }
                if (AnimatCartUtils.this.onAnimationEndEvent != null){
                    AnimatCartUtils.this.onAnimationEndEvent.animationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(mAnimationSet);
    }


    private void clear() {
        isClean = true;
        try{
            animation_viewGroup.removeAllViews();
        }catch(Exception e){
            e.printStackTrace();
        }
        isClean = false;
    }


    public void setAnimationEndEvent(OnAnimationEndEvent onAnimationEndEvent) {
        this.onAnimationEndEvent = onAnimationEndEvent;
    }

    public interface OnAnimationEndEvent {
        void animationEnd();
    }
}
