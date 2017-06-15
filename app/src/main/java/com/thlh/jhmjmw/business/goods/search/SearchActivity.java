package com.thlh.jhmjmw.business.goods.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.Seengood;
import com.thlh.baselib.model.response.SearchIndexResponse;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.search.adapter.SearchHotTextAdapter;
import com.thlh.jhmjmw.business.goods.search.adapter.SearchRecommendAdapter;
import com.thlh.jhmjmw.business.goods.search.adapter.SearchSeenAdapter;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.HorizontaltemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  搜索页
 */
public class SearchActivity extends BaseActivity {
    private final String TAG = "SearchActivity";
    private final int ACTIVITY_CODE_SCANNIN = 1;
    @BindView(R.id.search_goods_et)
    EditText searchGoodsEt;
    @BindView(R.id.search_goods_right_fl)
    FrameLayout searchGoodsCancelFl;
    @BindView(R.id.search_goods_text_rv)
    EasyRecyclerView textRv;
    @BindView(R.id.search_goods_recommend_rv)
    EasyRecyclerView recommendRv;
    @BindView(R.id.search_goods_seen_rv)
    EasyRecyclerView seenRv;
    @BindView(R.id.search_header_left_fl)
    FrameLayout searchGoodsFl;
    @BindView(R.id.search_delete_seengoods_ll)
    LinearLayout deleteFootLl;
    @BindView(R.id.search_goods_iv)
    ImageView searchGoodsIv;
    @BindView(R.id.search_title_search_tv)
    TextView searchGoodsTv;
    @BindView(R.id.search_goods_seen_ll)
    LinearLayout searchGoodsSeenLl;

    private SearchRecommendAdapter recommendAdapter;
    private SearchSeenAdapter seenAdapter;
    private SearchHotTextAdapter textAdapter;

    private BaseObserver<SearchIndexResponse> searchIndexObserver;
    private List<Seengood> seenGoodslist = new ArrayList<Seengood>();

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, SearchActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        //文字分类
        LinearLayoutManager textLM = new LinearLayoutManager(textRv.getContext());
        textLM.setOrientation(LinearLayoutManager.HORIZONTAL);
        textRv.setLayoutManager(textLM);
        textAdapter = new SearchHotTextAdapter();
        textAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                SearchResultActivity.activityStart(SearchActivity.this, (String) textAdapter.getItem(position));
            }
        });
        textRv.addItemDecoration(new HorizontaltemDecoration((int) getResources().getDimension(R.dimen.x60)));
        textRv.setAdapter(textAdapter);

        //推荐商品
        GridLayoutManager hotsaleLM = new GridLayoutManager(this, 3);
        recommendRv.setLayoutManager(hotsaleLM);
        recommendAdapter = new SearchRecommendAdapter(this);
        recommendAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(SearchActivity.this, ((Goods) recommendAdapter.getItem(position)).getItem_id());
            }
        });
        recommendRv.setAdapter(recommendAdapter);

        //足迹
        GridLayoutManager seenLM = new GridLayoutManager(this, 3);
        seenAdapter = new SearchSeenAdapter(this);
//        seenGoodslist = DbManager.getInstance().getAllSeenGoods();
//        seenAdapter.setList(seenGoodslist);
        seenAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                String seengoodid = ((Seengood) seenAdapter.getItem(position)).getGoodsdb().getItem_id();
                L.e("点击看过商品 seengoodid:"  + seengoodid );
                GoodsDetailV3Activity.activityStart(SearchActivity.this,seengoodid);
            }
        });
        seenRv.setLayoutManager(seenLM);
        seenRv.setAdapter(seenAdapter);
//        recommendRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));

        searchGoodsEt.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.KEYCODE_ENTER||event.getAction() == KeyEvent.KEYCODE_SEARCH) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_SEARCH:
                            String tempStr = searchGoodsEt.getText().toString().trim();
                            SearchResultActivity.activityStart(SearchActivity.this, tempStr);
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        searchIndexObserver = new BaseObserver<SearchIndexResponse>() {
            @Override
            public void onErrorResponse(SearchIndexResponse searchIndexResponse) {
                progressMaterial.dismiss();
            }

            @Override
            public void onNextResponse(SearchIndexResponse searchIndexResponse) {
                progressMaterial.dismiss();
                textAdapter.setList(searchIndexResponse.getData().getHot_search());
                recommendAdapter.setList(searchIndexResponse.getData().getHot());
            }
        };


    }

    @Override
    protected void loadData() {
        progressMaterial.show();
        subscription = NetworkManager.getSearchApi()
                .getIndex()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchIndexObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        seenGoodslist.clear();
        seenGoodslist = DbManager.getInstance().getAllSeenGoods();
        seenAdapter.setList(seenGoodslist);
        if(seenGoodslist == null ||seenGoodslist.size() == 0){
            deleteFootLl.setVisibility(View.GONE);
            searchGoodsSeenLl.setVisibility(View.GONE);
        }else {
            deleteFootLl.setVisibility(View.VISIBLE);
            searchGoodsSeenLl.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.search_header_left_fl, R.id.search_title_search_tv,R.id.search_goods_iv,R.id.search_delete_seengoods_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_header_left_fl:
//                Intent intent = new Intent();
//                intent.setClass(this, BindDevicesActivity.class);
//                startActivityForResult(intent, ACTIVITY_CODE_SCANNIN);
                finish();
                break;
            case R.id.search_title_search_tv: //取消
//                String tempSearchStr = searchGoodsEt.getText().toString();
//                if(tempSearchStr.equals("")){
//                    finish();
//                }else {
//                    searchGoodsEt.setText("");
//                }
//                break;
//            case R.id.search_goods_iv:
                if (!searchGoodsEt.getText().toString().trim().equals("")) {
                    SearchResultActivity.activityStart(SearchActivity.this, searchGoodsEt.getText().toString().trim());
                }
                break;
            case R.id.search_delete_seengoods_ll:
                DbManager.getInstance().deleteAllSeenGoods();
                seenGoodslist.clear();
                seenAdapter.setList(seenGoodslist);
                deleteFootLl.setVisibility(View.GONE);
                searchGoodsSeenLl.setVisibility(View.GONE);
                break;
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_SCANNIN:
                    String scan_result = data.getStringExtra("scan_result");
                    searchGoodsEt.setText(scan_result);
                    SearchResultActivity.activityStart(SearchActivity.this, scan_result);
                    break;
                default:
                    break;
            }
        }
    }
}
