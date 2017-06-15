package com.thlh.jhmjmw.other;

import android.app.Activity;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.thlh.baselib.R;
import com.thlh.baselib.utils.DialogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ShareUtils {
    private static final String DEFAULT_CONTENT = "每家美物";
    private static final String DEFAULT_TITLE = "万元冰箱免费送";
    private static final String DEFAULT_URL = "http://v2.mjmw365.com";
    private static  int DEFAULT_IMAGE = R.mipmap.ic_launcher;

    private Activity activity;
    private String title = DEFAULT_TITLE;
    private String content = DEFAULT_CONTENT ;
    private String url = DEFAULT_URL;
    private int imageResource = DEFAULT_IMAGE;
    private String imagePath;



    private static  final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    public ShareUtils(Activity activity,String title,String content,String url,int imageResource){
        this.activity = activity;
        this.title = title;
        this.content = content;
        this.url = url;
        this.imageResource = imageResource;
    }

    public ShareUtils(Activity activity,String title,String content,String url,String imagePath){
        this.activity = activity;
        this.title = title;
        this.content = content;
        this.url = url;
        this.imagePath = imagePath;
    }


    public  void showShareWindow(){
        Glide.with(activity)
                .load(Deployment.IMAGE_PATH + imagePath)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        UMImage umImage;
                        if(resource!=null){
                            umImage =  new UMImage(activity, resource);
                            new ShareAction(activity).setDisplayList(displaylist)
                                    .withText(content)
                                    .withTitle(title)
                                    .withTargetUrl(url)
                                    .withMedia(umImage)
//                                    .setListenerList(new UMShareListener() {
//                                        @Override
//                                        public void onResult(SHARE_MEDIA platform) {
//                                            DialogUtils.showNormal(activity,DialogUtils.TYPE_NORMAL_SUCCESS,"分享成功");
//                                        }
//
//                                        @Override
//                                        public void onError(SHARE_MEDIA platform, Throwable t) {
//                                            DialogUtils.showNormal(activity,DialogUtils.TYPE_NORMAL_ERROR,"分享失败");
//
//                                        }
//
//                                        @Override
//                                        public void onCancel(SHARE_MEDIA platform) {
//                                            DialogUtils.showNormal(activity,DialogUtils.TYPE_NORMAL_WARNING,"分享取消了");
//                                        }
//                                    })
                                    .open();
                            }else {
                                DialogUtils.showNormal((RxAppCompatActivity) activity,DialogUtils.TYPE_NORMAL_ERROR,"分享失败");
                            }
                        }
                    });
    }



;

}
