package com.thlh.jhmjmw.business.user.wallet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.WalletResponse;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.recharge.RechargeActivity;
import com.thlh.jhmjmw.business.recharge.RechargeQRActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 美家钻界面
 */
public class MjzActivity extends BaseViewActivity {
    private static final String TAG = "MjzActivity";
    private final int ACTIVITY_CODE_RECHARGE = 0;
    @BindView(R.id.mjcurrency_mjb_tv)
    TextView mjbTv;
    @BindView(R.id.mjcurrency_mjb_recharge_ll)
    LinearLayout rechargeLl;
    @BindView(R.id.mjcurrency_tablayout)
    CommonTabLayout tablayout;
    @BindView(R.id.mjcurrency_vp)
    ViewPager viewpager;



    private DealRecordFragment allFragment, saveFragment, expendFragment;
    private int cotent_type;
    
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mIconUnselectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null,};
    private int[] mIconSelectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null,};


    private BaseObserver<WalletResponse> walletObserver;


    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, MjzActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }


    public static void activityStart(Activity context,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, MjzActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    int mjz_money;
    int out_money;
    @Override
    protected void initVariables() {
        allFragment = new DealRecordFragment();
        saveFragment = new DealRecordFragment();
        expendFragment = new DealRecordFragment();

       /* Intent  intent =getIntent();
        Bundle  bun=  intent.getExtras();
        if(intent!=null){
            //获取的值
            mjz_money= bun.getInt("mjz_money");
            out_money= bun.getInt("out_money");
            expendFragment.setArguments(bun);
        }
*/
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mjcurrency);
        ButterKnife.bind(this);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(allFragment);
        fragments.add(saveFragment);
        fragments.add(expendFragment);

        PagerAdapter PagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(PagerAdapter);

        viewpager.setOffscreenPageLimit(3);


        String[] mSegmentTLTitles = {getResources().getString(R.string.alls), getResources().getString(R.string.in), getResources().getString(R.string.out),};

        for (int i = 0; i < mSegmentTLTitles.length; i++) {

            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        
        tablayout.setTabData(mTabEntities);

        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                tablayout.setCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        /*
        *
        * */
        viewpager.setCurrentItem(cotent_type);



        walletObserver = new BaseObserver<WalletResponse>() {
            @Override
            public void onErrorResponse(WalletResponse walletResponse) {
                showErrorDialog( walletResponse.getErr_msg());
            }
            @Override
            public void onNextResponse(WalletResponse walletResponse) {
                mjbTv.setText(getResources().getString(R.string.money)+walletResponse.getData().getMjb());
            }
        };
    }


    @Override
    protected void loadData() {}

    @Override
    protected void onStart() {
        super.onStart();
        mjbTv.setText(getResources().getString(R.string.money)+ SPUtils.get("user_mjb","").toString());
    }

    @OnClick({ R.id.mjcurrency_mjb_recharge_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mjcurrency_mjb_recharge_ll:

                int isch = Integer.valueOf(SPUtils.get("user_isch",0).toString());
                if(isch>0){
                    RechargeActivity.activityStart(this,Constants.PAY_PURPOSE_MJB,ACTIVITY_CODE_RECHARGE);
                }else {
                    RechargeQRActivity.activityStart(this);
                }
                break;
        }
    }

    //ViewPager适配器
    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();

            switch (position) {
                case 0:
                    args.putString("recordType", Constants.RECORD_TYPE_All);
                    args.putString("recordForm", Constants.RECORD_FROM_MJB);
                    allFragment.setArguments(args);
                    return allFragment;
                case 1:
                    args.putString("recordType", Constants.RECORD_TYPE_RECHARGE);
                    args.putString("recordForm", Constants.RECORD_FROM_MJB);
                    saveFragment.setArguments(args);
                    return saveFragment;
                case 2:
                    args.putString("recordType", Constants.RECORD_TYPE_CONSUME);
                    args.putString("recordForm", Constants.RECORD_FROM_MJB);
                    expendFragment.setArguments(args);

                    return expendFragment;
                default:
                    args.putString("recordType", Constants.RECORD_TYPE_All);
                    args.putString("recordForm", Constants.RECORD_FROM_MJB);
                    allFragment.setArguments(args);
                    return allFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.alls);
                case 1:
                    return getResources().getString(R.string.in);
                case 2:
                    return getResources().getString(R.string.out);
                default:
                    return getResources().getString(R.string.alls);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_RECHARGE:
                    allFragment.loadDealRecord();
                    mjbTv.setText(getResources().getString(R.string.money)+ SPUtils.get("user_mjb","").toString());
                    break;
                default:
                    break;
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
