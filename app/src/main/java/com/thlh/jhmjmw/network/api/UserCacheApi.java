// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.function.rxcache.RxCacheResult;
import com.thlh.baselib.model.response.AddrCodeResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface UserCacheApi {

    //获取省市区地址
    @GET("area")
    Observable<RxCacheResult<AddrCodeResponse>> getArea();

}
