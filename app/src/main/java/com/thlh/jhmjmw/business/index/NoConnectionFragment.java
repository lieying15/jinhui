package com.thlh.jhmjmw.business.index;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.config.Constants;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.devices.BindDevicesActivity;
import com.thlh.jhmjmw.view.HeaderIndex;

import butterknife.BindView;
import butterknife.OnClick;

//无网络连接
public class NoConnectionFragment extends BaseFragment {
    private static final String TAG = "NoConnectionFragment";
    @BindView(R.id.homepage_header)
    HeaderIndex homepageHeader;
    @BindView(R.id.noconnection_check_ll)
    LinearLayout noconnectionCheckLl;
    @BindView(R.id.frgdialog_normal_content_tv)
    TextView frgdialogNormalContentTv;
    @BindView(R.id.reconnection_ll)
    LinearLayout reconnectionLl;



    //静态内部类方法创建单例
    public static class NOConnectionFragmentLoader {
        private static final NoConnectionFragment instance = new NoConnectionFragment();
    }

    public static NoConnectionFragment newInstance() {
        return NOConnectionFragmentLoader.instance;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_noconnection;
    }

    @Override
    protected void initVariables() {

    }


    @Override
    protected void initView() {
        homepageHeader.setActivityContext(getActivity());
        homepageHeader.setOnClickScanListener(new HeaderIndex.OnClickScanListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BindDevicesActivity.class);
                getActivity().startActivityForResult(intent, Constants.ACTIVITYCODE_SCANNQR);
            }
        });


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
    }



    @OnClick({R.id.noconnection_check_ll, R.id.reconnection_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.noconnection_check_ll:
//                NetUtils.openSetting(getActivity());
                break;
            case R.id.reconnection_ll:
                if(isNetworkConnected(getActivity())){
                    IndexActivity.activityStart(getActivity(),IndexActivity.POSITON_HOMEPAGE);
                }

            break;
        }
    }
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
