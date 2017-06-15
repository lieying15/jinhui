// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.model.response.WeChatCashResponse;
import com.thlh.baselib.model.response.WeChatPayResponse;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface WeixinApi {

//    @POST("unifiedorder")
//    Observable<WeChatPayResponse> getPrepayId(@Header("Accept") String headerparam1,@Header("Content-type") String headerparam2,@Body String content);

    @Multipart
    @POST("pay/unifiedorder")
    Observable<WeChatPayResponse> getPrepayId2(@Header("Accept") String headerparam1, @Header("Content-type") String headerparam2, @Part("photo\"; filename=\"cropimage.xml") RequestBody params);

    @Multipart
    @POST("mmpaymkttransfers/promotion/transfers")
    Observable<WeChatCashResponse> getCash(@Part("cash\"; filename=\"cash.xml") RequestBody params);



}
