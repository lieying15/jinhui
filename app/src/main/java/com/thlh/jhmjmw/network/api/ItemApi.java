// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network.api;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.CommentResponse;
import com.thlh.baselib.model.response.CommentSaveResponse;
import com.thlh.baselib.model.response.GoodsCommentResponse;
import com.thlh.baselib.model.response.GoodsListResponse;
import com.thlh.baselib.model.response.PicResponse;
import com.thlh.baselib.model.response.StorageResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

public interface ItemApi {

    @Multipart
    @POST("add_comment")
    Observable<BaseResponse> addComment(@Header(Constants.API_HEADER) String header, @PartMap Map<String, RequestBody> params0, @PartMap Map<String, RequestBody> params1);

    @FormUrlEncoded
    @POST("comment_center")
    Observable<GoodsCommentResponse> getMyComments(@Header(Constants.API_HEADER) String header, @Field("flag") String flag, @Field("page") int page, @Field("count") int count);

    @FormUrlEncoded
    @POST("comments")
    Observable<CommentResponse> getAllComments(@Header(Constants.API_HEADER) String header,  @Field("item_id") String item_id,@Field("grade") int grade, @Field("page") int page, @Field("count") int count);

    @FormUrlEncoded //查看未保存评论接口
    @POST("unsaved_comment")
    Observable<CommentSaveResponse>  getSaveInfo(@Header(Constants.API_HEADER) String header, @Field("order_id") String order_id, @Field("item_id") String item_id);

    @FormUrlEncoded //查看评论图
    @POST("comment_pic")
    Observable<PicResponse> getCommentPhotos(@Header(Constants.API_HEADER) String header, @Field("item_id") String item_id);



    @FormUrlEncoded
    @POST("supplier_items/")
    Observable<GoodsListResponse> getSupplierItems(@Header(Constants.API_HEADER) String header, @Field("page") int page, @Field("count") int count, @Field("suppier_id") String suppier_id);

    @FormUrlEncoded
    @POST("storage")  //查询商品库存
    Observable<StorageResponse> getStorageInfo(@Header(Constants.API_HEADER) String header, @Field("items") String catid);



}
