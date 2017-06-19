package com.thlh.jhmjmw.business.index.homepage;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.response.HomepageResponse;
import com.thlh.baselib.model.response.SearchResponse;
import com.thlh.baselib.utils.AnimatCartUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class ViewpagerFragment extends BaseFragment {

    private static final String TAG = "ViewpagerFragment";
    @BindView(R.id.homepage_goods_rv)
    RecyclerView homepageGoodsRv;
    Unbinder unbinder;
    private HomePageGoodsAdapter goodsAdapter;
    private GridLayoutManager goodsRvLM;
    private AnimatCartUtils animatCart;
    private int current_page = 1;
    private boolean flag;
    private String  catid;
    private Context context;
    private List<Goods> goodsList = new ArrayList<>();

    private BaseObserver<HomepageResponse> homePageDateObserver;
    private BaseObserver<SearchResponse> searchOserver;
    private int total_page;


    public ViewpagerFragment(Context context) {
        this.context = context;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_viewpager;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initView() {

        animatCart = new AnimatCartUtils(getActivity(), ((IndexActivity) getActivity()).getBottomTabView().getTabShopcartIv());
        goodsAdapter = new HomePageGoodsAdapter(getActivity(),goodsList);


        goodsAdapter.setBuyListener(new HomePageGoodsAdapter.OnClickListener() {
            @Override
            public void onClickBuy(View view, int position, int[] item_location, Drawable drawable) {
                final Goods goods = goodsList.get(position);
                L.e("item_location" + item_location.length + "drawable===" + drawable);
                showAddCartAnimat(item_location,drawable);
                addShopCart(getActivity(),goods);

            }

            @Override
            public void onClickDetails(Goods goods) {
                if (goods.getStorage() > 0  || goods.getIs_prepare().equals("false")) {
                    GoodsDetailV3Activity.activityStart(getActivity(), goods.getItem_id());
                }

            }

            @Override
            public void loadMore() {
                if (flag){
                    current_page ++;
                    loadHomePageData(true,current_page);

                }else{
                    if (current_page >= total_page){
                        Tos.show(getResources().getString(R.string.all_shops));
                    }else {
                        current_page++;
                        loadSearchData(catid,current_page);
                    }
                }
            }
        });
        homepageGoodsRv.setAdapter(goodsAdapter);
        int rVspace = (int) getResources().getDimension(R.dimen.x10);
        homepageGoodsRv.addItemDecoration(new GridSpacingItemDecoration(2, rVspace, false));
        goodsRvLM = new GridLayoutManager(getActivity(), 2);
        homepageGoodsRv.setLayoutManager(goodsRvLM);

    }

    @Override
    protected void initData() {
        if (flag){
            current_page = 1;
            loadHomePageData(true,current_page); loadHomePageData(true,current_page);
        }
    }

    @Override
    protected void loadData() {

    }

    public void loadGoodsData(){
        if (flag){
            current_page = 1;
            loadHomePageData(true,current_page);
        }else {
            current_page = 1;
            loadSearchData(catid,current_page);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addShopCart(Activity activity, Goods goods) {
        // 单品套装
        if (goods.getIs_part() != null && goods.getIs_part().equals("1")) {
            if (goods.getIs_bundling()!= null &&  goods.getPart_is_bundling().equals("1")) {
                GoodsSuitDetailActivity.activityStart(activity, goods.getPart_of_id(), 0);
            } else {
                GoodsDetailV3Activity.activityStart(activity, goods.getPart_of_id());
            }
            return;
        }
        // 整箱套装
        if (goods.getIs_pack().equals("1")) {
            int packNum = goods.getPack_num() == null ? 0 : Integer.parseInt(goods.getPack_num());
            for (int i = 0; i < packNum; i++) {
                DbManager.getInstance().insertCart(goods);
            }
        } else {
            DbManager.getInstance().insertCart(goods);
        }
    }

    public void showAddCartAnimat(int[] item_location, Drawable drawable) {
        animatCart.setAnimationEndEvent(new AnimatCartUtils.OnAnimationEndEvent() {
            @Override
            public void animationEnd() {
                ((IndexActivity) getActivity()).updateShopCartTab();
            }
        });
        animatCart.startShopCartAnim(item_location, drawable);
    }

    /**
     * 主页数据请求 包括  ：轮播图 今日特供，首页的商品
     * @param needBar
     * @param current_page
     *
     */
    public void loadHomePageData(final boolean needBar, int current_page) {
        L.e(TAG + " loadHomePageData");
        homePageDateObserver = new BaseObserver<HomepageResponse>() {
            @Override
            public void onErrorResponse(HomepageResponse homepageBaseResponse) {

            }

            @Override
            public void onNextResponse(HomepageResponse homepageBaseResponse) {
                goodsList.addAll(homepageBaseResponse.getData().getStar());
                goodsAdapter.notifyDataSetChanged();
            }
        };

        NetworkManager.getGoodsDataApi()
                .getHomepageDate(current_page, Constants.PageDataCount)
                .compose(RxUtils.androidSchedulers(this,needBar))
                .subscribe(homePageDateObserver);
    }

    public void loadSearchData(String catid, int current_page) {
        L.e(TAG + " loadSearchData  catid:" + catid +" current_page：" +current_page );
        searchOserver   = new BaseObserver<SearchResponse>() {
            @Override
            public void onErrorResponse(SearchResponse searchResponse) {

            }

            @Override
            public void onNextResponse(SearchResponse searchResponse) {
                total_page = searchResponse.getData().getTotal_page();
                goodsList.addAll(searchResponse.getData().getItem());
                goodsAdapter.notifyDataSetChanged();
            }
        };

        NetworkManager.getSearchApi()
                .getSearch(catid ,current_page , Constants.PageDataCount)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(searchOserver);
    }


    public String getCatid(){
        return catid;
    }

    public void setCatid(String catid){
        goodsList.clear();
        this.catid = catid;
    }

    public boolean getIsHome(){
        return flag;
    }

    public void setIsHome(boolean flag){
        goodsList.clear();
        this.flag = flag;
    }

    public void clearGoodsList(){
        goodsList.clear();
        goodsAdapter.notifyDataSetChanged();
    }


}
