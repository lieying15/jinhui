package com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo;

import com.thlh.baselib.base.BaseView;
import com.thlh.baselib.model.Comment;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsDetailProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqiang on 2017/1/23.
 */

public interface GoodsInfoContract  {

//    interface View extends BaseView<GoodsInfoContract.Presenter> {
    interface View extends BaseView {

        void updateCollection(boolean isCollected);

        void updateComment(List<Comment> comments,int total,double goodrate);

        void showErrorDialog(String msg);

        void updateViewPath(String path);
        //播放视频提示
        void showPlayVideoHintDialog();

        void showGoodsVideo(String mVideoUrl, String mVideoPic);

        void showGoodsImg(String imgUrl,String imgThumbUrl);

        void showGoodsName(String  goodsName);

        void showGoodsHint(String  hint);

        void showGoodsPrice(double  price, double mjz, String ismjb);

        void showGoodsSupplier(String supplierStr,String supplierId);

        void showGoodsSuit(boolean isBudingPart, String partOfId);

        void showGoodsPack(int packNum);

        void showGoodsProperty(List<GoodsDetailProperty> mPropertyList);

        void showGoodsBunding(ArrayList<GoodsBundling> goodsBundlings, String is_part);

        void showGoodsIsIceBox();

        void showGoodsDetail(String content);

        void showGoodsStatusNoStorage();

        void showGoodsStatusOffShelves();

        void showGoodsStatusPrepare();

        void showGoodsLimit(String num);

        void hideImmediatelyBuy();

        void gotoTop();//回顶部

        void updateShareInfo(String name,String content,String img); //更新分享信息
    }


    interface Presenter {

        void loadGoodsDetail(String goodsId);

        void loadComment(String goodsId);

        void addShopCart();

        void buyImmediately();
    }

}
