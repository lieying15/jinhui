package com.thlh.jhmjmw.business.buy.buyconfirm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.ExpressSupplier;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.response.ExpressfreeResponse;
import com.thlh.baselib.utils.ActivityUtils;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.buyconfirm.adapter.BuyConfirmImgAdapter;
import com.thlh.jhmjmw.business.buy.buyconfirm.express.BuyExpressActivity;
import com.thlh.jhmjmw.business.buy.buyconfirm.list.BuyConfirmListActivity;
import com.thlh.jhmjmw.business.buy.buyconfirm.selectmjz.SelectPayMjbActivity;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.order.list.OrderListActivity;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.business.pay.PayPasswordActivity;
import com.thlh.jhmjmw.business.user.address.AddrManageActivity;
import com.thlh.jhmjmw.business.user.password.PasswordSetActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.WeChatUtils;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;
import com.thlh.viewlib.progress.ProgressMaterial;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单确认界面
 */
public class BuyConfirmActivity extends BaseActivity implements View.OnClickListener, BuyConfirmContract.View {
    private final String TAG = "BuyConfirmActivity";
    private final int ACTIVITY_CODE_ADDR = 1;
    @BindView(R.id.order_confirm_header)
    HeaderNormal orderConfirmHeader;
    @BindView(R.id.order_address_name_tv)
    TextView orderAddressNameTv;
    @BindView(R.id.order_address_phone_tv)
    TextView orderAddressPhoneTv;
    @BindView(R.id.order_address_addr_tv)
    TextView orderAddressAddrTv;
    @BindView(R.id.order_address_arrow_iv)
    ImageView orderAddressArrowIv;
    @BindView(R.id.order_confirm_addr_rl)
    RelativeLayout orderConfirmAddrRl;
    @BindView(R.id.order_confirm_goods_rv)
    EasyRecyclerView orderConfirmGoodsRv;
    @BindView(R.id.order_confirm_goods_num_tv)
    TextView orderConfirmGoodsNumTv;
    @BindView(R.id.order_confirm_goods_total_price_tv)
    TextView orderConfirmGoodsTotalPriceTv;
    @BindView(R.id.order_confirm_goodslist_tv)
    TextView orderConfirmGoodslistTv;
    @BindView(R.id.order_confirm_goodslist_arrow_iv)
    ImageView orderConfirmGoodslistArrowIv;
    @BindView(R.id.order_confirm_goods_ll)
    LinearLayout orderConfirmGoodsLl;
    @BindView(R.id.order_confirm_info_freight_hint_iv)
    ImageView orderConfirmInfoFreightHintIv;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.order_confirm_info_freight_tv)
    TextView orderConfirmInfoFreightTv;
    @BindView(R.id.order_confirm_info_freight_ll)
    LinearLayout orderConfirmInfoFreightLl;
    @BindView(R.id.order_confirm_info_deliverytype_shopsend_iv)
    ImageView orderConfirmInfoDeliverytypeShopsendIv;
    @BindView(R.id.order_confirm_info_deliverytype_userget_iv)
    ImageView orderConfirmInfoDeliverytypeUsergetIv;
    @BindView(R.id.order_confirm_info_times_tv)
    TextView orderConfirmInfoTimesTv;
    @BindView(R.id.order_confirm_info_times_show_iv)
    ImageView orderConfirmInfoTimesShowIv;
    @BindView(R.id.order_confirm_info_time_select_ll)
    LinearLayout orderConfirmInfoTimeSelectLl;
    @BindView(R.id.order_confirm_info_timeone_rb)
    RadioButton orderConfirmInfoTimeoneRb;
    @BindView(R.id.order_confirm_info_timetwo_rb)
    RadioButton orderConfirmInfoTimetwoRb;
    @BindView(R.id.order_confirm_info_timethree_rb)
    RadioButton orderConfirmInfoTimethreeRb;
    @BindView(R.id.order_confirm_info_timefour_rb)
    RadioButton orderConfirmInfoTimefourRb;
    @BindView(R.id.order_confirm_info_times_rg)
    RadioGroup orderConfirmInfoTimesRg;
    @BindView(R.id.order_confirm_info_deliverytype_ll)
    LinearLayout orderConfirmInfoDeliverytypeLl;
    @BindView(R.id.order_confirm_info_coupon_tv)
    TextView orderConfirmInfoCouponTv;
    @BindView(R.id.order_confirm_info_coupon_ll)
    LinearLayout orderConfirmInfoCouponLl;
    @BindView(R.id.order_confirm_info_freight_iv)
    ImageView orderConfirmInfoFreightIv;
    @BindView(R.id.order_confirm_paytype_mjmwcurrency_price_tv)
    TextView orderConfirmPaytypeMjmwcurrencyPriceTv;
    @BindView(R.id.order_confirm_paytype_mjmwcurrency_iv)
    ImageView orderConfirmPaytypeMjmwcurrencyIv;
    @BindView(R.id.order_confirm_paytype_mjmwcurrency_ll)
    LinearLayout orderConfirmPaytypeMjmwcurrencyLl;
    @BindView(R.id.order_confirm_paytype_weixin_iv)
    ImageView orderConfirmPaytypeWeixinIv;
    @BindView(R.id.order_confirm_paytype_weixin_ll)
    LinearLayout orderConfirmPaytypeWeixinLl;
    @BindView(R.id.order_confirm_paytype_zhifubao_iv)
    ImageView orderConfirmPaytypeZhifubaoIv;
    @BindView(R.id.order_confirm_paytype_zhifubao_ll)
    LinearLayout orderConfirmPaytypeZhifubaoLl;
    @BindView(R.id.order_offer_beizhu)
    EditText orderOfferBeizhu;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.order_confirm_pricelist_total_tv)
    TextView orderConfirmPricelistTotalTv;
    @BindView(R.id.order_confirm_pricelist_total_ll)
    LinearLayout orderConfirmPricelistTotalLl;
    @BindView(R.id.order_confirm_pricelist_mjb_tv)
    TextView orderConfirmPricelistMjbTv;
    @BindView(R.id.order_confirm_pricelist_mjb_ll)
    LinearLayout orderConfirmPricelistMjbLl;
    @BindView(R.id.order_confirm_pricelist_balacne_tv)
    TextView orderConfirmPricelistBalacneTv;
    @BindView(R.id.order_confirm_pricelist_balacne_ll)
    LinearLayout orderConfirmPricelistBalacneLl;
    @BindView(R.id.order_confirm_pricelist_express_tv)
    TextView orderConfirmPricelistExpressTv;
    @BindView(R.id.order_confirm_pricelist_express_ll)
    LinearLayout orderConfirmPricelistExpressLl;
    @BindView(R.id.order_confirm_pricelist_shouldpay_tv)
    TextView orderConfirmPricelistShouldpayTv;
    @BindView(R.id.order_confirm_pricelist_shouldpay_ll)
    LinearLayout orderConfirmPricelistShouldpayLl;
    @BindView(R.id.order_confirm_pricelist_shouldpay_rl)
    RelativeLayout orderConfirmPricelistShouldpayRl;
    @BindView(R.id.order_confirm_topll)
    LinearLayout orderConfirmTopll;
    @BindView(R.id.order_confirm_total_price_title)
    TextView orderConfirmTotalPriceTitle;
    @BindView(R.id.order_confirm_total_price_tv)
    TextView orderConfirmTotalPriceTv;
    @BindView(R.id.order_confirm_bottom_selectall_rl)
    RelativeLayout orderConfirmBottomSelectallRl;
    @BindView(R.id.order_confirm_bottom_gopay_tv)
    TextView orderConfirmBottomGopayTv;
    @BindView(R.id.order_confirm_bottom_gopay_ll)
    LinearLayout orderConfirmBottomGopayLl;

    @BindView(R.id.textView_cuxiao)
    TextView textViewCuxiao;
    @BindView(R.id.order_confirm_info_cuxiao_iv)
    ImageView orderConfirmInfoCuxiaoIv;
    @BindView(R.id.order_confirm_info_cuxiao_ll)
    LinearLayout orderConfirmInfoCuxiaoLl;


    private double totalprice;
    private double expressfree;
    private int goodsnum;
    private Address selectAddress;
    private List<Cartgoods> cartgoods = new ArrayList<>();
    private BuyConfirmImgAdapter buyConfirmImgAdapter;

    //    微信支付 start
    private IWXAPI msgApi;
    private PayReq weChatPayRequest;
    //    微信支付 end
    private String paytype = "";

    private boolean paywechat = false;
    private boolean payalipay = false;
    private boolean hasMjbPayGoods = false; //有美家币能支付的商品
    private String itemIdAndNum, itemIdAndNumAndMjb;
    private String user_mjb = "0";
    private int inner_member = 0;
    private double useMjb = 0.00d;//使用的美家钻
    /*
    *
    * 备注内容*/
    private String note = "0";


    //微信取消判断
    private boolean isStartWechat = false;
    //是否为立即购买
    private boolean isBuyImmediately;

    private BuyConfirmContract.Presenter mPresenter;
    private boolean flag = false;
    private String getPack;
    private String time;
    private boolean containIceBox = false;
    private double temptotalprice;

    public static void activityStart(Context context, boolean isBuyImmediately) {
        Intent intent = new Intent();
        intent.setClass(context, BuyConfirmActivity.class);
        intent.putExtra("isBuyImmediately", isBuyImmediately);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        weChatPayRequest = new PayReq();
        isBuyImmediately = getIntent().getBooleanExtra("isBuyImmediately", false);
        msgApi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        msgApi.registerApp(Constants.WECHAT_APP_ID);
        mPresenter = new BuyConfirmPresenter(getApplicationContext(), this, isBuyImmediately);
        cartgoods = mPresenter.getAllSelectCartGoods();
        selectAddress = mPresenter.getDefaultAddress();

        if (cartgoods == null) {
            L.i(TAG + "购物车数据为 null");
            return;
        }

        user_mjb = (String) SPUtils.get("user_mjb", "0");
        inner_member = (int) SPUtils.get("user_inner_member", 0);
        itemIdAndNum = mPresenter.getCartInfoStr(cartgoods, false);
        itemIdAndNumAndMjb = mPresenter.getCartInfoStr(cartgoods, true);
        L.e(TAG + " item: " + itemIdAndNum);
        for (Cartgoods cartgood : cartgoods) {
            int canMjb = Integer.parseInt(cartgood.getGoodsdb().getIs_mjb());
            if (canMjb > 0) {
                hasMjbPayGoods = true;
                break;
            }
        }
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy_confirm);
        ButterKnife.bind(this);
        progressMaterial = ProgressMaterial.create(this, false, null);
        buyConfirmImgAdapter = new BuyConfirmImgAdapter(this);
        List<Goods> adapterCartData = mPresenter.getAdapterCartData(cartgoods);
        buyConfirmImgAdapter.setList(adapterCartData);
        for (Goods good : adapterCartData) {
            if (good.getItem_id().equals("1")) {
                orderConfirmInfoCuxiaoLl.setVisibility(View.VISIBLE);
                containIceBox = true;
            }
        }
        orderConfirmGoodsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        orderConfirmGoodsRv.setNestedScrollingEnabled(false);
        orderConfirmGoodsRv.setAdapter(buyConfirmImgAdapter);
        orderConfirmGoodsRv.addItemDecoration(new HorizontaltemDecoration((int) getResources().getDimension(R.dimen.x10)));
        updateNumTv();
        updateAddressView();
    }


    @OnClick({R.id.order_confirm_paytype_mjmwcurrency_ll, R.id.order_confirm_paytype_zhifubao_ll,
            R.id.order_confirm_paytype_weixin_ll, R.id.order_confirm_goods_ll,
            R.id.order_confirm_addr_rl, R.id.order_confirm_bottom_gopay_ll,
            /*
            * 备注*/
            R.id.order_offer_beizhu,
            R.id.order_confirm_info_time_select_ll,
            R.id.order_confirm_info_timeone_rb,
            R.id.order_confirm_info_timetwo_rb,
            R.id.order_confirm_info_timethree_rb,
            R.id.order_confirm_info_timefour_rb,
            R.id.order_confirm_info_deliverytype_userget_iv,
            R.id.order_confirm_info_deliverytype_shopsend_iv,
//            /*
//            * 去支付*/
            R.id.order_confirm_bottom_gopay_tv,
            R.id.order_confirm_info_freight_tv, R.id.order_confirm_info_freight_iv,
            R.id.order_confirm_info_freight_hint_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_confirm_paytype_mjmwcurrency_ll:
                SelectPayMjbActivity.activityStart(BuyConfirmActivity.this, isBuyImmediately);
                break;
            case R.id.order_confirm_paytype_zhifubao_ll:
                L.e("xuanzhe zhifubao");
                changePayType(Constants.PAY_TYPE_ALIPAY);
                break;
            case R.id.order_confirm_paytype_weixin_ll:
                L.e("xuanze weixin");
                changePayType(Constants.PAY_TYPE_WECHAT);
                break;
            case R.id.order_confirm_addr_rl:
                if (SPUtils.getIsLogin()) {
                    Intent intent = new Intent();
                    intent.putExtra("start_type", AddrManageActivity.START_TYPE_ORDERCONFIRM);
                    intent.setClass(BuyConfirmActivity.this, AddrManageActivity.class);
                    startActivityForResult(intent, ACTIVITY_CODE_ADDR);
                } else {
                    LoginActivity.activityStart(this);
                }

                break;
            /*
            *
            * 去支付*/
            case R.id.order_confirm_bottom_gopay_ll:
            case R.id.order_confirm_bottom_gopay_tv:
    /*         * 备注内容*/
                note = orderOfferBeizhu.getText().toString().trim();
                orderOfferBeizhu.setText(note);

                if (SPUtils.getIsLogin()) {
                    paytype = mPresenter.getPayType(useMjb, paywechat, payalipay);
                    if (mPresenter.judgePayCondition(cartgoods, selectAddress.getId(), paytype, useMjb, user_mjb,temptotalprice, note)) {

                        mPresenter.postGenerateOrder(this, selectAddress.getId(),getPack,time, itemIdAndNumAndMjb, paytype, useMjb, note);
                    }

                } else {
                    LoginActivity.activityStart(this);
                }
                break;
            case R.id.order_confirm_goods_ll:
                BuyConfirmListActivity.activityStart(this, isBuyImmediately);
                break;
            case R.id.order_confirm_info_freight_iv:
            case R.id.order_confirm_info_freight_tv: //配送信息
                BuyExpressActivity.activityStart(this, itemIdAndNum, isBuyImmediately);
                break;
            case R.id.order_confirm_info_freight_hint_iv:
                showExprnseDialog();
                break;
            case R.id.order_offer_beizhu:
                break;
            case R.id.order_confirm_info_time_select_ll:
                if (flag) {
                    orderConfirmInfoTimesRg.setVisibility(View.GONE);
                    orderConfirmInfoTimesShowIv.setBackgroundResource(R.drawable.icon_arrow_down_gray);
                    flag = false;
                } else {
                    orderConfirmInfoTimesRg.setVisibility(View.VISIBLE);
                    orderConfirmInfoTimesShowIv.setBackgroundResource(R.drawable.icon_arrow_up_gray);
                    flag = true;
                }
                break;
            case R.id.order_confirm_info_timeone_rb:
                time = "8";
                orderConfirmInfoTimesTv.setText("8:00-12:00");
                break;
            case R.id.order_confirm_info_timetwo_rb:
                time = "12";
                orderConfirmInfoTimesTv.setText("12:00-14:00");
                break;
            case R.id.order_confirm_info_timethree_rb:
                time = "14";
                orderConfirmInfoTimesTv.setText("14:00-16:00");
                break;
            case R.id.order_confirm_info_timefour_rb:
                time = "16";
                orderConfirmInfoTimesTv.setText("16:00-18:00");
                break;
            case R.id.order_confirm_info_deliverytype_userget_iv:
                orderConfirmInfoDeliverytypeShopsendIv.setImageResource(R.drawable.icon_check_wine);
                orderConfirmInfoDeliverytypeUsergetIv.setImageResource(R.drawable.icon_check_wine_select);
                orderConfirmInfoTimesRg.setVisibility(View.GONE);
                orderConfirmInfoTimeSelectLl.setVisibility(View.GONE);
                getPack = "0"; //0 用户自提
                break;
            case R.id.order_confirm_info_deliverytype_shopsend_iv:
                orderConfirmInfoDeliverytypeShopsendIv.setImageResource(R.drawable.icon_check_wine_select);
                orderConfirmInfoDeliverytypeUsergetIv.setImageResource(R.drawable.icon_check_wine);
                orderConfirmInfoTimesRg.setVisibility(View.VISIBLE);
                orderConfirmInfoTimeSelectLl.setVisibility(View.VISIBLE);
                getPack = "1"; //1 小店配送
                break;
        }
    }


    @Override
    public void changePayType(String type) {
        if (totalprice == useMjb && useMjb > 0 && expressfree == 0) {
            showErrorDialog(getResources().getString(R.string.choose));
            paywechat = false;
            payalipay = false;
            orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
            orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine);
            return;
        }

        switch (type) {
            case Constants.PAY_TYPE_WECHAT:
                if (paywechat) {
                    paywechat = false;
                    orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine);
                } else {
                    paywechat = true;
                    orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine_select);
                }
                payalipay = false;
                orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
                break;
            case Constants.PAY_TYPE_ALIPAY:
                if (payalipay) {
                    payalipay = false;
                    orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
                } else {
                    payalipay = true;
                    orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine_select);
                }
                paywechat = false;
                orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine);
                break;
            default:
                break;
        }
    }


    /**
     * 更新显示价格 购物车中
     */
    @Override
    public void updatePriceTv() {
        useMjb = DbManager.getInstance().getUseMjbPrice(isBuyImmediately);
        double userMjb = Double.parseDouble(user_mjb);
        useMjb = useMjb > userMjb ? userMjb : useMjb; //最大取用户现在美家钻
        L.e("jiage"+hasMjbPayGoods + "==========" + useMjb +"=========="+ userMjb);
        if (hasMjbPayGoods) {
            orderConfirmPaytypeMjmwcurrencyPriceTv.setText(getResources().getString(R.string.use) + TextUtils.showPrice(useMjb));
            if (useMjb == 0.00d) {
                paywechat = false;
                L.e("xuanze weixin");
                changePayType(Constants.PAY_TYPE_WECHAT);
                orderConfirmPricelistMjbTv.setText("-0.00");
            } else {
                orderConfirmPricelistMjbTv.setText("- " + TextUtils.showPrice(useMjb));
            }
        } else {
            orderConfirmPaytypeMjmwcurrencyPriceTv.setText(getResources().getString(R.string.shop_no_mjz1));
        }

        totalprice = DbManager.getInstance().getCartGoodsPrice(isBuyImmediately);//商品总价

        if (totalprice > 0) {
            orderConfirmGoodsTotalPriceTv.setText(getResources().getString(R.string.money) + TextUtils.showPrice(totalprice));
            orderConfirmPricelistTotalTv.setText(getResources().getString(R.string.money) + TextUtils.showPrice(totalprice));
        } else {
            orderConfirmGoodsTotalPriceTv.setText(getResources().getString(R.string.zero));
            orderConfirmPricelistTotalTv.setText(getResources().getString(R.string.zero));
        }

        temptotalprice = expressfree + totalprice - useMjb;
        L.e("shagnpinshegnyujiage====" + temptotalprice);
        if (temptotalprice == 0.00d && useMjb > 0.00d) {
            orderConfirmTotalPriceTv.setText(TextUtils.showPrice(useMjb) + getResources().getString(R.string.ch_mjz));
            payalipay = false;
            paywechat = false;
            orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine);
            orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
        } else {
            if (temptotalprice > 0) {
                orderConfirmTotalPriceTv.setText(getResources().getString(R.string.money) + TextUtils.showPrice(temptotalprice));
            } else {
                orderConfirmTotalPriceTv.setText(getResources().getString(R.string.zero));
            }
        }
    }

    /**
     * 获取商品清单数据
     */
    @Override
    public void updateNumTv() {
        goodsnum = DbManager.getInstance().getCartGoodsNum(isBuyImmediately);
        Spannable supplierSpan = new SpannableString(getResources().getString(R.string.gong) + goodsnum + getResources().getString(R.string.shops));
        supplierSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.wine)), 1, supplierSpan.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        orderConfirmGoodsNumTv.setText(supplierSpan);
    }

    @Override
    protected void loadData() {
        if (SPUtils.getIsLogin()) {
            mPresenter.loadWallet();
        }
        if (judgeCondition()) {
            mPresenter.loadExpressFree(selectAddress.getId(), itemIdAndNum);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isStartWechat) {
            OrderListActivity.activityStart(BuyConfirmActivity.this, Constants.ORDER_TYPE_WAIT_PAY);
            ActivityUtils.finishActivity("ShopCartActivity");
            finish();
        }
        updatePriceTv();
        itemIdAndNumAndMjb = mPresenter.getCartInfoStr(cartgoods, true);
    }

    @Override
    public void showPaySuccessDialog() {
        ActionResponse response = new ActionResponse();
        response.setHeadertitle(getResources().getString(R.string.pay_money));
        response.setTitle(getResources().getString(R.string.congratulation));
        response.setContent(getResources().getString(R.string.pay_money_success));
        ResponseActivity.activityStart(BuyConfirmActivity.this, response);
        finish();
    }

    @Override
    public void showPayPwHintDialog() {
        NormalDialogFragment payPwHintDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        payPwHintDialog.setContentStr(getResources().getString(R.string.pay_password));
        payPwHintDialog.setTitleIvRes(R.drawable.img_dialog_key);
        payPwHintDialog.setFirstBtnVisible(View.VISIBLE);
        payPwHintDialog.setFirstBtnStr(getResources().getString(R.string.set_password));
        payPwHintDialog.setCancelVisible(View.VISIBLE);
        payPwHintDialog.setFirstBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordSetActivity.activityStart(BuyConfirmActivity.this, Constants.PAYPW_TYPE_NEW);
            }
        });
        payPwHintDialog.setFinalBtnStr(getResources().getString(R.string.Continue_pay_money));
        payPwHintDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.postGenerateOrder(BuyConfirmActivity.this, selectAddress.getId(),getPack,time,itemIdAndNumAndMjb, paytype, useMjb, note);
            }
        });
        payPwHintDialog.show(ft, "payPwHintDialog");
    }

    @Override
    public void showHintDialog(String hintContent) {
        showErrorDialog(hintContent);
    }

    /**
     * 更新收货地址
     */
    @Override
    public void updateAddressView() {

        selectAddress = mPresenter.getDefaultAddress();
        String province = selectAddress.getProvince();
        String city = selectAddress.getCity();
        String district = selectAddress.getDistrict();
        L.e(TAG + " 地址数据 id " + selectAddress.getId() + " Address " + selectAddress.getAddress());
        if (selectAddress.getId().equals("")) {
            orderAddressNameTv.setText(getResources().getString(R.string.input_goods_address));
            orderAddressPhoneTv.setText("");
        } else {
            orderAddressNameTv.setText(getResources().getString(R.string.name) + selectAddress.getName());
            orderAddressPhoneTv.setText(getResources().getString(R.string.phone) + selectAddress.getPhone());
            if (province.equals(city)) {
                orderAddressAddrTv.setText(getResources().getString(R.string.address) + province + district + selectAddress.getAddress());
            } else {
                orderAddressAddrTv.setText(getResources().getString(R.string.address) + province + city + district + selectAddress.getAddress());
            }
        }

        if (selectAddress.getId().equals("0")) { //小店地址
            orderConfirmInfoDeliverytypeLl.setVisibility(View.VISIBLE);
            orderConfirmInfoDeliverytypeShopsendIv.setImageResource(R.drawable.icon_check_wine_select);
            orderConfirmInfoDeliverytypeUsergetIv.setImageResource(R.drawable.icon_check_wine);
            getPack = "1"; //1 小店配送
            time = "0";
        } else {
            orderConfirmInfoDeliverytypeLl.setVisibility(View.GONE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_ADDR:
                    updateAddressView();
                    mPresenter.loadExpressFree(selectAddress.getId(), itemIdAndNum);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void updateExpressFree(double expressfree, ExpressfreeResponse.DataBean data) {
        this.expressfree = expressfree;
        List<ExpressSupplier> suppliers = data.getSupplier();
        String free = null;
        if (!(data.getTotal() == 0.00)) {
            free = getResources().getString(R.string.money) + data.getTotal();
        }
        for (ExpressSupplier supplier : suppliers) {
            if (supplier.getSupplier_id().equals("1")) {
                if (supplier.getItem().get(0).getItem_id() == 1) {
                    if (free != null) {
                        if (!free.contains(getResources().getString(R.string.mail))) {
                            free = free + "+" + getResources().getString(R.string.mail);
                        }
                    } else {
                        free = getResources().getString(R.string.mail);
                    }
                }
            } else if (supplier.getSupplier_id().equals("48")) {
                if (free != null) {
                    if (!free.contains(getResources().getString(R.string.expressfree_pay))) {
                        free = free + "+" + getResources().getString(R.string.expressfree_pay);
                    }
                } else {
                    free = getResources().getString(R.string.expressfree_pay);
                }

            } else if (supplier.getExpress_fee() <= 0.00d) {
                if (free != null) {
                    if (!free.contains(getResources().getString(R.string.mail))) {
                        free = free + "+" + getResources().getString(R.string.mail);
                    }
                } else {
                    free = getResources().getString(R.string.mail);
                }

            } else {
                if (free != null) {
                    if (!free.contains(getResources().getString(R.string.money))) {
                        free = free + "+" + getResources().getString(R.string.money) + supplier.getExpress_fee();
                    }
                } else {
                    free = getResources().getString(R.string.money) + supplier.getExpress_fee();
                }

                paywechat = true;
                payalipay = false;
                orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine_select);
                orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
            }

        }
        orderConfirmInfoFreightTv.setText(free);
        orderConfirmPricelistExpressTv.setText(free);
        updatePriceTv();
    }



    @Override
    public void startPasswordPayActivity(String orderid) {
        PayPasswordActivity.activityStart(BuyConfirmActivity.this, Constants.PAYPW_TYPE_ORDERCONFIRM_PAY, orderid, paytype, itemIdAndNumAndMjb);
        finish();
    }

    @Override
    public void updateWeChatPayRequest(String prepay_id, String nonce_str) {
        weChatPayRequest = WeChatUtils.postPayRequest(weChatPayRequest, prepay_id, nonce_str);
        msgApi.registerApp(Constants.WECHAT_APP_ID);
        msgApi.sendReq(weChatPayRequest);
        isStartWechat = true;
    }

    private void showExprnseDialog() {
        DialogUtils.showExprnse(this, getString(R.string.mjmw_bz));
    }

    @Override
    public void finish() {
        super.finish();
        mPresenter.ClearBuyImmediatelyGoods();
    }

    // 回退键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBuyImmediately)
                mPresenter.ClearBuyImmediatelyGoods();
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private boolean judgeCondition() {
        if (selectAddress.getId() == null || selectAddress.getId().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}