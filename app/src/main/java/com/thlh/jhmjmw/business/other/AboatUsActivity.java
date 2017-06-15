package com.thlh.jhmjmw.business.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.view.HeaderNormal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们
 */
public class AboatUsActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "AboatUsActivity";

    @BindView(R.id.aboutus_header)
    HeaderNormal aboutusHeader;
    @BindView(R.id.aboutus_introduce_tv)
    TextView aboutusIntroduceTv;
    @BindView(R.id.aboutus_copyright_tv)
    TextView aboutusCopyrightTv;
    @BindView(R.id.aboutus_copyright_name_tv)
    TextView aboutusCopyrightNameTv;


    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AboatUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
        String str = "每家美物由天汇联合（北京）科技发展有限公司创建，通过强大的大规模集约采购优势、丰富的电子商务管理服务经验和最先进的互 联网技术为用户提供最新最优质的商品。主要经营技术开发；技术推广；技术服务；技术转让；技术咨询；基础软件服务；应用软件服务；计算机系统服务；设计、制作、代理、发布广告；组织文化艺术交流活动；会议服务；承办展览展会互动；企业策划；销售食品、日用品、服务、家用电器、厨房用具、针纺织品、工艺品、文化用品、体育用品、计算机、软件及辅助设备、通讯设备、电子产品、汽车（不含九及以下乘用车）等。";

        aboutusIntroduceTv.setText(str);

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {

    }

}
