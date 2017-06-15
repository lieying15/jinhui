package com.thlh.jhmjmw.business.user.coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Card;
import com.thlh.baselib.model.response.ExchangeCardResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.view.ExchangeCardView;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * fyx
 * 4/7
 * 兑换卡
 */
public class ExchangeCardActivity extends BaseActivity {


    @BindView(R.id.exchange_card_headernormal)
    HeaderNormal exchangeCardHeadernormal;
    @BindView(R.id.exchange_card_noinfoview)
    ExchangeCardView exchangeCardNoinfoview;
    @BindView(R.id.exchange_card_infos)
    NoInfoView exchangeCardInfos;
    @BindView(R.id.activity_water_card)
    LinearLayout activityWaterCard;
    private BaseObserver<ExchangeCardResponse> baseOobserver;
    private final int ACTIVITY_CODE_COUPON_FINISH = 0;

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, ExchangeCardActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange_card);
        ButterKnife.bind(this);
        exchangeCardNoinfoview.setVisibility(View.VISIBLE);
        exchangeCardInfos.setVisibility(View.GONE);


        exchangeCardNoinfoview.setExchangeCardlistener(new ExchangeCardView.OnExchangeCardlistener() {
            @Override
            public void getPassword(String pass) {
                loadExchangeCard(pass);
            }
        });

        baseOobserver = new BaseObserver<ExchangeCardResponse>() {
            @Override
            public void onNextResponse(ExchangeCardResponse exchangeCardResponse) {
                progressMaterial.dismiss();
                Card card = exchangeCardResponse.getData().getCard();
                exchangeCardNoinfoview.setVisibility(View.GONE);
                exchangeCardInfos.setVisibility(View.VISIBLE);
                /**
                 * 判断返回信息的情况，
                 * 根据返回结果显示不同的页面
                 */

                if (card.getCoupon_id().equals("1")){
                    exchangeCardInfos.setTitleIv(R.drawable.icon_dialog_success);
                    exchangeCardInfos.setTitle(getResources().getString(R.string.dh_success));
                }else {
                    exchangeCardInfos.setTitleIv(R.drawable.i_recharge_fail);
                    exchangeCardInfos.setTitle(getResources().getString(R.string.dh_fail));

                }
                exchangeCardInfos.setNextactionListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (card.getCoupon_id().equals("1")){
                            finish();
                        }else{
                            exchangeCardNoinfoview.setVisibility(View.VISIBLE);
                            exchangeCardInfos.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onErrorResponse(ExchangeCardResponse exchangeCardResponse) {
                progressMaterial.dismiss();
                Tos.show(exchangeCardResponse.getErr_msg());
            }
        };
    }

    @Override
    protected void loadData() {

    }

    public void loadExchangeCard(String pass) {
        progressMaterial.show();
        subscription = NetworkManager.getCardApi()
                .getExchangeCard(SPUtils.getToken(), pass)
                .compose(RxUtils.androidSchedulers(this, false))
                .subscribe(baseOobserver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
