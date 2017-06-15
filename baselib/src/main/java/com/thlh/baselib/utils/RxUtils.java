package com.thlh.baselib.utils;

import android.app.Activity;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseView;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by huqiang on 2017/1/22.
 */

public class RxUtils {

    //Rxjava切换线程的封装。


    public static <T> Observable.Transformer<T, T> androidSchedulers(final BaseView view,boolean needBar) {
        if(needBar){
            return new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> observable) {
                    return observable.subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {//显示进度条
                                    view.showLoadingBar();
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doAfterTerminate(new Action0() {
                                @Override
                                public void call() {
                                    view.hideLoadindBar();//隐藏进度条
                                }
                            }).compose(RxUtils.<T>bindToLifecycle(view));
                }
            };
        }else {
            return new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> observable) {
                    return observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(RxUtils.<T>bindToLifecycle(view));
                }
            };
        }
    }

    public static <T> Observable.Transformer<T, T> androidSchedulers(final BaseView view) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {//显示进度条
                                view.showLoadingBar();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                view.hideLoadindBar();//隐藏进度条
                            }
                        }).compose(RxUtils.<T>bindToLifecycle(view));
            }
        };
    }



    public static <T> LifecycleTransformer<T> bindToLifecycle(BaseView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindToLifecycle();
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(Activity activity) {
        return ((BaseActivity) activity).<T>bindToLifecycle();
    }

}
