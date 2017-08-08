package com.thlh.jhmjmw.business.goods.suit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.GoodsDetail;
import com.thlh.baselib.model.Supplier;
import com.thlh.baselib.model.response.GoodsDetailResponse;
import com.thlh.baselib.utils.AnimatCartUtils;
import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.S;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.suit.adapter.GoodsSuitContentAdapter;
import com.thlh.jhmjmw.business.goods.suit.adapter.GoodsSuitNameAdapter;
import com.thlh.jhmjmw.business.buy.shopcart.ShopCartActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.tablayout.MsgView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 套裝商品详情页
 */
public class GoodsSuitDetailActivity extends BaseActivity {
    private static final String TAG = "GoodsSuitDetailActivity";
    private final int ACTIVITY_CODE_LOGIN = 1;
    @BindView(R.id.goodsdetail_header)
    HeaderNormal goodsdetailHeader;
    @BindView(R.id.goodsdetail_goods_iv)
    ImageView goodsdetailGoodsIv;
    @BindView(R.id.goodsdetail_goods_back_iv)
    ImageView goodsdetailGoodsBackIv;
    @BindView(R.id.goodsdetail_name_tv)
    TextView goodsdetailNameTv;
    @BindView(R.id.goodsdetail_describle_tv)
    TextView goodsdetailDescribleTv;
    @BindView(R.id.goodsdetail_price_tv)
    TextView goodsdetailPriceTv;
    @BindView(R.id.goodsdetail_limit_line)
    View goodsdetailLimitLine;
    @BindView(R.id.goodsdetail_limit_tv)
    TextView goodsdetailLimitTv;
    @BindView(R.id.goodsdetail_name_rv)
    EasyRecyclerView goodsdetailNameRv;
    @BindView(R.id.homgpage_goods_title_tv)
    TextView homgpageGoodsTitleTv;
    @BindView(R.id.goods_suitdetail_webview)
    WebView goodsdetailWebView;
    @BindView(R.id.goodsdetail_goods_rv)
    EasyRecyclerView goodsdetailGoodsRv;
    @BindView(R.id.goods_suitdetail_scroll)
    NestedScrollView goodsSuitdetailScroll;
    @BindView(R.id.suitdetail_bottom_totalprice_title_tv)
    TextView suitdetailBottomTotalpriceTitleTv;
    @BindView(R.id.suitdetail_bottom_totalprice_tv)
    TextView suitdetailBottomTotalpriceTv;
    @BindView(R.id.suitdetail_bottom_shopcart_iv)
    ImageView suitdetailBottomShopcartIv;
    @BindView(R.id.suitdetail_bottom_num_tv)
    MsgView suitdetailBottomNumTv;
    @BindView(R.id.suitdetail_bottom_shopcart_rl)
    RelativeLayout suitdetailBottomShopcartRl;
    @BindView(R.id.suitdetail_bottom_selectall_rl)
    RelativeLayout suitdetailBottomSelectallRl;
    @BindView(R.id.suitdetail_bottom_clearing_tv)
    TextView suitdetailBottomClearingTv;
    @BindView(R.id.suitdetail_bottom_addcart_ll)
    LinearLayout suitdetailBottomAddcartLl;
    @BindView(R.id.goodsdetail_snack_cl)
    CoordinatorLayout goodsdetailSnackCl;


    private GoodsSuitNameAdapter nameAdapter;
    private GoodsSuitContentAdapter contentAdapter;
    private int bundingPosition;
    ;
    private int goods_num;
    private String itemid;
    private Goods goods;
    private GoodsDetail goodsDetail;
    private BaseObserver<GoodsDetailResponse> goodsdetailObserver;
    //动画
    private AnimatCartUtils animatCart;
    private boolean isLogin;

    private Gson gson;
    private GsonBuilder builder;
    private DialogNormal.Builder prepareDialog;
    private boolean isPrepare = false;
    private String url;


    public static void activityStart(Activity context, String itemid, int bundingPosition) {
        Intent intent = new Intent();
        intent.putExtra("bundingPosition", bundingPosition);
        intent.putExtra("itemid", itemid);
        intent.setClass(context, GoodsSuitDetailActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        bundingPosition = getIntent().getIntExtra("bundingPosition", 0);
        itemid = getIntent().getStringExtra("itemid");
        contentAdapter = new GoodsSuitContentAdapter();
        nameAdapter = new GoodsSuitNameAdapter(this);
        L.e(TAG + " bundingPosition " + bundingPosition);
        builder = new GsonBuilder();
        gson = builder.create();
    }


    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_suit_detail);
        ButterKnife.bind(this);
        goodsdetailWebView.setMinimumWidth(BaseApplication.width);
        goodsdetailGoodsRv.setMinimumWidth(BaseApplication.width);
        animatCart = new AnimatCartUtils(this, suitdetailBottomShopcartIv, AnimatCartUtils.ANIMA_TYPE_GOODS_DETAIL);
        goodsdetailHeader.setTitle(getResources().getString(R.string.suit_));

        goodsdetailNameRv.setLayoutManager(new LinearLayoutManager(this));
        goodsdetailNameRv.setAdapter(nameAdapter);

        goodsdetailGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        goodsdetailGoodsRv.setAdapter(contentAdapter);

        goodsdetailObserver = new BaseObserver<GoodsDetailResponse>() {
            @Override
            public void onErrorResponse(GoodsDetailResponse goodsDetailResponse) {
                new S.Builder(goodsdetailSnackCl, goodsDetailResponse.getErr_msg()).create().show();
            }

            @Override
            public void onNextResponse(GoodsDetailResponse goodsDetailResponse) {
                L.i(TAG + "load goodsdetail:  onNextResponse");
                goodsDetail = goodsDetailResponse.getData().getItem();
                if (goodsDetailResponse.getData().getItem().getItem_img_thumb().contains("http")){
                    url = goodsDetailResponse.getData().getItem().getItem_img_thumb();
                }else {
                    url = Deployment.IMAGE_PATH + goodsDetailResponse.getData().getItem().getItem_img_thumb();
                }
                ImageLoader.display(url, goodsdetailGoodsIv, Priority.HIGH);
                if (goodsDetail.getStorage() == 0) {
                    L.e(TAG + "库存为 0 ");
                    goodsdetailGoodsBackIv.setVisibility(View.VISIBLE);
                    suitdetailBottomAddcartLl.setOnClickListener(null);
                    suitdetailBottomAddcartLl.setBackgroundResource(R.color.gray);
                    suitdetailBottomClearingTv.setText(getResources().getString(R.string.gone));
                } else {
                    goodsdetailGoodsBackIv.setVisibility(View.GONE);
                }


                if (goodsDetail.getIs_prepare().equals("1")) {
                    isPrepare = true;
                    goodsdetailGoodsBackIv.setVisibility(View.GONE);
                    suitdetailBottomAddcartLl.setOnClickListener(null);
                    suitdetailBottomAddcartLl.setBackgroundResource(R.color.gray);
                    suitdetailBottomClearingTv.setText(getResources().getString(R.string.stock));
                }

                goodsdetailNameTv.setText(goodsDetailResponse.getData().getItem().getItem_name());
                if (goodsDetail.getIs_limit().equals("1") && !goodsDetail.getLimit_num().equals("0")) {
                    goodsdetailLimitLine.setVisibility(View.VISIBLE);
                    goodsdetailLimitTv.setVisibility(View.VISIBLE);
                    goodsdetailLimitTv.setText(getResources().getString(R.string.every_limit) + goodsDetail.getLimit_num() + getResources().getString(R.string.ones));
                }
                Spannable priceSpan = new SpannableString(getResources().getString(R.string.money) + goodsDetailResponse.getData().getItem().getItem_price());
                priceSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(GoodsSuitDetailActivity.this, 13)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                goodsdetailPriceTv.setText(priceSpan);
                suitdetailBottomTotalpriceTv.setText(getResources().getString(R.string.money) + goodsDetailResponse.getData().getItem().getItem_price());
                goodsdetailDescribleTv.setText(goodsDetailResponse.getData().getItem().getItem_subtitle());
                List<GoodsBundling> bundlingList = goodsDetailResponse.getData().getItem().getBundling();
                L.i(TAG + "load goodsdetail:  bundlingList size " + bundlingList.size());
                contentAdapter.setList(bundlingList.get(0).getItem());
                nameAdapter.setList(bundlingList.get(0).getItem());
                nameAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View convertView, int position) {
                        GoodsDetailV3Activity.activityStart(GoodsSuitDetailActivity.this, ((GoodsBundlingItem) nameAdapter.getItem(position)).getItem_id());
                    }
                });

                String goodsCotent = goodsDetail.getContent();
                if (goodsCotent != null && !goodsCotent.equals("")) {
                    String tempgoodsCotent = changeContent(goodsCotent);
                    String CSS_STYPE = "<head><style>p,body{margin: 0;}img{width: 100%;}</style></head>";
                    goodsdetailWebView.loadDataWithBaseURL(null, CSS_STYPE + tempgoodsCotent, "text/html", "utf-8", null);
                    WebSettings webSettings = goodsdetailWebView.getSettings();
                    webSettings.setUseWideViewPort(true);
                    webSettings.setLoadWithOverviewMode(true);
                    webSettings.setTextZoom(170);
                    webSettings.setRenderPriority(WebSettings.RenderPriority.LOW);
                    goodsdetailGoodsRv.setVisibility(View.GONE);
                    goodsdetailWebView.setVisibility(View.VISIBLE);
                } else {
                    goodsdetailGoodsRv.setVisibility(View.VISIBLE);
                    goodsdetailWebView.setVisibility(View.GONE);
                }
            }
        };
    }


    @Override
    protected void loadData() {
        L.e(TAG + " load goodsdetail:  " + itemid);
        subscription = NetworkManager.getGoodsDataApi()
                .getGoodsDetail(SPUtils.getToken(), itemid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goodsdetailObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLogin = SPUtils.getIsLogin();
        goods_num = DbManager.getInstance().getAllGoodsNum();
        if (goods_num != 0) {
            suitdetailBottomNumTv.setVisibility(View.VISIBLE);
            suitdetailBottomNumTv.setText("" + goods_num);
        } else {
            suitdetailBottomNumTv.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.suitdetail_bottom_shopcart_rl, R.id.suitdetail_bottom_addcart_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.suitdetail_bottom_shopcart_rl:
                ShopCartActivity.activityStart(this);
                break;
            case R.id.suitdetail_bottom_addcart_ll:
                if (isPrepare) {
                    prepareDialog.setSubTitle(getResources().getString(R.string.shoping_look))
                            .setRightBtnStr(getResources().getString(R.string.back))
                            .setRightClickListener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                } else {
                    animatCart.setAnimationEndEvent(new AnimatCartUtils.OnAnimationEndEvent() {
                        @Override
                        public void animationEnd() {
                            insetShopCart();
                            int num = DbManager.getInstance().getAllGoodsNum();
                            if (num > 0) {
                                suitdetailBottomNumTv.setVisibility(View.VISIBLE);
                                suitdetailBottomNumTv.setText("" + num);
                            } else {
                                suitdetailBottomNumTv.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    int[] item_location = new int[2];
                    goodsdetailGoodsIv.getLocationInWindow(item_location);//获取点击商品图片的位置
                    item_location[0] = goodsdetailGoodsIv.getWidth() / 4;
                    item_location[1] += goodsdetailGoodsIv.getHeight();
                    Drawable drawable = goodsdetailGoodsIv.getDrawable();//复制一个新的商品图标
                    animatCart.startShopCartAnim(item_location, drawable);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_LOGIN:
                    isLogin = SPUtils.getIsLogin();
                    loadData();
                    break;
                default:
                    break;
            }
        }
    }

    private void insetShopCart() {
        GoodsBundling goodsBundling = goodsDetail.getBundling().get(0);
        if (goodsBundling == null) {
            return;
        }
        String bundinginfo = gson.toJson(goodsBundling, GoodsBundling.class);
        Supplier supplier = new Supplier();
        supplier.setId("-1");
        supplier.setName(getResources().getString(R.string.suit_));
        DbManager.getInstance().insertSupplier(supplier);
        goods = GoodsChangeUtils.changeGoods(goodsDetail);
        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        long goodsDbid = DbManager.getInstance().getGoodsDBid(goodsDb.getItem_id());
        if (goodsDbid == -1) {
            goodsDbid = DbManager.getInstance().getGoodsSize() + 1;
        }
        goodsDb.setDbid(goodsDbid);
        DbManager.getInstance().insertGoods(goodsDb);
        Cartgoods insertCartGoods = new Cartgoods();
        insertCartGoods.setGoodsdb(goodsDb);
        insertCartGoods.setDb_goods_id(goodsDbid);
        insertCartGoods.setBunding_info(bundinginfo);
        DbManager.getInstance().insertCart(insertCartGoods);
    }

    private String changeContent(String content) {
        String tempgoodsCotent = content.replaceAll("/BmUploadFile/ueditor/", Deployment.IMAGE_PATH + "/BmUploadFile/ueditor/");
        L.i(TAG + " tempgoodsCotent:" + tempgoodsCotent);
        tempgoodsCotent = tempgoodsCotent.replace("<p><br/></p>", "");
        return tempgoodsCotent;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

}
