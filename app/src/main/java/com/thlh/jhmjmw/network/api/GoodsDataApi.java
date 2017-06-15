// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.CategoryTopResponse;
import com.thlh.baselib.model.response.ChoiceResponse;
import com.thlh.baselib.model.response.GoodsRecommendResponse;
import com.thlh.baselib.model.response.GoodsDetailResponse;
import com.thlh.baselib.model.response.HomepageResponse;
import com.thlh.baselib.model.response.OnShelvesResponse;
import com.thlh.baselib.model.response.ScanResponse;
import com.thlh.baselib.model.response.SimilarResponse;
import com.thlh.baselib.model.response.SupplierAllResonse;
import com.thlh.baselib.model.response.VersionResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface GoodsDataApi {
    @GET("item")
    Observable<GoodsDetailResponse> getGoodsDetail(@Header(Constants.API_HEADER) String header,@Query("itemid") String itemid);

    @FormUrlEncoded
    @POST("index")
    Observable<HomepageResponse> getHomepageDate(@Field("page") int page,@Field("count") int count);
    @FormUrlEncoded
    @POST("index")
    Observable<HomepageResponse> getHomepageDate(@Field("page") int page,@Field("count") int count,@Field("is_mjb") int is_mjb);

    @GET("index")
    Observable<HomepageResponse> getHomepageDate();

    @GET("category")
    Observable<CategoryTopResponse> getCategoryTop();

    @GET("category")
    Observable<CategoryTopResponse> getCategorySecond(@Query("catid") String catid);


    @FormUrlEncoded
    @POST("on_shelves")  //查询商品是否下架
    Observable<OnShelvesResponse> getOnShelvesInfo(@Header(Constants.API_HEADER) String header,@Field("items") String catid);


    @FormUrlEncoded
    @POST("similar") //相似商品 推荐商品
    Observable<SimilarResponse> getSimilarGoods(@Field("item") String itemid,@Field("count") String count);

    @FormUrlEncoded
    @POST("similar") //相似商品 推荐商品
    Observable<SimilarResponse> getSimilarGoods(@Field("item") String item);

    @POST("supplier_list") //供应商列表接口
    Observable<SupplierAllResonse> getSupplier();

    @POST("choice") //商城精选
    Observable<ChoiceResponse> getChoice();


    @POST("version") //查询版本号
    Observable<VersionResponse> getVersion();

    //绑定小店 代理商
    @FormUrlEncoded
    @POST("qr_scan")
    Observable<ScanResponse> scanQR(@Header(Constants.API_HEADER) String header, @Field("ch") String ch);

    //推荐商品
    @GET("recommend")
    Observable<GoodsRecommendResponse> getRecommendGoods();


}
