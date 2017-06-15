// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.AlbumResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

public interface AlbumApi {
    @FormUrlEncoded
    @POST("index") //相册列表
    Observable<AlbumResponse> getIndex(@Header(Constants.API_HEADER) String header,@Field("page") int page, @Field("count") int count);

    @Multipart
    @POST("add_photo")
    Observable<BaseResponse> addPhoto(@Header(Constants.API_HEADER) String header,  @PartMap Map<String, RequestBody> photo);


    @FormUrlEncoded
    @POST("screen_saver")
    Observable<BaseResponse> saveScreenParam(@Header(Constants.API_HEADER) String header, @Field("type") String type);

    @FormUrlEncoded
    @POST("del_photo")
    Observable<BaseResponse> deletePhoto(@Header(Constants.API_HEADER) String header, @Field("id") String id);




}
