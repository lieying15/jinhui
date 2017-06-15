package com.thlh.baselib.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thlh.baselib.utils.BaseLog;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;


public  abstract class BaseFragment extends RxFragment implements BaseView{

    protected Subscription subscription;
    protected List<Subscription> subscriptionList = new ArrayList<>();

    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    private View convertView;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BaseLog.i( "Fragment onAttach: "+ getClass().getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseLog.i("Fragment onCreate: "+ getClass().getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseLog.i("Fragment onCreateView: " + getClass().getSimpleName());
        convertView = inflater.inflate(setLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, convertView);
        initVariables();
        initView();
        initData();
        isInitView = true;
        lazyloadData();

        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseLog.i("Fragment onActivityCreated: "  + getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseLog.i("Fragment onStart: "  +  getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        BaseLog.i("Fragment onPause: "   + getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseLog.i("Fragment onResume: "   + getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseLog.i("Fragment onStop: "   + getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseLog.i("Fragment onDestroy: "   + getClass().getSimpleName());
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        BaseLog.i("isVisibleToUser " + isVisibleToUser + "   " + this.getClass().getSimpleName());
        if (isVisibleToUser) {
            isVisible = true;
            lazyloadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyloadData() {
        String frgName = this.getClass().getSimpleName();
        if (isFirstLoad) {
            BaseLog.i("第一次加载 " + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + frgName);
        } else {
            BaseLog.i("不是第一次加载" + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + frgName);
        }
        if(!frgName.equals("HomePageFragment")){ //指定Fragment不加载
           if (!isFirstLoad || !isVisible || !isInitView) {
               BaseLog.i("不加载" + "   " + frgName);
               return;
           }
        }


        BaseLog.i("完成数据第一次加载"+ "   " + frgName);
        loadData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 页面传值等变量处理
     * @return
     */
    protected abstract void initVariables();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView();

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();
    /**
     * 读取网络数据
     * @return
     */
    protected abstract void loadData();


    public boolean isInitView() {
        return isInitView;
    }

    public void setInitView(boolean initView) {
        isInitView = initView;
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        if(subscriptionList.size()!=0){
            for (Subscription subscription : subscriptionList){
                if (subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
        unbinder.unbind();
        BaseLog.i("Fragment onDestroyView: "   + getClass().getSimpleName());
    }

    @Override
    public void showLoadingBar() {
        ((BaseActivity)getActivity()).getProgressMaterial().show();
    }

    @Override
    public void hideLoadindBar() {
        ((BaseActivity)getActivity()).getProgressMaterial().hide();
    }

    public void showWaringDialog(String msg){
        ((BaseActivity)getActivity()).showWaringDialog(msg);
    }

    public void showErrorDialog(String msg){
        ((BaseActivity)getActivity()).showErrorDialog(msg);
    }

    public void showSuccessDialog(String msg){
        ((BaseActivity)getActivity()).showSuccessDialog(msg);
    }

}
