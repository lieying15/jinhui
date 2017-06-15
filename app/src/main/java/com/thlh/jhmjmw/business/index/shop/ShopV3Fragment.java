package com.thlh.jhmjmw.business.index.shop;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.jhmjmw.R;

//最初首页的
public class ShopV3Fragment extends BaseFragment {
    private static final String TAG = "shopFragment";

    //静态内部类方法创建单例
    public static class ShopFragmentLoader  {
        private static final ShopV3Fragment instance = new ShopV3Fragment();
    }

    public static ShopV3Fragment newInstance(){
        return ShopFragmentLoader.instance;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initVariables() {

    }


    @Override
    protected void loadData() {

    }



}
