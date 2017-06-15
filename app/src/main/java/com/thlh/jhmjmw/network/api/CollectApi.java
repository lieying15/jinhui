// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.GoodListResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface CollectApi {

    @FormUrlEncoded
    @POST("index") //type G:商品 B:品牌 M:视频
    Observable<GoodListResponse> getGoods(@Header(Constants.API_HEADER) String header, @Field("type") String type, @Field("page") int page, @Field("count") int count);

    @FormUrlEncoded
    @POST("add_goods")
    Observable<BaseResponse> addCollect(@Header(Constants.API_HEADER) String header, @Field("item_id") String item_id, @Field("store_id") String store_id);


    @FormUrlEncoded
    @POST("del_goods")
    Observable<BaseResponse> deleteCollect(@Header(Constants.API_HEADER) String header, @Field("item_id") String item_id);




}
