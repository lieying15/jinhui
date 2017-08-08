package com.thlh.jhmjmw.business.goods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.DirectionalViewPager;
import android.widget.LinearLayout;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.index.IndexAdFragment;
import com.thlh.jhmjmw.other.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品广告页
 */
public class GoodsAdActivity extends BaseActivity {
    private static final String TAG = "GoodsAdActivity";

    @BindView(R.id.goods_ad_dvp)
    DirectionalViewPager goodsAdDvp;
    @BindView(R.id.goods_ad_back_ll)
    LinearLayout goodsAdBackLl;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String goods_id;

    public static void activityStart(Context context, String goods_id) {
        Intent intent = new Intent();
        intent.putExtra("goods_id", goods_id);
        intent.setClass(context, GoodsAdActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void initVariables() {
        goods_id = getIntent().getStringExtra("goods_id");
        L.e("  GoodsAdActivity  goods_id " + goods_id);
    }


    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_ad);
        ButterKnife.bind(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        goodsAdDvp.setAdapter(mSectionsPagerAdapter);
        goodsAdDvp.setOrientation(DirectionalViewPager.VERTICAL);
    }


    @Override
    protected void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.goods_ad_back_ll)
    public void onClick() {
        finish();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return IndexAdFragment.newInstance(position, goods_id);
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
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
        }
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
