package com.thlh.jhmjmw.business.buy.buyconfirm;

import android.app.Activity;

import com.thlh.baselib.base.BaseView;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.response.ExpressfreeResponse;

import java.util.List;

/**
 * Created by huqiang on 2017/2/9.
 */

public interface BuyConfirmContract {

    interface View extends BaseView {
        void updateExpressFree(double free, ExpressfreeResponse.DataBean data);

        void showHintDialog(String msg);

        void showPayPwHintDialog();

        void showPaySuccessDialog();

        void updateNumTv();

        void updateAddressView();

        void changePayType(String type);

        void updatePriceTv();

        void finish();

        void startPasswordPayActivity(String orderid);

        void updateWeChatPayRequest(String prepay_id, String nonce_str);
    }


    interface Presenter {

        void loadExpressFree(String addressid, String itemIdAndNum);

        void loadWallet();
        //
        void postGenerateOrder(Activity activity, String addressid, String getPack, String time, String itemIdAndNumAndMjb, String paytype, double useMjb, String note);

        void postPayOrder(Activity activity, String orderid, String itemIdAndNumAndMjb, String paytype);

        boolean judgePayCondition(List<Cartgoods> cartgoods, String addressId,String paytype, double useMjb, String user_mjb, String note);

        Address getDefaultAddress();

        List<Cartgoods> getAllSelectCartGoods();

        String getCartInfoStr(List<Cartgoods> cartgoods, boolean payMjb);

        void startWechatPay(String tempprice, String orderid);

        void startAliPay(Activity activity, String tempprice, String orderid);

        List<Goods> getAdapterCartData(List<Cartgoods> cartgoods);

        String getPayType(double useMjb, boolean paywechat, boolean payalipay) ;

        void ClearBuyImmediatelyGoods();
    }
}
