package com.thlh.jhmjmw.business.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.response.OrderDetailsResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.order.orderdetail.OrderDetailActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单支付返回页面
 */
public class PayActivity extends BaseViewActivity {

    private static final String SUCCESS="1";
    private static final String FAIL = "2";

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
    private String payState = SUCCESS;
    private String orderid;
    private BaseObserver<OrderDetailsResponse> orderObserver;


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, PayActivity.class);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context, String payState) {
        Intent intent = new Intent();
        intent.setClass(context, PayActivity.class);
        intent.putExtra("payState", payState);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        payState = getIntent().getStringExtra("payState");
        orderid = (String) SPUtils.get("orderid", "");
        L.e("orderid===========" + orderid);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);

        if (payState.equals(SUCCESS)){
            wechatPayResultContentTv.setVisibility(View.VISIBLE);
            wechatPayResultTitleIv.setImageResource(R.drawable.icon_check_select_pink);
            wechatPayResultTitleTv.setText(getResources().getString(R.string.pay_money_success));
            wechatPayResultBackBtn.setText(getResources().getString(R.string.return_home));
        }else {
            wechatPayResultContentTv.setVisibility(View.GONE);
            wechatPayResultTitleIv.setImageResource(R.drawable.i_recharge_fail);
            wechatPayResultTitleTv.setText(getResources().getString(R.string.pay_fail));
            wechatPayResultBackBtn.setText(getResources().getString(R.string.return_pay));
        }

        orderObserver = new BaseObserver<OrderDetailsResponse>() {
            @Override
            public void onNextResponse(OrderDetailsResponse orderDetailsResponse) {
                progressMaterial.show();
                Order data = orderDetailsResponse.getData();
                OrderDetailActivity.activityStart(PayActivity.this,data);
                finish();
            }

            @Override
            public void onErrorResponse(OrderDetailsResponse orderDetailsResponse) {
                progressMaterial.show();
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void loadOrderDetails(String orderid) {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getOrderApi()
                .getOrderDetails(SPUtils.getToken(),orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderObserver);
        subscriptionList.add(subscription);
    }


    @OnClick(R.id.wechat_pay_result_back_btn)
    public void onClick() {
        if (payState.equals(SUCCESS)){
            IndexActivity.activityStart(this);

        }else {
            if (orderid != null) {
                loadOrderDetails(orderid);
            }
        }
        finish();
    }

}
