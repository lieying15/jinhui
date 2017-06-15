package com.thlh.baselib.model;


import com.thlh.baselib.utils.BaseLog;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/4/29.
 */
public abstract class BaseSubscriber<T> extends  Subscriber<T> {

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
//            BaseLog.i("BaseObserver onNext response: " + toString());
//            if(response.getErr_code() == 200){
//            onNextResponse(response);
//
//            }else{
//            BaseLog.e("BaseObserver Err_code: " +response.getErr_code()+" Err_msg:" + response.getErr_msg());
//            onErrorResponse(response);
//            }


            }
    public abstract void onNextResponse(T t);

    public abstract void onErrorResponse(T t);


}
