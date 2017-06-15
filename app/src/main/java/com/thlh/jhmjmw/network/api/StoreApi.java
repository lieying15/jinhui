// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.StoreNearResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface StoreApi {

    //根据经纬度获取附近小区店接口
    @FormUrlEncoded
    @POST("in_area")
    Observable<StoreNearResponse> getNearStore(@Header(Constants.API_HEADER) String header, @Field("lat") double lat, @Field("lng") double lng);


}
