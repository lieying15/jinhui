package com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.kingsoft.media.httpcache.KSYProxyService;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.model.Comment;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsDetailProperty;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.NetUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.buyconfirm.BuyConfirmActivity;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.goods.SupplierGoodsActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo.adapter.GoodsCommentAdapter;
import com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo.adapter.GoodsPropertyAdapter;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitActivity;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.recharge.RechargeQRActivity;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.SlideDetailsLayout;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.progress.CircleProgressBar;
import com.thlh.viewlib.ripple.RippleLinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsInfoFragment extends BaseFragment implements GoodsInfoContract.View, SlideDetailsLayout.OnSlideDetailsListener {
    private static final String TAG = "GoodsInfoFragment";
    @BindView(R.id.goodsdetail_goods_iv)
    ImageView goodsdetailGoodsIv;
    @BindView(R.id.goodsdetail_goods_back_iv)
    ImageView goodsBackgroundIv;
    @BindView(R.id.video_plview)
    PLVideoView videoPlview;
    @BindView(R.id.vedio_circleprogressbar)
    CircleProgressBar videoProgressbar;
    @BindView(R.id.video_loading_cover)
    FrameLayout videoLoadingCover;
    @BindView(R.id.goodsdetail_goods_videoplay_iv)
    ImageView videoplayIv;
    @BindView(R.id.goodsdetail_goods_videoplay_fl)
    FrameLayout videoplayFl;
    @BindView(R.id.goodsdetail_name_tv)
    TextView goodsdetailNameTv;
    @BindView(R.id.goodsdetail_describle_tv)
    TextView goodsdetailDescribleTv;
    @BindView(R.id.goodsdetail_price_tv)
    TextView goodsdetailPriceTv;
    @BindView(R.id.goodsdetail_mjz_tv)
    TextView goodsdetailMjzTv;
    @BindView(R.id.goodsdetail_proterty_rv)
    EasyRecyclerView goodsdetailProtertyRv;
    @BindView(R.id.goodsdetail_promotion_set_tv)
    TextView goodsdetailPromotionSetTv;
    @BindView(R.id.goodsdetail_promotion_coupon_tv)
    TextView goodsdetailPromotionCouponTv;
    @BindView(R.id.goodsdetail_promotion_ll)
    LinearLayout goodsdetailPromotionLl;
    @BindView(R.id.goodsdetail_interval_delivery_ll)
    LinearLayout goodsdetailIntervalDeliveryLl;
    @BindView(R.id.goodsdetail_supplier_tv)
    TextView goodsdetailSupplierTv;
    @BindView(R.id.goodsdetail_supplier_ll)
    LinearLayout goodsdetailSupplierLl;
    @BindView(R.id.goodsdetail_comment_tv)
    TextView goodsdetailCommentTv;
    @BindView(R.id.goodsdetail_comment_goodsrate_tv)
    TextView goodsdetailCommentGoodsrateTv;
    @BindView(R.id.goodsdetail_comment_ll)
    LinearLayout goodsdetailCommentLl;
    @BindView(R.id.goodsdetail_comment_rv)
    EasyRecyclerView goodsdetailCommentRv;
    @BindView(R.id.video_content_rl)
    RelativeLayout plVideoViewRl;
    @BindView(R.id.goodsdetail_ispack_tv)
    TextView goodsdetailIspackTv;
    @BindView(R.id.goodsdetail_ispack_ll)
    LinearLayout goodsdetailIspackLl;
    @BindView(R.id.goodsdetail_ispart_ll)
    LinearLayout goodsdetailIspartLl;
    
    @BindView(R.id.goodsdetail_scroll)
    ScrollView goodsdetailScroll;
    @BindView(R.id.goodsdetail_slidelayout)
    SlideDetailsLayout goodsdetailSlidelayout;
    @BindView(R.id.goodsdetail_floatbtn)
    FloatingActionButton goodsdetailFloatbtn;

    @BindView(R.id.goodsdetail_webview)
    WebView goodsdetailWebView;


    private GoodsDetailV3Activity activity;
    private String goods_id;
    private String supplier_id;
    private String supplier_name;
    private GoodsInfoContract.Presenter mPresenter;
    private GoodsPropertyAdapter propertyAdapter;
    private GoodsCommentAdapter commentAdapter;
    private ArrayList<GoodsBundling> goodsBundlings;

    private boolean videoPlaying;
    private AVOptions avOptions;


    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_goodsdetail_info;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GoodsDetailV3Activity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof GoodsDetailV3Activity) {
                this.activity = (GoodsDetailV3Activity) activity;
            } else {
                throw new RuntimeException(activity.toString() + " must implement ABC_Listener");
            }
        }
    }

    @Override
    protected void initView() {
        mPresenter = new GoodsInfoPresenter(this);
        initVideo();
        propertyAdapter = new GoodsPropertyAdapter();
        goodsdetailProtertyRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        goodsdetailProtertyRv.setAdapter(propertyAdapter);
        goodsdetailProtertyRv.setNestedScrollingEnabled(false);
        goodsdetailSlidelayout.setOnSlideDetailsListener(this);

        commentAdapter = new GoodsCommentAdapter(getActivity());
        goodsdetailCommentRv.setAdapter(commentAdapter);
        goodsdetailCommentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        goodsdetailCommentRv.setNestedScrollingEnabled(false);
        goodsdetailCommentRv.addItemDecoration(new VerticalltemDecoration(getResources().getDimensionPixelSize(R.dimen.y2)));
        activity.setAddCartListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleView) {
                activity.showAddCartDialog();
                mPresenter.addShopCart();
                activity.updateCartTv();
            }
        });
        activity.setBuytListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                if (SPUtils.getIsLogin()){
                    mPresenter.buyImmediately();
                    BuyConfirmActivity.activityStart(getActivity(),true);
                }else {
                    LoginActivity.activityStart(getActivity());
                }

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
        mPresenter.loadGoodsDetail(goods_id);
        mPresenter.loadComment(goods_id);
    }

    @Override
    public void updateCollection(boolean isCollected) {
        activity.updateCollection(isCollected);
    }

    @Override
    public void showLoadingBar() {
        activity.getProgressMaterial().show();
    }

    @Override
    public void hideLoadindBar() {
        activity.getProgressMaterial().dismiss();
    }

    @Override
    public void showPlayVideoHintDialog() {
        final NormalDialogFragment normalFrgDialog = new NormalDialogFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        normalFrgDialog.setContentStr(getResources().getString(R.string.ice_play));
        normalFrgDialog.setContentType(DialogUtils.TYPE_NORMAL_WARNING);
        normalFrgDialog.setMiddleBtnVisible(View.VISIBLE);
        normalFrgDialog.setMiddleBtnStr(getResources().getString(R.string.shopcart_total_cannal));
        normalFrgDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalFrgDialog.dismiss();
            }
        });
        normalFrgDialog.setFinalBtnStr(getResources().getString(R.string.shopcart_total_confirm));
        normalFrgDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plVideoViewRl.setVisibility(View.VISIBLE);
                videoplayIv.setVisibility(View.GONE);
                goodsdetailGoodsIv.setVisibility(View.GONE);
                goodsBackgroundIv.setVisibility(View.GONE);
                videoPlaying = true;
                videoPlview.start();
                normalFrgDialog.dismiss();
            }
        });
        normalFrgDialog.show(ft, "normalFrgDialog");
    }

    @Override
    public void showGoodsVideo(String mVideoUrl, String mVideoPic) {
        videoplayIv.setVisibility(View.VISIBLE);
        videoplayFl.setVisibility(View.VISIBLE);
        ImageLoader.display(mVideoPic, goodsdetailGoodsIv, true);
        String proxyUrl = initproxy().getProxyUrl(mVideoUrl); //金山云视频缓存
        videoPlview.setVideoPath(proxyUrl);
    }


    private KSYProxyService initproxy() {
        KSYProxyService proxy = BaseApplication.getKSYProxy(getActivity());
        proxy.setCacheRoot(new File(Environment.getExternalStorageDirectory(), "cachevedio"));
//        proxy.registerCacheStatusListener(this, mVideoPath);
//        proxy.registerErrorListener(this);
        proxy.setMaxCacheSize(500 * 1024 * 1024);
        proxy.startServer();
        return proxy;
    }

    @Override
    public void showGoodsImg(String imgUrl, String imgThumbUrl) {
        videoplayIv.setVisibility(View.GONE);
        videoplayFl.setVisibility(View.GONE);
        plVideoViewRl.setVisibility(View.GONE);
        goodsdetailGoodsIv.setVisibility(View.VISIBLE);
        if (imgUrl.equals("")) {
            ImageLoader.display(imgThumbUrl, goodsdetailGoodsIv, Priority.HIGH);
        } else {
            ImageLoader.display(imgUrl, goodsdetailGoodsIv, Priority.HIGH);
        }
    }

    @Override
    public void showGoodsSuit(boolean isBudingPart, String partOfId) {
        goodsdetailIspartLl.setVisibility(View.VISIBLE);
        activity.setBottomCartText(getResources().getString(R.string.suit));

        activity.setAddCartListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleView) {
                if (isBudingPart) {
                    GoodsSuitDetailActivity.activityStart(getActivity(), partOfId, 0);
                } else {
                    GoodsDetailV3Activity.activityStart(getActivity(), partOfId);
                }
            }
        });
    }

    @Override
    public void showGoodsPack(int packNum) {
        goodsdetailIspackLl.setVisibility(View.VISIBLE);
        goodsdetailIspackTv.setText("*" + packNum + getResources().getString(R.string.suit_sell));
        activity.setAddCartListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleView) {
                activity.showAddCartDialog();
                for (int i = 0; i < packNum; i++) {
                    mPresenter.addShopCart();
                }
                activity.updateCartTv();
            }
        });
    }

    @Override
    public void showGoodsStatusNoStorage() {
        goodsBackgroundIv.setVisibility(View.VISIBLE);
        activity.setAddCartListener(null);
        activity.setBottomCartText(getResources().getString(R.string.gone), false);
        activity.getGoodsBottomBuyRl().setVisibility(View.GONE);
    }

    @Override
    public void showGoodsStatusOffShelves() {
        goodsBackgroundIv.setVisibility(View.VISIBLE);
        activity.setAddCartListener(null);
        activity.setBottomCartText(getResources().getString(R.string.shelves), false);
    }

    @Override
    public void showGoodsStatusPrepare() {
        goodsBackgroundIv.setVisibility(View.VISIBLE);
        activity.setAddCartListener(null);
        activity.setBottomCartText(getResources().getString(R.string.stock), false);
    }


    @Override
    public void showGoodsLimit(String num) {
        goodsdetailDescribleTv.setText(getResources().getString(R.string.every_limit) + num + getResources().getString(R.string.ones));
    }

    @Override
    public void showGoodsName(String goodsName) {
        goodsdetailNameTv.setText(goodsName);
    }

    @Override
    public void showGoodsHint(String hint) {
        goodsdetailDescribleTv.setText(hint);
    }

    @Override
    public void showGoodsPrice(double price, double mjz, String ismjz) {
        String priceStr = TextUtils.showPrice(price);
        String mjzStr = TextUtils.showPrice(mjz);
        //
        goodsdetailPriceTv.setText(getResources().getString(R.string.money_) + priceStr);
        int textx =  (int)getResources().getDimension(com.thlh.baselib.R.dimen.x22);
        int texty = (int)getResources().getDimension(com.thlh.baselib.R.dimen.y22);

        switch (ismjz) {
            case "1":
                goodsdetailMjzTv.setVisibility(View.VISIBLE);
                goodsdetailMjzTv.setText(TextUtils.showMjz(getActivity(), priceStr,textx,texty));
                break;
            case "2":
                goodsdetailMjzTv.setVisibility(View.VISIBLE);
                goodsdetailMjzTv.setText(TextUtils.showMjz(getActivity(), mjzStr,textx,texty));
                break;
        }
    }

    @Override
    public void showGoodsProperty(List<GoodsDetailProperty> mPropertyList) {
        propertyAdapter.setList(mPropertyList);

    }

    /**
     * 显示冰箱页
     */
    @Override
    public void showGoodsIsIceBox() {
//        goodsdetailPriceTv.setText(getResources().getString(R.string.goodsdetail_addcart));
//        goodsdetailPriceTv.setTextColor(getResources().getColor(R.color.white));
//        goodsdetailPriceTv.setBackgroundResource(R.drawable.shap_radius_theme_r20);
//        goodsdetailPriceTv.setPadding((int)getResources().getDimension(R.dimen.x20),0,(int)getResources().getDimension(R.dimen.x20),0);
        goodsdetailMjzTv.setVisibility(View.GONE);
        goodsdetailProtertyRv.setVisibility(View.GONE);
        activity.setAddCartListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                int isch = Integer.valueOf(SPUtils.get("user_isch",0).toString());
                if(isch>0){
//                    RechargeActivity.activityStart(getActivity(),Constants.PAY_PURPOSE_MJB);
                    activity.showAddCartDialog();
                    mPresenter.addShopCart();
                    activity.updateCartTv();
                }else {
                    RechargeQRActivity.activityStart(getActivity());
                }
            }
        });
        activity.setBottomCartText(getResources().getString(R.string.goodsdetail_addcart), true);
      /*  goodsdetailPriceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isch = Integer.valueOf(SPUtils.get("user_isch",0).toString());
                if(isch>0){
                    RechargeActivity.activityStart(getActivity(),Constants.PAY_PURPOSE_MJB);
                }else {
                    RechargeQRActivity.activityStart(getActivity());
                }
            }
        });*/
    }

    @Override
    public void showGoodsSupplier(String suppliername, String supplierId) {
        supplier_name = suppliername;
        supplier_id = supplierId;
        Spannable supplierSpan = new SpannableString(getResources().getString(R.string.will) + suppliername + getResources().getString(R.string.offer_service));
        supplierSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme)), 2, supplierSpan.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsdetailSupplierTv.setText(supplierSpan);
    }


    @Override
    public void showGoodsBunding(ArrayList<GoodsBundling> goodsBundlings) {
        this.goodsBundlings = goodsBundlings;
        if (goodsBundlings != null)
            goodsdetailPromotionLl.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.goodsdetail_supplier_ll, R.id.goodsdetail_promotion_ll,R.id.goodsdetail_floatbtn,
        R.id.goodsdetail_comment_ll,R.id.goodsdetail_goods_videoplay_fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goodsdetail_supplier_ll:
                SupplierGoodsActivity.activityStart(getActivity(), supplier_id, supplier_name);

                break;
            case R.id.goodsdetail_promotion_ll:
                GoodsSuitActivity.activityStart(getActivity(), goodsBundlings);
                break;
            case R.id.goodsdetail_floatbtn:
                gotoTop();
                break;
            case R.id.goodsdetail_comment_ll:
                activity.changeToCommentFrg();
                break;
            case R.id.goodsdetail_goods_videoplay_fl:
                L.e(TAG + " videoplayIv OnClickListener" );
                if(videoPlaying){
                    videoPlaying = false;
                    videoPlview.pause();
                    videoplayIv.setVisibility(View.VISIBLE);
                    plVideoViewRl.setVisibility(View.VISIBLE);
                }else {
                    if(NetUtils.isUseFlow(getActivity())){
                        videoplayIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPlayVideoHintDialog();
                            }
                        });
                    }else {
                        L.i(TAG + " now UseFlow" );
                        plVideoViewRl.setVisibility(View.VISIBLE);
                        videoplayIv.setVisibility(View.GONE);
                        goodsBackgroundIv.setVisibility(View.GONE);
                        videoPlaying = true;
                        videoPlview.start();
                    }
//                    videoPlview.start();
//                    plVideoViewRl.setVisibility(View.GONE);
                }
                break;
        }
    }
    /**
    * questions
    * 1.去掉间隙
    * 2.详情图上拉没有出现全图，直接拉到商品页
    * 3,详情页上要添加“— 商品简介 —”
    * */
    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.OPEN) {
            //当前为图文详情页
            goodsdetailFloatbtn.show();
            activity.getGoodsdetailContentVp().setNoScroll(true);
            activity.getGoodsdetailTab().setCurrentTab(1);

        } else {
            //当前为商品详情页
            goodsdetailFloatbtn.hide();
            activity.getGoodsdetailContentVp().setNoScroll(false);
            activity.getGoodsdetailTab().setCurrentTab(0);
            gotoTop();
        }
    }

    @Override
    public void updateComment(List<Comment> comments,int total,double goodrate) {
        L.i(TAG + "comment onNextResponse ");
        if (comments == null || comments.size() == 0) {
            goodsdetailCommentLl.setVisibility(View.GONE);
            goodsdetailCommentRv.setVisibility(View.GONE);
            return;
        }
        goodsdetailCommentLl.setVisibility(View.VISIBLE);
        goodsdetailCommentRv.setVisibility(View.VISIBLE);
        commentAdapter.setList(comments);
        goodsdetailCommentTv.setText("(" + total + ")");
        goodsdetailCommentGoodsrateTv.setText(getResources().getString(R.string.good_probability) + goodrate + "%)");
    }

    @Override
    public void showGoodsDetail(String content) {
        activity.setGoodsContent(content);
        goodsdetailWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        goodsdetailWebView.setMinimumWidth(BaseApplication.width);
        WebSettings webSettings = goodsdetailWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(150);
        webSettings.setRenderPriority(WebSettings.RenderPriority.LOW);
    }

    @Override
    public void hideImmediatelyBuy() {
        activity.getGoodsBottomCartRl().setBackgroundResource(R.drawable.selector_theme);
        activity.getGoodsBottomBuyRl().setVisibility(View.GONE);
    }

    private  void initVideo(){
        avOptions = new AVOptions();
        // 解码方式，codec＝1，硬解; codec=0, 软解
        // 默认值是：0
        avOptions.setInteger(AVOptions.KEY_MEDIACODEC, 1);

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        // 默认值是：无
        avOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        // 读取视频流超时时间，单位是 ms
        // 默认值是：10 * 1000
        avOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

        // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
        // 默认值是：0
        avOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);

        // 是否开启"延时优化"，只在在线直播流中有效
        // 默认值是：0
        avOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 默认的缓存大小，单位是 ms
        // 默认值是：2000
        avOptions.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

        // 最大的缓存大小，单位是 ms
        // 默认值是：4000
        avOptions.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
        // 默认值是：1
        avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        videoPlview.setAVOptions(avOptions);
        videoPlview.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        videoPlview.setOnCompletionListener(mOnCompletionListener);
        videoPlview.setOnInfoListener(mOnInfoListener);
        videoPlview.setOnErrorListener(mOnErrorListener);
        videoPlview.setOnPreparedListener(mOnPreparedListener);
        videoPlview.setMinimumWidth(BaseApplication.width);
        videoPlview.setLooping(true);


    }

    /**
     * 设置缓冲进度更新监听器
     */
    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int precent) {
            if(videoProgressbar!=null)
            videoProgressbar.setProgress(precent);
        }
    };

    /**
     * 视频播放结束回调此方法
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            if(videoPlview!=null)
            videoPlview.start();
        }
    };

    @Override
    public void updateViewPath(String path) {
        if(videoPlview!=null)
            videoPlview.setVideoPath(path);
    }

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    videoPlview.pause();
                    videoLoadingCover.setVisibility(View.VISIBLE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    videoLoadingCover.setVisibility(View.GONE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    videoPlview.start();
                    videoLoadingCover.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return true;
        }
    };


    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer,int e) {
//            if (null == goodsDetail.getVideo().get(0).getUrl()) {
                plVideoViewRl.setVisibility(View.GONE);
                videoplayIv.setVisibility(View.GONE);
                goodsdetailGoodsIv.setVisibility(View.VISIBLE);
                goodsBackgroundIv.setVisibility(View.VISIBLE);
                videoPlaying = false;
//            }
            return true;
        }
    };


    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp) {
            goodsdetailGoodsIv.setVisibility(View.GONE);
        }
    };

    @Override
    public void updateShareInfo(String name, String content,String img) {
        activity.setGoodsShareInfo(name,content,img);
    }

    @Override
    public void gotoTop() {
        goodsdetailScroll.smoothScrollTo(0, 0);
        goodsdetailSlidelayout.smoothClose(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(videoPlaying && videoPlview!=null){
            videoPlview.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if( videoPlview!=null) videoPlview.pause();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if( videoPlview!=null) videoPlview.stopPlayback();
    }

}
