package com.thlh.jhmjmw.business.goods.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.Brand;
import com.thlh.baselib.model.response.BrandAllResponse;
import com.thlh.baselib.pinyin.CharacterParser;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.BrandComparator;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;
import com.thlh.viewlib.quicksidebar.QuickSideBarTipsView;
import com.thlh.viewlib.quicksidebar.QuickSideBarView;
import com.thlh.viewlib.quicksidebar.listener.OnQuickSideBarTouchListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 筛选品牌页
 */
public class GoodsFilterBrandActivity extends BaseViewActivity implements OnQuickSideBarTouchListener {
    private final String TAG = "GoodsFilterBrandActivity";


    @BindView(R.id.filter_brand_header)
    HeaderNormal filterBrandHeader;
    @BindView(R.id.filter_content_rv)
    EasyRecyclerView filterContentRv;
    @BindView(R.id.filter_sidebar_tips)
    QuickSideBarTipsView filterSidebarTips;
    @BindView(R.id.filter_sidebar)
    QuickSideBarView filterSidebar;


    private EasyDividerItemDecoration dataDecoration;
    private LinearLayoutManager layoutManager;
    private FilterBrandAdapter filterBrandAdapter;
    private BaseObserver<BrandAllResponse> filterOserver;
    private List<Boolean> checkStates = new ArrayList<>();
    private List<Brand> brandList;
    private ArrayList<Brand> brandSelectList = new ArrayList<>();

    private HashMap<String, Integer> letters = new HashMap<>();
    private CharacterParser characterParser;
    private String brandid, brand;


    public static void activityStart(Activity context, int code) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsFilterBrandActivity.class);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected void initVariables() {
        characterParser = CharacterParser.getInstance();

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_filter_content);
        ButterKnife.bind(this);
        //设置监听
        filterSidebar.setOnQuickSideBarTouchListener(this);
        filterBrandHeader.setRightText(getResources().getString(R.string.trues));
        filterBrandHeader.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBrandInfo();
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("brands", brandSelectList);
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );
        filterBrandAdapter = new FilterBrandAdapter(checkStates);
        filterBrandAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                boolean temp = checkStates.get(position);
                checkStates.set(position, !temp);
                filterBrandAdapter.notifyDataSetChanged();
            }
        });

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(filterBrandAdapter);
        filterContentRv.setAdapter(filterBrandAdapter);
        layoutManager = new LinearLayoutManager(this);
        filterContentRv.setLayoutManager(layoutManager);
        filterContentRv.addItemDecoration(headersDecor);

        filterOserver = new BaseObserver<BrandAllResponse>() {
            @Override
            public void onErrorResponse(BrandAllResponse filterBrandResponse) {
            }

            @Override
            public void onNextResponse(BrandAllResponse filterBrandResponse) {
                brandList = changeResponseData(filterBrandResponse);
                checkStates.clear();
                for (int i = 0; i < brandList.size(); i++) {
                    checkStates.add(i, false);
                }
                changeData(brandList);
                filterBrandAdapter.setList(brandList);
            }
        };

    }

    private void changeData(List<Brand> brandList) {
        List<String> customLetters = new ArrayList<>();
        int position = 0;
        for (Brand brand : brandList) {
            String pinyin = characterParser.getSelling(brand.getBrand());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                brand.setSortLetters(sortString);
            } else {
                brand.setSortLetters("#");
            }

            //如果没有这个key则加入并把位置也加入
            if (!letters.containsKey(sortString)) {
                if (sortString.matches("[A-Z]")) {
                    letters.put(sortString, position);
                    customLetters.add(sortString);
                } else {
                    letters.put("#", position);
                    if (!customLetters.contains("#")) {
                        customLetters.add("#");
                    }
                }
            }
            position++;
        }
        Collections.sort(customLetters, Collator.getInstance(Locale.CHINA));
        Collections.sort(brandList, new BrandComparator());
        filterSidebar.setLetters(customLetters);
    }


    @Override
    protected void loadData() {
        subscription = NetworkManager.getSearchApi()
                .getFilterBrand()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(filterOserver);
    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        filterSidebarTips.setText(letter, position, y);
        layoutManager.findFirstVisibleItemPosition();

        //有此key则获取位置并滚动到该位置
        int dataposition = 0;
        for (int i = 0; i < brandList.size(); i++) {
            String sortStr = brandList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == letter.charAt(0)) {
                dataposition = i;
                break;
            }
        }
        filterContentRv.scrollToPosition(dataposition);
    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        filterSidebarTips.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }

    private void getBrandInfo() {
        for (int i = 0; i < checkStates.size(); i++) {
            if (checkStates.get(i)) {
                brandSelectList.add(brandList.get(i));
            }
        }
    }

    private List<Brand> changeResponseData(BrandAllResponse resonse) {
        List<Brand> brandList = new ArrayList<>();
        if (resonse.getData().getBrand().getA() != null) {
            brandList.addAll(resonse.getData().getBrand().getA());
        }
        if (resonse.getData().getBrand().getB() != null) {
            brandList.addAll(resonse.getData().getBrand().getB());
        }
        if (resonse.getData().getBrand().getC() != null) {
            brandList.addAll(resonse.getData().getBrand().getC());
        }
        if (resonse.getData().getBrand().getD() != null) {
            brandList.addAll(resonse.getData().getBrand().getD());
        }
        if (resonse.getData().getBrand().getE() != null) {
            brandList.addAll(resonse.getData().getBrand().getE());
        }
        if (resonse.getData().getBrand().getF() != null) {
            brandList.addAll(resonse.getData().getBrand().getF());
        }
        if (resonse.getData().getBrand().getG() != null) {
            brandList.addAll(resonse.getData().getBrand().getG());
        }
        if (resonse.getData().getBrand().getH() != null) {
            brandList.addAll(resonse.getData().getBrand().getH());
        }
        if (resonse.getData().getBrand().getI() != null) {
            brandList.addAll(resonse.getData().getBrand().getI());
        }
        if (resonse.getData().getBrand().getJ() != null) {
            brandList.addAll(resonse.getData().getBrand().getJ());
        }
        if (resonse.getData().getBrand().getK() != null) {
            brandList.addAll(resonse.getData().getBrand().getK());
        }
        if (resonse.getData().getBrand().getL() != null) {
            brandList.addAll(resonse.getData().getBrand().getL());
        }
        if (resonse.getData().getBrand().getM() != null) {
            brandList.addAll(resonse.getData().getBrand().getM());
        }
        if (resonse.getData().getBrand().getN() != null) {
            brandList.addAll(resonse.getData().getBrand().getN());
        }
        if (resonse.getData().getBrand().getO() != null) {
            brandList.addAll(resonse.getData().getBrand().getO());
        }
        if (resonse.getData().getBrand().getP() != null) {
            brandList.addAll(resonse.getData().getBrand().getP());
        }
        if (resonse.getData().getBrand().getQ() != null) {
            brandList.addAll(resonse.getData().getBrand().getQ());
        }
        if (resonse.getData().getBrand().getR() != null) {
            brandList.addAll(resonse.getData().getBrand().getR());
        }
        if (resonse.getData().getBrand().getS() != null) {
            brandList.addAll(resonse.getData().getBrand().getS());
        }
        if (resonse.getData().getBrand().getT() != null) {
            brandList.addAll(resonse.getData().getBrand().getT());
        }
        if (resonse.getData().getBrand().getU() != null) {
            brandList.addAll(resonse.getData().getBrand().getU());
        }
        if (resonse.getData().getBrand().getV() != null) {
            brandList.addAll(resonse.getData().getBrand().getV());
        }
        if (resonse.getData().getBrand().getW() != null) {
            brandList.addAll(resonse.getData().getBrand().getW());
        }
        if (resonse.getData().getBrand().getX() != null) {
            brandList.addAll(resonse.getData().getBrand().getX());
        }
        if (resonse.getData().getBrand().getY() != null) {
            brandList.addAll(resonse.getData().getBrand().getY());
        }
        if (resonse.getData().getBrand().getZ() != null) {
            brandList.addAll(resonse.getData().getBrand().getZ());
        }
        return brandList;
    }

}
