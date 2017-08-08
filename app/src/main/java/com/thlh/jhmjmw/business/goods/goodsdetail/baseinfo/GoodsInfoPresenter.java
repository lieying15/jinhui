package com.thlh.jhmjmw.business.goods.goodsdetail.baseinfo;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsDetail;
import com.thlh.baselib.model.GoodsDetailProperty;
import com.thlh.baselib.model.response.CommentResponse;
import com.thlh.baselib.model.response.GoodsDetailResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqiang on 2017/1/23.
 */

public class GoodsInfoPresenter implements GoodsInfoContract.Presenter {
    private final GoodsInfoContract.View mInfoView;
    private BaseObserver<GoodsDetailResponse> goodsdetailObserver;
    private BaseObserver<CommentResponse> commentObserver;
    private String TAG = "GoodsInfoPresenter";
    private GoodsDetail goodsDetail;

    public GoodsInfoPresenter(GoodsInfoContract.View mInfoView) {
        this.mInfoView = mInfoView;
    }

    @Override
    public void loadGoodsDetail(String goodsId) {
        goodsdetailObserver = new BaseObserver<GoodsDetailResponse>() {
            @Override
            public void onErrorResponse(GoodsDetailResponse goodsDetailResponse) {
                mInfoView.showErrorDialog( goodsDetailResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(GoodsDetailResponse goodsDetailResponse) {
                goodsDetail = goodsDetailResponse.getData().getItem();
                DbManager.getInstance().insertSeenGoods(goodsDetail);
                boolean canbuy = true;
                mInfoView.showGoodsName(TextUtils.showString(goodsDetail.getItem_name()));
                mInfoView.showGoodsHint(TextUtils.showString(goodsDetail.getItem_subtitle()));

                if (goodsDetail.getSupplier() != null) {
                    mInfoView.showGoodsSupplier(TextUtils.showString(goodsDetail.getSupplier().getName()),goodsDetail.getSupplier().getId());
                }
                double itemprice = Double.parseDouble(TextUtils.showPrice(goodsDetail.getItem_price()));
                double itemMjzprice = 0;
                if (goodsDetail.getMjb_value() != null)
                    itemMjzprice = Double.parseDouble(TextUtils.showPrice(goodsDetail.getMjb_value()));
                final int packNum = goodsDetail.getPack_num() == null ? 0 : Integer.parseInt(goodsDetail.getPack_num());
                if(packNum!=0){
                    itemprice = itemprice * packNum;
                    itemMjzprice = itemMjzprice * packNum;
                }
                mInfoView.showGoodsPrice(itemprice,itemMjzprice,goodsDetail.getIs_mjb());

                //视频处理
                boolean hasVideo =false;
                if (goodsDetail.getVideo() != null && goodsDetail.getVideo().size() > 0) {
                    hasVideo = true;
                }
                if (hasVideo) {
                    mInfoView.showGoodsVideo(goodsDetail.getVideo().get(0).getUrl(),goodsDetail.getVideo().get(0).getVideo_pic());
                    mInfoView.updateViewPath(goodsDetail.getVideo().get(0).getUrl());
                } else {
                    mInfoView.showGoodsImg(goodsDetail.getItem_img(),goodsDetail.getItem_img_thumb());
                }
                //收藏处理
                if (goodsDetail.getCollection().equals("1")) {
                    mInfoView.updateCollection(true);
                } else {
                    mInfoView.updateCollection(false);
                }

                //显示商品类型处理
                if (goodsDetail.getIs_part() != null && goodsDetail.getIs_part().equals("1")) {
                    //单品套装
                    boolean isBundingPart;
                    if (goodsDetail.getPart_is_bundling() != null && goodsDetail.getPart_is_bundling().equals("1")) {
                        isBundingPart = true;
                    } else {
                        isBundingPart = false;
                    }
                    mInfoView.showGoodsSuit(isBundingPart,goodsDetail.getPart_of_id());
                } else {
                    //单品整箱
                    if (goodsDetail.getIs_pack() != null && goodsDetail.getIs_pack().equals("1")) {
                        mInfoView.showGoodsPack(packNum);
                    }
                }

                //商品显示限购
                if (goodsDetail.getLimit_num() != null && goodsDetail.getIs_limit().equals("1") && !goodsDetail.getLimit_num().equals("0")) {
                    mInfoView.showGoodsLimit(goodsDetail.getLimit_num());
                }

                //显示商品状态理
                if (goodsDetail.getStorage() == 0) {
                    L.e(TAG + "库存为 0 ");
                   mInfoView.showGoodsStatusNoStorage();
                }
                //已下架
                if (goodsDetail.getIs_shelves().equals("0")) {
                    mInfoView.showGoodsStatusOffShelves();
                    canbuy = false;
                }
                //备货中
                if (goodsDetail.getIs_prepare() != null && goodsDetail.getIs_prepare().equals("1")) {
                    mInfoView.showGoodsStatusPrepare();
                    canbuy = false;
                }
                //商品为冰箱
                if (goodsDetail.getItem_id().equals("1")) {
                    mInfoView.showGoodsIsIceBox();
                    canbuy = true;
                }

                //显示商品属性
                List<GoodsDetailProperty> mPropertyList = goodsDetail.getSpec();
                if (mPropertyList.size() > 0) {
                    mInfoView.showGoodsProperty(mPropertyList);
                }

                //处理商品套装数据
                final ArrayList<GoodsBundling> tempbunding = new ArrayList<GoodsBundling>();
                List<GoodsBundling> goodsbunding  = goodsDetail.getBundling();
                if (goodsbunding.size() > 0) {
                    for (GoodsBundling bundling : goodsbunding) {
                        tempbunding.add(bundling);
                    }
                    mInfoView.showGoodsBunding(tempbunding);
                }

                if(!canbuy){
                    mInfoView.hideImmediatelyBuy();
                }

                String goodsCotent = goodsDetail.getContent();
//                String tempgoodsCotent = goodsCotent.replaceAll("/BmUploadFile/ueditor/", Deployment.IMAGE_PATH + "/BmUploadFile/ueditor/");
                String tempgoodsCotent = "";
                String[] array = goodsCotent.split("/BmUploadFile/ueditor/");
                for (int i =0;i<array.length-1;i++){
                    if ("src=".equals(array[i].substring(array[i].length()-5,array[i].length()-1))){
                        tempgoodsCotent += array[i]+"http://v2.mjmw365.com/BmUploadFile/ueditor/";
                    }else {
                        tempgoodsCotent += array[i]+"/BmUploadFile/ueditor/";
                    }
                }
                tempgoodsCotent+=array[array.length-1];


//                String tempgoodsCotent = goodsCotent.replaceAll("/BmUploadFile/ueditor/", Deployment.IMAGE_PATH + "/BmUploadFile/ueditor/");
                L.i(TAG + " tempgoodsCotent:" + tempgoodsCotent);
                tempgoodsCotent = tempgoodsCotent.replace("<p><br/></p>", "");

                String CSS_STYPE = "<head><style>p,body{margin: 0;}img{width: 100%;}</style></head>";
                mInfoView.showGoodsDetail( CSS_STYPE + tempgoodsCotent);

                mInfoView.updateShareInfo(goodsDetail.getItem_name(),goodsDetail.getItem_subtitle(),goodsDetail.getItem_img_thumb());
            }
        };

        NetworkManager.getGoodsDataApi()
                .getGoodsDetail(SPUtils.getToken(), goodsId)
                .compose(RxUtils.androidSchedulers(mInfoView))
                .subscribe(goodsdetailObserver);
    }

    @Override
    public void loadComment(String goodsId) {
        commentObserver = new BaseObserver<CommentResponse>() {
            @Override
            public void onErrorResponse(CommentResponse commentResponse) {
                mInfoView.showErrorDialog( commentResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(CommentResponse commentResponse) {
                L.i(TAG + "comment onNextResponse ");
                mInfoView.updateComment( commentResponse.getData().getComments(),commentResponse.getData().getTotal(),commentResponse.getData().getGood() * 100);
            }
        };

        NetworkManager.getItemApi()
                .getAllComments(SPUtils.getToken(), goodsId, Constants.COMMENT_GRADE_ALL, 1, 3) //item_id  grade0:全部 1:好评 2:中评 3:差评  page count
                .compose(RxUtils.androidSchedulers(mInfoView))
                .subscribe(commentObserver);
    }

    @Override
    public void addShopCart() {
        DbManager.getInstance().insertCart(goodsDetail);
    }

    @Override
    public void buyImmediately() {
        DbManager.getInstance().insertCart(goodsDetail,true);
    }
}
