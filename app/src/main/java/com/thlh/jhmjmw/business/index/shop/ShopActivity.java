package com.thlh.jhmjmw.business.index.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.recharge.RechargeActivity;
import com.thlh.jhmjmw.business.recharge.RechargeQRActivity;
import com.thlh.jhmjmw.other.ShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thlh.jhmjmw.R.id.shop_content_tv;

/**
 * 我的小店页
 */
public class ShopActivity extends BaseViewActivity {
    private final String TAG = "ShopActivity";
    private final int ACTIVITY_CODE_RECHARGE = 0;

    @BindView(R.id.shop_share)
    TextView shopShare;
    @BindView(R.id.shop_recharge)
    TextView shopRecharge;
    @BindView(R.id.shop_shtore)
    TextView shopShtore;
    @BindView(R.id.shop_activity)
    TextView shopActivity;
    @BindView(R.id.shop_top_rl)
    RelativeLayout shopTopRl;
    @BindView(shop_content_tv)
    TextView shopContentTv;


    //活动pop
    private View popRootView;
    private PopupWindow popActivityWindow;
    private TextView contentTv;
    private TextView toStoreIv;
    private TextView tipsTv;
    private int agency_id;

    public static void activityStart(Activity context, int currIndex) {
        Intent intent = new Intent();
        intent.putExtra("currIndex", currIndex);
        intent.setClass(context, ShopActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    public static void activityStartForResult(Activity context, int code, int currIndex) {
        Intent intent = new Intent();
        intent.putExtra("currIndex", currIndex);
        intent.setClass(context, ShopActivity.class);
        context.startActivityForResult(intent, code);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        agency_id = Integer.parseInt(SPUtils.get("user_agency_id","0").toString());

        initAcPop();

//        String contentStr = "真正用心的人才会知道，\n" +
//                "土壤、气温、水分、日照对于橄榄油品质的影响，\n" +
//                "我们甚至寻味西班牙，为您甄选百年好品质......\n" +
//                "\n" +
//                "现在，足不出户，身边小店送货上门\n" +
//                "品质生活，从每家美物智能冰箱开始\n" +
//                "\n" +
//                "此刻，“万元冰箱抱回家”活动风暴\n" +
//                "\n" +
//                "商城充值¥"+Constants.RECHARGE_SENDICEBOX_PRICE+"，等值购物无忧，\n" +
//                "另送万元智能冰箱，智慧生活掌控在手！\n";
        if (agency_id == 10 || agency_id == 11) {
            shopContentTv.setText(getResources().getString(R.string.myshop_bj));
        } else if (agency_id == 37) {
            shopContentTv.setText(getResources().getString(R.string.myshop));
        } else {
            shopContentTv.setText(getResources().getString(R.string.myshop));
        }
    }

    @Override
    protected void loadData() {
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    @OnClick({R.id.shop_share, R.id.shop_recharge, R.id.shop_shtore, R.id.shop_activity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_share:
                ShareUtils shareUtils = new ShareUtils(ShopActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.ice_free_song),"http://v2.mjmw365.com",R.mipmap.ic_launcher);
                shareUtils.showShareWindow();
                break;
            case R.id.shop_recharge:
                if(SPUtils.getIsLogin()){
                    int isch = Integer.valueOf(SPUtils.get("user_isch",0).toString());
                    if(isch>0){
                        RechargeActivity.activityStart(ShopActivity.this,Constants.PAY_PURPOSE_MJB,ACTIVITY_CODE_RECHARGE);
                    }else {
                        RechargeQRActivity.activityStart(ShopActivity.this);
                    }
                }else {
                    LoginActivity.activityStart(ShopActivity.this);
                }
                break;
            case R.id.shop_shtore:
                if(SPUtils.getIsLogin()){
                    ShopMapActivity.activityStart(ShopActivity.this);
                }else { popActivityWindow.dismiss();
                    LoginActivity.activityStart(ShopActivity.this);
                }
                break;
            case R.id.shop_activity:
                if(SPUtils.getIsLogin()){
//                    popActivityWindow.showAtLocation(shopTopRl, Gravity.BOTTOM,0,0);
                    ShopMapActivity.activityStart(ShopActivity.this);
                }else {
                    LoginActivity.activityStart(ShopActivity.this);
                }
                break;
        }
    }


    private void initAcPop(){
        popRootView = LayoutInflater.from(this).inflate(R.layout.popup_shop_activity, null);
        toStoreIv = (TextView) popRootView.findViewById(R.id.pop_shop_action_tv);
        contentTv = (TextView) popRootView.findViewById(R.id.pop_shop_content_tv);
        tipsTv = (TextView) popRootView.findViewById(R.id.pop_shop_tips_tv);
//        String contentStr = "真正用心的人才会知道，\n" +
//                "土壤、气温、水分、日照对于橄榄油品质的影响，\n" +
//                "我们甚至寻味西班牙，为您甄选百年好品质......\n" +
//                "\n" +
//                "现在，足不出户，身边小店送货上门\n" +
//                "品质生活，从每家美物智能冰箱开始\n" +
//                "\n" +
//                "此刻，“万元冰箱抱回家”活动风暴\n" +
//                "\n" +
//                "商城充值¥"+Constants.RECHARGE_SENDICEBOX_PRICE+"，等值购物无忧，\n" +
//                "另送万元智能冰箱，智慧生活掌控在手！\n";

        contentTv.setText(getResources().getString(R.string.myshop));
        String tipsStr = "前往身边小店，立即参与";
        tipsTv.setText(tipsStr);
        toStoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopMapActivity.activityStart(ShopActivity.this);
            }
        });
        popActivityWindow = new PopupWindow(popRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popActivityWindow.setContentView(popRootView);
        popActivityWindow.setOutsideTouchable(true);
        popActivityWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_null));
        popActivityWindow.setAnimationStyle(R.style.popwin_anim_bottom_style);
    }

}
