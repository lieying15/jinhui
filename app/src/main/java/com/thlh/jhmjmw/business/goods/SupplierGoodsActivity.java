package com.thlh.jhmjmw.business.goods;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.response.GoodsListResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 供应商商品页
 */
public class SupplierGoodsActivity extends BaseActivity {
    private final String TAG = "SupplierGoodsActivity";
    @BindView(R.id.supplier_goods_ptpr_rv)
    PullToRefreshRecyclerView goodsPtprRv;
    @BindView(R.id.supplier_header)
    HeaderNormal supplierHeader;

    private String supplier_id ="";
    private String supplier_name ="";
    private List<Goods> mList = new ArrayList<Goods>();

    private EasyRecyclerView supplierGoodsRv;
    private SupplierGoodsAdapter shopGridAdapter;
//    private EasyDividerItemDecoration dataDecoration;
    private BaseObserver<GoodsListResponse> goodsrObserver;

    private GridLayoutManager mLayoutManager;
    private boolean isLoadingMore = false;
    private int total_page = 1;
    private int current_page = 1;
    private SweetAlertDialog collectDialog;

    public static void activityStart(Activity context, String supplier_id,String supplier_name) {
        Intent intent = new Intent();
        intent.putExtra("supplier_id", supplier_id);
        intent.putExtra("supplier_name", supplier_name);
        intent.setClass(context, SupplierGoodsActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }


    @Override
    protected void initVariables() {
        supplier_id = getIntent().getStringExtra("supplier_id");
        supplier_name = getIntent().getStringExtra("supplier_name");
        L.i(TAG + " 供应商 supplier_id " +  supplier_id +" supplier_name"  +supplier_name);
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_suppliergoods);
        ButterKnife.bind(this);
        supplierHeader.setTitle(supplier_name);

        goodsPtprRv.setHasPullUpFriction(false);
        supplierGoodsRv = goodsPtprRv.getRefreshableView();
        goodsPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        goodsPtprRv.setHeaderLayout(new PtorHeaderLayout(this));
        goodsPtprRv.setFooterLayout(new PtorFooterLayout(this));
        goodsPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadSupplierData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                goodsPtprRv.onRefreshComplete();
            }
        });

        supplierGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoadingMore) return;
                if (current_page >= total_page) return;
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载， dy>0 表示向下滑动
                if (totalItemCount >= Constants.PageDataCount && lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    isLoadingMore = true;
                    current_page++;
                    L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " dy" + dy);
                    L.e("加载更多数据 current_page" + current_page);
                    loadSupplierData();//这里多线程也要手动控制isLoadingMore
                }
            }
        });

//        dataDecoration = new EasyDividerItemDecoration(
//                this,
//                EasyDividerItemDecoration.VERTICAL_LIST,
//                R.drawable.divider_mainback
//        );
//        dataDecoration.bottomDivider = true;
        mLayoutManager = new GridLayoutManager(this,3);
        supplierGoodsRv.setLayoutManager(mLayoutManager);
        shopGridAdapter = new SupplierGoodsAdapter(this);
        shopGridAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(SupplierGoodsActivity.this, ((Goods) shopGridAdapter.getItem(position)).getItem_id());
            }
        });
        shopGridAdapter.setBuyListener(new SupplierGoodsAdapter.OnClickListener() {
            @Override
            public void onClickBuy(View view, int position, int[] item_location, Drawable drawable) {
                final Goods goods = shopGridAdapter.getItem(position);
                // 单品套装
                if(goods.getIs_part().equals("1")){
                    if(goods.getPart_is_bundling().equals("1")){
                        GoodsSuitDetailActivity.activityStart(SupplierGoodsActivity.this,goods.getPart_of_id(),0);
                    }else {
                        GoodsDetailV3Activity.activityStart(SupplierGoodsActivity.this,goods.getPart_of_id());
                    }
                    return;
                }

                // 整箱套装
                if(goods.getIs_pack().equals("1")){
                    int packNum  = goods.getPack_num()==null?0:Integer.parseInt(goods.getPack_num());
                    for (int i = 0; i < packNum; i++) {
                        DbManager.getInstance().insertCart(goods);
                    }
                }else {
                    DbManager.getInstance().insertCart(goods);
                }
                collectDialog = new SweetAlertDialog(SupplierGoodsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                collectDialog.setTitleText(getResources().getString(R.string.add_car)).show();
            }
        });
//        supplierGoodsRv.addItemDecoration(dataDecoration);
        supplierGoodsRv.setAdapter(shopGridAdapter);
        supplierGoodsRv.setNestedScrollingEnabled(false);


        goodsrObserver= new BaseObserver<GoodsListResponse>() {
            @Override
            public void onErrorResponse(GoodsListResponse goodListResponse) {
                progressMaterial.dismiss();
                goodsPtprRv.onRefreshComplete();
                isLoadingMore = false;
            }

            @Override
            public void onNextResponse(GoodsListResponse goodListResponse) {
                goodsPtprRv.onRefreshComplete();
                total_page = goodListResponse.getData().getTotal_page();
                progressMaterial.dismiss();

                List<Goods> tempgoodsList = goodListResponse.getData().getItem();
                if (isLoadingMore) {
                    shopGridAdapter.setList(tempgoodsList, true);
                    isLoadingMore = false;
                } else {
                    if(tempgoodsList == null || tempgoodsList.size() ==0){

                    }else {
                        shopGridAdapter.setList(tempgoodsList);
                    }
                }
            }
        };
    }

    @Override
    protected void loadData() {
        progressMaterial.show();
        loadSupplierData();
;
    }

    private void loadSupplierData(){
        NetworkManager.getItemApi()
                .getSupplierItems(SPUtils.getToken(), current_page, Constants.PageDataCount , supplier_id)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(goodsrObserver);
    }


}
