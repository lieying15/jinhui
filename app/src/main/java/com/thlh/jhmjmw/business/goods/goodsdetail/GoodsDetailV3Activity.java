package com.thlh.jhmjmw.business.goods.goodsdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.ShopCartActivity;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo.GoodsInfoFragment;
import com.thlh.jhmjmw.business.goods.goodsdetail.comment.GoodsCommentTopFragment;
import com.thlh.jhmjmw.business.goods.goodsdetail.imginfo.GoodsImgInfoFragment;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.goodsdetailview.NoScrollViewPager;
import com.thlh.viewlib.ripple.RippleFrameLayout;
import com.thlh.viewlib.ripple.RippleLinearLayout;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.MsgView;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thlh.jhmjmw.R.id.goods_bottom_shopcart;

/**
 * 商品详情页
 */
public class GoodsDetailV3Activity extends BaseActivity implements GoodsDetailContract.View{
    private static final String TAG = "GoodsDetailV3Activity";
    private final int ACTIVITY_CODE_LOGIN = 1;
    @BindView(R.id.goodsdetail_headerback_iv)
    ImageView goodsdetailHeaderbackIv;
    @BindView(R.id.goodsdetail_share_iv)
    ImageView goodsdetailShareIv;
    @BindView(R.id.goodsdetail_tab)
    CommonTabLayout goodsdetailTab;
    @BindView(R.id.goodsdetail_title_tv)
    TextView goodsdetailTitleTv;
    @BindView(R.id.goodsdetail_content_vp)
    NoScrollViewPager goodsdetailContentVp;


    @BindView(R.id.goodsdetail_collect_iv)
    ImageView goodsdetailCollectIv;
    @BindView(R.id.goods_bottom_collect)
    RippleFrameLayout goodsBottomCollect;
    @BindView(R.id.goodsdetail_shopcart_iv)
    ImageView goodsdetailShopcartIv;
    @BindView(R.id.goodsdetail_shopcart_tv)
    TextView goodsdetailShopcartTv;
    @BindView(R.id.goodsdetail_shopcart_num_tv)
    MsgView goodsdetailShopcartNumTv;
    @BindView(goods_bottom_shopcart)
    RelativeLayout goodsBottomShopcart;
    @BindView(R.id.goods_bottom_addcart_tv)
    TextView goodsBottomAddcartTv;
    @BindView(R.id.goods_bottom_addcart)
    RippleLinearLayout goodsBottomCartRl;
    //立即购买
    @BindView(R.id.goods_bottom_buy)
    RippleLinearLayout goodsBottomBuyRl;

    private GoodsDetailContract.Presenter mPresenter;
    private String goods_id;
    private List<Fragment> fragmentList = new ArrayList<>();
    private GoodsInfoFragment goodsInfoFragment;
    private GoodsImgInfoFragment goodsImgInfoFragment;
    private GoodsCommentTopFragment goodsCommentFragment;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private SweetAlertDialog addCartDialog;
    private boolean isCollected ;

    private String goodsName,goodsContent,goodsImg; //分享用的商品信息


    public static void activityStart(Activity context, String goods_id) {
        L.e(TAG + "activityStart goods_id:" + goods_id);
        Intent intent = new Intent();
        intent.putExtra("goods_id", goods_id);
        intent.setClass(context, GoodsDetailV3Activity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    public static void activityStart(Context context, String goods_id) {
        L.e(TAG + "activityStart goods_id:" + goods_id);
        Intent intent = new Intent();
        intent.putExtra("goods_id", goods_id);
        intent.setClass(context, GoodsDetailV3Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        mPresenter = new GoodsDetailPresenter(this);
        goods_id = getIntent().getStringExtra("goods_id");
        L.e(TAG + " goods_id:" + goods_id);
        //初始化tab
        String[] mSegmentTLTitles = {getResources().getString(R.string.shop),getResources().getString(R.string.shop_details),getResources().getString(R.string.shop_evaluation)};
        int[] mIconUnselectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null};
        int[] mIconSelectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null};
        for (int i = 0; i < mSegmentTLTitles.length; i++) {
            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
    }


    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_detail_v3);
        ButterKnife.bind(this);
        addCartDialog = new SweetAlertDialog(GoodsDetailV3Activity.this, SweetAlertDialog.SUCCESS_TYPE);
        addCartDialog.setTitleText(getResources().getString(R.string.add_car));
        goodsBottomCollect.setOnRippleCompleteListener(new RippleFrameLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleFrameLayout rippleRelativeLayout) {
                if(SPUtils.getIsLogin()){
                    //收藏
                    if(isCollected){
                        L.e(TAG +" onComplete isCollected="+isCollected+" postDelCollect");
                        mPresenter.postDelCollect(goods_id);
                    }else {
                        L.e(TAG +" onComplete isCollected="+isCollected+" postCollect");
                        mPresenter.postCollect(goods_id);
                    }
                }else {
                    LoginActivity.activityStart(GoodsDetailV3Activity.this);
                }
            }
        });

        fragmentList.add(goodsInfoFragment = new GoodsInfoFragment());
        fragmentList.add(goodsImgInfoFragment = new GoodsImgInfoFragment());
        fragmentList.add(goodsCommentFragment = new GoodsCommentTopFragment());
        goodsdetailTab.setTabData(mTabEntities);
        goodsdetailTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                goodsdetailContentVp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        goodsdetailContentVp.setOffscreenPageLimit(3);
        goodsdetailContentVp.setAdapter(new GoodsDetailVpAdapter(getSupportFragmentManager()));
        goodsdetailContentVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                goodsdetailTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateCartTv();
    }

    @Override
    protected void loadData() {}

    @Override
    protected void onRestart() {
        super.onRestart();
        updateCartTv();
    }

    @OnClick({R.id.goodsdetail_headerback_iv,R.id.goods_bottom_shopcart,R.id.goodsdetail_share_iv})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.goodsdetail_headerback_iv:
                finish();
                break;
            case R.id.goods_bottom_shopcart:
                ShopCartActivity.activityStart(GoodsDetailV3Activity.this);
                break;
            case R.id.goodsdetail_share_iv:
//                ShareUtils shareUtils = new ShareUtils(this,goodsName,goodsContent,"http://www.mjmw365.com/app_download/mall/index.html",goodsImg);
//                shareUtils.showShareWindow();
                break;
        }
    }

    //ViewPager适配器
    private class GoodsDetailVpAdapter extends FragmentPagerAdapter {
        public GoodsDetailVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("goods_id", goods_id);
            switch (position) {
                case 1:
                    goodsImgInfoFragment.setArguments(args);
                    return goodsImgInfoFragment;
                case 2:
                    goodsCommentFragment.setArguments(args);
                    return goodsCommentFragment;
                default:
                    goodsInfoFragment.setArguments(args);
                    return goodsInfoFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "详情";
                case 2:
                    return "评价";
                default:
                    return "商品";
            }
        }
    }





    public NoScrollViewPager getGoodsdetailContentVp() {
        return goodsdetailContentVp;
    }

    public TextView getGoodsdetailTitleTv() {
        return goodsdetailTitleTv;
    }

    public CommonTabLayout getGoodsdetailTab() {
        return goodsdetailTab;
    }

    public void setAddCartListener(RippleLinearLayout.OnRippleCompleteListener listener) {
        goodsBottomCartRl.setLLRippleCompleteListener(listener);
    }

    public void setBuytListener(RippleLinearLayout.OnRippleCompleteListener listener) {
        goodsBottomBuyRl.setLLRippleCompleteListener(listener);
    }

    public void setBottomCartText(String string) {
        setBottomCartText(string, true);
    }

    public void setBottomCartText(String string, boolean canClick) {
        goodsBottomAddcartTv.setText(string);
        /**
         * 去套装时去掉立即购买
         * */
        if (string.equals(getResources().getString(R.string.suit))){
            goodsBottomBuyRl.setVisibility(View.GONE);
        }

        if (!canClick)
            goodsBottomAddcartTv.setBackgroundResource(R.color.gray);
    }

    public void showAddCartDialog() {
        //产品要求 不显示提示窗口
//        addCartDialog.show();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_LOGIN:
                    break;
                default:
                    break;
            }
        }
    }



    //更新商品详情
    public void setGoodsContent(String goodsContent){
        if(goodsImgInfoFragment !=null){
            goodsImgInfoFragment.updateContent(goodsContent);
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

//    public void updateCollect(boolean isCollected) {
//        if (isCollected) {
//            goodsdetailCollectIv.setImageResource(R.drawable.icon_collect_wine);
//        } else {
//            goodsdetailCollectIv.setImageResource(R.drawable.icon_collect_gray);
//        }
//    }

    @Override
    public void updateCollection(boolean isCollected) {
        L.e(TAG +" updateCollect isCollected="+isCollected);
        this.isCollected = isCollected;
        if (isCollected) {
            goodsdetailCollectIv.setImageResource(R.drawable.icon_collect_wine);
        } else {
            goodsdetailCollectIv.setImageResource(R.drawable.icon_collect_gray);
        }
    }


    public void updateCartTv() {
        int num = DbManager.getInstance().getAllGoodsNum();
        updateCartTv(num);
    }

    @Override
    public void updateCartTv(int num) {
        if (num > 0) {
            goodsdetailShopcartNumTv.setVisibility(View.VISIBLE);
            TextUtils.showNum(goodsdetailShopcartNumTv,num);
        } else {
            goodsdetailShopcartNumTv.setVisibility(View.INVISIBLE);
        }
    }



    public RippleLinearLayout getGoodsBottomBuyRl() {
        return goodsBottomBuyRl;
    }

    public RippleLinearLayout getGoodsBottomCartRl() {
        return goodsBottomCartRl;
    }

    public void changeToCommentFrg(){
        goodsdetailTab.setCurrentTab(2);
        goodsdetailContentVp.setCurrentItem(2);
    };

    public void setGoodsShareInfo(String goodsName,String goodsContent,String img) {
        this.goodsName = goodsName;
        this.goodsContent = goodsContent;
        this.goodsImg = img;
    }
}
