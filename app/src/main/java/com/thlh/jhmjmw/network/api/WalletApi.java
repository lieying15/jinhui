// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.CouponListResponse;
import com.thlh.baselib.model.response.DealRecordResponse;
import com.thlh.baselib.model.response.RechargeBalanceResponse;
import com.thlh.baselib.model.response.RechargeMjbResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface WalletApi {

    //钱包余额充值
    @FormUrlEncoded
    @POST("balance_recharge")
    Observable<RechargeBalanceResponse> rechargeBalance(@Header(Constants.API_HEADER) String header, @Field("amount") String amount, @Field("by") String by);

    //钱包余额充值
    @FormUrlEncoded
    @POST("mjb_recharge")
    Observable<RechargeMjbResponse> rechargeMjb(@Header(Constants.API_HEADER) String header, @Field("amount") String amount, @Field("by") String by);

    //交易记录 标识 from 来源标识 0:全部=default 1:美家钻 2:余额   type 收支标识 A:全部=default R:收入, E:支出
    @FormUrlEncoded
    @POST("index")
    Observable<DealRecordResponse> recordIndex(@Header(Constants.API_HEADER) String header, @Field("page") int page, @Field("count") int count, @Field("from") String from, @Field("type") String type);

    //56. 用户优惠券列表接口  flag标识：flag=0未使用, flag=1已使用 flag=2即将过期？
    @FormUrlEncoded
    @POST("coupon")
    Observable<CouponListResponse> couponIndex(@Header(Constants.API_HEADER) String header, @Field("page") int page, @Field("count") int count, @Field("flag") String flag);

}
