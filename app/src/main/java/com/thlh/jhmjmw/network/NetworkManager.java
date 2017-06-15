// (c)2016 Flipboard Inc, All Rights Reserved.

package com.thlh.jhmjmw.network;


import android.content.Context;

import com.thlh.baselib.function.rxcache.BasicCache;
import com.thlh.baselib.function.rxcache.RxCacheCallAdapterFactory;
import com.thlh.baselib.retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import com.thlh.jhmjmw.network.api.AlbumApi;
import com.thlh.jhmjmw.network.api.CardApi;
import com.thlh.jhmjmw.network.api.CollectApi;
import com.thlh.jhmjmw.network.api.DownLoadImgApi;
import com.thlh.jhmjmw.network.api.FridgeApi;
import com.thlh.jhmjmw.network.api.GoodsDataApi;
import com.thlh.jhmjmw.network.api.ItemApi;
import com.thlh.jhmjmw.network.api.MessageApi;
import com.thlh.jhmjmw.network.api.OrderApi;
import com.thlh.jhmjmw.network.api.SearchApi;
import com.thlh.jhmjmw.network.api.StoreApi;
import com.thlh.jhmjmw.network.api.UserApi;
import com.thlh.jhmjmw.network.api.UserCacheApi;
import com.thlh.jhmjmw.network.api.WalletApi;
import com.thlh.jhmjmw.network.api.WeixinApi;
import com.thlh.jhmjmw.other.Deployment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager {
    private static final String baseUrl = Deployment.BASE_URL;
    private static GoodsDataApi goodsDataApi;
    private static AlbumApi albumApi;
    private static ItemApi itemApi;
    private static UserApi userApi;
    private static UserCacheApi userCacheApi;
    private static SearchApi searchApi;
    private static OrderApi orderApi;
    private static CollectApi collectApi;
    private static WeixinApi weixinApi;
    private static WalletApi walletApi;
    private static MessageApi messageApi;
    private static FridgeApi fridgApi;
    private static StoreApi storeApi;
    private static CardApi cardApi;
    private static DownLoadImgApi downLoadImgApi;
//    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static Converter.Factory xmlConverterFactory = SimpleXmlConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static GoodsDataApi getGoodsDataApi() {
        if (goodsDataApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Index/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            goodsDataApi = retrofit.create(GoodsDataApi.class);
        }
        return goodsDataApi;
    }

    public static UserApi getUserDataApi() {
        if (userApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Member/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

    public static UserCacheApi getUserCacheApi(Context context) {
        if (userCacheApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Member/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxCacheCallAdapterFactory.create(BasicCache.fromCtx(context), false))// 先读取缓存,再获取网络
//                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userCacheApi = retrofit.create(UserCacheApi.class);
        }
        return userCacheApi;
    }

    public static SearchApi getSearchApi() {
        if (searchApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Search/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            searchApi = retrofit.create(SearchApi.class);
        }
        return searchApi;
    }

    public static OrderApi getOrderApi() {
        if (orderApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Order/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            orderApi = retrofit.create(OrderApi.class);
        }
        return orderApi;
    }

    public static CollectApi getCollectApi() {
        if (collectApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Collection/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            collectApi = retrofit.create(CollectApi.class);
        }
        return collectApi;
    }

    public static WeixinApi getWeixinApi() {
        if (weixinApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.mch.weixin.qq.com/")
                    .addConverterFactory(xmlConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            weixinApi = retrofit.create(WeixinApi.class);
        }
        return weixinApi;
    }

    public static ItemApi getItemApi() {
        if (itemApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Item/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            itemApi = retrofit.create(ItemApi.class);
        }
        return itemApi;
    }

    public static AlbumApi getAlbumApi() {
        if (albumApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Album/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            albumApi = retrofit.create(AlbumApi.class);
        }
        return albumApi;
    }

    public static WalletApi getWalletApi() {
        if (walletApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Wallet/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            walletApi = retrofit.create(WalletApi.class);
        }
        return walletApi;
    }

    public static MessageApi getMessageApi() {
        if (messageApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Message/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            messageApi = retrofit.create(MessageApi.class);
        }
        return messageApi;
    }

    public static FridgeApi getFridgeApi() {
        if (fridgApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Fridge/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            fridgApi = retrofit.create(FridgeApi.class);
        }
        return fridgApi;
    }

    public static StoreApi getStoreApi() {
        if (storeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Store/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            storeApi = retrofit.create(StoreApi.class);
        }
        return storeApi;
    }

    public static CardApi getCardApi() {
        if (cardApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl + "Card/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            cardApi = retrofit.create(CardApi.class);
        }
        return cardApi;
    }


    public static DownLoadImgApi getDownLoadImgApi() {
        if (downLoadImgApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Deployment.IMAGE_PATH + "/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            downLoadImgApi = retrofit.create(DownLoadImgApi.class);
        }
        return downLoadImgApi;
    }

}
