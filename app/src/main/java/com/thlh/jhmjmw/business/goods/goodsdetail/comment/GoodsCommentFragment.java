package com.thlh.jhmjmw.business.goods.goodsdetail.comment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Comment;
import com.thlh.baselib.model.response.CommentResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.order.comment.CommentAdapter;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.List;

import butterknife.BindView;

public class GoodsCommentFragment extends BaseFragment {
    private static final String TAG = "GoodsCommentFragment";
    @BindView(R.id.goodsdetail_comment_ptpr_rv)
    PullToRefreshRecyclerView commentPtprRv;
    @BindView(R.id.goodsdetail_floatbtn)
    FloatingActionButton goodsdetailFloatbtn;
    @BindView(R.id.comment_noinfoview)
    NoInfoView noinfoview;
    private EasyRecyclerView commentRv;
    private CommentAdapter commentAdapter;

    private int cotent_type = 0;
    private int current_page = 1 ;
    private int total_page = 1;
    private boolean isLoadingMore = false;
    private LinearLayoutManager mLayoutManager;
    private String goods_id ;
    private BaseObserver<CommentResponse> commentObserver;

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        cotent_type = bundle.getInt("commentType");
        L.e(TAG + " goodsid:"+goods_id + " cotent_type:"+cotent_type);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_goodsdetail_comment;
    }

    @Override
    protected void initView() {
        noinfoview.setTitle(getResources().getString(R.string.no_evaluation));
        noinfoview.setTitleIv(R.drawable.img_dialog_pen);

        commentPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        commentPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        commentPtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));
        commentPtprRv.setFooterLayout(new PtorFooterLayout(getActivity()));
        commentPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadCommentData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                commentPtprRv.onRefreshComplete();
            }
        });
        commentRv = commentPtprRv.getRefreshableView();
        commentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(getActivity());
        commentRv.setAdapter(commentAdapter);
        commentRv.addItemDecoration(new VerticalltemDecoration((int)getResources().getDimension(R.dimen.y10)));
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
        commentObserver   = new BaseObserver<CommentResponse>() {
            @Override
            public void onErrorResponse(CommentResponse commentResponse) {
                commentPtprRv.onRefreshComplete();
            }

            @Override
            public void onNextResponse(CommentResponse commentResponse) {
                commentPtprRv.onRefreshComplete();

                total_page = commentResponse.getData().getTotal_page();
                List<Comment> commentsList = commentResponse.getData().getComments();
                if (isLoadingMore) {
                    isLoadingMore = false;
                    commentAdapter.setList(commentsList,true);
                } else {
                    if(commentsList == null || commentsList.size() ==0){
                        noinfoview.setVisibility(View.VISIBLE);
                        commentPtprRv.setVisibility(View.GONE);
                    }else {
                        noinfoview.setVisibility(View.GONE);
                        commentPtprRv.setVisibility(View.VISIBLE);
                    }
                    commentAdapter.setList(commentsList);
                }
                int total = Integer.parseInt(commentResponse.getData().getRate1())+
                        Integer.parseInt(commentResponse.getData().getRate2())+
                        Integer.parseInt(commentResponse.getData().getRate3());
                ((GoodsCommentTopFragment)getParentFragment()).setCommentNumAll(total+"");
                ((GoodsCommentTopFragment)getParentFragment()).setCommentNumGoods(commentResponse.getData().getRate1());
                ((GoodsCommentTopFragment)getParentFragment()).setCommentNumNormal(commentResponse.getData().getRate2());
                ((GoodsCommentTopFragment)getParentFragment()).setCommentNumBad(commentResponse.getData().getRate3());
                ((GoodsCommentTopFragment)getParentFragment()).updateTabTitle();
            }
        };
        loadCommentData();
    }

    @Override
    protected void loadData() {
    }

    private void loadCommentData(){
        L.e(TAG + " loadCommentData ");
        NetworkManager.getItemApi()
                .getAllComments(SPUtils.getToken(), goods_id, cotent_type  ,current_page, Constants.PageDataCount ) //item_id  grade0:全部 1:好评 2:中评 3:差评  page count
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(commentObserver);
    }


}
