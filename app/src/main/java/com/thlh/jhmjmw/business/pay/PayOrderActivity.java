package com.thlh.jhmjmw.business.pay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.OrderItem;
import com.thlh.baselib.model.response.OrderPayResponse;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.model.response.WeChatPayResponse;
import com.thlh.baselib.utils.AliPayResult;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.buyconfirm.selectmjz.SelectPayMjbActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.AliPay;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.WeChatUtils;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.viewlib.progress.ProgressMaterial;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 待支付界面
 */
public class PayOrderActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "PayOrderActivity";

    @BindView(R.id.order_confirm_paytype_mjmwcurrency_price_tv)
    TextView mjmwcurrencyPriceTv;
    @BindView(R.id.order_confirm_paytype_mjmwcurrency_iv)
    ImageView orderConfirmPaytypeMjmwcurrencyIv;
    @BindView(R.id.order_confirm_paytype_mjmwcurrency_ll)
    LinearLayout paytypeMjbLl;

    @BindView(R.id.order_confirm_paytype_weixin_iv)
    ImageView orderConfirmPaytypeWeixinIv;
    @BindView(R.id.order_confirm_paytype_weixin_ll)
    LinearLayout orderConfirmPaytypeWeixinLl;
    @BindView(R.id.order_confirm_paytype_zhifubao_iv)
    ImageView orderConfirmPaytypeZhifubaoIv;
    @BindView(R.id.order_confirm_paytype_zhifubao_ll)
    LinearLayout orderConfirmPaytypeZhifubaoLl;
    @BindView(R.id.pay_confrim_rip)
    RippleRelativeLayout payConfrimRip;
//    @BindView(R.id.pay_snake_cl)
//    CoordinatorLayout paySnakeCl;

    @BindView(R.id.order_confirm_pricelist_total_tv)
    TextView pricelistTotalTv;
    @BindView(R.id.order_confirm_pricelist_total_ll)
    LinearLayout pricelistTotalLl;
    @BindView(R.id.order_confirm_pricelist_mjb_tv)
    TextView pricelistMjbTv;
    @BindView(R.id.order_confirm_pricelist_mjb_ll)
    LinearLayout pricelistMjbLl;

    @BindView(R.id.order_confirm_pricelist_express_tv)
    TextView pricelistExpressTv;
    @BindView(R.id.order_confirm_pricelist_express_ll)
    LinearLayout pricelistExpressLl;
    @BindView(R.id.order_confirm_pricelist_shouldpay_tv)
    TextView shouldpayTv;
    @BindView(R.id.order_confirm_pricelist_shouldpay_rl)
    RelativeLayout shouldpayRl;

    private BaseObserver<OrderPayResponse> payOrderObserver;
    private Observer<WeChatPayResponse> weChatObserver;
    private BaseObserver<WalletResponse> walletObserver;

    private DialogNormal.Builder dialogPayResult;

    //    微信支付 start
    private IWXAPI msgApi;
    private PayReq weChatPayRequest;
    private int inner_member;
    //    微信支付 end
    private String paytype = "";
    private boolean paywechat = false;
    private boolean payalipay = false;
    private double useMjb = 0;//使用的美家钻
    private Order order;
    private String orderid;
    private double expressfree;
    private String isPay;
    private List<OrderItem> orderItemList;
    private String user_mjb;
    private String itemidAndNumAndMjb;
    private DialogNormal.Builder dialogHint;


    private ArrayList<GoodsOrder> goodsOrderList = new ArrayList<>();
    private ArrayList<String> useMjbItemId = new ArrayList<>(); //需要每家钻支付的商品id数组
    private double amount;
    private double express_fee;
    private double tempShouldPay;

    public static void activityStart(Activity context, int code, Order order) {
        Intent intent = new Intent();
        intent.putExtra("Order", order);
        intent.setClass(context, PayOrderActivity.class);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected void initVariables() {
        weChatPayRequest = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        msgApi.registerApp(Constants.WECHAT_APP_ID);

        user_mjb = (String) SPUtils.get("user_mjb", "0");
        inner_member = (int) SPUtils.get("user_inner_member", 0);
        order = getIntent().getParcelableExtra("Order");
        orderid = order.getOrder_id();
        orderItemList = order.getOrder_items();
        if (order.getExpress_fee() == null || order.getExpress_fee().equals("")) {
            expressfree = 0;
        } else {
            expressfree = Double.parseDouble(order.getExpress_fee());
        }
        isPay = order.getIs_pay(); //0 未支付 1已支付 2部分支付
        if (isPay.equals("0")) {
            goodsOrderList = getCartGoodsFormOrder(order);
            for (GoodsOrder goodsOrder : goodsOrderList) { //能用每家币支付的商品加入数组
                if (!goodsOrder.getIs_mjb().equals("0")) {
                    useMjbItemId.add(goodsOrder.getItem_id());
                }
                useMjb += Double.parseDouble(goodsOrder.getMjb_value());
            }
        }
        if (isPay.equals("2")) {
            goodsOrderList = getCartGoodsFormOrder(order);
            useMjb = Double.parseDouble(order.getPay_by_mjb()); //可用的每家币
            for (GoodsOrder goodsOrder : goodsOrderList) { //能用每家币支付的商品加入数组
                if (!goodsOrder.getIs_mjb().equals("0")) {
                    useMjbItemId.add(goodsOrder.getItem_id());
                }
            }
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_payorder);
        ButterKnife.bind(this);
        shouldpayRl.setVisibility(View.VISIBLE); //复用，默认隐藏
        progressMaterial = ProgressMaterial.create(this, false, null);//不能取消
        dialogPayResult = new DialogNormal.Builder(this);
        dialogHint = new DialogNormal.Builder(this);
        amount = Double.parseDouble(order.getGoods_amount());
        express_fee = Double.parseDouble(order.getExpress_fee());
        pricelistTotalTv.setText(getResources().getString(R.string.money) + amount);
        pricelistExpressTv.setText(getResources().getString(R.string.money) + express_fee);
        updateUseMjb();
        useMjb = useMjb < Double.parseDouble(user_mjb) ? useMjb : Double.parseDouble(user_mjb);
        tempShouldPay = amount + express_fee - useMjb;
        if (tempShouldPay == 0 && useMjb > 0) {
            shouldpayTv.setText(TextUtils.showPrice(useMjb) + getResources().getString(R.string.ch_mjz));
        }

        if (useMjb == 0) {
            shouldpayTv.setText(getResources().getString(R.string.money) + TextUtils.showPrice(tempShouldPay));
        }

        if (isPay.equals("2")) { //部分支付后不能再选每家币。
            paytypeMjbLl.setVisibility(View.GONE);
        }

        payConfrimRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                if (judgePayCondition()) {
                    itemidAndNumAndMjb = getItemidAndNumAndMjb();
                    int user_ispaypass = (int) SPUtils.get("user_ispaypass", 0);//支付密码  -1免密开启不使用支付密码 0未设置 1使用支付密码
                    if (user_ispaypass == 1 && useMjb > 0) {
                        PayPasswordActivity.activityStart(PayOrderActivity.this, Constants.PAYPW_TYPE_ORDERCONFIRM_PAY, orderid, paytype, itemidAndNumAndMjb);
                        finish();
                    } else {
                        SPUtils.put("orderid",orderid);
                        postOrderPay(orderid);
                    }
                }
            }
        });

        payOrderObserver = new BaseObserver<OrderPayResponse>() {
            @Override
            public void onErrorResponse(OrderPayResponse payResponse) {
                progressMaterial.dismiss();
                L.e(TAG + "  generateOrderObserver onErrorResponse  " + payResponse.getErr_msg());
                dialogPayResult.setSubTitle(payResponse.getErr_msg())
                        .setRightBtnStr(getResources().getString(R.string.back))
                        .setRightClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
            }

            @Override
            public void onNextResponse(OrderPayResponse payResponse) {
                SPUtils.put("order_need_update", "1");//更改订单状态：0无变化，1付款，2确认收货，3取消订单,4评价
                //保存支付订单号
                String pay_no = payResponse.getData().getPay_no();
                SPUtils.put("orderPayNo",pay_no);
                String tempprice = Double.toString(payResponse.getData().getAmount());
                SPUtils.put("pay_price", tempprice);
                L.i(TAG + " 支付订单后 orderid " + pay_no + "amont " + tempprice);
                shouldpayTv.setText(TextUtils.showPrice(tempShouldPay));
                //保存支付参数
                SPUtils.setPayParam(tempprice, paytype, Constants.PAY_PURPOSE_ORDER, pay_no);
                if (payResponse.getData().getAmount() == 0) {
                    dealPaySuccessResult();
                    return;
                } else {
                    if (paywechat) {
                        L.i(TAG + " 调用微信支付" + pay_no);
                        startWechatPay(tempprice, pay_no);
                        return;
                    }
                    if (payalipay) {
                        L.i(TAG + " 调用支付宝支付 tempprice " + tempprice + " pay_no" + pay_no);
                        startAliPay(tempprice, pay_no);
                        return;
                    }
                    loadWallet();
                    showPayPartSuccessDialog(tempprice);
                }
            }
        };

        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
            }

            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                progressMaterial.dismiss();
                SPUtils.put("user_mjb", walletResponse.getData().getMjb() + "");
            }
        };

        weChatObserver = new Observer<WeChatPayResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                L.e(TAG + "  WeChatPayResponse onError" + e.toString());
                progressMaterial.dismiss();
            }

            @Override
            public void onNext(WeChatPayResponse result) {
                progressMaterial.dismiss();
                L.e(TAG + "  WeChatPayResponse onNext" + result.toString() + " Return_msg " + result.getReturn_msg().toString()
                        + " Return_code " + result.getReturn_code().toString() + " Prepay_id " + result.getPrepay_id().toString());
                weChatPayRequest = WeChatUtils.postPayRequest(weChatPayRequest, result.getPrepay_id(), result.getNonce_str());
                msgApi.registerApp(Constants.WECHAT_APP_ID);
                msgApi.sendReq(weChatPayRequest);

            }
        };

    }


    @OnClick({R.id.order_confirm_paytype_mjmwcurrency_ll, R.id.order_confirm_paytype_zhifubao_ll, R.id.order_confirm_paytype_weixin_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_confirm_paytype_mjmwcurrency_ll:
                SelectPayMjbActivity.activityStart(PayOrderActivity.this, Constants.MJBSELECT_TYPE_ORDER_PAY, goodsOrderList, useMjbItemId);
                break;
            case R.id.order_confirm_paytype_zhifubao_ll:
                changePayType(Constants.PAY_TYPE_ALIPAY);
                break;
            case R.id.order_confirm_paytype_weixin_ll:
                changePayType(Constants.PAY_TYPE_WECHAT);
                break;
        }
    }

    private void changePayType(String type) {
        double temptotalprice = tempShouldPay;
        if (temptotalprice == 0 && useMjb > 0) {
//            new S.Builder(paySnakeCl, "你不需支付现金").create().show();
            dialogHint.setTitle("")
                    .setCancelOutside(false)
                    .setSubTitle(getResources().getString(R.string.shop_mjz_pay))
                    .setRightBtnStr(getResources().getString(R.string.me_know))
                    .setRightClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create().show();
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
        }
    }


    private String getPayType() {
        StringBuilder paytypeStr = new StringBuilder();
        if (useMjb > 0) {
            paytypeStr.append(Constants.PAY_TYPE_MJB);
        }
        if (paywechat) {
            if (paytypeStr.toString().equals("")) {
                paytypeStr.append("3");
            } else {
                paytypeStr.append("|3");
            }
        }
        if (payalipay) {
            if (paytypeStr.toString().equals("")) {
                paytypeStr.append("4");
            } else {
                paytypeStr.append("|4");
            }
        }
        return paytypeStr.toString();
    }

    @Override
    protected void loadData() {
    }


    private void postOrderPay(String orderid) {
        L.e(TAG + " 下订单 orderid " + orderid + "paytype " + paytype);
        NetworkManager.getOrderApi()
                .payOrderV2(SPUtils.getToken(), orderid, paytype, itemidAndNumAndMjb)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(payOrderObserver);
    }


    private String getItemidAndNumAndMjb() {
        StringBuilder itemidStr = new StringBuilder();
        int selectCount = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            selectCount += orderItemList.get(i).getItem().size();
        }
        int selectCounttemp = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            for (int n = 0; n < orderItemList.get(i).getItem().size(); n++) {
                String itemid = orderItemList.get(i).getItem().get(n).getItem_id();
                if (useMjbItemId.contains(itemid)) {
                    itemidStr.append(itemid + "|" + orderItemList.get(i).getItem().get(n).getItem_num() + "|1");
                } else {
                    itemidStr.append(itemid + "|" + orderItemList.get(i).getItem().get(n).getItem_num() + "|0");
                }
                selectCounttemp++;
                if (selectCounttemp < selectCount) {
                    itemidStr.append(",");
                }
            }
        }
        return itemidStr.toString().trim();
    }


    public void loadWallet() {
        Subscription subscription = NetworkManager.getUserDataApi()
                .wallet(SPUtils.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletObserver);
        subscriptionList.add(subscription);
    }

    private void startWechatPay(String tempprice, String orderid) {
        //开始微信支付，预下单
        int price = (int) (Double.parseDouble(tempprice) * 100 + 0.5);

        Map<String, String> premapdata = WeChatUtils.setAheadOrderParam(price + "", getResources().getString(R.string.mjmw_shop), orderid, Constants.PAY_PURPOSE_ORDER, inner_member);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/*"), WeChatUtils.genAheadOrderXML(premapdata));
        Subscription subscription = NetworkManager.getWeixinApi()
                .getPrepayId2("application/json", "application/json", requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weChatObserver);
        subscriptionList.add(subscription);
    }

    private void startAliPay(String tempprice, String orderid) {
        String paytypeForRecall = "1";  //1订单支付 2钱包充值 3美家钻充值
        //支付宝支付
        AliPay aliPay = new AliPay(PayOrderActivity.this, getResources().getString(R.string.mjmw_shop), getResources().getString(R.string.mjmw_shop), tempprice + "", orderid, paytypeForRecall, new AliPay.OnPayResultListener() {
            @Override
            public void onPaySucceed(AliPayResult payResult) {
                progressMaterial.dismiss();
                switch (payResult.resultStatus) {
                    case "9000":
                        L.e("zhifubao====9000");
                        PayActivity.activityStart(PayOrderActivity.this,"1");
                        finish();
                        break;
                    case "4000":
                        L.e("zhifubao====4000");
                        PayActivity.activityStart(PayOrderActivity.this, "2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.order_pay_fail));
                        break;
                    case "6001":
                        L.e("zhifubao====6001");
                        PayActivity.activityStart(PayOrderActivity.this, "2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.cannal_zfb_pay));
                        break;
                    case "6002":
                        L.e("zhifubao====6002");
                        PayActivity.activityStart(PayOrderActivity.this, "2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.net_wrong));
                        break;
                    default:
                        L.e("zhifubao====default");
                        PayActivity.activityStart(PayOrderActivity.this, "2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.order_pay_wrong));
                        break;
                }
            }

            @Override
            public void onPayFailed(Exception e) {
                progressMaterial.dismiss();
                L.e("zhifubao====fail");
                PayActivity.activityStart(PayOrderActivity.this, "2");
                finish();
            }
        });
        aliPay.pay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUseMjb();

    }

    private boolean judgePayCondition() {
        paytype = getPayType();
        L.e(TAG + " paytype " + paytype);
        if (paytype.equals("") || paytype.equals(" ")) {
            showWaringDialog(getResources().getString(R.string.pay_choose));
            return false;
        }
        if (tempShouldPay > 0 && paytype.equals("1")) {
            showWaringDialog(getResources().getString(R.string.pay_choose));
            return false;
        }

        if (expressfree > 0 && paytype.equals(Constants.PAY_TYPE_MJB)) {
            showWaringDialog(getResources().getString(R.string.pay_choose_way));
            /*dialogHint.setTitle("")
                    .setCancelOutside(false)
                    .setSubTitle(getResources().getString(R.string.pay_choose_way))
                    .setRightBtnStr(getResources().getString(R.string.back))
                    .setRightClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create().show();*/
            return false;
        }

        double tempmjb = Double.parseDouble(user_mjb);
        if (tempShouldPay > tempmjb && paytype.equals(Constants.PAY_TYPE_MJB)) {
            showWaringDialog(getResources().getString(R.string.another_pays_way));
            return false;
        }
        boolean canPayMjbIndependent = true;
        String goodname = "";
        for (OrderItem orderItem : orderItemList) {
            for (GoodsOrder goodsOrder : orderItem.getItem()) {
                if (goodsOrder.getIs_mjb().equals("0")) {
                    canPayMjbIndependent = false;
                    goodname = goodsOrder.getItem_name();
                    break;
                }
            }
        }

        if (canPayMjbIndependent == false && paytype.equals(Constants.PAY_TYPE_MJB)) {
            showWaringDialog(getResources().getString(R.string.mjz_no_pay_another_pays_way));
            return false;
        }

        return true;
    }

    private void dealPaySuccessResult() {
//        ActionResponse response = new ActionResponse();
//        response.setHeadertitle(getResources().getString(R.string.pay_money));
//        response.setTitle(getResources().getString(R.string.congratulation));
//        response.setContent(getResources().getString(R.string.pay_money_success));
//        ResponseActivity.activityStart(PayOrderActivity.this, response);
        finish();
    }


    private void showPayPartSuccessDialog(String price) {
        dialogPayResult.setTitle(getResources().getString(R.string.pay_money_success))
                .setCancelOutside(false)
                .setSubTitle(getResources().getString(R.string.need_pays) + price + getResources().getString(R.string.qian))
                .setRightBtnStr(getResources().getString(R.string.back))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
    }

    //订单商品转化成购物车类型。
    private ArrayList<GoodsOrder> getCartGoodsFormOrder(Order order) {
        ArrayList<GoodsOrder> goodsOrderList = new ArrayList<>();
        List<OrderItem> orderItemsList = order.getOrder_items();
        for (OrderItem orderItem : orderItemsList) {
            for (GoodsOrder goodsOrder : orderItem.getItem()) {
                goodsOrderList.add(goodsOrder);
            }
        }
        return goodsOrderList;
    }

   /* private double getMjbPrice(ArrayList<GoodsOrder> goodsOrderList){
        double tempMjb = 0;
        for(GoodsOrder goodsOrder :goodsOrderList){
            if(goodsOrder.getIs_mjb().equals("1")){
                tempMjb += Double.parseDouble(goodsOrder.getItem_price()) * Integer.parseInt(goodsOrder.getItem_num());
            }
            if(goodsOrder.getIs_mjb().equals("2")){
                tempMjb += Double.parseDouble(goodsOrder.getMjb_value()) * Integer.parseInt(goodsOrder.getItem_num());
            }
        }
        return   tempMjb ;
    }*/

    private void updateUseMjb() {

        if (useMjb > 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            pricelistMjbTv.setText("- " + getResources().getString(R.string.money) + df.format(useMjb));
        } else {
            pricelistMjbTv.setText("- " + getResources().getString(R.string.money) + "0");
        }
        double userMjb = Double.parseDouble(user_mjb);
        useMjb = useMjb > userMjb ? userMjb : useMjb;
        DecimalFormat df = new DecimalFormat("0.00");
        mjmwcurrencyPriceTv.setText(getResources().getString(R.string.use) + df.format(useMjb) + getResources().getString(R.string.ch_mjz));
        shouldpayTv.setText(getResources().getString(R.string.money) + TextUtils.showPrice(tempShouldPay));
        double temptotalprice = tempShouldPay;
        if (temptotalprice == 0 && useMjb > 0) {
            paywechat = false;
            payalipay = false;
            orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
            orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine);
        } else if (temptotalprice > 0) {

            if (payalipay) {
                orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine);
                orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine_select);
            } else {
                paywechat = true;
                orderConfirmPaytypeWeixinIv.setImageResource(R.drawable.icon_check_wine_select);
                orderConfirmPaytypeZhifubaoIv.setImageResource(R.drawable.icon_check_wine);
            }

        }
        tempShouldPay = amount + express_fee - useMjb;
        shouldpayTv.setText(getResources().getString(R.string.money) + TextUtils.showPrice(tempShouldPay));

        if (useMjb == 0) {
            paywechat = false;
            changePayType(Constants.PAY_TYPE_WECHAT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.e("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case Constants.MJBSELECT_TYPE_ORDER_PAY:
                    useMjb = data.getDoubleExtra("useMjb", 0);
                    useMjbItemId = data.getStringArrayListExtra("useMjbItemId");
                    updateUseMjb();
                    break;
                default:
                    break;
            }
        }
    }
}
