package com.thlh.jhmjmw.network.api;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.ExchangeCardResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by LD on 2017/4/21.
 */
public interface CardApi {

    @FormUrlEncoded
    @POST("exchange") //type G:商品 B:品牌 M:视频
    Observable<ExchangeCardResponse> getExchangeCard(@Header(Constants.API_HEADER) String header, @Field("pass") String pass);

}
