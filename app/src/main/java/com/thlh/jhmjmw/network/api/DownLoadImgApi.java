// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface DownLoadImgApi {

    @GET
    Observable<ResponseBody> download(@Url String fileUrl);
}
