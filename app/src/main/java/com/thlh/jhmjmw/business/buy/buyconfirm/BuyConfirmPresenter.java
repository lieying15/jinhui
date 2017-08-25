package com.thlh.jhmjmw.business.buy.buyconfirm;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.baselib.model.response.ExpressfreeResponse;
import com.thlh.baselib.model.response.OrderGenerateResponse;
import com.thlh.baselib.model.response.OrderPayResponse;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.model.response.WeChatPayResponse;
import com.thlh.baselib.utils.AliPayResult;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.pay.PayActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.AliPay;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.WeChatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;

import static com.thlh.viewlib.PickerView.TAG;


public class BuyConfirmPresenter implements BuyConfirmContract.Presenter{

    private BuyConfirmContract.View mView;
    private BaseObserver<OrderGenerateResponse> generateOrderObserver;
    private BaseObserver<ExpressfreeResponse> getExpressfreeObserver;
    private BaseObserver<OrderPayResponse> payOrderObserver;
    private BaseObserver<WalletResponse> walletObserver;
    private Observer<WeChatPayResponse> weChatObserver;

    private double totalprice;
    private double expressfree;
    private boolean isBuyImmediately;
    private Context context;
    private String coupon = null;

    public BuyConfirmPresenter(Context context,BuyConfirmContract.View mView, boolean isBuyImmediately) {
        this.context=context;
        this.mView = mView;
        this.isBuyImmediately = isBuyImmediately;
    }

    @Override
    public void loadExpressFree(String address,String itemIdAndNum) {
        L.e(TAG + "  loadExpressFree 运费 :  address " + address + " itemIdAndNum:"+itemIdAndNum);
        getExpressfreeObserver = new BaseObserver<ExpressfreeResponse>() {
            @Override
            public void onErrorResponse(ExpressfreeResponse expressfreeResponse) {
                mView.showHintDialog(expressfreeResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(ExpressfreeResponse expressfreeResponse) {
                expressfree = expressfreeResponse.getData().getTotal();
                ExpressfreeResponse.DataBean data = expressfreeResponse.getData();
                mView.updateExpressFree(expressfree,data);
                mView.updatePriceTv();
            }
        };
        String tostore  ;
        if(address.equals("0")){
            tostore = "1";
        }else {
            tostore = "0";
        }
        NetworkManager.getOrderApi()
                .getExpressfreeV2(SPUtils.getToken(), address, itemIdAndNum,tostore)
                .compose(RxUtils.androidSchedulers(mView,false))
                .subscribe(getExpressfreeObserver);
    }

    @Override
    public void loadWallet() {
        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
                mView.showHintDialog(walletResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                SPUtils.put("user_mjb", walletResponse.getData().getMjb() + "");
            }
        };
        NetworkManager.getUserDataApi()
                .wallet(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(mView,false))
                .subscribe(walletObserver);
    }

    @Override
    public void postGenerateOrder(Activity activity, String addressid, String getPack, String time, String itemIdAndNumAndMjb, String paytype, double useMjb, String note) {
        // 生成订单号的返回信息
        generateOrderObserver = new BaseObserver<OrderGenerateResponse>() {
            @Override
            public void onErrorResponse(OrderGenerateResponse generateResponse) {
                mView.showHintDialog(generateResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(OrderGenerateResponse generateResponse) {
                if(isBuyImmediately){
                    DbManager.getInstance().deleteBuyImmediatelyGoods(); //清除购物车
                }else {
                    DbManager.getInstance().deleteSelectGoods(); //清除购物车
                }
                String orderid = generateResponse.getData().getOrder_id();
                SPUtils.put("orderid",orderid);
                L.e(TAG + "  generateOrderObserver orderid " + orderid);
                SPUtils.put("needupdate_userinfo", true); //刷新我的界面
                //支付密码弹窗判断
                int user_ispaypass =Integer.parseInt(SPUtils.get("user_ispaypass", "").toString());//支付密码  -1免密开启不使用支付密码 0未设置 1使用支付密码
                boolean user_paypass_hint = Boolean.valueOf(SPUtils.get("user_paypass_hint", false).toString());//是否进行过弹窗提示
                L.e("判断设置支付密码弹窗 user_ispaypass:" + user_ispaypass + " user_paypass_hint:" + user_paypass_hint + " paytype:" + paytype + " include:" + paytype.contains(Constants.PAY_TYPE_MJB));
                if (user_ispaypass == 0 && !user_paypass_hint && paytype.contains(Constants.PAY_TYPE_MJB)) {
                    SPUtils.put("user_paypass_hint", true);
                    mView.showPayPwHintDialog();
                }else {
                    if(user_ispaypass == 1 && useMjb >0){
                        mView.startPasswordPayActivity(orderid);
                    }else {
                        postPayOrder(activity,orderid,itemIdAndNumAndMjb,paytype);
                    }
                }
            }
        };
        String tostore;
        if(addressid.equals("0")){
            tostore = "1";
        }else {
            tostore = "0";
        }

        L.e(TAG + " 生成订单 Address.Id() " + addressid + " itemIdAndNumAndMjb " + itemIdAndNumAndMjb +" paytype：" + paytype+ "note"+note);
        NetworkManager.getOrderApi()
                .generateOrderV2(SPUtils.getToken(), addressid, itemIdAndNumAndMjb, getPack, paytype, time , tostore, coupon, note)
                .compose(RxUtils.androidSchedulers(mView,false))
                .subscribe(generateOrderObserver);
    }

    @Override
    public void postPayOrder(Activity activity,String orderid,String itemIdAndNumAndMjb,String paytype) {
        payOrderObserver = new BaseObserver<OrderPayResponse>() {
            @Override
            public void onErrorResponse(OrderPayResponse payResponse) {
                mView.showHintDialog(payResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(OrderPayResponse payResponse) {
                //保存支付订单号
                String pay_no = payResponse.getData().getPay_no();
                SPUtils.put("pay_no", pay_no);
                String tempprice = Double.toString(payResponse.getData().getAmount());
                SPUtils.put("pay_price", tempprice);
                L.i(TAG + " 生成 orderid " + pay_no + "; amount " + tempprice);
                //保存支付参数
                SPUtils.setPayParam(tempprice, paytype, Constants.PAY_PURPOSE_ORDER, pay_no);
                if (payResponse.getData().getAmount() == 0) {
                    mView.showPaySuccessDialog();
                    return;
                } else {
                    if (paytype.contains(Constants.PAY_TYPE_WECHAT)) {
                        L.i(TAG + " 调用微信支付" + pay_no);
                        startWechatPay(tempprice, pay_no);
                        return;
                    }
                    if (paytype.contains(Constants.PAY_TYPE_ALIPAY)) {
                        L.i(TAG + " 调用支付宝支付 totalprice " + tempprice + " pay_no" + pay_no);
                        startAliPay(activity,tempprice, pay_no);
                        return;
                    }
                    loadWallet();
                }
            }
        };
        NetworkManager.getOrderApi()
                .payOrderV2(SPUtils.getToken(), orderid, paytype,itemIdAndNumAndMjb)
                .compose(RxUtils.androidSchedulers(mView))
                .subscribe(payOrderObserver);
    }

    @Override
    public boolean judgePayCondition(List<Cartgoods> cartgoods, String addressId, String paytype, double useMjb, String user_mjb, double temptotalprice, String note) {
        if (addressId.equals("")) {
            /*
            *
            * */
            mView.showHintDialog(context.getResources().getString(R.string.please_choose_goods_adress));
            return false;
        }

        L.e(TAG + " paytype " + paytype);
        if (paytype.equals("")) {
            mView.showHintDialog(context.getResources().getString(R.string.pay_choose));
            return false;
        }

        L.e("BuyConfirm==temptotalprice====" + temptotalprice  + "===totalprice==="  + totalprice);
        if (temptotalprice > 0 && paytype.equals(Constants.PAY_TYPE_MJB)) {
            mView.showHintDialog(context.getResources().getString(R.string.pay_choose));
            return false;
        }

        if (expressfree > 0 && paytype.equals(Constants.PAY_TYPE_MJB)) {
            mView.showHintDialog(context.getResources().getString(R.string.pay_choose_way));
            return false;
        }

        double tempmjb = Double.parseDouble(user_mjb);
        L.e("BuyConfirm==tempmjb====" + tempmjb); //用户的金惠币
        if (totalprice > tempmjb && paytype.equals(Constants.PAY_TYPE_MJB)) {
            mView.showHintDialog(context.getResources().getString(R.string.another_pays_way));
            return false;
        }

        boolean canPayMjbIndependent = true;
        String goodname = "";
        for (int i = 0; i < cartgoods.size(); i++) {
            if (cartgoods.get(i).getIsSelect()) {
                if (cartgoods.get(i).getGoodsdb().getIs_mjb().equals("0")) {
                    canPayMjbIndependent = false;
                    goodname = cartgoods.get(i).getGoodsdb().getItem_name();
                    break;
                }
            }
        }
        if (canPayMjbIndependent == false && paytype.equals(Constants.PAY_TYPE_MJB)) {
            mView.showHintDialog(goodname + context.getResources().getString(R.string.mjz_no_pay_another_pays_way));
            return false;
        }
        return true;
    }


    @Override
    public Address getDefaultAddress() {
        Address selectAddress = new Address();
        selectAddress.setId((String) SPUtils.get("user_address_id", "").toString());
        selectAddress.setName((String) SPUtils.get("user_address_name", "").toString());
        selectAddress.setPhone((String) SPUtils.get("user_address_phone", "").toString());
        selectAddress.setAddress((String) SPUtils.get("user_address_address", "").toString());
        selectAddress.setIs_on((String) SPUtils.get("user_address_is_on", "0").toString());
        selectAddress.setProvince((String) SPUtils.get("user_address_province", "").toString());
        selectAddress.setCity((String) SPUtils.get("user_address_city", "").toString());
        selectAddress.setDistrict((String) SPUtils.get("user_address_district", "").toString());
        return selectAddress;
    }

    @Override
    public List<Cartgoods> getAllSelectCartGoods() {
        if(isBuyImmediately){
            return DbManager.getInstance().getImmediatelyBuyGoods();
        }else {
            return DbManager.getInstance().getAllSelectCartGoods(true);
        }
    }

    @Override
    public String getCartInfoStr(List<Cartgoods> cartgoods ,boolean payMjb) {
        StringBuilder shopCartInfo = new StringBuilder();
        int selectCount = 0;
        for (int i = 0; i < cartgoods.size(); i++) {
            if (cartgoods.get(i).getIsSelect()) {
                selectCount++;
            }
        }
        int selectCounttemp = 0;
        for (int i = 0; i < cartgoods.size(); i++) {
            if (cartgoods.get(i).getIsSelect()) {
                if (payMjb) { //返回值带num，用于传值POST
                    String ispaymjb = cartgoods.get(i).getIsPayMjb()&&(Integer.parseInt(cartgoods.get(i).getGoodsdb().getIs_mjb())>0)?"1":"0";
                    shopCartInfo.append(cartgoods.get(i).getGoodsdb().getItem_id() + "|" + cartgoods.get(i).getGoods_num() +"|" + ispaymjb );
                } else {
                    shopCartInfo.append(cartgoods.get(i).getGoodsdb().getItem_id() + "|" + cartgoods.get(i).getGoods_num());
                }
                selectCounttemp++;
                if (selectCounttemp < selectCount) {
                    shopCartInfo.append(",");
                }
            }
        }
        return shopCartInfo.toString().trim();
    }


    @Override
    public void startWechatPay(String tempprice, String orderid) {
        weChatObserver = new Observer<WeChatPayResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                L.e(TAG + "  WeChatPayResponse onError" + e.toString());
                mView.showHintDialog(context.getResources().getString(R.string.wechat_pay_fail));
            }

            @Override
            public void onNext(WeChatPayResponse result) {
                mView.updateWeChatPayRequest(result.getPrepay_id(), result.getNonce_str());
            }
        };
        //开始微信支付，预下单
        L.e("前=======================" + tempprice);
        int price = (int) (Double.parseDouble(tempprice) * 100 + 0.5);
        L.e("后=======================" + price);
        // inner_member = 1 内部测试账号
        Map<String, String> premapdata = WeChatUtils.setAheadOrderParam(price + "", context.getResources().getString(R.string.mjmw_shop), orderid, Constants.PAY_PURPOSE_ORDER, 0);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/*"), WeChatUtils.genAheadOrderXML(premapdata));
        NetworkManager.getWeixinApi()
                .getPrepayId2("application/json", "application/json", requestBody)
                .compose(RxUtils.androidSchedulers(mView))
                .subscribe(weChatObserver);
    }

    @Override
    public void startAliPay(Activity activity,String price, String orderid) {
        String paytypeForRecall = "1";  //1订单支付 2钱包充值 3美家钻充值
        //支付宝支付
        AliPay aliPay = new AliPay(activity, context.getResources().getString(R.string.mjmw_shop), context.getResources().getString(R.string.mjmw_shop), price + "", orderid, paytypeForRecall, new AliPay.OnPayResultListener() {
            @Override
            public void onPaySucceed(AliPayResult payResult) {
                switch (payResult.resultStatus) {
                    case "9000":
                        L.e("zhifubao====9000");
                        PayActivity.activityStart(activity);
                        break;
                    case "4000":
                        L.e("zhifubao====4000");
                        PayActivity.activityStart(activity, "2");
//                        showErrorDialog(getResources().getString(R.string.order_pay_fail));
                        break;
                    case "6001":
                        L.e("zhifubao====6001");
                        PayActivity.activityStart(activity, "2");
//                        showErrorDialog(getResources().getString(R.string.cannal_zfb_pay));
                        break;
                    case "6002":
                        L.e("zhifubao====6002");
                        PayActivity.activityStart(activity, "2");
//                        showErrorDialog(getResources().getString(R.string.net_wrong));
                        break;
                    default:
                        L.e("zhifubao====default");
                        PayActivity.activityStart(activity, "2");
//                        showErrorDialog(getResources().getString(R.string.order_pay_wrong));
                        break;
                }
            }

            @Override
            public void onPayFailed(Exception e) {
                L.e("zhifubao====fail");
                PayActivity.activityStart(activity, "2");
            }
        });
        aliPay.pay();
    }

    @Override
    public List<Goods> getAdapterCartData(List<Cartgoods> cartgoods) {
        List<Goods> goodsList = new ArrayList<>();
        for (Cartgoods cartgood : cartgoods) {
            if (cartgood.getGoodsdb().getIs_bundling().equals("1")) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String bundinginfo = cartgood.getBunding_info();
                GoodsBundling goodsBundling = gson.fromJson(bundinginfo, GoodsBundling.class);
                goodsBundling.getItem();
                for (GoodsBundlingItem item : goodsBundling.getItem()) {
                    Goods goods = new Goods();
                    goods.setItem_id(item.getItem_id());
                    goods.setItem_img_thumb(item.getItem_img_thumb());
                    goodsList.add(goods);
                }
            } else {
                Goods goods = new Goods();
                goods.setItem_id(cartgood.getGoodsdb().getItem_id());
                goods.setItem_img_thumb(cartgood.getGoodsdb().getItem_img_thumb());
                goodsList.add(goods);
            }
        }
        return goodsList;
    }

    @Override
    public String getPayType(double useMjb,boolean paywechat,boolean payalipay) {
        StringBuilder paytypeStr = new StringBuilder();
        if(useMjb>0){
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
    public void ClearBuyImmediatelyGoods() {
        DbManager.getInstance().deleteBuyImmediatelyGoods();
    }
}
