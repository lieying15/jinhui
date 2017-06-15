// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.ExpressfreeResponse;
import com.thlh.baselib.model.response.GoodListResponse;
import com.thlh.baselib.model.response.OrderGenerateResponse;
import com.thlh.baselib.model.response.OrderHintGenerateResponse;
import com.thlh.baselib.model.response.OrderPayResponse;
import com.thlh.baselib.model.response.OrderResponse;
import com.thlh.baselib.model.response.OrderSummaryResponse;
import com.thlh.baselib.model.response.OrderTraceResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface OrderApi {
    @FormUrlEncoded
    @POST("place")  //下订单查价格
    Observable<OrderHintGenerateResponse> generateOrderHint(@Header(Constants.API_HEADER) String header, @Field("address") String address, @Field("item") String item, @Field("get_pack") String get_pack, @Field("pay") String pay, @Field("time") String time, @Field("to_store") String to_store, @Field("hint") String hint);


    @FormUrlEncoded
    @POST("place")  //下订单
    Observable<OrderGenerateResponse> generateOrder(@Header(Constants.API_HEADER) String header,@Field("address") String address,@Field("item") String item,@Field("get_pack") String get_pack,@Field("pay") String pay,@Field("time") String time,@Field("to_store") String to_store);

    @FormUrlEncoded
    @POST("place")  //下订单(含优惠券)
    Observable<OrderGenerateResponse> generateOrder(@Header(Constants.API_HEADER) String header,@Field("address") String address,@Field("item") String item,@Field("get_pack") String get_pack,@Field("pay") String pay,@Field("time") String time,@Field("to_store") String to_store,@Field("coupon") String coupon);

    @FormUrlEncoded
    @POST("place_v2")  //下订单v2版本
    Observable<OrderGenerateResponse> generateOrderV2(@Header(Constants.API_HEADER) String header,@Field("address") String address,@Field("item") String item,@Field("get_pack") String get_pack,@Field("pay") String pay,@Field("time") String time,@Field("to_store") String to_store,@Field("coupon") String coupon);


    @FormUrlEncoded
    @POST("place_v2")  //下订单v2(含优惠券)
    Observable<OrderGenerateResponse> generateOrderV2(@Header(Constants.API_HEADER) String header,@Field("address") String address,@Field("item") String item,@Field("get_pack") String get_pack,@Field("pay") String pay,@Field("time") String time,@Field("to_store") String to_store,@Field("coupon") String coupon,@Field("note") String note);

    @FormUrlEncoded
    @POST("pay")
    Observable<OrderPayResponse> payOrder (@Header(Constants.API_HEADER) String header, @Field("order_id") String order_id, @Field("pay") String pay);

    @FormUrlEncoded
    @POST("pay_v2")
    Observable<OrderPayResponse> payOrderV2 (@Header(Constants.API_HEADER) String header, @Field("order_id") String order_id, @Field("pay") String pay,@Field("pass") String pass, @Field("item") String itemIdAndNumAndMjb);

    @FormUrlEncoded
    @POST("pay_v2") //无支付密码
    Observable<OrderPayResponse> payOrderV2 (@Header(Constants.API_HEADER) String header, @Field("order_id") String order_id, @Field("pay") String pay, @Field("item") String itemIdAndNumAndMjb);

    @FormUrlEncoded
    @POST("del") //删除
    Observable<BaseResponse> delOrder(@Header(Constants.API_HEADER) String header,@Field("id") String id);

    @FormUrlEncoded
    @POST("sign_for") //确认收货
    Observable<BaseResponse> confrimOrder(@Header(Constants.API_HEADER) String header,@Field("id") String id);

    @FormUrlEncoded
    @POST("cancel") //取消订单
    Observable<BaseResponse> cancelOrder(@Header(Constants.API_HEADER) String header,@Field("order_id") String order_id,@Field("reason") String reason);


    @POST("goods") //购物足迹
    Observable<GoodListResponse> footprint(@Header(Constants.API_HEADER) String header);

    @FormUrlEncoded
    @POST("goods") //购物足迹
    Observable<GoodListResponse> footprint(@Header(Constants.API_HEADER) String header, @Field("page") String page, @Field("count") String count);


    @FormUrlEncoded
    @POST("index") //订单列表
    Observable<OrderResponse> index(@Header(Constants.API_HEADER) String header,@Field("type") String type,@Field("page") int page,@Field("count") int count);


    @POST("index") //订单列表
    Observable<OrderResponse> index(@Header(Constants.API_HEADER) String header);


    @FormUrlEncoded
    @POST("track_pack") //订单追踪
    Observable<OrderTraceResponse> trace(@Header(Constants.API_HEADER) String header, @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("get_express_fee") //获取购物车商品配送费接口
    Observable<ExpressfreeResponse> getExpressfree(@Header(Constants.API_HEADER) String header, @Field("address") String address, @Field("item") String item);

    @FormUrlEncoded
    @POST("get_express_fee_v2") //获取购物车商品配送费接口
    Observable<ExpressfreeResponse> getExpressfreeV2(@Header(Constants.API_HEADER) String header, @Field("address") String address, @Field("item") String item,@Field("to_store") String tostore);


    @POST("summary") //订单汇总数据接口
    Observable<OrderSummaryResponse> getSummary(@Header(Constants.API_HEADER) String header);


}
