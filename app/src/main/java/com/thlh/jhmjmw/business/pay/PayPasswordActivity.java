package com.thlh.jhmjmw.business.pay;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.OrderPayResponse;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.model.response.WeChatPayResponse;
import com.thlh.baselib.utils.ActivityUtils;
import com.thlh.baselib.utils.AliPayResult;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.order.list.OrderListActivity;
import com.thlh.jhmjmw.business.user.PhoneVerifyCodeActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.AliPay;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.WeChatUtils;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.ripple.RippleRelativeLayout;

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
 * 输入支付密码界面
 */
public class PayPasswordActivity extends BaseActivity {
    private static final String TAG = "PayPasswordActivity";


    @BindView(R.id.paypw_header)
    HeaderNormal paypwHeader;
    @BindView(R.id.paypw_input_et)
    EditText paypwInputEt;
    @BindView(R.id.paypw_input_ll)
    LinearLayout paypwInputLl;
    @BindView(R.id.paypw_forget_tv)
    TextView paypwForgetTv;
    @BindView(R.id.paypw_forget_ll)
    LinearLayout paypwForgetLl;
    @BindView(R.id.paypw_next_rip)
    RippleRelativeLayout paypwNextRip;
    @BindView(R.id.paypw_third_hint_tv)
    TextView paypwThirdHintTv;
    //    微信支付 start
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
    private PayReq weChatPayRequest;
    //微信取消判断
    private boolean isStartWechat = false;
    //    微信支付 end

    private int inputtype;
    private String paytype;
    private String orderid;
    private String paypw;
    private String itemIdAndNumAndMjb ;
    private BaseObserver<OrderPayResponse> payObserver;
    private DialogNormal.Builder dialogPay;

    private BaseObserver<WalletResponse> walletObserver;
    private Observer<WeChatPayResponse> weChatObserver;
    private int eronum = 5;

    public static void activityStart(Context context, int inputtype, String orderid,String paytype,String itemIdAndNumAndMjb) {
        Intent intent = new Intent();
        intent.putExtra("inputtype", inputtype); //进入界面区分
        intent.putExtra("paytype", paytype);
        intent.putExtra("orderid", orderid);
        intent.putExtra("itemIdAndNumAndMjb", itemIdAndNumAndMjb);
        intent.setClass(context, PayPasswordActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {
        msgApi.registerApp(Constants.WECHAT_APP_ID);
        weChatPayRequest = new PayReq();
        inputtype = getIntent().getIntExtra("inputtype", Constants.PAYPW_TYPE_ORDERCONFIRM_PAY);
        paytype = getIntent().getStringExtra("paytype");
        orderid = getIntent().getStringExtra("orderid");
        itemIdAndNumAndMjb = getIntent().getStringExtra("itemIdAndNumAndMjb");
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password_pay);
        ButterKnife.bind(this);

        if (paytype.contains("3") || paytype.contains("4")){
            if (paypwInputEt.isFocusable()){
                paypwThirdHintTv.setVisibility(View.VISIBLE);
                SpannableStringBuilder spannableString = new SpannableStringBuilder(getResources().getString(R.string.pay_password_hint));
                ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.app_theme));
                spannableString.setSpan(redSpan,24,27, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                paypwThirdHintTv.setText(spannableString);
            }
        }
        paypwHeader.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity("OrderListActivity");
                OrderListActivity.activityStart(PayPasswordActivity.this, Constants.ORDER_TYPE_WAIT_PAY);
                finish();
            }
        });


        dialogPay = new DialogNormal.Builder(this);
        paypwNextRip.setRLRippleCompleteListener(new RippleRelativeLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
                if (judgeCondition()) {
                    payOrder();
                }
            }
        });
        paypwHeader.setTitle(getResources().getString(R.string.pays_password));

        paypwInputEt.setFocusable(true);
        paypwInputEt.setFocusableInTouchMode(true);
        paypwInputEt.requestFocus();
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(paypwInputEt, 0);

        payObserver = new BaseObserver<OrderPayResponse>() {
            @Override
            public void onErrorResponse(OrderPayResponse payResponse) {
                progressMaterial.dismiss();

                OrderPayResponse.DataBean data = payResponse.getData();
                if (data == null) {
                    if (payResponse.getErr_code() == 503009) {
                        showPWErrorDialog(getResources().getString(R.string.paypass_ero));
                    }else {
                        showPWErrorDialog(payResponse.getErr_msg());
                    }

                } else {
                    if (!String.valueOf(payResponse.getData().getErr_num()).equals("")
                            && payResponse.getData().getErr_num() == 5) {
                        showPWErrorDialog(getResources().getString(R.string.paypass_ero));
                    }
                    if (!String.valueOf(payResponse.getData().getErr_num()).equals("")
                            && payResponse.getData().getErr_num() == 4) {
                        showPWErrorDialog(getResources().getString(R.string.paypass_last));
                    } else {
                        int err_num = eronum -  payResponse.getData().getErr_num();
                        showPWErrorDialog(payResponse.getErr_msg() + getResources().getString(R.string.pay_num) +
                                err_num + getResources().getString(R.string.pay_text));
                    }
                }
            }

            @Override
            public void onNextResponse(OrderPayResponse payResponse) {
                //保存支付订单号
                String pay_no = payResponse.getData().getPay_no();
                SPUtils.put("pay_no", pay_no);
                SPUtils.put("orderPayNo",pay_no);
                String tempprice = Double.toString(payResponse.getData().getAmount());
                SPUtils.put("pay_price", tempprice);
                L.i(TAG + " 生成 orderid " + pay_no + "; amount " + tempprice);
                //保存支付参数
                SPUtils.setPayParam(tempprice, paytype, Constants.PAY_PURPOSE_ORDER, pay_no);

                if (payResponse.getData().getAmount() == 0) {
                    PayActivity.activityStart(PayPasswordActivity.this,"1");
                    finish();
                    return;
                } else {
                    if ( paytype.indexOf(Constants.PAY_TYPE_WECHAT)> -1) {
                        L.i(TAG + " 调用微信支付 tempprice" + tempprice + " pay_no" + pay_no);
                        startWechatPay(tempprice, pay_no);
                        return;
                    }
                    if ( paytype.indexOf(Constants.PAY_TYPE_ALIPAY)>-1) {
                        L.i(TAG + " 调用支付宝支付 tempprice " + tempprice + " pay_no" + pay_no);
                        startAliPay(tempprice, pay_no);
                        return;
                    }
                    loadWallet();
                    showPayPartSuccessDialog(tempprice);
                }
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
                isStartWechat = true;
            }
        };

        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
                progressMaterial.dismiss();
                showErrorDialog(walletResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                progressMaterial.dismiss();
                SPUtils.put("user_balance", walletResponse.getData().getBalance() + "");
                SPUtils.put("user_mjb", walletResponse.getData().getMjb() + "");
            }
        };
    }

    @Override
    protected void loadData() {
    }

    private boolean judgeCondition() {
        paypw = paypwInputEt.getText().toString().trim();
        if (paypw.length() < 6) {
            showWaringDialog(getResources().getString(R.string.re_password_success_num));
            return false;
        }
        return true;
    }

    private void payOrder() {
        L.e( "payOrder参数 orderid:"+orderid +" paytype:"+paytype + " paypw:"+paypw +" itemIdAndNumAndMjb:"+itemIdAndNumAndMjb);
        SPUtils.put("orderid",orderid);
        Subscription subscription = NetworkManager.getOrderApi()
                .payOrderV2(SPUtils.getToken(), orderid, paytype ,paypw,itemIdAndNumAndMjb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(payObserver);
        subscriptionList.add(subscription);
    }

    public void loadWallet() {
        Subscription subscription = NetworkManager.getUserDataApi()
                .wallet(SPUtils.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletObserver);
        subscriptionList.add(subscription);
    }

    @OnClick(R.id.paypw_forget_ll)
    public void onClick() {
        PhoneVerifyCodeActivity.activityStart(PayPasswordActivity.this,Constants.PHONECODE_TYPE_RESET);
    }


    private void showPayPartSuccessDialog(String price) {
        dialogPay.setTitle(getResources().getString(R.string.pay_money_success))
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

//    private void showHintDialog(String hintContent) {
//        hintDialog.setTitle("")
//                .setCancelOutside(false)
//                .setSubTitle(hintContent)
//                .setRightBtnStr("返回")
//                .setRightClickListener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                }).create().show();
//    }

    private void startWechatPay(String tempprice, String orderid) {
        //开始微信支付，预下单
        int price = (int) (Double.parseDouble(tempprice) * 100 + 0.5);
        // inner_member = 1 内部测试账号
        Map<String, String> premapdata = WeChatUtils.setAheadOrderParam(price + "", getResources().getString(R.string.mjmw_shop), orderid, Constants.PAY_PURPOSE_ORDER, 0);
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
        AliPay aliPay = new AliPay(PayPasswordActivity.this, getResources().getString(R.string.mjmw_shop), getResources().getString(R.string.mjmw_shop), tempprice + "", orderid, paytypeForRecall, new AliPay.OnPayResultListener() {
            @Override
            public void onPaySucceed(AliPayResult payResult) {
                progressMaterial.dismiss();
                switch (payResult.resultStatus) {
                    case "9000":
                        L.e("zhifubao====9000");
                        PayActivity.activityStart(PayPasswordActivity.this,"1");
                        finish();
                        break;
                    case "4000":
                        L.e("zhifubao====4000");
                        PayActivity.activityStart(PayPasswordActivity.this,"2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.order_pay_fail));
                        break;
                    case "6001":
                        L.e("zhifubao====6001");
                        PayActivity.activityStart(PayPasswordActivity.this,"2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.cannal_zfb_pay));
                        break;
                    case "6002":
                        L.e("zhifubao====6002");
                        PayActivity.activityStart(PayPasswordActivity.this,"2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.net_wrong));
                        break;
                    default:
                        L.e("zhifubao====default");
                        PayActivity.activityStart(PayPasswordActivity.this,"2");
                        finish();
//                        showErrorDialog(getResources().getString(R.string.order_pay_wrong));
                        break;
                }
            }

            @Override
            public void onPayFailed(Exception e) {
                progressMaterial.dismiss();
                L.e("zhifubao====fail");
                PayActivity.activityStart(PayPasswordActivity.this,"2");
                finish();
            }
        });
        aliPay.pay();
    }

    // 回退键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            ActivityUtils.finishActivity("OrderListActivity");
            OrderListActivity.activityStart(PayPasswordActivity.this, Constants.ORDER_TYPE_WAIT_PAY);
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isStartWechat) {
            OrderListActivity.activityStart(PayPasswordActivity.this, Constants.ORDER_TYPE_WAIT_PAY);
            ActivityUtils.finishActivity("ShopCartActivity");
            finish();
        }
    }

    public void showPWErrorDialog(String contentStr) {
        final NormalDialogFragment normalFrgDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        normalFrgDialog.setContentStr(contentStr);
        normalFrgDialog.setContentType(DialogUtils.TYPE_NORMAL_ERROR);
        normalFrgDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paypwInputEt.setText("");
                normalFrgDialog.dismiss();
            }
        });
        normalFrgDialog.show(ft, "errorPwDialog");
    }
}


