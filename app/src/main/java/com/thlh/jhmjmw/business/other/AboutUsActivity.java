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
public class AboutUsActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "AboutUsActivity";

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
        intent.setClass(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
        String str = getResources().getText(R.string.about_our).toString();

        aboutusIntroduceTv.setText(str);

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {

    }

}
