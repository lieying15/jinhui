package com.thlh.jhmjmw.business.buy.buyconfirm.express;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.response.ExpressfreeResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 配送信息页
 */

public class BuyExpressActivity extends BaseActivity {
    private final String TAG = "BuyExpressActivity";
    
    @BindView(R.id.order_express_header)
    HeaderNormal orderExpressHeader;
    @BindView(R.id.order_express_list_rv)
    EasyRecyclerView orderExpressListRv;
    @BindView(R.id.order_express_list_topll)
    LinearLayout expressListTopLl;

    private String itemIdAndNum;
    private List<CartSupplier> cartSupplierList = new ArrayList<CartSupplier>();
    private BaseObserver<ExpressfreeResponse> getExpressfreeObserver;
    private BuyExpressListAdapter listAdapter;


    private boolean isBuyImmediately;

    public static void activityStart(Activity context, String itemIdAndNum,boolean isBuyImmediately) {
        Intent intent = new Intent();
        intent.setClass(context, BuyExpressActivity.class);
        intent.putExtra("itemIdAndNum", itemIdAndNum);
        intent.putExtra("isBuyImmediately", isBuyImmediately);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        itemIdAndNum = getIntent().getStringExtra("itemIdAndNum");
        isBuyImmediately = getIntent().getBooleanExtra("isBuyImmediately",false);
        if(isBuyImmediately){
            cartSupplierList = DbManager.getInstance().getCartSupplierList(false,true);
        }else {
            cartSupplierList = DbManager.getInstance().getCartSupplierList(true,false);
        }
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy_express);
        ButterKnife.bind(this);
        listAdapter = new BuyExpressListAdapter(this);
        orderExpressListRv.setLayoutManager(new LinearLayoutManager(this));
        orderExpressListRv.setAdapter(listAdapter);
        orderExpressListRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.x10)));
        progressMaterial.show();

        getExpressfreeObserver = new BaseObserver<ExpressfreeResponse>() {
            @Override
            public void onErrorResponse(ExpressfreeResponse expressfreeResponse) {
                showErrorDialog(expressfreeResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(ExpressfreeResponse expressfreeResponse) {
                listAdapter.setList(expressfreeResponse.getData().getSupplier());
            }
        };

    }

    @Override
    protected void loadData() {
        loadExpressFree();
    }

    private void loadExpressFree() {
        L.e(TAG + " loadExpressFree itemIdAndNum" + itemIdAndNum);
        String addressId = (String) SPUtils.get("user_address_id", "").toString();
        String toStore ;
        if(addressId.equals("0")){
            toStore = "1";
        }else {
            toStore = "0";
        }
        NetworkManager.getOrderApi()
                .getExpressfreeV2(SPUtils.getToken(), addressId, itemIdAndNum,toStore)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(getExpressfreeObserver);

    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
