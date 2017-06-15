package com.thlh.jhmjmw.business.goods.goodsdetail.imginfo;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseFragment;
import com.thlh.jhmjmw.R;

import butterknife.BindView;

public class GoodsImgInfoFragment extends BaseFragment {
    private static final String TAG = "GoodsInfoFragment";
    @BindView(R.id.goodsdetail_webview)
    WebView goodsdetailWebview;

    @Override
    protected void initVariables() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_goodsdetail_detail;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {

    }


    public void updateContent(String goodsContent){
        goodsdetailWebview.loadDataWithBaseURL(null, goodsContent, "text/html", "utf-8", null);
        goodsdetailWebview.setMinimumWidth(BaseApplication.width);
        WebSettings webSettings = goodsdetailWebview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(150);
        webSettings.setRenderPriority(WebSettings.RenderPriority.LOW);
    }
}
