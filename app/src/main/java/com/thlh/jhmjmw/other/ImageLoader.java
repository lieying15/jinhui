package com.thlh.jhmjmw.other;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.thlh.baselib.R;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.utils.GlideRoundTransform;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ImageLoader {

    public static void display(String path, ImageView view){
        Glide.with(BaseApplication.getInstance())
                .load(Deployment.IMAGE_PATH +path)
                .placeholder(R.drawable.img_loading)//加载中显示的图片
//                .placeholder(R.drawable.load2)//加载中显示的图片
                .error(R.drawable.img_empty)//加载失败时显示的图片
                .into(view);
    }

    //显示优先级的
    public static void display(String path, ImageView view,Priority priority){
        Glide.with(BaseApplication.getInstance())
                .load(Deployment.IMAGE_PATH +path)
                .placeholder(R.drawable.img_loading)//加载中显示的图片
//                .placeholder(R.drawable.load2)//加载中显示的图片
                .error(R.drawable.img_empty)//加载失败时显示的图片
                .priority(priority)
                .into(view);
    }
    //不带前缀，显示本地图片
    public static void display(String path, ImageView view,boolean islocation){
        Glide.with(BaseApplication.getInstance())
                .load(path)
                .placeholder(R.drawable.img_loading)//加载中显示的图片
                .error(R.drawable.img_empty)//加载失败时显示的图片
                .into(view);
    }

    public static void displayRoundImg(String path, ImageView view){
        int radius = (int) BaseApplication.getInstance().getBaseContext().getResources().getDimension(com.thlh.jhmjmw.R.dimen.x10);
        Glide.with(BaseApplication.getInstance())
                .load(Deployment.IMAGE_PATH +path)
                .placeholder(R.drawable.img_loading)//加载中显示的图片
//                .error(defautlt)//加载失败时显示的图片
                .transform(new GlideRoundTransform(BaseApplication.getInstance().getBaseContext(),radius))
                .into(view);
    }

    public static void display(String path, ImageView view, int defautlt, BitmapTransformation bitmapTransformation){
        Glide.with(BaseApplication.getInstance())
                .load(Deployment.IMAGE_PATH +path)
                .placeholder(defautlt)//加载中显示的图片
                .error(defautlt)//加载失败时显示的图片
                .transform(bitmapTransformation)
                .into(view);
    }
}
