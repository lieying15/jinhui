package com.thlh.jhmjmw.business.index;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.recharge.RechargeActivity;
import com.thlh.jhmjmw.business.recharge.RechargeQRActivity;

import butterknife.BindView;

public class IndexAdFragment extends BaseFragment {
    private static final String TAG = "IndexAdFragment";
    private static final String AD_SELETED_NUMBER = "ad_num";
    private static final String AD_GOODS_ID = "goods_id";
    @BindView(R.id.goods_ad_back_iv)
    ImageView goodsAdBackIv;
    @BindView(R.id.goods_ad_todetail_tv)
    TextView goodsAdTodetailTv;
    @BindView(R.id.goods_ad_torecharge_tv)
    TextView goodsAdTorechargeTv;
    @BindView(R.id.goods_ad_arrow_iv)
    ImageView goodsAdArrowIv;

    private int select_num;
    private String goods_id;

    private int[] mImge_bj = {
            R.drawable.icebox_ad01, R.drawable.icebox_ad021, R.drawable.icebox_ad03,
            R.drawable.icebox_ad04, R.drawable.icebox_ad05, R.drawable.icebox_ad06
    ,R.drawable.icebox_ad021,R.drawable.icebox_ad08};
    private int[] mImge = {
            R.drawable.icebox_ad01, R.drawable.icebox_ad02, R.drawable.icebox_ad03,
            R.drawable.icebox_ad04, R.drawable.icebox_ad05, R.drawable.icebox_ad06
            ,R.drawable.icebox_ad021,R.drawable.icebox_ad08};

    public static IndexAdFragment newInstance(int sectionNumber, String goods_id) {
        IndexAdFragment fragment = new IndexAdFragment();
        Bundle args = new Bundle();
        args.putInt(AD_SELETED_NUMBER, sectionNumber);
        args.putString(AD_GOODS_ID, goods_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_index_goodsad;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        select_num = bundle.getInt(AD_SELETED_NUMBER);
        goods_id = bundle.getString(AD_GOODS_ID);
    }

    @Override
    protected void initView() {
        if (SPUtils.get("user_agency_id","0").toString().equals("10") || SPUtils.get("user_agency_id","0").toString().equals("11")){
            goodsAdBackIv.setImageResource(mImge_bj[select_num]);
        }else {
            goodsAdBackIv.setImageResource(mImge[select_num]);
        }

        if (select_num == 7) {
            goodsAdTodetailTv.setVisibility(View.GONE);
            goodsAdTorechargeTv.setVisibility(View.GONE);
            goodsAdArrowIv.setVisibility(View.GONE);
        } else {
            goodsAdTodetailTv.setVisibility(View.GONE);
            goodsAdTorechargeTv.setVisibility(View.GONE);
            goodsAdArrowIv.setVisibility(View.VISIBLE);

            Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.view_arrow_donw);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    goodsAdArrowIv.startAnimation(animation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
//            goodsAdArrowIv.setAnimation(animation);
            goodsAdArrowIv.startAnimation(animation);
        }

        goodsAdTodetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailV3Activity.activityStart(getActivity(), goods_id);
                getActivity().finish();
            }
        });
        goodsAdTorechargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int isch = Integer.valueOf(SPUtils.get("user_isch",0).toString());
                if(isch>0){
                    RechargeActivity.activityStart(getActivity(),Constants.PAY_PURPOSE_MJB);
                }else {
                    RechargeQRActivity.activityStart(getActivity());
                }
                getActivity().finish();
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void loadData() {

    }



}
