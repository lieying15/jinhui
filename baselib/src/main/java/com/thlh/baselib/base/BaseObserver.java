package com.thlh.baselib.base;

import com.thlh.baselib.utils.BaseLog;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.Tos;

import rx.Observer;

/**
 * Created by Administrator on 2016/4/29.
 */
public abstract class BaseObserver<T extends BaseResponse> implements Observer<T> {

    @Override
    public void onCompleted() {
            BaseLog.i("BaseObserver onCompleted");
            }

    @Override
    public void onError(Throwable e) {
            BaseLog.e("BaseObserver onError e:" + e);

            }

    @Override
    public void onNext(T response) {
            BaseLog.i("BaseObserver onNext response: " + toString());
            if(response.getErr_code() == 200){
                onNextResponse(response);

            }else{
                BaseLog.e("BaseObserver Err_code: " +response.getErr_code()+" Err_msg:" + response.getErr_msg());
                onErrorResponse(response);
                if(response.getErr_code() == 300001){
                    Tos.show("登录时效已过期，请重新登录");
                    SPUtils.clearUserInfo();
                }
            }
    }

    public abstract void onNextResponse(T t);

    public abstract void onErrorResponse(T t);
}
