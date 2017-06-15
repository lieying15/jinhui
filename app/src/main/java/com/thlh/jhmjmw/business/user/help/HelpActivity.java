package com.thlh.jhmjmw.business.user.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.thlh.baselib.model.SystemInfo;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帮助界面
 */
public class HelpActivity extends BaseViewActivity {
    private final String TAG = "HelpActivity";

    @BindView(R.id.help_explv)
    ExpandableListView helpExplv;

    
    private List<SystemInfo> mList = new ArrayList<SystemInfo>();
    private HelpAdapter helpAdapter;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HelpActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
    //列表测试数据
        SystemInfo systemInfo1 = new SystemInfo();
        systemInfo1.setInfo_title(getResources().getString(R.string.help_title_one));
        systemInfo1.setInfo_content(getResources().getString(R.string.help_title_one_content));
        SystemInfo systemInfo2 = new SystemInfo();
        systemInfo2.setInfo_title(getResources().getString(R.string.help_title_two));
        systemInfo2.setInfo_content(getResources().getString(R.string.help_title_two_content));

        SystemInfo systemInfo3 = new SystemInfo();
        systemInfo3.setInfo_title(getResources().getString(R.string.help_title_three));
        systemInfo3.setInfo_content(getResources().getString(R.string.help_title_three_content));
        SystemInfo systemInfo4 = new SystemInfo();
        systemInfo4.setInfo_title(getResources().getString(R.string.help_title_four));
        systemInfo4.setInfo_content(getResources().getString(R.string.help_title_four_content));
        SystemInfo systemInfo5 = new SystemInfo();
        systemInfo5.setInfo_title(getResources().getString(R.string.help_title_five));
        systemInfo5.setInfo_content(getResources().getString(R.string.help_title_five_content));
        SystemInfo systemInfo6 = new SystemInfo();
        systemInfo6.setInfo_title(getResources().getString(R.string.help_title_six));
        systemInfo6.setInfo_content(getResources().getString(R.string.help_title_six_content));


        mList.add(systemInfo1);
        mList.add(systemInfo2);
        mList.add(systemInfo3);
        mList.add(systemInfo4);
        mList.add(systemInfo5);
        mList.add(systemInfo6);
    }



    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        helpAdapter = new HelpAdapter(this, mList);
        helpExplv.setAdapter(helpAdapter);
        helpExplv.setGroupIndicator(null);
//        helpExplv.setChildDivider(null);
        helpExplv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
//                     关闭其他分组
//                    for (int i = 0, count = parent.getExpandableListAdapter().getGroupCount(); i < count; i++) {
//                        if (groupPosition != i) {
//                            parent.collapseGroup(i);
//                        }
//                    }
                    parent.expandGroup(groupPosition);
                }
                return true;
            }
        });
        helpExplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                parent.collapseGroup(groupPosition);

                return true;
            }
        });
    }


    @Override
    protected void loadData() {
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
