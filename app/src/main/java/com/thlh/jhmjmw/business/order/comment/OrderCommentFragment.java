package com.thlh.jhmjmw.business.order.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.GoodsComment;
import com.thlh.baselib.model.GoodsDetail;
import com.thlh.baselib.model.response.GoodsCommentResponse;
import com.thlh.baselib.model.response.GoodsDetailResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.S;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.ShopCartActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderCommentFragment extends BaseFragment {
    private static final String TAG = "OrderCommentFragment";
    private final int ACTIVITY_CODE_WRITECOMMENT = 1;
    private final int COMMENT_TYPE_WAIT = 0; //全部订单
    private final int COMMENT_TYPE_COMPLETE = 1;

    @BindView(R.id.comment_mine_ptpr_rv)
    PullToRefreshRecyclerView commentPtprRv;
    @BindView(R.id.comment_snack_cl)
    CoordinatorLayout commentSnackCl;
    @BindView(R.id.comment_noinfoview)
    NoInfoView commentNoinfoview;

    private List<GoodsComment> mList = new ArrayList<GoodsComment>();

    private EasyRecyclerView commentRv;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = false;

    private OrderCommentAdapter commentAdapter;
    private int cotent_type;
    private int total_page = 1;
    private int current_page = 1 ;
    private BaseObserver<GoodsCommentResponse> commentObserver;
    private BaseObserver<GoodsDetailResponse> goodsdetailObserver;



    @Override
    protected int setLayoutId() {
        return R.layout.fragment_comment_list;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        cotent_type = bundle.getInt("commentType");
    }

    @Override
    protected void initView() {
        commentNoinfoview.setTitle(getResources().getString(R.string.no_evaluation_shop));
        commentNoinfoview.setTitleIv(R.drawable.img_dialog_pen);
        commentPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        commentPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        commentPtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));
        commentPtprRv.setFooterLayout(new PtorFooterLayout(getActivity()));
        commentPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                commentPtprRv.onRefreshComplete();
            }
        });

        commentRv = commentPtprRv.getRefreshableView();
        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                getActivity(),
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );

        dataDecoration.bottomDivider = true;
//        commentRv.addItemDecoration(dataDecoration);
        commentRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
        mLayoutManager = new LinearLayoutManager(getActivity());
        commentRv.setLayoutManager(mLayoutManager);
        commentAdapter = new OrderCommentAdapter(getActivity(),cotent_type);
        commentRv.setAdapter(commentAdapter);
        commentAdapter.setOnClickEvent(new OrderCommentAdapter.OnClickEvent() {
            @Override
            public void onComment(View view, int position) {
                L.e("pinjia");
                GoodsComment goodsComment = commentAdapter.getItem(position);
                OrderCommentWriteActivity.activityStart(getActivity(),goodsComment,ACTIVITY_CODE_WRITECOMMENT);
            }

            @Override
            public void onRebuy(int position, String orderid) {
                loadGoodsDetail(orderid);
            }
        });
        commentAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(getActivity(),((GoodsComment)commentAdapter.getItem(position)).getItem_id());
            }
        });

        commentRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    loadData();//这里多线程也要手动控制isLoadingMore
                }
            }
        });
    }

    @Override
    protected void initData() {
        commentObserver  = new BaseObserver<GoodsCommentResponse>() {
            @Override
            public void onErrorResponse(GoodsCommentResponse commentResponse) {
                commentPtprRv.onRefreshComplete();
                isLoadingMore = false;
                new S.Builder(commentSnackCl, commentResponse.getErr_msg()).create().show();
//                new S.Builder(getActivity(), commentSnackCl, commentResponse.getErr_msg()).create().show();
            }

            @Override
            public void onNextResponse(GoodsCommentResponse commentResponse) {
                commentPtprRv.onRefreshComplete();
                total_page = commentResponse.getData().getTotal_page();
                List<GoodsComment> commentsList = commentResponse.getData().getItems();
                if (isLoadingMore) {
                    isLoadingMore = false;
                    commentAdapter.setList(commentsList,true);
                } else {
                    if(commentsList == null || commentsList.size() ==0){
                        commentNoinfoview.setVisibility(View.VISIBLE);
                        commentPtprRv.setVisibility(View.GONE);
                    }else {
                        commentNoinfoview.setVisibility(View.GONE);
                        commentPtprRv.setVisibility(View.VISIBLE);
                    }
                    commentAdapter.setList(commentsList);
                }
            }
        };

        goodsdetailObserver = new BaseObserver<GoodsDetailResponse>() {
            @Override
            public void onErrorResponse(GoodsDetailResponse goodsDetailResponse) {
                showErrorDialog( goodsDetailResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(GoodsDetailResponse goodsDetailResponse) {
                GoodsDetail goodsDetail = goodsDetailResponse.getData().getItem();
                DbManager.getInstance().insertCart(goodsDetail);
                ShopCartActivity.activityStart(getActivity());
            }
        };
    }

    @Override
    protected void loadData() {
        NetworkManager.getItemApi()
                .getMyComments(SPUtils.getToken(), cotent_type +"" , current_page, Constants.PageDataCount)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(commentObserver);
    }

    private void loadGoodsDetail(String goodsId) {
        NetworkManager.getGoodsDataApi()
                .getGoodsDetail(SPUtils.getToken(), goodsId)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(goodsdetailObserver);
    }



    public  void reLoadComment(){
        current_page = 1;
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_WRITECOMMENT:
                    ((OrderCommentActivity)getActivity()).reLoadComment();
                    break;

                default:
                    break;
            }
        }
    }

}
