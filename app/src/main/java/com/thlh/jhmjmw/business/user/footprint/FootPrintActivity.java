package com.thlh.jhmjmw.business.user.footprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.Seengood;
import com.thlh.baselib.model.response.GoodsRecommendResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogSimple;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HQ on 2016/4/6. 我的足迹页
 */
public class FootPrintActivity extends BaseViewActivity {
    private final String TAG = "FootPrintActivity";


    @BindView(R.id.footprint_rv)
    EasyRecyclerView footprintRv;
    @BindView(R.id.footprint_noinfoview)
    NoInfoView footprintNoinfoview;

    private BaseObserver<GoodsRecommendResponse> recommendObserver;

    private List<Seengood> footGoodsList = new ArrayList<>();
    private DialogSimple.Builder dialog;
    private FootprintAdapter footprintAdapter;


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, FootPrintActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        footGoodsList = DbManager.getInstance().getAllSeenGoods();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_footprint);
        ButterKnife.bind(this);

        
        dialog = new DialogSimple.Builder(this);
        footprintNoinfoview.setTitle(getResources().getString(R.string.foot_once_look));
        footprintNoinfoview.setTitleIv(R.drawable.img_dialog_foot);
        footprintNoinfoview.setNextactionStr(getResources().getString(R.string.foot_goLook));
        footprintNoinfoview.setNextactionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexActivity.activityStart(FootPrintActivity.this);
            }
        });

        if(footGoodsList==null ||footGoodsList.size() == 0){
            footprintNoinfoview.setVisibility(View.VISIBLE);
            loadRecommand();
        }else {
            footprintNoinfoview.setVisibility(View.GONE);
        }

        footprintAdapter = new FootprintAdapter(this, footGoodsList);
        footprintAdapter.setOnClickEvent(new FootprintAdapter.OnClickEvent() {
            @Override
            public void onDelete(View view, int position) {
                showBaseDialog(position);
            }
        });

        footprintAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(FootPrintActivity.this, ((Seengood) footprintAdapter.getItem(position)).getGoodsdb().getItem_id());
            }
        });
        footprintAdapter.setList(footGoodsList);
        footprintRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
        footprintRv.setLayoutManager(new LinearLayoutManager(this));
        footprintRv.setAdapter(footprintAdapter);
    }

    @Override
    protected void loadData() {
    }


    @OnClick({R.id.footprint_delete_tv,R.id.footprint_delete_iv})
    public void onClick() {
        showAllDialog();
    }




    public void showBaseDialog(final int position){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        final NormalDialogFragment deleteDialog = new NormalDialogFragment();
        deleteDialog.setContentStr(getResources().getString(R.string.foot_true_delete_shop_record));
        deleteDialog.setTitleIvRes(com.thlh.baselib.R.drawable.icon_dialog_warning);
        deleteDialog.setFinalBtnStr(getResources().getString(R.string.foot_true));
        deleteDialog.setCancelVisible(View.VISIBLE);
        deleteDialog.setContentGravity(Gravity.LEFT);
        deleteDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbManager.getInstance().deleteSeenGoods(footGoodsList.get(position).getGoodsdb());
                footGoodsList.remove(position);
                footprintAdapter.setList(footGoodsList);
                footprintAdapter.closeOpenedSwipeItemLayoutWithAnim();
                if(footGoodsList.size() ==0){
                    footprintNoinfoview.setVisibility(View.VISIBLE);
                    footprintRv.setVisibility(View.GONE);
                }
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show(ft, "deleteDialog");
    }

    public void showAllDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        final NormalDialogFragment deleteAllDialog = new NormalDialogFragment();
        deleteAllDialog.setContentStr(getResources().getString(R.string.foot_true_delete_shop_record));
        deleteAllDialog.setTitleIvRes(com.thlh.baselib.R.drawable.icon_dialog_warning);
        deleteAllDialog.setFinalBtnStr(getResources().getString(R.string.foot_true));
        deleteAllDialog.setCancelVisible(View.VISIBLE);
        deleteAllDialog.setContentGravity(Gravity.LEFT);
        deleteAllDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbManager.getInstance().deleteAllSeenGoods();
                footGoodsList.clear();
                footprintAdapter.setList(footGoodsList);
                footprintAdapter.closeOpenedSwipeItemLayoutWithAnim();
                footprintNoinfoview.setVisibility(View.VISIBLE);
                loadRecommand();
                footprintRv.setVisibility(View.GONE);
                deleteAllDialog.dismiss();
            }
        });
        deleteAllDialog.show(ft, "deleteAllDialog");
    }


    public void loadRecommand() {
        recommendObserver  = new BaseObserver<GoodsRecommendResponse>() {
            @Override
            public void onErrorResponse(GoodsRecommendResponse recommendResponse) {
            }

            @Override
            public void onNextResponse(GoodsRecommendResponse recommendResponse) {
                List<Goods> goodsList = recommendResponse.getData().getItem();
                L.e(TAG + " 查询推荐商品 goodsList.size "+ goodsList.size());
                footprintNoinfoview.setList(goodsList);
            }
        };
        L.e(TAG + " 查询推荐商品");
        NetworkManager.getGoodsDataApi()
                .getRecommendGoods()
                .compose(RxUtils.androidSchedulers(this,false))
                .subscribe(recommendObserver);
    }






    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
    
}
