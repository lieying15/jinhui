package com.thlh.jhmjmw.business.user.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.model.SystemInfo;
import com.thlh.jhmjmw.R;

import java.util.List;


/**
 * 帮助适配器
 */
public class HelpAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<SystemInfo> infoList;


    public HelpAdapter(Context context, List<SystemInfo> groupDatas) {
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
            convertView  = mInflater.inflate(R.layout.item_help_group,null);
            holder = new ViewGroupHolder();
            holder.title_tv = (TextView) convertView.findViewById(R.id.help_title_tv);
            holder.arrow_iv = (ImageView) convertView.findViewById(R.id.help_title_arrow_iv);
            holder.title_iv = (ImageView) convertView.findViewById(R.id.help_title_iv);
            holder.title_ll = (LinearLayout) convertView.findViewById(R.id.help_title_ll);
            convertView .setTag(holder);
        } else {
            holder = (ViewGroupHolder) convertView .getTag();
        }
        holder.title_tv.setText(infoList.get(groupPosition).getInfo_title());
        if(isExpanded){
            holder.arrow_iv.setImageResource(R.drawable.icon_arrow_up_gray);
            holder.title_iv.setImageResource(R.drawable.icon_help_active);
            holder.title_ll.setBackgroundResource(R.drawable.shap_radius_half_top_white);
        }else{
            holder.arrow_iv.setImageResource(R.drawable.icon_arrow_down_gray);
            holder.title_iv.setImageResource(R.drawable.icon_help);
            holder.title_ll.setBackgroundResource(R.drawable.shap_radius_white);

        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        ViewChildHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_help_child,null);
            holder = new ViewChildHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.help_content_tv);
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
        private TextView title_tv;
        private ImageView title_iv;
        private ImageView arrow_iv;
        private LinearLayout title_ll;
    }

    private class ViewChildHolder {
        private TextView tv_content;
    }

}
