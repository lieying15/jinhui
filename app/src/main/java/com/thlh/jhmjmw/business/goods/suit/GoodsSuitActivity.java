package com.thlh.jhmjmw.business.goods.suit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsBundling;
import com.thlh.baselib.model.GoodsBundlingItem;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.GoodsDetail;
import com.thlh.baselib.model.Supplier;
import com.thlh.baselib.model.response.GoodsDetailResponse;
import com.thlh.baselib.utils.AnimatCartUtils;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.S;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.ShopCartActivity;
import com.thlh.jhmjmw.business.goods.suit.adapter.GoodsSuitAdapter;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.recyclerview.EasyGridItemDecoration;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;
import com.thlh.viewlib.tablayout.MsgView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 产品套装
 */
public class GoodsSuitActivity extends BaseActivity {
    private final String TAG = "GoodsSuitActivity";
    private final int ACTIVITY_CODE_LOGIN = 1;

    @BindView(R.id.goodssuit_rv)
    EasyRecyclerView suitRv;
    @BindView(R.id.shopcart_bottom_selectall_iv)
    ImageView shopcartBottomSelectallIv;
    @BindView(R.id.shopcart_total_price_tv)
    TextView shopcartTotalPriceTv;
    @BindView(R.id.shopcart_total_price_symbol_tv)
    TextView shopcartTotalPriceSymbolTv;
    @BindView(R.id.goodsdetail_shopcart_iv)
    ImageView goodsdetailShopcartIv;
    @BindView(R.id.goodsdetail_shopcart_tv)
    TextView goodsdetailShopcartTv;
    @BindView(R.id.goodsdetail_shopcart_num_tv)
    MsgView goodsdetailShopcartNumTv;
    @BindView(R.id.goods_bottom_shopcart)
    RelativeLayout goodsBottomShopcart;
    @BindView(R.id.shopcart_bottom_selectall_rl)
    RelativeLayout shopcartBottomSelectallRl;
    @BindView(R.id.shopcart_bottom_clearing_tv)
    TextView shopcartBottomClearingTv;
    @BindView(R.id.shopcart_bottom_clearing_ll)
    LinearLayout shopcartBottomClearingLl;
    @BindView(R.id.goodssuit_snakecl)
    CoordinatorLayout goodssuitSnakecl;

    private List<GoodsBundling> bundingList;
    private ArrayList<Boolean> checkList = new ArrayList<>();
    private GoodsSuitAdapter suitAdapter;
    private AnimatCartUtils animatCart;
    private Gson gson;
    private GsonBuilder builder;
    private String jsonListTest;
    private SweetAlertDialog cartDialog;
    private Goods goods;

    private boolean isLogin;
    private int goods_num;
    private BaseObserver<GoodsDetailResponse> goodsdetailObserver;
    private GoodsDetail goodsDetail;

    public static void activityStart(Context context, ArrayList<GoodsBundling> bundingList) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsSuitActivity.class);
        intent.putParcelableArrayListExtra("bundingList", bundingList);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        bundingList = getIntent().getParcelableArrayListExtra("bundingList");
        builder = new GsonBuilder();
        gson = builder.create();
        for (int i = 0; i < bundingList.size(); i++) {
            checkList.add(false);
        }
    }


    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goodssuit);
        ButterKnife.bind(this);
        judgeIsSelectAll();
        updatPrice();
        animatCart = new AnimatCartUtils(this, goodsdetailShopcartIv, AnimatCartUtils.ANIMA_TYPE_GOODS_DETAIL);

        shopcartBottomClearingTv.setText(getResources().getString(R.string.goodsdetail_addcart));
        shopcartBottomClearingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasSelectGoos = false;
                for (int i = 0; i < bundingList.size(); i++) {
                    if (checkList.get(i)) {
//                        addShopCart(i);
                        loadShop(bundingList.get(i).getItem_id());
                        hasSelectGoos = true;
                    }
                }
                if (hasSelectGoos) {
                    cartDialog = new SweetAlertDialog(GoodsSuitActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    cartDialog.setTitleText(getResources().getString(R.string.add_car)).show();
                }
            }
        });


        suitAdapter = new GoodsSuitAdapter(this, checkList);
        suitRv.setAdapter(suitAdapter);
        suitRv.setLayoutManager(new LinearLayoutManager(this));
        suitRv.addItemDecoration(new EasyGridItemDecoration(this));
        suitAdapter.setList(bundingList);
        suitAdapter.setEventListener(new GoodsSuitAdapter.OnClickListener() {
            @Override
            public void addShopCart(int position) {
                loadShop(bundingList.get(position).getItem_id());
            }

            @Override
            public void onCheck(int position) {
                checkList.set(position, !checkList.get(position));
                suitAdapter.notifyDataSetChanged();
                judgeIsSelectAll();
                updatPrice();
            }
        });

        goodsdetailObserver = new BaseObserver<GoodsDetailResponse>() {

            @Override
            public void onNextResponse(GoodsDetailResponse goodsDetailResponse) {
                hideLoadindBar();
                goodsDetail = goodsDetailResponse.getData().getItem();
                addShopCart(goodsDetail);
            }

            @Override
            public void onErrorResponse(GoodsDetailResponse goodsDetailResponse) {
                hideLoadindBar();
                Tos.show(goodsDetailResponse.getErr_msg());
            }
        };

    }


    public void updatPrice() {
        double temptotalprice = 0;
        for (int i = 0; i < bundingList.size(); i++) {
            if (checkList.get(i)) {
                temptotalprice += Double.parseDouble(bundingList.get(i).getPrice());
            }
        }
        shopcartTotalPriceTv.setText("" + temptotalprice);
    }


    @Override
    protected void loadData() {
    }

    private void judgeIsSelectAll() {
        boolean isSelectall = true;
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i) == false) {
                isSelectall = false;
                break;
            }
        }
        if (isSelectall) {
            shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_select_black);
        } else {
            shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_black);
        }
    }

    public void loadShop(String itemid) {
        showLoadingBar();
        subscription = NetworkManager.getGoodsDataApi()
                .getGoodsDetail(SPUtils.getToken(), itemid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goodsdetailObserver);
    }

    private void addShopCart(GoodsDetail goodsDetail) {
        GoodsBundling goodsBundling = goodsDetail.getBundling().get(0);
        if (goodsBundling == null) {
            new S.Builder(goodssuitSnakecl, getResources().getString(R.string.youhui_suit)).create().show();
        }
        String bundinginfo = gson.toJson(goodsBundling, GoodsBundling.class);
        Supplier supplier = new Supplier();
        supplier.setId("-1");
        supplier.setName(getResources().getString(R.string.suit_));
        DbManager.getInstance().insertSupplier(supplier);
        for (GoodsBundlingItem item : goodsBundling.getItem()) {
            Supplier itemSupplier = new Supplier();
            itemSupplier.setId(item.getSupplier_id());
            itemSupplier.setName(item.getItem_name());
            itemSupplier.setStore_name(item.getItem_name());
            DbManager.getInstance().insertSupplier(itemSupplier);
            long supplierDbid = DbManager.getInstance().getSupplierDBid(item.getSupplier_id());
            GoodsDb bundingItem = GoodsChangeUtils.changeGoodsDb(item);
            itemSupplier.setDbid(supplierDbid);
            bundingItem.setSupplier(itemSupplier);
            DbManager.getInstance().insertGoods(bundingItem);
        }
        goods = GoodsChangeUtils.changeGoods(goodsDetail);
        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        long goodsDbid = DbManager.getInstance().getGoodsDBid(goodsDb.getItem_id());
        if (goodsDbid == -1) {
            goodsDbid = DbManager.getInstance().getGoodsSize() + 1;
        }
        goodsDb.setDbid(goodsDbid);
        DbManager.getInstance().insertGoods(goodsDb);

        Cartgoods insertCartGoods = new Cartgoods();
        insertCartGoods.setGoodsdb(goodsDb);
        insertCartGoods.setDb_goods_id(goodsDbid);
        insertCartGoods.setBunding_info(bundinginfo);
        L.e("GoodsSuitAct bundinginfo:" + bundinginfo);
        DbManager.getInstance().insertCart(insertCartGoods);
        int num = DbManager.getInstance().getAllGoodsNum();
        if (num > 0) {
            goodsdetailShopcartNumTv.setVisibility(View.VISIBLE);
            goodsdetailShopcartNumTv.setText("" + num);
        } else {
            goodsdetailShopcartNumTv.setVisibility(View.INVISIBLE);
        }

        cartDialog.setTitleText(getResources().getString(R.string.add_car)).show();
    }

    private void insetShopCart(int position) {
        GoodsBundling goodsBundling = bundingList.get(position);
        if (goodsBundling == null) {
            return;
        }
        GoodsDetail bundle = goodsBundling.getBundle();
        String bundinginfo = gson.toJson(goodsBundling, GoodsBundling.class);
        Supplier supplier = new Supplier();
        supplier.setId("-1");
        supplier.setName(getResources().getString(R.string.suit_));
        DbManager.getInstance().insertSupplier(supplier);



        goods = GoodsChangeUtils.changeGoods(bundle);
        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(goods);


        long goodsDbid = DbManager.getInstance().getGoodsDBid(goodsDb.getItem_id());
        if (goodsDbid == -1) {
            goodsDbid = DbManager.getInstance().getGoodsSize() + 1;
        }
        goodsDb.setDbid(goodsDbid);
        DbManager.getInstance().insertGoods(goodsDb);
        Cartgoods insertCartGoods = new Cartgoods();
        insertCartGoods.setGoodsdb(goodsDb);
        insertCartGoods.setDb_goods_id(goodsDbid);
        insertCartGoods.setBunding_info(bundinginfo);
        DbManager.getInstance().insertCart(insertCartGoods);
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }


    @OnClick({R.id.goods_bottom_shopcart, R.id.shopcart_bottom_selectall_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_bottom_shopcart:
                ShopCartActivity.activityStart(this);
                break;
            case R.id.shopcart_bottom_selectall_iv:
                selectAll();
                break;
        }
    }

    // 全选
    private void selectAll() {
        // 先判断是否已经全选
        boolean isSelectAll = true;
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i) == false) {
                isSelectAll = false;
                break;
            }
        }
        if (isSelectAll) {
            for (int i = 0; i < checkList.size(); i++) {
                checkList.set(i, false);
            }
            DbManager.getInstance().setAllGoodsSelect(false);
            suitAdapter.notifyDataSetChanged();
            shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_black);
        } else {
            for (int i = 0; i < checkList.size(); i++) {
                checkList.set(i, true);
            }
            DbManager.getInstance().setAllGoodsSelect(true);
            suitAdapter.notifyDataSetChanged();
            shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_select_black);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLogin = SPUtils.getIsLogin();
        goods_num = DbManager.getInstance().getAllGoodsNum();
        if (goods_num != 0) {
            goodsdetailShopcartNumTv.setVisibility(View.VISIBLE);
            goodsdetailShopcartNumTv.setText("" + goods_num);
        } else {
            goodsdetailShopcartNumTv.setVisibility(View.INVISIBLE);
        }
    }


}
