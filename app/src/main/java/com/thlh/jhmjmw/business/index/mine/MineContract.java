package com.thlh.jhmjmw.business.index.mine;

import com.thlh.baselib.base.BaseView;

/**
 * Created by huqiang on 2016/12/30.
 */

public interface MineContract {

    interface View extends BaseView{
//    interface View extends BaseView<Presenter> {

        void updateMineAvatarIv();

        void updateMineNameTv();

        void updateMjzTv();

        void updateCouponTv(String num);

        void setWaitPayTv(String waitPay);

        void setWaitSendoutTv(String waitSendout);

        void setWaitGainTv(String waitGain);

        void setWaitCommentTv(String waitComment);

        void showServiceDialog();

    }


    interface Presenter {

        void loadWallet();

        void loadCoupon();

        void loadOrderSummary();

    }

}
