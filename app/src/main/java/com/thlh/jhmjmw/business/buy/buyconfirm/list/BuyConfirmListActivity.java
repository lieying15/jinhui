package com.thlh.jhmjmw.business.buy.buyconfirm.list;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.CartSupplierCheck;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品清单
 */

public class BuyConfirmListActivity extends BaseViewActivity {
    private final String TAG = "BuyConfirmListActivity";
   @BindView(R.id.orderconfirmlist_rv)
    EasyRecyclerView orderconfirmlistRv;
   @BindView(R.id.orderconfirmlist_header)
    HeaderNormal orderconfirmlistHeader;


    private List<CartSupplier> cartSupplierList = new ArrayList<CartSupplier>();
    private List<CartSupplierCheck> cartSupplierCheck = new ArrayList<>();
    private BuyConfirmListAdapter listAdapter;
    private boolean isBuyImmediately;

    public static void activityStart(Activity context,boolean isBuyImmediately) {
        Intent intent = new Intent();
        intent.setClass(context, BuyConfirmListActivity.class);
        intent.putExtra("isBuyImmediately", isBuyImmediately);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        isBuyImmediately = getIntent().getBooleanExtra("isBuyImmediately",false);
        if(isBuyImmediately){
            cartSupplierList = DbManager.getInstance().getCartSupplierList(false,true);
        }else {
            cartSupplierList = DbManager.getInstance().getCartSupplierList(true,false);
        }

        for (int i = 0; i < cartSupplierList.size(); i++) {
            CartSupplierCheck list = new CartSupplierCheck();
            int num = cartSupplierList.get(i).getCartgoods().size();
            List<Boolean> checkStates = new ArrayList<>();
            for (int n = 0; n < num; n++) {
                checkStates.add(n, true);
            }
            list.setGoodsCheckStates(checkStates);
            cartSupplierCheck.add(list);
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_confirmlist);
        ButterKnife.bind(this);

        listAdapter = new BuyConfirmListAdapter(this);

        listAdapter.setList(cartSupplierList);
        orderconfirmlistRv.setLayoutManager(new LinearLayoutManager(this));
        orderconfirmlistRv.setAdapter(listAdapter);
        orderconfirmlistRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
        orderconfirmlistHeader.getHeaderNomalRightTv().setBackgroundResource(R.drawable.bg_null);
        orderconfirmlistHeader.getHeaderNomalRightTv().setTextColor(Color.WHITE);
        orderconfirmlistHeader.setRightText(getResources().getString(R.string.gong) +  DbManager.getInstance().getCartGoodsNum(isBuyImmediately)+ getResources().getString(R.string.ones));
    }

    @Override
    protected void loadData() {
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

}
