package com.thlh.jhmjmw.business.index.homepage;

import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.Category;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.CategoryHashMap;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 首页 标签适配器
 */
public class HomePageTabTextAdapter extends EasyRecyclerViewAdapter {
    private SparseArray<Boolean> selectStates = new SparseArray<>();

    public HomePageTabTextAdapter(SparseArray<Boolean> selectStates) {
        this.selectStates = selectStates;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_homepage_tabtext
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        Category category  = getItem(position);
        LinearLayout categorLl = viewHolder.findViewById(R.id.homgpage_tabtext_ll);
//        categorLl.setMinimumWidth(R.dimen.x132);
//        categorLl.setMinimumHeight(R.dimen.y50);
        TextView categoryTv = viewHolder.findViewById(R.id.homgpage_tabtext_tv);
        ImageView categoryIv = viewHolder.findViewById(R.id.homgpage_tabtext_iv);
        categoryTv.setText(category.getCatname());
        int imageResource = 0;
        if(selectStates.get(position)){
            categoryTv.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.white));
            categorLl.setBackgroundResource(R.drawable.shap_radius_winelight_r20);
            imageResource = changeCagegoryIv(position,true);
        }else {
            categoryTv.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.text_tips));
            categorLl.setBackgroundResource(R.drawable.bg_null);
            imageResource = changeCagegoryIv(position,false);
        }
        if(imageResource !=0){
//            Glide.get(viewHolder.);
            categoryIv.setBackgroundResource(imageResource);
        }
    }


    //根据名字匹配icon图标
    private int changeCagegoryIv(String name,boolean isSelect){
        int imgResourse = 0;
        if(isSelect){
            if((CategoryHashMap.hpSelectTab.get(name))== null) {
                return CategoryHashMap.hpSelectTab.get("其他");
            }
            imgResourse = CategoryHashMap.hpSelectTab.get(name);
        }else{
            if((CategoryHashMap.hpUnSelectTab.get(name))== null  ) {
                return CategoryHashMap.hpUnSelectTab.get("其他");
            }
            imgResourse = CategoryHashMap.hpUnSelectTab.get(name);
        }
        return imgResourse;
    }

    //根据位置匹配icon图标
    private int changeCagegoryIv(int position,boolean isSelect){
        int imgResourse = 0;
        if(isSelect){
            if((CategoryHashMap.hpSelectedTextTab.get(position +""))== null ) {
                return CategoryHashMap.hpSelectedTextTab.get("其他");
            }
            imgResourse = CategoryHashMap.hpSelectedTextTab.get(position +"");
        }else{
            if((CategoryHashMap.hpSelectTextTab.get(position +""))== null) {
                return CategoryHashMap.hpSelectTextTab.get("其他");
            }
            imgResourse = CategoryHashMap.hpSelectTextTab.get(position +"");
        }
        return imgResourse;
    }



    @Override
    public int getRecycleViewItemType(int position) {
            return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public void setSelectStates(SparseArray<Boolean> selectStates) {
        for (int i = 0; i < selectStates.size(); i++) {
            this.selectStates.setValueAt(i, selectStates.get(i));
        }
        notifyDataSetChanged();
    }
}