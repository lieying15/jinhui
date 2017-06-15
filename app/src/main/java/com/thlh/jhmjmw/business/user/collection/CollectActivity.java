package com.thlh.jhmjmw.business.user.collection;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.response.GoodListResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogSimple;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  收藏页
 */
public class CollectActivity extends BaseActivity {
    private final String TAG = "CollectActivity";

//    @BindView(R.id.collect_snack_cl)
//    CoordinatorLayout collectSnackCl;
    @BindView(R.id.collect_goods_noinfoview)
    NoInfoView collectGoodsNoinfoview;
    @BindView(R.id.collect_goods_ptpr_rv)
    PullToRefreshRecyclerView collectGoodsPtprRv;


    private List<Goods> goodsList = new ArrayList<>();
    private CollectGoodsAdapter collectGoodsAdapter;
    private EasyRecyclerView collectGoodsRv;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = false;
    private int current_page = 1;
    private int total_page =1;
    private BaseObserver<GoodListResponse> goodsObserver;

    private BaseObserver<BaseResponse> delCollectObserver;

    private DialogSimple.Builder dialog;
    private SweetAlertDialog cartDialog;
    private int deletePositon = -1;

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, CollectActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        dialog = new DialogSimple.Builder(this);
        collectGoodsNoinfoview.setTitle(getResources().getString(R.string.no_collect_shop));
        collectGoodsNoinfoview.setTitleIv(R.drawable.noinfo_collect);
        collectGoodsNoinfoview.setNextactionStr(getResources().getString(R.string.collect_goLook));
        collectGoodsNoinfoview.setNextactionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexActivity.activityStart(CollectActivity.this, IndexActivity.POSITON_HOMEPAGE);

            }
        });

        collectGoodsPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        collectGoodsRv = collectGoodsPtprRv.getRefreshableView();

        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );
        collectGoodsAdapter = new CollectGoodsAdapter(this);
        collectGoodsAdapter.setOnClickEvent(new CollectGoodsAdapter.OnClickEvent() {
            @Override
            public void onDelete(View view, int position) {
                final Goods goods = collectGoodsAdapter.getItem(position);
                showDialog(goods.getItem_id(),position);
            }

            @Override
            public void onAddCart(int position) {
                final Goods goods = collectGoodsAdapter.getItem(position);
                // 单品套装
                if(goods.getIs_part().equals("1")){
                    if(goods.getPart_is_bundling().equals("1")){
                        GoodsSuitDetailActivity.activityStart(CollectActivity.this,goods.getPart_of_id(),0);
                    }else {
                        GoodsDetailV3Activity.activityStart(CollectActivity.this,goods.getPart_of_id());
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
                cartDialog = new SweetAlertDialog(CollectActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                cartDialog.setTitleText(getResources().getString(R.string.add_car)).show();
            }
        });
        
        collectGoodsAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(CollectActivity.this, ((Goods) collectGoodsAdapter.getItem(position)).getItem_id());
            }
        });
        collectGoodsRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
        mLayoutManager = new LinearLayoutManager(this);
        collectGoodsRv.setLayoutManager(mLayoutManager);
        collectGoodsRv.setAdapter(collectGoodsAdapter);
        collectGoodsPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        collectGoodsPtprRv.setHeaderLayout(new PtorHeaderLayout(this));
        collectGoodsPtprRv.setFooterLayout(new PtorFooterLayout(this));
        collectGoodsPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                goodsList.clear();
                collectGoodsAdapter.notifyDataSetChanged();
                collectGoodsAdapter.closeOpenedSwipeItemLayoutWithAnim();
                current_page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                collectGoodsPtprRv.onRefreshComplete();
            }
        });

        collectGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " dy" + dy);
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载， dy>0 表示向下滑动
                if (totalItemCount >= Constants.PageDataCount && lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    isLoadingMore = true;
                    current_page++;
                    L.e("加载更多数据 current_page" + current_page);
                    loadCollectData();
                }
            }
        });


        goodsObserver = new BaseObserver<GoodListResponse>() {
            @Override
            public void onErrorResponse(GoodListResponse response) {
                progressMaterial.dismiss();
                collectGoodsPtprRv.onRefreshComplete();
                isLoadingMore = false;
                showErrorDialog(response.getErr_msg());
            }

            @Override
            public void onNextResponse(GoodListResponse response) {
                progressMaterial.dismiss();
                total_page = response.getData().getTotal_page();
                collectGoodsPtprRv.onRefreshComplete();
                goodsList = response.getData().getItem();
                if (isLoadingMore) {
                    isLoadingMore = false;
                    collectGoodsAdapter.setList(goodsList,true);
                } else {
                    if(goodsList ==null || goodsList.size() ==0){
                        collectGoodsNoinfoview.setVisibility(View.VISIBLE);
                    }else {
                        collectGoodsNoinfoview.setVisibility(View.GONE);
                    }
                    collectGoodsAdapter.setList(goodsList);
                }
            }
        };

        delCollectObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                progressMaterial.dismiss();
                showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                progressMaterial.dismiss();
                L.i(TAG + " 取消收藏 成功  deletePositon" +deletePositon + "  size  " + goodsList.size());
                goodsList.remove(deletePositon);
                collectGoodsAdapter.setList(goodsList);
                collectGoodsAdapter.closeOpenedSwipeItemLayoutWithAnim();
                if(goodsList.size()==0){
                    collectGoodsNoinfoview.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    protected void loadData() {
        progressMaterial.show();
        loadCollectData();
    }

    private void loadCollectData(){
        Subscription subscription = NetworkManager.getCollectApi()
                .getGoods(SPUtils.getToken(),"G",current_page,Constants.PageDataCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goodsObserver);
        subscriptionList.add(subscription);
    }


    private void postDelCollect(String goods_id) {
        progressMaterial.show();
        Subscription subscription = NetworkManager.getCollectApi()
                .deleteCollect(SPUtils.getToken(), goods_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delCollectObserver);
        subscriptionList.add(subscription);
    }


    public void showDialog(final String goodsId,final int position){
        dialog.setTitle(getResources().getString(R.string.ture_collect_shop)).setLeftBtnStr(getResources().getString(R.string.collect_ture)).setRightBtnStr(getResources().getString(R.string.collect_cannal))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setLeftClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePositon = position;

                        postDelCollect(goodsId);
                    }
                }).create().show();
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

}
