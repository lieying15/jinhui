// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.InfoTransportListResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface MessageApi {

    //订单物流信息
    @FormUrlEncoded
    @POST("order")
    Observable<InfoTransportListResponse> orderInfo(@Header(Constants.API_HEADER) String header, @Field("page") int page, @Field("count") int count,@Field("last") String last);

    //删除信息
    @FormUrlEncoded
    @POST("del_order_msg")
    Observable<InfoTransportListResponse> deleteInfo(@Header(Constants.API_HEADER) String header, @Field("id") String id);


}
