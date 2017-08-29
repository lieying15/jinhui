package com.thlh.jhmjmw.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.response.OrderDetailsResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.order.orderdetail.OrderDetailActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.recharge.RechargeActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WXPayEntryActivity extends BaseViewActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    @BindView(R.id.wechat_pay_result_header)
    HeaderNormal wechatPayResultHeader;
    @BindView(R.id.wechat_pay_result_title_iv)
    ImageView wechatPayResultTitleIv;
    @BindView(R.id.wechat_pay_result_title_tv)
    TextView wechatPayResultTitleTv;
    @BindView(R.id.wechat_pay_result_content_tv)
    TextView wechatPayResultContentTv;
    @BindView(R.id.wechat_pay_result_back_btn)
    TextView wechatPayResultBackBtn;


    private IWXAPI api;
    private String orderid;
    private boolean flag = false;
    private BaseObserver<OrderDetailsResponse> orderObserver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weichat_pay_result);
        ButterKnife.bind(this);
        L.e(TAG, "  WXPayEntryActivity onCreate ");
        orderid = (String) SPUtils.get("orderid", "");
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);

        orderObserver = new BaseObserver<OrderDetailsResponse>() {
            @Override
            public void onNextResponse(OrderDetailsResponse orderDetailsResponse) {
                hideLoadindBar();
                Order data = orderDetailsResponse.getData();
                OrderDetailActivity.activityStart(WXPayEntryActivity.this,data);
                finish();
            }

            @Override
            public void onErrorResponse(OrderDetailsResponse orderDetailsResponse) {
                hideLoadindBar();
            }
        };


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        L.e(TAG, "  WXPayEntryActivity onNewIntent ");
    }

    @Override
    public void onReq(BaseReq req) {
        L.e(TAG, "onPayFinish, errCode = " + req.toString());
    }

    @Override
    public void onResp(BaseResp resp) {
        L.e(TAG, " onPayFinish, errCode = " + resp.errCode + "; errStr = " + resp.errStr + "; transaction =" + resp.transaction + "; openId =" + resp.openId);
        switch (resp.errCode) {
            case 0:
                flag = true;
                String pay_purpose = (String) SPUtils.get("pay_purpose", Constants.PAY_PURPOSE_ORDER).toString(); // 1订单支付 2钱包充值 3美家币充值
                String pay_no = (String) SPUtils.get("pay_no", "").toString();
                String pay_price = (String) SPUtils.get("pay_price", "").toString();
                L.e(TAG, " onPayFinish, pay_price  " + pay_price + " pay_purpose:" + pay_purpose + " pay_no:" + pay_no);
                if (pay_purpose.equals(Constants.PAY_PURPOSE_ORDER)) {
                    wechatPayResultContentTv.setVisibility(View.VISIBLE);
                    wechatPayResultTitleIv.setImageResource(R.drawable.icon_check_select_pink);
                    wechatPayResultTitleTv.setText(getResources().getString(R.string.pay_money_success));
                    wechatPayResultBackBtn.setText(getResources().getString(R.string.return_home));
                } else {
                    RechargeActivity.activityStart(this, pay_purpose, true);
                    finish();
                }

                break;
            case -1:
                flag = false;
                wechatPayResultContentTv.setVisibility(View.GONE);
                wechatPayResultTitleIv.setImageResource(R.drawable.i_recharge_fail);
                wechatPayResultTitleTv.setText(getResources().getString(R.string.pay_fail));
                wechatPayResultBackBtn.setText(getResources().getString(R.string.return_pay));
                break;
            case -2:
                flag = false;
                wechatPayResultContentTv.setVisibility(View.GONE);
                wechatPayResultTitleIv.setImageResource(R.drawable.i_recharge_fail);
                wechatPayResultTitleTv.setText(getResources().getString(R.string.pay_fail));
                wechatPayResultBackBtn.setText(getResources().getString(R.string.return_pay));
                L.e(TAG, " 用户取消");
                break;
        }
    }

    @OnClick(R.id.wechat_pay_result_back_btn)
    public void onClick() {
        String orderPayNo = (String) SPUtils.get("orderPayNo", "");
        if (orderPayNo.contains("MR")){
            finish();
        }else if (orderPayNo.contains("ZF")) {
            if (flag) {
                IndexActivity.activityStart(this, IndexActivity.POSITON_HOMEPAGE);
                finish();
            } else {
                loadOrderDetails(orderid);
            }
        }
    }

    private void loadOrderDetails(String orderid) {
        showLoadingBar();
        Subscription subscription = NetworkManager.getOrderApi()
                .getOrderDetails(SPUtils.getToken(),orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderObserver);
        subscriptionList.add(subscription);
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }
}