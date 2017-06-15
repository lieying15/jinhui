package com.thlh.jhmjmw.business.index.choiceness;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.ChoiceGoods;
import com.thlh.baselib.model.response.ChoiceResponse;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChoicenessV3Fragment extends BaseFragment {
    private static final String TAG = "ChoicenessFragment";
    @BindView(R.id.choiceness_header)
    HeaderNormal choicenessHeader;

    @BindView(R.id.choiceness_ptpr_rv)
    PullToRefreshRecyclerView choicenessPtprRv;

    private BaseObserver<ChoiceResponse> choiceObserver;

    private ChoicenessAdapter choicenessAdapter;
    private List<ChoiceGoods> choicenessList = new ArrayList<>();
    private EasyRecyclerView choicenessRv;
    private int current_page = 1;

    //静态内部类方法创建单例
    public static class ChoicenessFragmentLoader {
        private static final ChoicenessV3Fragment instance = new ChoicenessV3Fragment();
    }

    public static ChoicenessV3Fragment newInstance() {
        return ChoicenessFragmentLoader.instance;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_choiceness;
    }

    @Override
    protected void initVariables() {

    }


    @Override
    protected void initView() {
        choicenessHeader.setLeftListener(null);
        choicenessHeader.setLeftImage(R.drawable.bg_null);

        choicenessPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        choicenessRv = choicenessPtprRv.getRefreshableView();
        choicenessPtprRv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        choicenessPtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));

        choicenessPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadChoice();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                choicenessPtprRv.onRefreshComplete();
            }
        });

        choicenessAdapter = new ChoicenessAdapter();
        choicenessAdapter.setList(choicenessList);
        choicenessAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                String catid = ((ChoiceGoods) choicenessAdapter.getItem(position)).getItem_id() + "";
                L.e(TAG + " onItemClick catid " + catid);
                GoodsDetailV3Activity.activityStart(getActivity(), catid);
            }
        });
        choicenessRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        choicenessRv.setAdapter(choicenessAdapter);

        choiceObserver = new BaseObserver<ChoiceResponse>() {
            @Override
            public void onNextResponse(ChoiceResponse choiceResponse) {
                choicenessPtprRv.onRefreshComplete();
                choicenessList = choiceResponse.getData().getChoiceGoods();
                choicenessAdapter.setList(choicenessList);
            }

            @Override
            public void onErrorResponse(ChoiceResponse baseResponse) {
                choicenessPtprRv.onRefreshComplete();
            }
        };
        loadChoice();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {

    }

    private void loadChoice() {
        Subscription subscription = NetworkManager.getGoodsDataApi()
                .getChoice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(choiceObserver);
        subscriptionList.add(subscription);
    }


}
