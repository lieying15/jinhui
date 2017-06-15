package com.thlh.jhmjmw.business.goods.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Brand;
import com.thlh.baselib.model.FilterSupplier;
import com.thlh.baselib.model.response.FilterBrandResponse;
import com.thlh.baselib.model.response.SupplierResponse;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 筛选页
 */
public class GoodsFilterActivity extends BaseViewActivity {


    private final String TAG = "GoodsFilterActivity";
    private final int ACTIVITY_CODE_BRAND = 1;
    private final int ACTIVITY_CODE_SUPPLIER = 2;

    @BindView(R.id.filter_header)
    HeaderNormal filterHeader;
    @BindView(R.id.filter_start_price_tv)
    EditText minPriceTv;
    @BindView(R.id.filter_end_price_tv)
    EditText maxPriceTv;
    @BindView(R.id.filter_supplier_result_rv)
    EasyRecyclerView filterSupplierResultRv;
    @BindView(R.id.filter_supplier_rv)
    EasyRecyclerView filterSupplierRv;
    @BindView(R.id.filter_brand_result_rv)
    EasyRecyclerView filterBrandResultRv;
    @BindView(R.id.filter_brand_rv)
    EasyRecyclerView filterBrandRv;
    @BindView(R.id.filter_reset_tv)
    TextView filterResetTv;
    @BindView(R.id.filter_submit_tv)
    TextView filterSubmitTv;


    private BaseObserver<FilterBrandResponse> brandOserver;
    private BaseObserver<SupplierResponse> supplierOserver;
    private FilterContentAdapter brandAdapter, supplierAdapter;
    private FilterResultAdapter brandResultAdapter, supplierResultAdapter;

    private List<Boolean> supplierChecks = new ArrayList<>();
    private List<Boolean> brandChecks = new ArrayList<>();
    private List<Brand> brandList;
    private List<FilterSupplier> filterSupplierList;
    private List<Brand> brandSelectList = new ArrayList<>();
    private List<FilterSupplier> filterSupplierSelectList = new ArrayList<>();


    private String keyword = "";
    private String minprice = "";
    private String maxprice = "";
    private String supplerid = "";
    private String brandid = "";
    private String brand = "";


    public static void activityStart(Activity context, int code, String keyword) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsFilterActivity.class);
        intent.putExtra("keyword", keyword);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected void initVariables() {
        keyword = getIntent().getStringExtra("keyword");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        brandAdapter = new FilterContentAdapter(Constants.FILTER_TYPE_BRAND, brandChecks);
        brandAdapter.setEventListener(new FilterContentAdapter.OnClickListener() {
            @Override
            public void onClickMore(View view, int position) {
                GoodsFilterBrandActivity.activityStart(GoodsFilterActivity.this, ACTIVITY_CODE_BRAND);
            }

            @Override
            public void onClickSelect(View view, int position) {
                boolean currentCheck = (boolean) brandChecks.get(position);
                brandChecks.set(position, !currentCheck);
                if (currentCheck) {
                    brandSelectList.remove(brandList.get(position));
                } else {
                    brandSelectList.add(brandList.get(position));
                }
                brandResultAdapter.setList(brandSelectList);
                brandAdapter.notifyDataSetChanged();
            }
        });

        filterBrandRv.setAdapter(brandAdapter);
        filterBrandRv.setLayoutManager(new GridLayoutManager(this, 3));

        supplierAdapter = new FilterContentAdapter(Constants.FILTER_TYPE_SUPPLIER, supplierChecks);
        supplierAdapter.setEventListener(new FilterContentAdapter.OnClickListener() {
            @Override
            public void onClickMore(View view, int position) {
                GoodsFilterSupplierActivity.activityStart(GoodsFilterActivity.this, ACTIVITY_CODE_SUPPLIER);
            }

            @Override
            public void onClickSelect(View view, int position) {
                boolean currentCheck = (boolean) supplierChecks.get(position);
                supplierChecks.set(position, !currentCheck);
                if (currentCheck) {
                    filterSupplierSelectList.remove(filterSupplierList.get(position));
                } else {
                    filterSupplierSelectList.add(filterSupplierList.get(position));
                }
                supplierAdapter.notifyDataSetChanged();
                supplierResultAdapter.setList(filterSupplierSelectList);
            }
        });

        filterSupplierRv.setAdapter(supplierAdapter);
        filterSupplierRv.setLayoutManager(new GridLayoutManager(this, 3));

        brandResultAdapter = new FilterResultAdapter(Constants.FILTER_TYPE_BRAND);
        supplierResultAdapter = new FilterResultAdapter(Constants.FILTER_TYPE_SUPPLIER);
        filterBrandResultRv.setAdapter(brandResultAdapter);
        filterBrandResultRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        filterSupplierResultRv.setAdapter(supplierResultAdapter);
        filterSupplierResultRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));


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

        supplierOserver = new BaseObserver<SupplierResponse>() {
            @Override
            public void onErrorResponse(SupplierResponse supplierResponse) {
            }

            @Override
            public void onNextResponse(SupplierResponse supplierResponse) {
                filterSupplierList = supplierResponse.getData().getSupplier();
                for (int i = 0; i < filterSupplierList.size(); i++) {
                    supplierChecks.add(false);
                }
                supplierAdapter.setList(filterSupplierList);
            }
        };

    }

    @Override
    protected void loadData() {
        loadRelateBrand();
        loadRelateSupplier();
    }

    private void loadRelateBrand() {
        Subscription subscription = NetworkManager.getSearchApi()
                .getRelateBrand(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(brandOserver);
        subscriptionList.add(subscription);
    }


    private void loadRelateSupplier() {
        Subscription subscription = NetworkManager.getSearchApi()
                .getRelateSupplier(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(supplierOserver);
        subscriptionList.add(subscription);
    }

    @OnClick({R.id.filter_reset_tv, R.id.filter_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_reset_tv:
                reSetData();
                break;
            case R.id.filter_submit_tv:
                getFilterData();
                break;
        }
    }

    private void reSetData() {
        minPriceTv.setText("");
        maxPriceTv.setText("");
        minprice = "";
        maxprice = "";
        supplerid = "";
        brandid = "";
        brand = "";
        for (int i = 0; i < brandChecks.size(); i++) {
            brandChecks.set(i, false);
        }
        for (int i = 0; i < brandChecks.size(); i++) {
            supplierChecks.set(i, false);
        }
        brandAdapter.notifyDataSetChanged();
        supplierAdapter.notifyDataSetChanged();
        brandSelectList.clear();
        filterSupplierSelectList.clear();
        brandResultAdapter.notifyDataSetChanged();
        supplierResultAdapter.notifyDataSetChanged();
    }


    private void getFilterData() {
        minprice = minPriceTv.getText().toString();
        maxprice = maxPriceTv.getText().toString();
        supplerid = getSupplerid();
        brandid = getBrindid();
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("brandid", brandid);
        bundle.putString("minprice", minprice);
        bundle.putString("maxprice", maxprice);
        bundle.putString("supplerid", supplerid);
        L.e(TAG + "筛选结果：" + " brandid " + brandid + " minprice " + minprice + " maxprice " + maxprice + " supplerid " + supplerid);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    private String getBrindid() {
        StringBuilder brandbuild = new StringBuilder();
        for (int i = 0; i < brandSelectList.size(); i++) {
            brandbuild.append(brandSelectList.get(i).getBrandid());
            if (i < brandSelectList.size() - 1) {
                brandbuild.append("|");
            }
        }
        return brandbuild.toString();
    }

    private String getSupplerid() {
        StringBuilder supplieridbuild = new StringBuilder();
        for (int i = 0; i < filterSupplierSelectList.size(); i++) {
            supplieridbuild.append(filterSupplierSelectList.get(i).getId());
            if (i < filterSupplierSelectList.size() - 1) {
                supplieridbuild.append("|");
            }
        }
        return supplieridbuild.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_BRAND:
                    ArrayList<Brand> tempbrands = data.getParcelableArrayListExtra("brands");
                    if (tempbrands != null) {
                        brandSelectList.addAll(tempbrands);
                        brandResultAdapter.setList(brandSelectList);
                    }
                    break;
                case ACTIVITY_CODE_SUPPLIER:
                    ArrayList<FilterSupplier> tempsupplier = data.getParcelableArrayListExtra("suppliers");
                    if (tempsupplier != null) {
                        filterSupplierSelectList.addAll(tempsupplier);
                        supplierResultAdapter.setList(filterSupplierSelectList);
                    }
                    break;
            }
        }
    }

}
