package com.thlh.jhmjmw.business.goods.goodsdetail;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

/**
 * Created by huqiang on 2017/1/23.
 */

public class GoodsDetailPresenter implements GoodsDetailContract.Presenter {
    private final GoodsDetailContract.View mInfoView;
    private BaseObserver<BaseResponse> collectObserver, delCollectObserver;
    private String TAG = "GoodsInfoPresenter";

    public GoodsDetailPresenter(GoodsDetailContract.View mInfoView) {
        this.mInfoView = mInfoView;
//        this.mInfoView.setPresenter(this);
    }

    @Override
    public void postCollect(String goodsId) {
        L.e(" postCollect goodsId:"+goodsId);
        collectObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                mInfoView.showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                L.e(TAG + " 收藏商品");
                mInfoView.updateCollection(true);
            }
        };

        NetworkManager.getCollectApi()
                .addCollect(SPUtils.getToken(), goodsId, "0")
                .compose(RxUtils.androidSchedulers(mInfoView))
                .subscribe(collectObserver);
    }

    @Override
    public void postDelCollect(String goodsId) {
        L.e(" postDelCollect goodsId:"+goodsId);
        delCollectObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                mInfoView.showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                L.e(TAG + " 取消收藏");
                mInfoView.updateCollection(false);
            }
        };
        NetworkManager.getCollectApi()
                .deleteCollect(SPUtils.getToken(), goodsId)
                .compose(RxUtils.androidSchedulers(mInfoView))
                .subscribe(delCollectObserver);
    }

}
