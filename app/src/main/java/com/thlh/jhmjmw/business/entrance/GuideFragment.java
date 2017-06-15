package com.thlh.jhmjmw.business.entrance;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.entrance.regist.RegisterActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;

import butterknife.BindView;

public class GuideFragment extends BaseFragment {
    private static final String TAG = "GuideFragment";
    @BindView(R.id.guide_iv)
    ImageView guideIv;
    @BindView(R.id.guide_logint_tv)
    TextView guideLogintTv;
    @BindView(R.id.guide_regist_tv)
    TextView guideRegistTv;

    private int guide_num;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        guide_num = bundle.getInt("guide_num");
    }

    @Override
    protected void initView() {
        switch (guide_num) {
            case 1:
                guideIv.setImageResource(R.drawable.img_guide_1);
                guideLogintTv.setVisibility(View.GONE);
                guideRegistTv.setVisibility(View.GONE);
                break;
            case 2:
                guideIv.setImageResource(R.drawable.img_guide_2);
                guideLogintTv.setVisibility(View.GONE);
                guideRegistTv.setVisibility(View.GONE);
                break;
            case 3:
                guideIv.setImageResource(R.drawable.img_guide_3);
                guideLogintTv.setVisibility(View.GONE);
                guideRegistTv.setVisibility(View.GONE);
                break;
            case 4:
                guideIv.setImageResource(R.drawable.img_guide_4);
                /**登录注册不显示*/
//                guideLogintTv.setVisibility(View.VISIBLE);
//                guideRegistTv.setVisibility(View.VISIBLE);
                break;
        }


        /**登录*/
        guideLogintTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IndexActivity.activityStart(getActivity());
                LoginActivity.activityStart(getActivity());
                getActivity().finish();
            }
        });
        /**注册*/
        guideRegistTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IndexActivity.activityStart(getActivity());
                RegisterActivity.activityStart(getActivity());
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
