// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.AddrAddResponse;
import com.thlh.baselib.model.response.AddrCodeResponse;
import com.thlh.baselib.model.response.AddrListResponse;
import com.thlh.baselib.model.response.AgencyResponse;
import com.thlh.baselib.model.response.AvatarResponse;
import com.thlh.baselib.model.response.BindIceBoxResponse;
import com.thlh.baselib.model.response.GetIceBoxInfoResponse;
import com.thlh.baselib.model.response.LoginResponse;
import com.thlh.baselib.model.response.RegistVerifyNumberByPwResponse;
import com.thlh.baselib.model.response.RegistVerifyNumberResponse;
import com.thlh.baselib.model.response.WalletResponse;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface UserApi {
    @FormUrlEncoded
    @POST("signin")
    Observable<LoginResponse> login(@Field("logname") String logname, @Field("password") String password,@Field("device") String sn);

    @FormUrlEncoded
    @POST("wx_signin")
    Observable<LoginResponse> weixinlogin(@Field("wx") String wx,@Field("device") String sn,@Field("ch") String ch);

    @POST("signout")
    Observable<BaseResponse> logout(@Header(Constants.API_HEADER) String header);

    @FormUrlEncoded
    @POST("register")
    Observable<BaseResponse> register(@Field("step") String step, @Field("mobile") String moblie);

    @FormUrlEncoded
    @POST("register")
    Observable<RegistVerifyNumberResponse> register(@Field("step") String step, @Field("mobile") String moblie, @Field("code") String code);

    @FormUrlEncoded
    @POST("register")
    Observable<RegistVerifyNumberResponse> register(@Field("step") String step, @Field("mobile") String moblie, @Field("code") String code, @Field("password") String password, @Field("ch") String ch);

    @FormUrlEncoded
    @POST("reset_password")
    Observable<BaseResponse> resetPw(@Header(Constants.API_HEADER) String header, @Field("old_pass") String old_pass, @Field("new_pass") String new_pass);


    //找回密码
    @FormUrlEncoded
    @POST("forget_password")  //step 1 获取验证码
    Observable<RegistVerifyNumberResponse> retrievePw(@Field("step") String step,  @Field("mobile") String moblie);

    @FormUrlEncoded
    @POST("forget_password")  //step 2 验证验证码
    Observable<RegistVerifyNumberResponse> retrievePw(@Field("step") String step,  @Field("mobile") String moblie, @Field("code") String code);

    @FormUrlEncoded
    @POST("forget_password")//step 3 重设密码
    Observable<RegistVerifyNumberResponse> retrievePw(@Field("step") String step,  @Field("mobile") String moblie, @Field("code") String code, @Field("password") String password);

    @FormUrlEncoded
    @POST("set_pay_pass")//设置支付密码
    Observable<RegistVerifyNumberResponse> setPayPw(@Header(Constants.API_HEADER) String header,@Field("password") String password);

    @FormUrlEncoded
    @POST("reset_pay_pass")//重置支付密码
    Observable<RegistVerifyNumberResponse> retrievePayPw(@Header(Constants.API_HEADER) String header,@Field("step") String step,  @Field("mobile") String moblie, @Field("code") String code, @Field("password") String password);




    //地址相关
    @GET("address")
    Observable<AddrListResponse> getAddrList(@Header(Constants.API_HEADER) String header);

    @FormUrlEncoded
    @POST("add_address")
    Observable<AddrAddResponse> addAddr(@Header(Constants.API_HEADER) String header, @Field("name") String name,
                                        @Field("address") String address, @Field("phone") String phone, @Field("top") String top,
                                        @Field("province") String province, @Field("city") String city, @Field("district") String district);

    @FormUrlEncoded
    @POST("edit_address")
    Observable<BaseResponse> editAddr(@Header(Constants.API_HEADER) String header, @Field("id") String id,
                                      @Field("name") String name, @Field("address") String address, @Field("phone") String phone, @Field("top") String top,
                                      @Field("province") String province,@Field("city") String city,@Field("district") String district);

    @FormUrlEncoded
    @POST("del_address")
    Observable<BaseResponse> deleteAddr(@Header(Constants.API_HEADER) String header, @Field("id") String id);

    //上传头像
    @Multipart
    @POST("photo")
    Observable<AvatarResponse> postAvatar(@Header(Constants.API_HEADER) String header, @Part("photo\"; filename=\"cropimage.jpg") RequestBody params);

    //绑定手机
    @FormUrlEncoded
    @POST("bind_mobile")
    Observable<RegistVerifyNumberResponse> bindPhone(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("mobile") String moblie);

    @FormUrlEncoded
    @POST("bind_mobile")
    Observable<BaseResponse> bindPhone(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("code") String code, @Field("mobile") String moblie);

    @FormUrlEncoded
    @POST("bind_mobile")
    Observable<BaseResponse> bindPhone(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("code") String code, @Field("mobile") String moblie,@Field("password") String password);

    //修改手机绑定 通过手机绑定
    @FormUrlEncoded
    @POST("bind_mobile_sms")  //step 1
    Observable<BaseResponse> changeBindPhoneByPhone(@Header(Constants.API_HEADER) String header, @Field("step") String step);

    //修改手机绑定 通过手机绑定
    @FormUrlEncoded
    @POST("bind_mobile_sms")  //step 2
    Observable<BaseResponse> changeBindPhoneByPhone(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("code") String code);

    @FormUrlEncoded
    @POST("bind_mobile_sms")  //step=3
    Observable<BaseResponse> changeBindPhoneByPhone(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("code") String code, @Field("mobile") String moblie);

    //修改手机绑定 通过手机绑定
    @FormUrlEncoded
    @POST("bind_mobile_password")  //step 1
    Observable<RegistVerifyNumberByPwResponse> changeBindPhoneByPW(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("password") String password);

    @FormUrlEncoded
    @POST("bind_mobile_password")  //step=2
    Observable<BaseResponse> changeBindPhoneByPW(@Header(Constants.API_HEADER) String header, @Field("step") String step, @Field("tmp") String tmp, @Field("mobile") String moblie);


    //扫码获取冰箱信息冰箱
    @FormUrlEncoded
    @POST("scan_fridge")
    Observable<GetIceBoxInfoResponse> getIcoboxInfo(@Header(Constants.API_HEADER) String header, @Field("fridge") String fridge);

    //绑定冰箱
    @FormUrlEncoded
    @POST("scan_fridge")
    Observable<BindIceBoxResponse> bindIcobox(@Header(Constants.API_HEADER) String header, @Field("fridge") String fridge);

    //绑定冰箱
    @FormUrlEncoded
    @POST("scan_fridge")
    Observable<BindIceBoxResponse> bindIcobox(@Header(Constants.API_HEADER) String header, @Field("fridge") String fridge, @Field("do") String text, @Field("lat") double lat, @Field("lng") double lng);

    //绑定冰箱
    @FormUrlEncoded
    @POST("scan_fridge_v2")
    Observable<BindIceBoxResponse> bindIcoboxV2Test(@Header(Constants.API_HEADER) String header, @Field("fridge") String fridge, @Field("do") String text, @Field("lat") double lat, @Field("lng") double lng);


    //解绑冰箱
    @FormUrlEncoded
    @POST("unbind_fridge")
    Observable<GetIceBoxInfoResponse> unBindIcobox(@Header(Constants.API_HEADER) String header, @Field("equipment_id") String equipment_id);

    //绑定小店
    @FormUrlEncoded
    @POST("bind_store")
    Observable<GetIceBoxInfoResponse> bindStore(@Header(Constants.API_HEADER) String header, @Field("store_id") String store_id);

    //绑定小店
    @FormUrlEncoded
    @POST("bind_store_v2")
    Observable<GetIceBoxInfoResponse> bindStoreV2(@Header(Constants.API_HEADER) String header, @Field("ch") String ch);



    //钱包、美家币余额
    @POST("my_wallet")
    Observable<WalletResponse> wallet(@Header(Constants.API_HEADER) String header);

    //推送参数设置接口
    @FormUrlEncoded
    @POST("set_push")
    Observable<BaseResponse> postPushInfo(@Header(Constants.API_HEADER) String header, @Field("pid") String pid, @Field("flag") String flag,@Field("type") String type);

    //昵称
    @FormUrlEncoded
    @POST("nickname")
    Observable<BaseResponse> postNickname(@Header(Constants.API_HEADER) String header, @Field("nickname") String nickname);

    //获取省市区地址
    @GET("area")
    Observable<AddrCodeResponse> getArea();

    //支付密码 免密开关
    @FormUrlEncoded
    @POST("pay_pass_on")  // is_pass 1不用密码  0用密码
    Observable<BaseResponse> setPayPassSwitch(@Header(Constants.API_HEADER) String header,@Field("is_pass")String status);

    //支付密码 免密开关
    @FormUrlEncoded
    @POST("pay_pass_on")  // is_pass 1不用密码  0用密码
    Observable<BaseResponse> setPayPassSwitch(@Header(Constants.API_HEADER) String header,@Field("is_pass")String status,@Field("step") String step, @Field("code") String code, @Field("mobile") String moblie);

    //94. 查询用户充值渠道接口
    @FormUrlEncoded
    @POST("get_channel")
    Observable<AgencyResponse> getAgency(@Header(Constants.API_HEADER) String header, @Field("lat")double lat, @Field("lng") double lng);

}
