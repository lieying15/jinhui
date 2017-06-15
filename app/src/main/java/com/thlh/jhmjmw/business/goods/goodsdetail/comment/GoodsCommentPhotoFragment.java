package com.thlh.jhmjmw.business.goods.goodsdetail.comment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.response.Pic;
import com.thlh.baselib.model.response.PicResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.PhotoPagerActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.GridSpacingItemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GoodsCommentPhotoFragment extends BaseFragment {
    private static final String TAG = "GoodsCommentPhotoFragment";
    @BindView(R.id.comment_mine_ptpr_rv)
    PullToRefreshRecyclerView commentPtprRv;
    @BindView(R.id.comment_noinfoview)
    NoInfoView noinfoview;

    private EasyRecyclerView commentRv;
    private List<Pic> picsList = new ArrayList<Pic>();

    private GoodsCommentPhotoAdapter picAdapter;
    private int current_page = 1;
    private String goodsid = "" ;

    private BaseObserver<PicResponse> commentPicObserver;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_comment_list;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        goodsid = bundle.getString("goods_id");
    }

    @Override
    protected void initView() {
        noinfoview.setTitle(getResources().getString(R.string.no_evaluation));
        noinfoview.setTitleIv(R.drawable.img_dialog_pen);

        commentPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        commentPtprRv.setPadding(30,30,30,0);
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
        commentRv.setPadding(0,15,0,15);

        int decorationSpace = (int)getActivity().getResources().getDimension(R.dimen.x4);
        commentRv.addItemDecoration(new GridSpacingItemDecoration(3,decorationSpace,false));
        commentRv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        picAdapter = new GoodsCommentPhotoAdapter(getActivity());
        commentRv.setAdapter(picAdapter);

    }

    @Override
    protected void initData() {
        commentPicObserver  = new BaseObserver<PicResponse>() {
            @Override
            public void onErrorResponse(PicResponse commentResponse) {
                commentPtprRv.onRefreshComplete();
            }

            @Override
            public void onNextResponse(PicResponse commentResponse) {
                commentPtprRv.onRefreshComplete();
                List<Pic> tempList = commentResponse.getData().getPic();
                if(tempList == null || tempList.size() ==0){
                    noinfoview.setVisibility(View.VISIBLE);
                    commentPtprRv.setVisibility(View.GONE);
                    ((GoodsCommentTopFragment)getParentFragment()).setCommentNumPhoto("0");
                    return;
                }else {
                    noinfoview.setVisibility(View.GONE);
                    commentPtprRv.setVisibility(View.VISIBLE);
                    ((GoodsCommentTopFragment)getParentFragment()).setCommentNumPhoto(tempList.size()+"");
                }
                picsList.addAll(tempList);
                picAdapter.setList(picsList);
                picAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View convertView, int position) {
                        PhotoPagerActivity.activityStart(getActivity(),getChangeList(picsList),position);
                    }
                });

            }
        };
        loadCommentImgData();
    }


    @Override
    protected void loadData() {
    }

    private  void loadCommentImgData() {
        L.e(TAG + " loadCommentImgData goodsid:"+goodsid );
        NetworkManager.getItemApi()
                .getCommentPhotos(SPUtils.getToken(), goodsid) //item_id  grade0:全部 1:好评 2:中评 3:差评  page count
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(commentPicObserver);
    }

    private ArrayList<String> getChangeList(List<Pic> picsList){
        ArrayList<String> picStrs = new ArrayList<>();
        for(Pic pic :picsList ){
            picStrs.add(pic.getUrl());
        }
        return picStrs;
    }

}
