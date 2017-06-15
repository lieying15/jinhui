// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.FridgeSettingResponse;
import com.thlh.baselib.model.response.InfoTransportListResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface FridgeApi {

    //获取冰箱设置数据
    @POST("get_spec")
    Observable<FridgeSettingResponse> getSettingInfo(@Header(Constants.API_HEADER) String header);

    //用户设置冰箱温度
    @FormUrlEncoded
    @POST("app_set_spec")
    Observable<InfoTransportListResponse> setParameter(@Header(Constants.API_HEADER) String header,
                                                       @Field("upper") String upper,@Field("lower") String lower,@Field("coolroom_kg") String coolroom_kg,
                                                       @Field("fastcool_kg") String fastcool_kg,@Field("fastfrozen_kg") String fastfrozen_kg,
                                                       @Field("smart_kg") String smart_kg, @Field("lock_kg") String lock_kg);

    @FormUrlEncoded
    @POST("app_set_spec")
    Observable<InfoTransportListResponse> setParameter(@Header(Constants.API_HEADER) String header, @Field("upper") String upper,@Field("lower") String lower);
}
