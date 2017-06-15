// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.model.response.BrandAllResponse;
import com.thlh.baselib.model.response.CategorySecondResponse;
import com.thlh.baselib.model.response.FilterBrandResponse;
import com.thlh.baselib.model.response.SearchIndexResponse;
import com.thlh.baselib.model.response.SearchResponse;
import com.thlh.baselib.model.response.SupplierResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchApi {

    @FormUrlEncoded
    @POST("search")
    Observable<SearchResponse> getSearch(@Field("catid") String catid,@Field("page") int page, @Field("count") int count);

    @FormUrlEncoded
    @POST("search")
    Observable<SearchResponse> getSearch(@Field("catid") String catid,@Field("page") int page, @Field("count") int count,@Field("is_mjb") String is_mjb);


    @FormUrlEncoded
    @POST("search")
    Observable<SearchResponse> getSearch(@Field("keyword") String keyword, @Field("sort") String sort, @Field("brandid") String brandid,
                                         @Field("sid") String sid, @Field("mi") String mi, @Field("ma") String ma,@Field("page") int page,
                                         @Field("count") int count);



    @GET("index")
    Observable<SearchIndexResponse> getIndex();

    @GET("brand") //全部品牌
    Observable<BrandAllResponse> getFilterBrand();

    @GET("category")
    Observable<CategorySecondResponse> getCategorySecond(@Query("catid") String catid);

    @FormUrlEncoded //相关店铺
    @POST("supplier_in_keyword")
    Observable<SupplierResponse> getRelateSupplier(@Field("keyword") String keyword);

    @FormUrlEncoded //相关品牌
    @POST("brand_in_keyword")
    Observable<FilterBrandResponse> getRelateBrand(@Field("keyword") String keyword);

}
