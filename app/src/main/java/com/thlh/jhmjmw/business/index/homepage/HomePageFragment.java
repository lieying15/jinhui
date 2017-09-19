package com.thlh.jhmjmw.business.index.homepage;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Category;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.HomepageTitleAD;
import com.thlh.baselib.model.response.CategoryTopResponse;
import com.thlh.baselib.model.response.HomepageResponse;
import com.thlh.baselib.model.response.SearchResponse;
import com.thlh.baselib.utils.AnimatCartUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.devices.BindDevicesActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.GestureSwipeRefreshLayout;
import com.thlh.jhmjmw.view.HeaderIndex;
import com.thlh.viewlib.convenientbanner.CBViewHolderCreator;
import com.thlh.viewlib.convenientbanner.ConvenientBanner;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;
import com.thlh.viewlib.tablayout.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomePageFragment extends BaseFragment {
    private static final String TAG = "HomePageFragment";
    private final int BANNER_TURN_TIME = 3000;

    @BindView(R.id.homepage_cl)
    CoordinatorLayout homepageCl;
    @BindView(R.id.homepage_convenientBanner)
    ConvenientBanner homepageConvenientBanner;
    @BindView(R.id.homepage_tabimg_rv)
    EasyRecyclerView tabTopImgRv;
    @BindView(R.id.homepage_header)
    HeaderIndex homepageHeader;
    @BindView(R.id.homepage_swiperl)
    GestureSwipeRefreshLayout homepageSwiperl;
    @BindView(R.id.homepage_scroll)
    NestedScrollView homepageScroll;
    @BindView(R.id.searchresult_totop_fbtn)
    FloatingActionButton gotoTopView;
    @BindView(R.id.homepage_appbarlayout)
    AppBarLayout homepageAppbarlayout;
    @BindView(R.id.homgpage_todaygoods_iv)
    ImageView todaygoodsIv;
    @BindView(R.id.homgpage_todaygoods_cart_iv)
    ImageView todaygoodsCartIv;
    @BindView(R.id.homgpage_todaygoods_name_tv)
    TextView todaygoodsNameTv;
    @BindView(R.id.homgpage_todaygoods_describe_tv)
    TextView todaygoodsDescribeTv;
    @BindView(R.id.homgpage_todaygoods_price_tv)
    TextView todaygoodsPriceTv;
    @BindView(R.id.homgpage_todaygoods_mjz_tv)
    TextView todaygoodsMjzTv;
    @BindView(R.id.homepage_todaygoods_toprl)
    RelativeLayout todaygoodsToprl;
    @BindView(R.id.homepage_tabtext_rv)
    EasyRecyclerView tabTopTextRv;
    @BindView(R.id.homepage_tabtext_ll)
    LinearLayout tabTopTextLl;

    Unbinder unbinder;
    @BindView(R.id.homepage_viewpage_rv)
    ViewPager homepageViewpageRv;
    @BindView(R.id.homgpage_todaygoods_describe_fl)
    FrameLayout homgpageTodaygoodsDescribeFl;
    private HomePageTabAdapter tabAdapter;
    private HomePageTabTextAdapter tabTextAdapter;
    private CBViewHolderCreator<CBPHpHolderView> cbViewHolderCreator;
    private GridLayoutManager goodsRvLM;

    private Handler handler;


    private boolean isSwiperRefresh = false; //是否下拉中
    private boolean isLoadingMore = false; //是否加载更多
    private boolean isShowSearchData = false;//是否显示搜索数据
    private int total_page = 1;
    private int current_page = 1;
    private int itemheight;//adapter item高度
    private SparseArray<Boolean> selectStates = new SparseArray<>(); //存储tap用
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //广告条幅
    private List<HomepageTitleAD> bannerListData = new ArrayList<HomepageTitleAD>();
    private List<Category> categoryTopList = new ArrayList<>();
    private String catid;
    //加入购物车动画
    private AnimatCartUtils animatCart;
    private Goods todayGoods;
    // toptab用
    private List<String> mTitles = new ArrayList<>();
    private boolean isShowTextTab = false;
    private CollapsingToolbarLayoutState state; // Toolbar滚动状态
    private LinearLayoutManager layoutManager;
    private boolean flag = true;

    List<Fragment> list = new ArrayList<>();

    private BaseObserver<CategoryTopResponse> categoryObserver;
    private BaseObserver<HomepageResponse> homePageDateObserver;
    private BaseObserver<SearchResponse> searchOserver;
    private FragAdapter adapter;
    private ViewpagerFragment viewpagerFragment0;
    private ViewpagerFragment viewpagerFragment1;
    private ViewpagerFragment viewpagerFragment2;
    private ViewpagerFragment viewpagerFragment3;
    private ViewpagerFragment viewpagerFragment4;
    private ViewpagerFragment viewpagerFragment5;
    private ViewpagerFragment viewpagerFragment6;
    private ViewpagerFragment viewpagerFragment7;
    private ViewpagerFragment viewpagerFragment8;
    private String url;

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

    private enum CollapsingToolbarLayoutState {
        EXPANDED, //展开
        COLLAPSED, //关闭
        INTERNEDIATE //中间
    }

    //静态内部类方法创建单例
    public static class HomePageFragmentLoader {
        private static final HomePageFragment instance = new HomePageFragment();
    }

    public static HomePageFragment newInstance() {
        return HomePageFragmentLoader.instance;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_homepage_v3;
    }

    @Override
    protected void initView() {
        homepageSwiperl.setEnabled(false);
        viewpagerFragment0 = new ViewpagerFragment(getContext());
        viewpagerFragment1 = new ViewpagerFragment(getContext());
        viewpagerFragment2 = new ViewpagerFragment(getContext());
        viewpagerFragment3 = new ViewpagerFragment(getContext());
        viewpagerFragment4 = new ViewpagerFragment(getContext());
        viewpagerFragment5 = new ViewpagerFragment(getContext());
        viewpagerFragment6 = new ViewpagerFragment(getContext());
        viewpagerFragment7 = new ViewpagerFragment(getContext());
        viewpagerFragment8 = new ViewpagerFragment(getContext());
        list.add(viewpagerFragment0);
        list.add(viewpagerFragment1);
        list.add(viewpagerFragment2);
        list.add(viewpagerFragment3);
        list.add(viewpagerFragment4);
        list.add(viewpagerFragment5);
        list.add(viewpagerFragment6);
        list.add(viewpagerFragment7);
        list.add(viewpagerFragment8);

        adapter = new FragAdapter(getActivity().getSupportFragmentManager(), list);
        homepageViewpageRv.setAdapter(adapter);
        homepageViewpageRv.setOffscreenPageLimit(9);
        homepageViewpageRv.setCurrentItem(0);
        ((ViewpagerFragment) list.get(0)).setIsHome(true);

        animatCart = new AnimatCartUtils(getActivity(), ((IndexActivity) getActivity()).getBottomTabView().getTabShopcartIv());
        homepageHeader.setActivityContext(getActivity());
        homepageHeader.setOnClickScanListener(new HeaderIndex.OnClickScanListener() {
            @Override
            public void onClick(View view) {
                startZxingActivity();
            }
        });

        // Appbarlayout出现才能下拉，解决卡Appbarlayout问题
        homepageAppbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < 0) {
                    homepageSwiperl.setEnabled(false);
                }
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        gotoTopView.hide();
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        tabTopTextLl.setVisibility(View.VISIBLE);//隐藏播放按钮
                        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
                        aa.setDuration(300);
                        //展示图片渐变动画
                        tabTopTextLl.startAnimation(aa);
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        gotoTopView.show();
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            AlphaAnimation aa = new AlphaAnimation(1.0f, 0.1f);
                            aa.setDuration(300);
                            //展示图片渐变动画
                            tabTopTextLl.startAnimation(aa);
                            tabTopTextLl.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮

                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                        gotoTopView.hide();
                    }
                }
            }
        });

        //回置顶按钮的显示
        homepageScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    homepageSwiperl.setEnabled(true);
                } else {
                    homepageSwiperl.setEnabled(false);
                    L.e("oldScrollY====" + oldScrollY);
                    if (oldScrollY > BaseApplication.height / 3) {
                        gotoTopView.show();
                    } else {
                        gotoTopView.hide();
                    }
                }

//                if(scrollY >(homepageConvenientBanner.getHeight() + tabTopImgRv.getHeight())){
//                    isShowTextTab = true;
//                }else {
//                    isShowTextTab = false;
//                }

               /* int totalItemCount = goodsRvLM.getItemCount();
                int itemHeightY = itemheight * ((totalItemCount - 5) / 2);
                int lastVisibleItem = ((GridLayoutManager) goodsRvLM).findLastVisibleItemPosition();

                L.e("current_page:" + current_page + " totalItemCount:" + totalItemCount + " total_page:" + total_page + "  lastVisibleItem" + lastVisibleItem + " itemHeightY" + itemHeightY + " scrollY " + scrollY);
                if (scrollY <= itemHeightY) return;
                if (lastVisibleItem >= totalItemCount - 4 && scrollY > 0) {
                    isLoadingMore = true;
                    L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " scrollY" + scrollY);
                    L.e("加载更多数据 current_page" + current_page);
                    current_page++;
                    if (isShowSearchData) {
                        loadHomePageData(true,is_mjb);
                    } else {
                        ((ViewpagerFragment)list.get(0)).loadGoodsData();//这里多线程也要手动控制isLoadingMore
                    }
                } else {
                    L.e(">>>:1");
                }*/
            }
        });

        homepageSwiperl.setOnRefreshListener(new GestureSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                L.d(TAG + " homepageSwiperl  onRefresh");
                homepageSwiperl.setRefreshing(true);
                isSwiperRefresh = true;
                updateHomePage();
            }
        });

        homepageSwiperl.setColorSchemeColors(getResources().getColor(R.color.app_theme));
        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                getActivity(),
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );
        dataDecoration.setVerticalpaddingInPx(R.dimen.x35);
        //图片标签
        final GridLayoutManager tabLM = new GridLayoutManager(getActivity(), 4);
        tabTopImgRv.setLayoutManager(tabLM);
        tabTopImgRv.setNestedScrollingEnabled(false);
        tabAdapter = new HomePageTabAdapter(selectStates);
        tabAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                L.e(TAG + " tabAdapter onItemClick");
                updateTopTab(position);
            }
        });
        tabTopImgRv.setAdapter(tabAdapter);

        //文字标签
        GridLayoutManager tabTextLM = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        tabTopTextRv.setLayoutManager(tabTextLM);
        tabTextAdapter = new HomePageTabTextAdapter(selectStates);
        tabTextAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                L.e(TAG + " tabTextAdapter onItemClick");
                updateTopTab(position);
            }
        });
        tabTopTextRv.addItemDecoration(new HorizontaltemDecoration((int) getResources().getDimension(R.dimen.x10)));
        tabTopTextRv.setAdapter(tabTextAdapter);


        //广告栏
        cbViewHolderCreator = new CBViewHolderCreator<CBPHpHolderView>() {
            @Override
            public CBPHpHolderView createHolder() {
                return new CBPHpHolderView(getActivity());
            }
        };
        homepageConvenientBanner.setPages(cbViewHolderCreator, new ArrayList<HomepageTitleAD>());
        homepageConvenientBanner.setPageIndicator(new int[]{R.drawable.shap_ring_black, R.drawable.shap_ring_white});
        homepageConvenientBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        homepageSwiperl.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        homepageSwiperl.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        homepageViewpageRv.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    ((ViewpagerFragment) list.get(position)).setIsHome(false);
                    updateTopTab(position - 1);
//                    ((ViewpagerFragment) list.get(homepageViewpageRv.getCurrentItem())).clearGoodsList();
//                    ((ViewpagerFragment) list.get(homepageViewpageRv.getCurrentItem())).setCatid(catid);
//                    ((ViewpagerFragment) list.get(homepageViewpageRv.getCurrentItem())).loadGoodsData();
                }
                if (position == 0){
                    ((ViewpagerFragment) list.get(position)).setIsHome(true);
                    for (int i = 0; i < selectStates.size(); i++) {
                        selectStates.setValueAt(i, false);
                    }
                    ((ViewpagerFragment) list.get(homepageViewpageRv.getCurrentItem())).loadGoodsData();
                    tabAdapter.setSelectStates(selectStates);
                    tabTextAdapter.setSelectStates(selectStates);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {
//        loadCategoryData(true);
//        loadHomePageData(true,is_mjb);
    }

    @Override
    protected void initVariables() {
        //goodsAdapter中每项的高度
        itemheight = (int) getResources().getDimension(R.dimen.y480);
    }


    @Override
    protected void loadData() {
//        showProgressBar();
        loadCategoryData(true);
        loadHomePageData(true);
    }

    public void initTopTab(List<Category> categoryTopList) {
        selectStates.clear();
        for (int i = 0; i < categoryTopList.size(); i++) {
            selectStates.put(i, false);
            mTitles.add(categoryTopList.get(i).getCatname());

        }
        tabTextAdapter.setList(categoryTopList);
        tabAdapter.setList(categoryTopList);
        tabTopImgRv.setVisibility(View.VISIBLE);

    }


    public void updateTopTab(int position) {

        catid = categoryTopList.get(position).getCatid();
        ((ViewpagerFragment) list.get(homepageViewpageRv.getCurrentItem())).setCatid(catid);
        L.e(TAG + " catid====" + catid);
        for (int i = 0; i < selectStates.size(); i++) {
            selectStates.setValueAt(i, false);
        }
        selectStates.setValueAt(position, true);
        tabAdapter.setSelectStates(selectStates);
        tabTextAdapter.setSelectStates(selectStates);
        homepageViewpageRv.setCurrentItem(position + 1);
        ((ViewpagerFragment) list.get(homepageViewpageRv.getCurrentItem())). loadSearchData(catid,1);
    }

    public void gotoTop() {
        homepageAppbarlayout.setExpanded(true);
        homepageScroll.fullScroll(ScrollView.FOCUS_UP);
        gotoTopView.setVisibility(View.INVISIBLE);
    }


    public void gotoBanner() {
        final int height = (int) getResources().getDimension(R.dimen.y570);
        L.e(TAG + " gotoBanner isShowTextTab " + isShowTextTab + " height:" + height);
        Runnable runnable = new Runnable() {
            public void run() {
//                homepageAppbarlayout.scrollTo(0,homepageConvenientBanner.getHeight());
//                homepageScroll.smoothScrollTo(0, height+1000);
                homepageScroll.fullScroll(ScrollView.FOCUS_UP);
//                homepageScroll.smoothScrollTo(0, 0);
//                homepageAppbarlayout.scrollBy(0,);
//                homepageScroll.scrollTo(0, height);
//                homepageCl.scrollTo(0,height);
            }
        };
        handler = new Handler();
        handler.post(runnable);
    }


    public void updateBanner(List<HomepageTitleAD> bannerListData) {
        homepageConvenientBanner.setPages(cbViewHolderCreator, bannerListData);
        homepageConvenientBanner.notifyDataSetChanged();
        homepageConvenientBanner.startTurning(BANNER_TURN_TIME);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasBannerDate()) {
            homepageConvenientBanner.startTurning(BANNER_TURN_TIME);
        }
    }

    @OnClick({R.id.homgpage_todaygoods_cart_iv, R.id.homepage_todaygoods_toprl, R.id.searchresult_totop_fbtn})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.homgpage_todaygoods_cart_iv:
                int[] item_location = new int[2];
                addShopCart(getActivity(), todayGoods);
                todaygoodsIv.getLocationInWindow(item_location);
                Drawable drawable5 = todaygoodsIv.getDrawable();
                showAddCartAnimat(item_location, drawable5);
                break;
            case R.id.homepage_todaygoods_toprl:
                if (todayGoods != null)
                    GoodsDetailV3Activity.activityStart(getActivity(), todayGoods.getItem_id());
                break;
            case R.id.searchresult_totop_fbtn:
                gotoTop();
                break;
        }
    }

    public void showTodayGoods(Goods goods) {
        todaygoodsToprl.setVisibility(View.VISIBLE);
        todayGoods = goods;

        if (goods.getItem_img_thumb().contains("http")){
            url = goods.getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + goods.getItem_img_thumb();
        }
        ImageLoader.display(url, todaygoodsIv);
        todaygoodsNameTv.setText(goods.getItem_name());

        todaygoodsDescribeTv.setText(goods.getItem_subtitle());

        String priceStr = goods.getItem_price();
        todaygoodsPriceTv.setText(getResources().getString(R.string.money_) + priceStr);
        if (goods.getIs_mjb().equals("0")) {
            todaygoodsMjzTv.setVisibility(View.GONE);
        } else {
            todaygoodsMjzTv.setVisibility(View.VISIBLE);
            String mjzStr = goods.getItem_price();
            if (goods.getIs_mjb().equals("2"))
                mjzStr = goods.getMjb_value();
            SpannableString spannableString = new SpannableString(getString(R.string.use_mjz) + mjzStr);
            Drawable drawable = getResources().getDrawable(R.drawable.icon_mjz);
            drawable.setBounds(0, 0, (int) getResources().getDimension(R.dimen.icon_mjz_x), (int) getResources().getDimension(R.dimen.icon_mjz_y));
            ImageSpan imageSpan = new ImageSpan(drawable);
            spannableString.setSpan(imageSpan, 3, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            todaygoodsMjzTv.setText(spannableString);
        }
    }


    public void hideTodayGoods() {
        todaygoodsToprl.setVisibility(View.GONE);
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


    public void setGoodsDatePage(int page) {
        current_page = page;

    }


    public int getGoodsDatePage() {
        return current_page;
    }


    public int getGoodsDateTotalPage() {
        return total_page;
    }


    public void setGoodsDateTotalPage(int page) {
        total_page = page;
    }


    public void stopBanner() {
        //停止翻页
        homepageConvenientBanner.stopTurning();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopBanner();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void startZxingActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BindDevicesActivity.class);
        getActivity().startActivityForResult(intent, Constants.ACTIVITYCODE_SCANNQR);
    }


    /**
     * 主页数据请求 包括  ：轮播图 今日特供，首页的商品
     *
     * @param needBar
     */

    public void loadHomePageData(final boolean needBar) {
        L.e(TAG + " loadHomePageData");
        homePageDateObserver = new BaseObserver<HomepageResponse>() {
            @Override
            public void onErrorResponse(HomepageResponse homepageBaseResponse) {

            }

            @Override
            public void onNextResponse(HomepageResponse homepageBaseResponse) {
                bannerListData.clear();
                bannerListData.addAll(homepageBaseResponse.getData().getTitle());
                updateBanner(bannerListData);
                if (homepageBaseResponse.getData().getToday().size() > 0) {
                    showTodayGoods(homepageBaseResponse.getData().getToday().get(0));
                } else {
                    hideTodayGoods();
                }
            }
        };

        NetworkManager.getGoodsDataApi()
                .getHomepageDate(current_page, Constants.PageDataCount)
                .compose(RxUtils.androidSchedulers(this, needBar))
                .subscribe(homePageDateObserver);
    }

    /**
     * 请求分类
     *
     * @param needBar
     */
    public void loadCategoryData(final boolean needBar) {
        categoryObserver = new BaseObserver<CategoryTopResponse>() {
            @Override
            public void onErrorResponse(CategoryTopResponse categoryTopResponse) {
            }

            @Override
            public void onNextResponse(CategoryTopResponse categoryTopResponse) {
                categoryTopList.addAll(categoryTopResponse.getData().getTop());
                initTopTab(categoryTopList);
                loadHomePageData(needBar);
            }
        };
        NetworkManager.getGoodsDataApi()
                .getCategoryTop()
                .compose(RxUtils.androidSchedulers(this, needBar))
                .subscribe(categoryObserver);
    }

    public void updateHomePage() {
        categoryTopList.clear();
        loadCategoryData(false);
        gotoTop();
        setGoodsDatePage(1);
        homepageViewpageRv.setCurrentItem(0);

    }


    public boolean hasBannerDate() {
        if (bannerListData != null && bannerListData.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void addShopCart(Activity activity, Goods goods) {
        // 单品套装
        if (goods.getIs_part().equals("1")) {
            if (goods.getPart_is_bundling().equals("1")) {
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


}
