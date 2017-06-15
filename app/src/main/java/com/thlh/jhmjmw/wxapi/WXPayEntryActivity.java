package com.thlh.jhmjmw.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.recharge.RechargeActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WXPayEntryActivity extends BaseViewActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";


    @BindView(R.id.wechat_pay_result_header)
    HeaderNormal wechatPayResultHeaderNormal;
    @BindView(R.id.wechat_pay_result_title_iv)
    ImageView wechatPayResultTitleIv;
    @BindView(R.id.wechat_pay_result_title_tv)
    TextView wechatPayResultTitleTv;
    @BindView(R.id.wechat_pay_result_content_tv)
    TextView wechatPayResultContentTv;
    @BindView(R.id.wechat_pay_result_result_iv)
    ImageView wechatPayResultResultIv;
    @BindView(R.id.wechat_pay_result_back_btn)
    Button wechatPayResultBackBtn;


    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weichat_pay_result);
        ButterKnife.bind(this);
        L.e(TAG, "  WXPayEntryActivity onCreate ");
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
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
        L.e(TAG, " onPayFinish, errCode = " + resp.errCode + "; errStr = " + resp.errStr + "; transaction =" + resp.transaction + "; openId =" +resp.openId );
        switch (resp.errCode) {
            case 0:
                String pay_purpose = (String)SPUtils.get("pay_purpose",Constants.PAY_PURPOSE_ORDER).toString(); // 1订单支付 2钱包充值 3美家币充值
                String pay_no =(String) SPUtils.get("pay_no", "").toString();
                String pay_price =(String) SPUtils.get("pay_price", "").toString();
                L.e(TAG, " onPayFinish, pay_price  " + pay_price +" pay_purpose:"+pay_purpose+" pay_no:"+pay_no);
                if(pay_purpose.equals(Constants.PAY_PURPOSE_ORDER)){
                    wechatPayResultHeaderNormal.setVisibility(View.VISIBLE);
                    wechatPayResultBackBtn.setVisibility(View.VISIBLE);
                    wechatPayResultTitleIv.setImageResource(R.drawable.img_face_laugh_wine);
                    wechatPayResultTitleTv.setText("支付成功！");
                    wechatPayResultContentTv.setText("系统正在处理您的订单！");
                    wechatPayResultResultIv.setImageResource(R.drawable.icon_success_black);
                }else {
                    RechargeActivity.activityStart(this,pay_purpose,true);
                    finish();
                }

                break;
            case -1:
                wechatPayResultTitleIv.setImageResource(R.drawable.img_face_sad_wine);
                wechatPayResultTitleTv.setText("支付失败！");
                if(resp.errStr != null){
                    wechatPayResultContentTv.setText(resp.errStr);
                }else {
                    wechatPayResultContentTv.setText("系统正在处理您的订单！");
                }
                wechatPayResultResultIv.setImageResource(R.drawable.icon_mistake_black);
                break;
            case -2:
                //用户取消
                finish();
                L.e(TAG, " 用户取消");
                break;
        }
    }

    @OnClick(R.id.wechat_pay_result_back_btn)
    public void onClick() {
        IndexActivity.activityStart(this);
        finish();
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }
}