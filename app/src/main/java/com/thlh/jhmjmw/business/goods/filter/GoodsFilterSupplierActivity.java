package com.thlh.jhmjmw.business.goods.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.model.FilterSupplier;
import com.thlh.baselib.model.response.SupplierAllResonse;
import com.thlh.baselib.pinyin.CharacterParser;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.SupplierComparator;
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
 * 筛选店铺页
 */
public class GoodsFilterSupplierActivity extends BaseViewActivity implements OnQuickSideBarTouchListener {
    private final String TAG = "GoodsFilterSupplierActivity";

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
    private FilterSupplierAdapter supplierAdapter;
    private BaseObserver<SupplierAllResonse> filterOserver;
    private List<Boolean> checkStates = new ArrayList<>();
    private List<FilterSupplier> filterSupplierList;
    private ArrayList<FilterSupplier> filterSupplierSelectList = new ArrayList<>();
    private HashMap<String, Integer> letters = new HashMap<>();
    private CharacterParser characterParser;
    private String supplierid;


    public static void activityStart(Activity context, int code) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsFilterSupplierActivity.class);
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
                getSupplierInfo();
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("suppliers", filterSupplierSelectList);
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
        supplierAdapter = new FilterSupplierAdapter(checkStates);
        supplierAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                boolean temp = checkStates.get(position);
                checkStates.set(position, !temp);
                supplierAdapter.notifyDataSetChanged();
            }
        });

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(supplierAdapter);
        filterContentRv.setAdapter(supplierAdapter);
        layoutManager = new LinearLayoutManager(this);
        filterContentRv.setLayoutManager(layoutManager);
        filterContentRv.addItemDecoration(headersDecor);

        filterOserver = new BaseObserver<SupplierAllResonse>() {
            @Override
            public void onErrorResponse(SupplierAllResonse supplierResponse) {
            }

            @Override
            public void onNextResponse(SupplierAllResonse supplierResponse) {
                filterSupplierList = changeResponseData(supplierResponse);
                checkStates.clear();
                for (int i = 0; i < filterSupplierList.size(); i++) {
                    checkStates.add(i, false);
                }
                changeData(filterSupplierList);
                supplierAdapter.setList(filterSupplierList);
            }
        };

    }

    private void changeData(List<FilterSupplier> filterSupplierList) {
        List<String> customLetters = new ArrayList<>();
        int position = 0;
        for (FilterSupplier filterSupplier : filterSupplierList) {
            String pinyin = characterParser.getSelling(filterSupplier.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                filterSupplier.setSortLetters(sortString);
            } else {
                filterSupplier.setSortLetters("#");
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
        Collections.sort(filterSupplierList, new SupplierComparator());
        filterSidebar.setLetters(customLetters);
    }


    @Override
    protected void loadData() {
        subscription = NetworkManager.getGoodsDataApi()
                .getSupplier()
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
        for (int i = 0; i < filterSupplierList.size(); i++) {
            String sortStr = filterSupplierList.get(i).getSortLetters();
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

    private void getSupplierInfo() {
        for (int i = 0; i < checkStates.size(); i++) {
            if (checkStates.get(i)) {
                filterSupplierSelectList.add(filterSupplierList.get(i));
            }
        }

    }


    private List<FilterSupplier> changeResponseData(SupplierAllResonse resonse) {
        List<FilterSupplier> filterSupplierList = new ArrayList<>();
        if (resonse.getData().getSupplier().getA() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getA());
        }
        if (resonse.getData().getSupplier().getB() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getB());
        }
        if (resonse.getData().getSupplier().getC() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getC());
        }
        if (resonse.getData().getSupplier().getD() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getD());
        }
        if (resonse.getData().getSupplier().getE() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getE());
        }
        if (resonse.getData().getSupplier().getF() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getF());
        }
        if (resonse.getData().getSupplier().getG() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getG());
        }
        if (resonse.getData().getSupplier().getH() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getH());
        }
        if (resonse.getData().getSupplier().getI() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getI());
        }
        if (resonse.getData().getSupplier().getJ() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getJ());
        }
        if (resonse.getData().getSupplier().getK() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getK());
        }
        if (resonse.getData().getSupplier().getL() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getL());
        }
        if (resonse.getData().getSupplier().getM() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getM());
        }
        if (resonse.getData().getSupplier().getN() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getN());
        }
        if (resonse.getData().getSupplier().getO() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getO());
        }
        if (resonse.getData().getSupplier().getP() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getP());
        }
        if (resonse.getData().getSupplier().getQ() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getQ());
        }
        if (resonse.getData().getSupplier().getR() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getR());
        }
        if (resonse.getData().getSupplier().getS() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getS());
        }
        if (resonse.getData().getSupplier().getT() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getT());
        }
        if (resonse.getData().getSupplier().getU() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getU());
        }
        if (resonse.getData().getSupplier().getV() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getV());
        }
        if (resonse.getData().getSupplier().getW() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getW());
        }
        if (resonse.getData().getSupplier().getX() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getX());
        }
        if (resonse.getData().getSupplier().getY() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getY());
        }
        if (resonse.getData().getSupplier().getZ() != null) {
            filterSupplierList.addAll(resonse.getData().getSupplier().getZ());
        }
        return filterSupplierList;
    }


}
