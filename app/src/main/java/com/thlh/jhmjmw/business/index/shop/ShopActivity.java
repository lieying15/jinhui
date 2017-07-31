package com.thlh.jhmjmw.business.index.shop;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.response.StoreStateResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.view.BaseDialog;
import com.thlh.jhmjmw.view.StoreBuildDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的小店页
 */
public class ShopActivity extends BaseViewActivity {
    private final String TAG = "ShopActivity";
    private final int ACTIVITY_CODE_RECHARGE = 0;
    @BindView(R.id.shop_binding_store_tv)
    TextView shopBindingStoreTv;
    @BindView(R.id.shop_unbind_store_ll)
    LinearLayout shopUnbindStoreLl;
    private BaseObserver<StoreStateResponse> observer;
    private int is_bind;
    private String id = null;
    private BaseObserver<? super StoreStateResponse> bindobserver;
    private BaseDialog.Builder builder;
    private String store_name;
    private String phone;
    private StoreBuildDialog.Builder builder1;


    public static void activityStart(Activity context, int currIndex) {
        Intent intent = new Intent();
        intent.putExtra("currIndex", currIndex);
        intent.setClass(context, ShopActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    public static void activityStartForResult(Activity context, int code, int currIndex) {
        Intent intent = new Intent();
        intent.putExtra("currIndex", currIndex);
        intent.setClass(context, ShopActivity.class);
        context.startActivityForResult(intent, code);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);

    }

    @Override
    protected void initVariables() {
        is_bind = (int) SPUtils.get("is_bind_store", 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);

        builder = new BaseDialog.Builder(this);
        builder1 = new StoreBuildDialog.Builder(this);

        observer = new BaseObserver<StoreStateResponse>() {
            @Override
            public void onNextResponse(StoreStateResponse storeStateResponse) {
                id = storeStateResponse.getData().getStore().getId();
                is_bind = storeStateResponse.getData().getIs_bind();
                SPUtils.put("is_bind_store", is_bind);
                if (is_bind == 1){
                    showBuildStore();
                }else {
                    shopUnbindStoreLl.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onErrorResponse(StoreStateResponse storeStateResponse) {

            }
        };

        bindobserver = new BaseObserver<StoreStateResponse>() {
            @Override
            public void onNextResponse(StoreStateResponse storeStateResponse) {
                is_bind = storeStateResponse.getData().getIs_bind();
                SPUtils.put("is_bind_store", is_bind);
                store_name = storeStateResponse.getData().getStore().getStore_name();
                phone = storeStateResponse.getData().getStore().getPhone();
                showSuccessStore();
            }

            @Override
            public void onErrorResponse(StoreStateResponse storeStateResponse) {

            }
        };
    }

    private void showBuildStore() {
        shopUnbindStoreLl.setVisibility(View.GONE);
        builder1.setLeftClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }

    private void showSuccessStore() {
        builder.setLeftBtnStr(getResources().getString(R.string.me_know))
                .setTitle(getResources().getString(R.string.bind_phone_success))
                .setAddress(getResources().getString(R.string.store_address )+ store_name)
                .setPhone(getResources().getString(R.string.store_phone )+ phone)
                .setLeftClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showBuildStore();
                    }
                })
                .create().show();
    }

    @Override
    protected void loadData() {
        NetworkManager.getStoreApi()
                .getStoreState(SPUtils.getToken(),SPUtils.getLatitude(),SPUtils.getLongitude())
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(observer);
    }

    @OnClick(R.id.shop_binding_store_tv)
    public void onClick() {
        NetworkManager.getStoreApi()
                .binidStore(SPUtils.getToken(),id)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(bindobserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}
