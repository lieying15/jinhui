package com.thlh.jhmjmw.business.recharge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.RechargeBalanceResponse;
import com.thlh.baselib.model.response.RechargeMjbResponse;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.model.response.WeChatPayResponse;
import com.thlh.baselib.utils.AliPayResult;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.index.shop.ShopMapActivity;
import com.thlh.jhmjmw.business.user.coupon.CouponActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.AliPay;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.WeChatUtils;
import com.thlh.jhmjmw.view.DialogCoupon;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.jhmjmw.view.HeaderNormal;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;

import static com.thlh.baselib.utils.SPUtils.get;


/**
 * 充值界面
 */
public class RechargeActivity extends BaseActivity {
    private static final int ACTIVITY_CODE_SCANNIN = 3;

    private final String TAG = "RechargeActivity";
    @BindView(R.id.recharge_header)
    HeaderNormal rechargeHeader;
    @BindView(R.id.recharge_wechat_select_iv)
    ImageView rechargeWechatSelectIv;
    @BindView(R.id.recharge_wechat_ll)
    LinearLayout rechargeWechatLl;
    @BindView(R.id.recharge_alipy_select_iv)
    ImageView rechargeAlipySelectIv;
    @BindView(R.id.recharge_alipay_ll)
    LinearLayout rechargeAlipayLl;
    @BindView(R.id.recharge_input_et)
    EditText rechargeInputEt;
    @BindView(R.id.money_mjz)
    LinearLayout moneyMjz;
    @BindView(R.id.recharge_rechargenormal_ll)
    LinearLayout rechargeRechargenormalLl;
    @BindView(R.id.recharge_protocol_cb)
    CheckBox rechargeProtocolCb;
    @BindView(R.id.recharge_protocol_tv)
    TextView rechargeProtocolTv;
    @BindView(R.id.recharge_protocol_ll)
    LinearLayout rechargeProtocolLl;
    @BindView(R.id.recharge_hint_tv)
    TextView rechargeHintTv;
    @BindView(R.id.call_phone_tv)
    TextView callPhoneTv;


    private boolean need_updata;
    private BaseObserver<WalletResponse> walletObserver;
    private BaseObserver<RechargeMjbResponse> rechargeMjbObserver;
    private BaseObserver<RechargeBalanceResponse> rechargeBalanceObserver;
    private String pay_third_type = Constants.PAY_TYPE_WECHAT;
    private boolean paywechat = false;
    private boolean payali = false;
    private String pay_purpose = Constants.PAY_PURPOSE_MJB;  // 3美家钻充值
    private DialogCoupon.Builder dialogCoupon;
    private DialogNormal.Builder dialogResult, dialogHint;

    //    微信支付 start
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
    private PayReq weChatPayRequest;
    private Observer<WeChatPayResponse> weChatObserver;
    private int inner_member;
    //    微信支付 end
//    private int is_coupon =0;
    private boolean hadRechargeBox = false;
    private boolean agreeRechargeProtocol = false;

    private static int is_ch = 0;
    private static final int SELECTNUMBER = 1;
    private static final int INPUTNUMBER = 2;
    private int selectnum = 0;
    private String selectMonney = "0";


    public static void activityStart(Activity context, String pay_purpose) {
        Intent intent = new Intent();
        intent.setClass(context, RechargeActivity.class);
        intent.putExtra("pay_purpose", pay_purpose);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context, String pay_purpose, int code) {
        Intent intent = new Intent();
        intent.setClass(context, RechargeActivity.class);
        intent.putExtra("pay_purpose", pay_purpose);
        context.startActivityForResult(intent, code);
    }

    //从微信支付结果跳转，需要刷新一下数据
    public static void activityStart(Activity context, String pay_purpose, boolean need_updata) {
        Intent intent = new Intent();
        intent.setClass(context, RechargeActivity.class);
        intent.putExtra("pay_purpose", pay_purpose);
        intent.putExtra("need_updata", need_updata);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        weChatPayRequest = new PayReq();
        msgApi.registerApp(Constants.WECHAT_APP_ID);
        pay_purpose = getIntent().getStringExtra("pay_purpose");
        need_updata = getIntent().getBooleanExtra("need_ata", false);
        inner_member = Integer.valueOf(SPUtils.get("user_inner_member", 0).toString());
        //
        hadRechargeBox = Boolean.valueOf(SPUtils.get("user_hadchange_icebox", false).toString());
//        agreeRechargeProtocol = (boolean)  SPUtils.get("user_agree_recharge_protocol",false);
        // 产品表示每次进入均为关闭状态。
        agreeRechargeProtocol = false;
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);

        dialogCoupon = new DialogCoupon.Builder(this);
        dialogResult = new DialogNormal.Builder(this);
        dialogHint = new DialogNormal.Builder(this);

        rechargeProtocolCb.setChecked(agreeRechargeProtocol);
        rechargeProtocolCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agreeRechargeProtocol = isChecked;
                if (isChecked) {
                    rechargeProtocolTv.setTextColor(getResources().getColor(R.color.wine_light));
                } else {
                    rechargeProtocolTv.setTextColor(getResources().getColor(R.color.wine_light));
                }
                SPUtils.put("user_agree_recharge_protocol", agreeRechargeProtocol);
                L.i(TAG + " 更改协议状态 agreeRechargeProtocol：" + agreeRechargeProtocol);
            }
        });
        rechargeProtocolTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargerAgreementActivity.activityStart(RechargeActivity.this);
            }
        });
        rechargeHeader.setTitle(getResources().getString(R.string.mjz_top_up));
        rechargeHintTv.setVisibility(View.VISIBLE);


        selectnum = INPUTNUMBER;
        rechargeHintTv.setText(getResources().getString(R.string.top_up_change_icebox));
        moneyMjz.setVisibility(View.VISIBLE);

        rechargeMjbObserver = new BaseObserver<RechargeMjbResponse>() {
            @Override
            public void onErrorResponse(RechargeMjbResponse rechargeMjbResponse) {

                dialogResult.setSubTitle(rechargeMjbResponse.getErr_msg())
                        .setRightBtnStr(getResources().getString(R.string.back))
                        .setRightClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create().show();
            }

            @Override
            public void onNextResponse(RechargeMjbResponse rechargeMjbResponse) {
                SPUtils.put("needupdate_userinfo", true);
                String pay_no = rechargeMjbResponse.getData().getPay_no();
                int is_coupon = rechargeMjbResponse.getData().getIs_coupon(); // Is_coupon =1 已经充值过6999.
                SPUtils.put("mjbrecharge_getcoupon", is_coupon);
                double amount = rechargeMjbResponse.getData().getThird_amount();
                L.e(TAG + " 充值结果  pay_no" + pay_no + " is_coupon" + is_coupon + " amount " + amount);
                //保存支付参数
                SPUtils.setPayParam(amount + "", pay_third_type, pay_purpose, pay_no);
                if (amount > 0) {
                    switch (pay_third_type) {
                        case Constants.PAY_TYPE_WECHAT:
                            L.i(TAG + " 调用微信支付 amount " + amount + " pay_no" + pay_no);
                            startWechatPay(amount + "", pay_no);
                            break;
                        case Constants.PAY_TYPE_ALIPAY:
                            L.i(TAG + " 调用支付宝支付 amount " + amount + " pay_no" + pay_no);
                            startAliPay(amount + "", pay_no);
                            break;
                    }
                } else {
                    showRechargeResultDialog();
                    loadWalletData();
                }
            }
        };
        /**
         * */
        rechargeBalanceObserver = new BaseObserver<RechargeBalanceResponse>() {
            @Override
            public void onErrorResponse(RechargeBalanceResponse rechargeMjbResponse) {
                showErrorDialog(rechargeMjbResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(RechargeBalanceResponse rechargeMjbResponse) {
                SPUtils.put("needupdate_userinfo", true);
                String pay_no = rechargeMjbResponse.getData().getPay_no();
                double amount = Double.parseDouble(rechargeMjbResponse.getData().getAmount());
                L.e(TAG + " 充值结果  pay_no" + pay_no + " amount " + amount);
                //保存支付参数
                SPUtils.setPayParam(amount + "", pay_third_type, pay_purpose, pay_no);
                if (amount > 0) {
                    switch (pay_third_type) {
                        case Constants.PAY_TYPE_WECHAT:
                            L.i(TAG + " 调用微信支付 amount " + amount + " pay_no" + pay_no);
                            startWechatPay(amount + "", pay_no);
                            break;
                        case Constants.PAY_TYPE_ALIPAY:
                            L.i(TAG + " 调用支付宝支付 amount " + amount + " pay_no" + pay_no);
                            startAliPay(amount + "", pay_no);
                            break;
                    }
                } else {
                    showRechargeNormalResultDialog();
                    loadWalletData();
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
            }
        };

        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
                showErrorDialog(walletResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                SPUtils.put("user_balance", walletResponse.getData().getBalance() + "");
                SPUtils.put("user_mjb", walletResponse.getData().getMjb() + "");
            }
        };
    }

    /**
     *  支付的钱数和方式
     * @param amount
     */
    private void postRechange(String amount) {
        progressMaterial.show();

        //方式
        String temppay = getPaytype();

        L.e(TAG + " 充值请求  Paytype" + temppay + " amount " + amount);
        //美家钻
        switch (pay_purpose) {
            case Constants.PAY_PURPOSE_MJB:
                //
                postRechargeMjb(amount, temppay);
                break;
        }
    }

    /**
     *   充值方式
     */
    private String getPaytype() {
        StringBuilder temppay = new StringBuilder();
        //微信充值
        if (paywechat) {
            temppay.append(Constants.PAY_TYPE_WECHAT);
            return temppay.toString();
        }
        //支付宝充值
        if (payali) {
            temppay.append(Constants.PAY_TYPE_ALIPAY);
        }
        return temppay.toString();
    }

    @Override
    protected void loadData() {
    }

    @OnClick({R.id.recharge_wechat_ll, R.id.recharge_alipay_ll,
            R.id.recharge_rechargenormal_ll, R.id.recharge_protocol_ll,R.id.call_phone_tv})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_wechat_ll:
                paywechat = !paywechat;
                if (paywechat) {
                    pay_third_type = Constants.PAY_TYPE_WECHAT;
                    rechargeWechatSelectIv.setImageResource(R.drawable.icon_check_wine_select);
                } else {
                    rechargeWechatSelectIv.setImageResource(R.drawable.icon_check_wine);
                }
                payali = false;
                rechargeAlipySelectIv.setImageResource(R.drawable.icon_check_wine);
                break;

            case R.id.recharge_alipay_ll:
                payali = !payali;
                if (payali) {
                    pay_third_type = Constants.PAY_TYPE_ALIPAY;
                    rechargeAlipySelectIv.setImageResource(R.drawable.icon_check_wine_select);
                } else {
                    rechargeAlipySelectIv.setImageResource(R.drawable.icon_check_wine);
                }
                paywechat = false;
                rechargeWechatSelectIv.setImageResource(R.drawable.icon_check_wine);
                break;
            /**
             * 点击充值
             * */
            case R.id.recharge_rechargenormal_ll:
                if (selectnum == INPUTNUMBER){
                    selectMonney = rechargeInputEt.getText().toString().trim();
                }

                if (jugeRechargeCondition(selectMonney)) {
                    postRechange(selectMonney);
                }
                break;

            /**
             * 协议
             */
            case R.id.recharge_protocol_ll:
                //ProtocolRechargeActivity.activityStart(this);
                break;
             case R.id.call_phone_tv:
                 showServiceDialog();
                break;


        }
    }

    public boolean payMJZ() {
        /**同意协议*/
        if (!agreeRechargeProtocol) {
            showWaringDialog(getResources().getString(R.string.disagree));
            return false;
        }
        /**充值方式*/
        switch (pay_purpose) {
            case Constants.PAY_PURPOSE_MJB:
                if (payali == false && paywechat == false) {
                    showWaringDialog(getResources().getString(R.string.pay_choose));
                    return false;
                }
        }
        return true;
    }

    /**
     * 判断充值前的支付方式和支付协议
     * 并且获取支付的金额
     * @return
     * @param selectMonney
     */
    private boolean jugeRechargeCondition(String selectMonney) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rechargeInputEt.getWindowToken(), 0);

        if (!agreeRechargeProtocol) {
            showWaringDialog(getResources().getString(R.string.disagree));
            return false;
        }
        if (selectnum == INPUTNUMBER) {
            if (rechargeInputEt.getText().toString().trim().equals("")) {
                showWaringDialog(getResources().getString(R.string.mjz_top_up_money_input));
                return false;
            }

            double price = Double.parseDouble(rechargeInputEt.getText().toString().trim());
            double balance = Double.parseDouble((String) get("user_balance", "0"));
            if (price > balance && (!payali) && (!paywechat)) {
                showWaringDialog(getResources().getString(R.string.no_money_choose_help));
                return false;
            }
        }

        if (Double.parseDouble(selectMonney) > 0){
            showWaringDialog(getResources().getString(R.string.no_money_show));
        }
        switch (pay_purpose) {
            case Constants.PAY_PURPOSE_MJB:
                if (payali == false && paywechat == false) {
                    showWaringDialog(getResources().getString(R.string.pay_choose));
                    return false;
                }
        }
        return true;
    }

    /**
     * 开始微信支付，预下单
     *
     */
    private void startWechatPay(String tempprice, String orderid) {
        int price = (int) (Double.parseDouble(tempprice) * 100 + 0.5);
        L.i(TAG + " startWechatPay price " + price);
        Map<String, String> premapdata = WeChatUtils.setAheadOrderParam(price + "", getResources().getString(R.string.mjmw_shop), orderid, pay_purpose, inner_member);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/*"), WeChatUtils.genAheadOrderXML(premapdata));
        NetworkManager.getWeixinApi()
                .getPrepayId2("application/json", "application/json", requestBody)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(weChatObserver);
    }

    /**
     * 支付宝支付
     */
    private void startAliPay(String tempprice, String orderid) {
        AliPay aliPay = new AliPay(RechargeActivity.this, getResources().getString(R.string.mj_wallet_topup), getResources().getString(R.string.mj_wallet_topup), tempprice,
                orderid, pay_purpose + "", new AliPay.OnPayResultListener() {
            @Override
            public void onPaySucceed(AliPayResult payResult) {
                /**
                 * 充值成功后隐藏
                 *
                 */
//               rechargeIceboxll.setVisibility(View.GONE);

                switch (payResult.resultStatus) {
                    case "9000":
                        loadWalletData();
                        showRechargeResultDialog();
                        break;
                    case "4000":
                        showErrorDialog(getResources().getString(R.string.order_pay_fail));
                        break;
                    case "6001":
                        showErrorDialog(getResources().getString(R.string.cannal_zfb_pay));
                        break;
                    case "6002":
                        showErrorDialog(getResources().getString(R.string.net_wrong));
                        break;
                    default:
                        showErrorDialog(getResources().getString(R.string.order_pay_wrong));
                        break;
                }
            }

            @Override
            public void onPayFailed(Exception e) {
                showErrorDialog(getResources().getString(R.string.pay_fail));
            }
        });
        aliPay.pay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.e(TAG + "onNewIntent ");
        //从微信支付结果跳转，需要刷新一下数据
        need_updata = intent.getBooleanExtra("need_updata", false);
        if (need_updata) {
            loadWalletData();
            showRechargeResultDialog();
        }
    }

    /**
     * 获取用户信息
     */
    protected void loadWalletData() {
        NetworkManager.getUserDataApi()
                .wallet(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(walletObserver);
    }

    /**
     * 每家钻充值
     * @param amount
     * @param pay
     */
    protected void postRechargeMjb(String amount, String pay) {
        SPUtils.put("recharge_mjb_amount", amount);
        NetworkManager.getWalletApi()
                .rechargeMjb(SPUtils.getToken(), amount, pay)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(rechargeMjbObserver);
    }


    /*private void showRechargeHintDialog() {
        final NormalDialogFragment rechargeHintDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        rechargeHintDialog.setMiddleBtnVisible(View.VISIBLE);
        rechargeHintDialog.setMiddleBtnStr(getResources().getString(R.string.join_return_change));
        rechargeHintDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeHintDialog.dismiss();
            }
        });

        if (agency_id == 10 || agency_id == 11) {
            rechargeHintDialog.setContentStr(getResources().getString(R.string.topup_money_no) + ICEBOX_PRICE + getResources().getString(R.string.topup_money_no_join_conti));
        } else if (agency_id == 37) {
            rechargeHintDialog.setContentStr(getResources().getString(R.string.topup_money_no) + RECHARGE_SENDICEBOX_PRICE + getResources().getString(R.string.topup_money_no_join_conti));
        }else {
            rechargeHintDialog.setContentStr(getResources().getString(R.string.topup_money_no) + RECHARGE_SENDICEBOX_PRICE + getResources().getString(R.string.topup_money_no_join_conti));
        }
        //rechargeHintDialog.setContentStr(getResources().getString(R.string.topup_money_no) + RECHARGE_SENDICEBOX_PRICE + getResources().getString(R.string.topup_money_no_join_conti));
        rechargeHintDialog.setContentType(DialogUtils.TYPE_NORMAL_WARNING);
        rechargeHintDialog.setFinalBtnStr(getResources().getString(R.string.no_join_next));
        rechargeHintDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRechange(selectMonney);
                rechargeHintDialog.dismiss();
            }
        });
        rechargeHintDialog.show(ft, "rechargeHintDialog");
    }*/

    private void showHintBindDialog() {
        final NormalDialogFragment bingdShopDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        bingdShopDialog.setCancelVisible(View.VISIBLE);
        bingdShopDialog.setContentStr(getResources().getString(R.string.no_bind_shop));
        bingdShopDialog.setContentType(DialogUtils.TYPE_NORMAL_WARNING);
        bingdShopDialog.setFinalBtnStr(getResources().getString(R.string.gps_near_shop));
        bingdShopDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPUtils.getIsLogin()) {
                    ShopMapActivity.activityStart(RechargeActivity.this);
                } else {
                    LoginActivity.activityStart(RechargeActivity.this);
                }
                bingdShopDialog.dismiss();
            }
        });
        bingdShopDialog.show(ft, "bingdShopDialog");
    }

    private void showRechargeResultDialog() {
        progressMaterial.dismiss();
        int is_coupon = (int) get("mjbrecharge_getcoupon", 0);
        double amount = Double.parseDouble((String) SPUtils.get("recharge_mjb_amount", "0").toString());
        String pay_purpose = (String) SPUtils.get("pay_purpose", "2").toString();  // 1下单支付 2钱包充值 3美家钻充值
        L.e(TAG + " showRechargeResultDialog is_coupon:" + is_coupon + " hadRechargeBox " + hadRechargeBox + " pay_purpose" + pay_purpose + " amount " + amount);

        if (amount >= Constants.RECHARGE_SENDICEBOX_PRICE&& !hadRechargeBox && pay_purpose.equals("3") && is_coupon == 0 ) {
            /**充值成功后隐藏*/
//            rechargeIceboxll.setVisibility(View.GONE);

            hadRechargeBox = true;
            SPUtils.put("user_hadchange_icebox", hadRechargeBox);
            dialogCoupon.setTitle(getResources().getString(R.string.topup_success))
                    .setSubTitle(getResources().getString(R.string.ice_one))
                    .setLeftBtnStr(getResources().getString(R.string.back))
                    .setRightBtnStr(getResources().getString(R.string.once_exchange))
                    .setLeftClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CouponActivity.activityStart(RechargeActivity.this);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    })
                    .setRightClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CouponActivity.activityStart(RechargeActivity.this);
                        }
                    })
                    .create().show();
        } else {


            showRechargeNormalResultDialog();
        }
    }

    public void showServiceDialog() {
        DialogUtils.showPhone(this, getResources().getString(R.string.call_return));
    }

    private void showRechargeNormalResultDialog() {
        final NormalDialogFragment rechargeResultDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        rechargeResultDialog.setContentStr(getResources().getString(R.string.topup_success));
        rechargeResultDialog.setTitleIvRes(R.drawable.img_dialog_recharge_sucess);
        rechargeResultDialog.setContentType(DialogUtils.TYPE_NORMAL_SUCCESS);
        rechargeResultDialog.setFinalBtnStr(getResources().getString(R.string.back));
        rechargeResultDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeResultDialog.dismiss();
                finish();
            }
        });

        rechargeResultDialog.show(ft, "rechargeResultDialog");
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
