package com.thlh.jhmjmw.business.user.info.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.SystemInfo;
import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;

import java.util.List;


/**
 * 订单的适配器
 */
public class MyInfoAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<SystemInfo> infoList;


    public MyInfoAdapter(Context context, List<SystemInfo> groupDatas) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.infoList = groupDatas;
    }



    @Override
    public int getGroupCount() {
        return infoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return infoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return infoList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewGroupHolder holder = null;

        if(convertView  == null){
            convertView  = mInflater.inflate(R.layout.item_myinfo_group,null);
            holder = new ViewGroupHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.myinfo_title_tv);
            holder.iv_arrow = (ImageView) convertView.findViewById(R.id.myinfo_title_arrow_iv);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.myinfo_title_delete_iv);
            convertView .setTag(holder);
        } else {
            holder = (ViewGroupHolder) convertView .getTag();
        }
        holder.tv_title.setText(infoList.get(groupPosition).getInfo_title());
        if(isExpanded){
            holder.iv_arrow.setVisibility(View.INVISIBLE);
        }else{
            holder.iv_arrow.setVisibility(View.VISIBLE);
        }
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tos.show("删除");

            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        ViewChildHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_myinfo_child,null);
            holder = new ViewChildHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.myinfo_content_tv);
            holder.ll_up = (LinearLayout) convertView.findViewById(R.id.myinfo_content_up_ll);
            convertView.setTag(holder);
        }else{
            holder = (ViewChildHolder) convertView.getTag();

        }
        holder.tv_content.setText(infoList.get(groupPosition).getInfo_content());

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    private class ViewGroupHolder{
        private TextView tv_title;
        private ImageView iv_arrow;
        private ImageView iv_delete;
    }

    private class ViewChildHolder {
        private TextView tv_content;
        private LinearLayout ll_up;
    }

}
