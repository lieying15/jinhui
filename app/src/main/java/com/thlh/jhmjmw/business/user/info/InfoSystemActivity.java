package com.thlh.jhmjmw.business.user.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.thlh.baselib.model.SystemInfo;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.user.info.adapter.MyInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 系统消息页
 */
public class InfoSystemActivity extends BaseViewActivity {
    private final String TAG = "InfoSystemActivity";

    @BindView(R.id.myinfo_explv)
    ExpandableListView myinfoExplv;

    
    private List<SystemInfo> mList = new ArrayList<SystemInfo>();
    private MyInfoAdapter myInfoAdapter;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, InfoSystemActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
//列表测试数据
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setInfo_title(getResources().getString(R.string.welcome_join_mjmw));
        systemInfo.setInfo_content(getResources().getString(R.string.welcome_join_mjmw) +
                getResources().getString(R.string.welcome_join_mjmw_choose_shop));
        mList.add(systemInfo);
    }



    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_info_system);
        ButterKnife.bind(this);
        myInfoAdapter = new MyInfoAdapter(this, mList);
        myinfoExplv.setAdapter(myInfoAdapter);
        myinfoExplv.setGroupIndicator(null);
        myinfoExplv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    for (int i = 0, count = parent.getExpandableListAdapter().getGroupCount(); i < count; i++) {
                        if (groupPosition != i) {// 关闭其他分组
                            parent.collapseGroup(i);
                        }
                    }
                    parent.expandGroup(groupPosition);
                }
                return true;
            }
        });
        myinfoExplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                parent.collapseGroup(groupPosition);

                return true;
            }
        });
    }

    private  void initExpList(List<SystemInfo> expList){
        if(expList==null ||expList.size() == 0) return;
        myinfoExplv.expandGroup(0);
    }


    @Override
    protected void loadData() {
        initExpList(mList);
        myInfoAdapter.notifyDataSetChanged();
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
