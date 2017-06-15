package com.thlh.jhmjmw.business.index.mine;


import com.thlh.baselib.config.Constants;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Coupon;
import com.thlh.baselib.model.response.CouponListResponse;
import com.thlh.baselib.model.response.OrderSummaryResponse;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

public class MinePresenter implements MineContract.Presenter {
    private final MineContract.View mMineView;
    private BaseObserver<OrderSummaryResponse> orderSummaryObserver;
    private BaseObserver<WalletResponse> walletObserver;
    private BaseObserver<CouponListResponse> couponObserver;


    public MinePresenter(MineContract.View mMineView) {
        this.mMineView = mMineView;
//        this.mMineView.setPresenter(this);
    }

    @Override
    public void loadWallet() {
        L.e(TAG + " loadWallet ");
        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
            }

            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                DecimalFormat df = new DecimalFormat("0.00");
                String mjbStr = df.format(walletResponse.getData().getMjb());
                SPUtils.put("user_mjb", mjbStr);
                mMineView.updateMjzTv();
                loadCoupon();
            }
        };
        NetworkManager.getUserDataApi()
                .wallet(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(mMineView,false))
                .subscribe(walletObserver);
    }

    @Override
    public void loadCoupon() {
        L.e(TAG + " loadCoupon ");
        couponObserver = new BaseObserver<CouponListResponse>() {
            @Override
            public void onErrorResponse(CouponListResponse response) {
            }

            @Override
            public void onNextResponse(CouponListResponse response) {
                int couponsize = response.getData().getTotal();
                mMineView.updateCouponTv(couponsize + "");
                for (Coupon coupon : response.getData().getCoupons()) {
                    if (coupon.getCoupon_name().equals("冰箱优惠券")) {
                        SPUtils.put("use_coupon_icebox", coupon.getId());
                    }
                }
                loadOrderSummary();
            }
        };
        NetworkManager.getWalletApi()
                .couponIndex(SPUtils.getToken(), 1, Constants.PageDataCount, " 0")
                .compose(RxUtils.androidSchedulers(mMineView,false))
                .subscribe(couponObserver);
    }

    @Override
    public void loadOrderSummary() {
        L.e(TAG + " loadOrderSummary ");
        orderSummaryObserver = new BaseObserver<OrderSummaryResponse>() {
            @Override
            public void onErrorResponse(OrderSummaryResponse response) {
            }

            @Override
            public void onNextResponse(OrderSummaryResponse response) {
                mMineView.setWaitPayTv("" + response.getData().getNo_pay());
                mMineView.setWaitSendoutTv("" + response.getData().getNo_delivery());
                mMineView.setWaitGainTv("" + response.getData().getNo_get());
                mMineView.setWaitCommentTv("" + response.getData().getNo_comment());
            }
        };
        NetworkManager.getOrderApi()
                .getSummary(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(mMineView,false))
                .subscribe(orderSummaryObserver);
    }

}
