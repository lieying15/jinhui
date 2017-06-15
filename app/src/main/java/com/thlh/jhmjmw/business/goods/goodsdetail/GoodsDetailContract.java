package com.thlh.jhmjmw.business.goods.goodsdetail;

import com.thlh.baselib.base.BaseView;

/**
 * Created by huqiang on 2017/1/23.
 */

public interface GoodsDetailContract {

    interface View extends BaseView {
//    interface View extends BaseView<GoodsDetailContract.Presenter> {

        void updateCollection(boolean isCollected);

        void updateCartTv(int num);

        void showErrorDialog(String msg);

    }


    interface Presenter {

        void postCollect(String goodsId);

        void postDelCollect(String goodsId);

    }

}
