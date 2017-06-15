package com.thlh.jhmjmw.business.goods.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Brand;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.response.FilterBrandResponse;
import com.thlh.baselib.model.response.SearchResponse;
import com.thlh.baselib.model.response.SimilarResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.filter.FilterContentAdapter;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.goods.search.adapter.GoodsNormalGridAdapter;
import com.thlh.jhmjmw.business.goods.search.adapter.GoodsNormalListAdapter;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;
import com.thlh.viewlib.expandableLayout.ExpandableLinearLayout;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;
import com.thlh.viewlib.recyclerview.DividerGridItemDecoration;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索结果商品页
 */
public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "SearchResultActivity";
    private final int ACTIVITY_CODE_FILTER = 1;


    @BindView(R.id.search_header_left_fl)
    FrameLayout selectGoodsBackFl;
    @BindView(R.id.search_goods_et)
    EditText selectGoodsEt;
    @BindView(R.id.search_title_search_tv)
    TextView selectGoodsSeachTv;

    @BindView(R.id.searchresult_ptpr_rv)
    PullToRefreshRecyclerView selectGoodsPtprRv;
    @BindView(R.id.searchresult_noinfoview)
    NoInfoView selectGoodsNoinfoview;
    @BindView(R.id.searchresult_totop_fbtn)
    FloatingActionButton toTopView;
    @BindView(R.id.search_goods_right_fl)
    FrameLayout searchGoodsCancelFl;
    @BindView(R.id.sortbar_title_synthesize_ll)
    LinearLayout sortbarTitleSynthesizeLl;
    @BindView(R.id.sortbar_title_priority_ll)
    LinearLayout sortbarTitlePriorityLl;
    @BindView(R.id.sortbar_title_filter_ll)
    LinearLayout sortbarTitleFilterLl;
    @BindView(R.id.explayout_synthesize_tv)
    TextView explayoutSynthesizeTv;
    @BindView(R.id.explayout_synthesize_fl)
    FrameLayout explayoutSynthesizeFl;
    @BindView(R.id.explayout_synthesize_htol_tv)
    TextView explayoutSynthesizeHtolTv;
    @BindView(R.id.explayout_synthesize_htol_fl)
    FrameLayout explayoutSynthesizeHtolFl;
    @BindView(R.id.explayout_synthesize_ltoh_tv)
    TextView explayoutSynthesizeLtohTv;
    @BindView(R.id.explayout_synthesize_ltoh_fl)
    FrameLayout explayoutSynthesizeLtohFl;
    @BindView(R.id.explayout_synthesize_credit_tv)
    TextView explayoutSynthesizeCreditTv;
    @BindView(R.id.explayout_synthesize_credit_fl)
    FrameLayout explayoutSynthesizeCreditFl;
    @BindView(R.id.searchresult_synthesize_expandableLayout)
    ExpandableLinearLayout expLayout;


    @BindView(R.id.filter_title_ll)
    LinearLayout filterLl;
    @BindView(R.id.filter_brand_rv)
    EasyRecyclerView filterBrandRv;
    @BindView(R.id.filter_start_price_tv)
    EditText minPriceTv;
    @BindView(R.id.filter_end_price_tv)
    EditText maxPriceTv;
    @BindView(R.id.filter_sure_tv)
    TextView filterSureTv;

    private GoodsNormalListAdapter goodsListAdapter;
    private GoodsNormalGridAdapter goodsGridAdapter;
    private EasyDividerItemDecoration dataDecoration;
    private DividerGridItemDecoration gridDecoration;
    private FilterContentAdapter brandAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private int searchType;
    private String searchContent;
   
    private int total_page = 1;
    private int current_page = 1;
    private boolean showGirdLayout = false;

    private EasyRecyclerView selectGoodsRv;
    private LinearLayoutManager mLinearLM;
    private GridLayoutManager mGridLM;
    private boolean isLoadingMore = false;
    private BaseObserver<SearchResponse> searchOserver;
    private BaseObserver<SimilarResponse> similarOserver;
    private BaseObserver<FilterBrandResponse> brandOserver;
    private SweetAlertDialog cartDialog;

    //筛选
    private List<Boolean> brandChecks = new ArrayList<>();
    private List<Brand> brandList;
    private List<Brand> brandSelectList = new ArrayList<>();
    private String brandid = "";
    private String sort;
    private String minprice = "";
    private String maxprice = "";
    private String supplerid = "";

    public static void activityStart(Context context, String searchContent) {
        Intent intent = new Intent();
        intent.setClass(context, SearchResultActivity.class);
        intent.putExtra("searchContent", searchContent);
        context.startActivity(intent);
    }

    public static void activityStart(Activity context, String searchContent) {
        Intent intent = new Intent();
        intent.setClass(context, SearchResultActivity.class);
        intent.putExtra("searchContent", searchContent);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    public static void activityStart(Activity context, int searchType, String searchContent) {
        Intent intent = new Intent();
        intent.setClass(context, SearchResultActivity.class);
        intent.putExtra("searchType", searchType);
        intent.putExtra("searchContent", searchContent);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        searchType = getIntent().getIntExtra("searchType", 0);
        searchContent = getIntent().getStringExtra("searchContent");

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_searchresult);
        ButterKnife.bind(this);
      //  selectGoodsNoinfoview.setTitle("抱歉，没有找到与\"" + searchContent + "\"相关的商品，\n您可以换个词试试");
        selectGoodsNoinfoview.setTitle(getResources().getString(R.string.sorry_no_find) + searchContent + getResources().getString(R.string.about_shop_try));
        selectGoodsNoinfoview.setTitleIv(R.drawable.img_dialog_search);
        selectGoodsPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        selectGoodsRv = selectGoodsPtprRv.getRefreshableView();
        selectGoodsEt.setText(searchContent);
        filterLl.setVisibility(View.GONE);
        dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );

        gridDecoration = new DividerGridItemDecoration(this);
        goodsListAdapter = new GoodsNormalListAdapter(this);
        goodsGridAdapter = new GoodsNormalGridAdapter(this);
        goodsListAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(SearchResultActivity.this, ((Goods) goodsListAdapter.getItem(position)).getItem_id());
            }
        });

        brandAdapter = new FilterContentAdapter(Constants.FILTER_TYPE_BRAND, brandChecks);
        brandAdapter.setEventListener(new FilterContentAdapter.OnClickListener() {
            @Override
            public void onClickMore(View view, int position) {}

            @Override
            public void onClickSelect(View view, int position) {
                boolean currentCheck = (boolean) brandChecks.get(position);
                brandChecks.set(position, !currentCheck);
                if (currentCheck) {
                    brandSelectList.remove(brandList.get(position));
                } else {
                    brandSelectList.add(brandList.get(position));
                }
                brandAdapter.notifyDataSetChanged();
            }
        });
        filterBrandRv.setAdapter(brandAdapter);
        filterBrandRv.setLayoutManager(new GridLayoutManager(this, 3));

//        goodsListAdapter.setOnClickEvent(new GoodsNormalListAdapter.OnClickListener() {
//            @Override
//            public void onClickCart(int position) {
//                Goods goods = goodsList.get(position);
//                // 单品套装
//                if (goods.getIs_part().equals("1")) {
//                    if (goods.getPart_is_bundling().equals("1")) {
//                        GoodsSuitDetailActivity.activityStart(SearchResultActivity.this, goods.getPart_of_id(), 0);
//                    } else {
//                        GoodsDetailV3Activity.activityStart(SearchResultActivity.this, goods.getPart_of_id());
//                    }
//                    return;
//                }
//
//                // 整箱套装
//                if (goods.getIs_pack().equals("1")) {
//                    int packNum = goods.getPack_num() == null ? 0 : Integer.parseInt(goods.getPack_num());
//                    for (int i = 0; i < packNum; i++) {
//                        DbManager.getInstance().insertCart(goods);
//                    }
//                } else {
//                    DbManager.getInstance().insertCart(goods);
//                }
//                cartDialog = new SweetAlertDialog(SearchResultActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//                cartDialog.setTitleText("已加入购物车").show();
//            }
//        });
        goodsGridAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(SearchResultActivity.this, ((Goods) goodsGridAdapter.getItem(position)).getItem_id());
            }
        });
        goodsGridAdapter.setOnClickEvent(new GoodsNormalGridAdapter.OnClickListener() {
            @Override
            public void onClickCart(int position) {
                Goods goods = goodsList.get(position);
                // 单品套装
                if (goods.getIs_part().equals("1")) {
                    if (goods.getPart_is_bundling().equals("1")) {
                        GoodsSuitDetailActivity.activityStart(SearchResultActivity.this, goods.getPart_of_id(), 0);
                    } else {
                        GoodsDetailV3Activity.activityStart(SearchResultActivity.this, goods.getPart_of_id());
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
                cartDialog = new SweetAlertDialog(SearchResultActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                cartDialog.setTitleText(getResources().getString(R.string.add_car)).show();
            }
        });
        mLinearLM = new LinearLayoutManager(this);
        mGridLM = new GridLayoutManager(this, 2);
        setListRv();
        selectGoodsPtprRv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        selectGoodsPtprRv.setHeaderLayout(new PtorHeaderLayout(this));

        selectGoodsPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                goodsListAdapter.setLoadOver(false);
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                selectGoodsPtprRv.onRefreshComplete();
            }
        });


        searchOserver = new BaseObserver<SearchResponse>() {
            @Override
            public void onErrorResponse(SearchResponse searchResponse) {
                selectGoodsPtprRv.onRefreshComplete();
                isLoadingMore = false;
            }

            @Override
            public void onNextResponse(SearchResponse searchResponse) {
                total_page = searchResponse.getData().getTotal_page();
                selectGoodsPtprRv.onRefreshComplete();
                goodsList = searchResponse.getData().getItem();
                if (isLoadingMore) {
                    if (goodsList == null || goodsList.size() == 0) {
                        goodsListAdapter.setLoadOver(true);
                        goodsListAdapter.notifyDataSetChanged();
                    } else {
                        goodsListAdapter.setList(goodsList, true);
                        goodsGridAdapter.setList(goodsList, true);
                    }
                    isLoadingMore = false;
                } else {
                    if (goodsList == null || goodsList.size() == 0) {
                        selectGoodsNoinfoview.setVisibility(View.VISIBLE);
                        selectGoodsNoinfoview.setTitle(getResources().getString(R.string.sorry_no_find) + searchContent + getResources().getString(R.string.about_shop_try));
                        selectGoodsPtprRv.setVisibility(View.GONE);
                        loadRecommendGoods();
                    } else {
                        if (searchResponse.getData().getCurrent_page() == searchResponse.getData().getTotal_page()) {
                            goodsListAdapter.setLoadOver(true);
                        }
                        selectGoodsNoinfoview.setVisibility(View.GONE);
                        selectGoodsPtprRv.setVisibility(View.VISIBLE);
                        goodsListAdapter.setList(searchResponse.getData().getItem());
                        goodsGridAdapter.setList(searchResponse.getData().getItem());
                    }
                }
                reSetFilterCondition();
            }
        };

        selectGoodsEt.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (event.getAction() == KeyEvent.KEYCODE_ENTER||event.getAction() == KeyEvent.KEYCODE_SEARCH) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_SEARCH:
                            searchContent = selectGoodsEt.getText().toString().trim();
                            loadData();
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });



        similarOserver = new BaseObserver<SimilarResponse>() {
            @Override
            public void onErrorResponse(SimilarResponse similarResponse) {

            }

            @Override
            public void onNextResponse(SimilarResponse similarResponse) {
                List<Goods> tempdate = similarResponse.getData().getItems();
                selectGoodsNoinfoview.setList(tempdate);
            }
        };

        brandOserver = new BaseObserver<FilterBrandResponse>() {
            @Override
            public void onErrorResponse(FilterBrandResponse filterBrandResponse) {
            }

            @Override
            public void onNextResponse(FilterBrandResponse filterBrandResponse) {
                brandList = filterBrandResponse.getData().getBrand();
                for (int i = 0; i < brandList.size(); i++) {
                    brandChecks.add(false);
                }
                brandAdapter.setList(brandList);
            }
        };
    }

    @Override
    protected void loadData() {
        loadRelateBrand();
        loadSearchGoods();
    }

    private void loadSearchGoods(){
        L.i(TAG + " searchContent  keyword" + searchContent + " sort " + sort + " brandid " + brandid + " supplier " + supplerid + " minprice " + minprice + " maxprice" + maxprice + " current_page " + current_page);
        NetworkManager.getSearchApi()
                .getSearch(searchContent, sort, brandid, supplerid, minprice, maxprice, current_page, Constants.PageDataCount)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(searchOserver);
    }

    private void reLoadSearchGoods(){
        current_page = 1;
        loadSearchGoods();
        expLayout.collapse();
    }
    
    @OnClick({R.id.search_header_left_fl,R.id.search_goods_right_fl,  R.id.search_title_search_tv,
            R.id.sortbar_title_synthesize_ll, R.id.sortbar_title_priority_ll,R.id.sortbar_title_filter_ll,
            R.id.explayout_synthesize_fl, R.id.explayout_synthesize_htol_fl,R.id.filter_sure_tv,
            R.id.explayout_synthesize_ltoh_fl, R.id.explayout_synthesize_credit_fl, R.id.searchresult_totop_fbtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_header_left_fl:
                finish();
                break;
            case R.id.search_goods_right_fl:
                break;
            case R.id.search_title_search_tv:
                if (!selectGoodsEt.getText().toString().equals("")) {
                    searchContent = selectGoodsEt.getText().toString();
                    loadData();
                }
                break;
            case R.id.sortbar_title_synthesize_ll:
                expLayout.toggle();
                filterLl.setVisibility(View.GONE);
                break;
            case R.id.sortbar_title_priority_ll:
                sort = "sales";
                reLoadSearchGoods();
                filterLl.setVisibility(View.GONE);

                break;
            case R.id.sortbar_title_filter_ll:
                filterLl.setVisibility(View.VISIBLE);

                break;
            case R.id.explayout_synthesize_fl:
                sort = "";
                reLoadSearchGoods();
                break;
            case R.id.explayout_synthesize_htol_fl:
                sort = "down";
                reLoadSearchGoods();

                break;
            case R.id.explayout_synthesize_ltoh_fl:
                sort = "up";
                reLoadSearchGoods();

                break;
            case R.id.explayout_synthesize_credit_fl:
                sort = "";
                reLoadSearchGoods();
                break;

            case R.id.searchresult_totop_fbtn:
                gotoTop();
                break;
            /*
            * 筛选*/
            case R.id.filter_sure_tv:
                updateFilterCondition();
                filterLl.setVisibility(View.GONE);
                reLoadSearchGoods();
                break;
        }
    }


    private void setGridRv() {
        selectGoodsRv.setLayoutManager(mGridLM);
        selectGoodsRv.removeItemDecoration(dataDecoration);
        selectGoodsRv.addItemDecoration(gridDecoration);
        selectGoodsRv.setAdapter(goodsGridAdapter);
        selectGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstposition = ((GridLayoutManager) mGridLM).findFirstVisibleItemPosition();
                if (firstposition > 8) {
                    toTopView.show();
                } else {
                    toTopView.hide();
                }
                if (isLoadingMore) return;
                if (current_page >= total_page) return;
                int lastVisibleItem = ((GridLayoutManager) mGridLM).findLastVisibleItemPosition();
                int totalItemCount = mLinearLM.getItemCount();
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

    private void setListRv() {
        selectGoodsRv.setLayoutManager(mLinearLM);
        selectGoodsRv.setAdapter(goodsListAdapter);
        selectGoodsRv.removeItemDecoration(gridDecoration);
        selectGoodsRv.addItemDecoration(new VerticalltemDecoration((int) getResources().getDimension(R.dimen.y10)));
        selectGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstposition = ((LinearLayoutManager) mLinearLM).findFirstVisibleItemPosition();
                if (firstposition > 5) {
                    toTopView.show();
                } else {
                    toTopView.hide();
                }
                if (isLoadingMore) return;
                if (current_page >= total_page) {
                    if (!goodsListAdapter.isLoadOver()) {
                        goodsListAdapter.setLoadOver(true);
                        goodsListAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                int lastVisibleItem = ((LinearLayoutManager) mLinearLM).findLastVisibleItemPosition();

                int totalItemCount = mLinearLM.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载， dy>0 表示向下滑动
                L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " dy" + dy);

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

    private void loadRecommendGoods() {
        L.i(TAG + "loadRecommendGoods");
        NetworkManager.getGoodsDataApi()
                .getSimilarGoods(Constants.SimilarDataCount)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(similarOserver);
    }

    private void gotoTop() {
        if (showGirdLayout) {
            if (goodsGridAdapter.getItemCount() >= 30) {
                selectGoodsRv.scrollToPosition(0);
            } else {
                selectGoodsRv.smoothScrollToPosition(RecyclerView.SCROLL_INDICATOR_TOP);
            }
        } else {
            if (goodsListAdapter.getItemCount() >= 30) {
                selectGoodsRv.scrollToPosition(0);
            } else {
                selectGoodsRv.smoothScrollToPosition(RecyclerView.SCROLL_INDICATOR_TOP);
            }
        }
        toTopView.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_FILTER:
                    brandid = data.getStringExtra("brandid");
                    minprice = data.getStringExtra("minprice");
                    maxprice = data.getStringExtra("maxprice");
                    supplerid = data.getStringExtra("supplerid");
//                    selectGoodsEt.setText("");
                    sort = "";
//                    selectGoodsBar.setSortTypeText("综合排序");
//                    searchContent = "";
                    current_page = 1;
                    loadData();
                    break;

                default:
                    break;
            }
        }
    }



    //=========筛选
    private void loadRelateBrand() {
        NetworkManager.getSearchApi()
                .getRelateBrand(searchContent)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(brandOserver);
    }
    
    private void reSetFilterCondition() {
        minPriceTv.setText("");
        maxPriceTv.setText("");
        minprice = "";
        maxprice = "";
        brandid = "";
        for (int i = 0; i < brandChecks.size(); i++) {
            brandChecks.set(i, false);
        }
        brandAdapter.notifyDataSetChanged();
        brandSelectList.clear();
    }
    private void updateFilterCondition() {
        minprice = minPriceTv.getText().toString().trim();
        maxprice = maxPriceTv.getText().toString().trim();
        StringBuilder brandbuild = new StringBuilder();
        for (int i = 0; i < brandSelectList.size(); i++) {
            brandbuild.append(brandSelectList.get(i).getBrandid());
            if (i < brandSelectList.size() - 1) {
                brandbuild.append("|");
            }
        }
        brandid = brandbuild.toString();
    }
}
