package com.thlh.jhmjmw.business.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.response.OrderDetailsResponse;
import com.thlh.baselib.utils.ActivityUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.order.orderdetail.OrderDetailActivity;
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
 * 处理各种请求的回调页
 */
public class ResponseActivity extends BaseActivity {
    private final String TAG = "ResponseActivity";
    @BindView(R.id.response_header)
    HeaderNormal responseHeader;
    @BindView(R.id.response_status_iv)
    ImageView statusIv;
    @BindView(R.id.response_title_tv)
    TextView responseTitleTv;
    @BindView(R.id.response_content_tv)
    TextView responseContentTv;
    @BindView(R.id.response_back_tv)
    TextView responseBackTv;
    private ActionResponse actionResponse = new ActionResponse();
    private String backType = Constants.RESPONSE_BACK_TYPE_HOME;
    private String orderid;
    private BaseObserver<OrderDetailsResponse> orderObserver;


    public static void activityStart(Activity context, ActionResponse actionResponse) {
        Intent intent = new Intent();
        intent.setClass(context, ResponseActivity.class);
        intent.putExtra("response", actionResponse);
        context.startActivity(intent);
    }
    public static void activityStart(Activity context, ActionResponse actionResponse,String orderid) {
        Intent intent = new Intent();
        intent.setClass(context, ResponseActivity.class);
        intent.putExtra("response", actionResponse);
        intent.putExtra("orderid", orderid);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        actionResponse = getIntent().getParcelableExtra("response");
        orderid = getIntent().getStringExtra("orderid");
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_response);
        ButterKnife.bind(this);
        if (actionResponse == null) {
            L.e(TAG + " actionResponse 数据为空");
            responseBackTv.setText(getResources().getString(R.string.return_home));
            statusIv.setImageResource(R.drawable.img_face_sad_wine);
            responseTitleTv.setText(getResources().getString(R.string.setting_fail));
            responseContentTv.setText(getResources().getString(R.string.resetting_call_phone));
        }

        String headerTitle = actionResponse.getHeadertitle();
        if (headerTitle != null && !headerTitle.equals("")) {
            responseHeader.setTitle(headerTitle);
        }


        if (actionResponse.isSuccess()) {
            statusIv.setImageResource(R.drawable.icon_dialog_success);
        } else {
            statusIv.setImageResource(R.drawable.icon_dialog_error);
        }

        String title = actionResponse.getTitle();
        if (title != null && !title.equals("")) {
            responseTitleTv.setText(title);
        }

        String content = actionResponse.getContent();
        if (content != null && !content.equals("")) {
            responseContentTv.setText(actionResponse.getContent());
        }


        String backStr = actionResponse.getBackStr();
        if (backStr != null && !backStr.equals("")) {
            responseBackTv.setText(backStr);
        }

        String tempBackType = actionResponse.getBackType();
        if (tempBackType != null && !tempBackType.equals("")) {
            backType = tempBackType;
        }

        orderObserver = new BaseObserver<OrderDetailsResponse>() {
            @Override
            public void onNextResponse(OrderDetailsResponse orderDetailsResponse) {
                Order data = orderDetailsResponse.getData();
                OrderDetailActivity.activityStart(ResponseActivity.this,data);
                finish();
            }

            @Override
            public void onErrorResponse(OrderDetailsResponse orderDetailsResponse) {

            }
        };

    }

    @Override
    protected void loadData() {

    }



    @OnClick({ R.id.response_back_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.response_back_tv:
                if (orderid != null ){

                }
                backAction();
                break;
        }
    }

    private void loadOrderDetails(String orderid) {
        Subscription subscription = NetworkManager.getOrderApi()
                .getOrderDetails(SPUtils.getToken(),orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderObserver);
        subscriptionList.add(subscription);
    }


    private void backAction(){
        switch (backType) {
            case Constants.RESPONSE_BACK_TYPE_HOME:
                ActivityUtils.popAllActivityUntilSpecify(IndexActivity.class);
                finish();
                break;
            case Constants.RESPONSE_BACK_TYPE_PAYPW:
                ActivityUtils.finishActivity("PhoneVerifyCodeActivity");
                finish();
        }
    }

}
