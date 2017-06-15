package com.thlh.jhmjmw.business.goods.goodsdetail.comment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.config.Constants;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.tablayout.CommonTabLayout;
import com.thlh.viewlib.tablayout.CustomTabEntity;
import com.thlh.viewlib.tablayout.OnTabSelectListener;
import com.thlh.viewlib.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 商品详情-评价
 */
public class GoodsCommentTopFragment extends BaseFragment {
    private static final String TAG = "GoodsCommentTopFragment";

    @BindView(R.id.comment_tablayout)
    CommonTabLayout commentTablayout;
    @BindView(R.id.comment_vp)
    ViewPager commentVp;

    private GoodsCommentFragment allFragment, goodsFragment, mediumFragment, badFragment;
    private GoodsCommentPhotoFragment photoFragment;
    private String goods_id;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    /*
    * questions*/
    private String[] mSegmentTLTitles = {"全部评价\n0", "好评\n0", "中评\n0", "差评\n0", "晒图\n0"};
    private int[] mIconUnselectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null};
    private int[] mIconSelectIds = {R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null, R.drawable.bg_null};

    private String commentNumAll,commentNumGoods,commentNumNormal,commentNumBad,commentNumPhoto;

    public static void activityStart(Activity context,String goods_id) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsCommentTopFragment.class);
        intent.putExtra("goods_id",goods_id);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_commenttop;
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        L.e(TAG + " goods_id:"+goods_id);
    }


    @Override
    protected void initView() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        allFragment = new GoodsCommentFragment();
        goodsFragment = new GoodsCommentFragment();
        mediumFragment = new GoodsCommentFragment();
        badFragment = new GoodsCommentFragment();
        photoFragment = new GoodsCommentPhotoFragment();
        fragments.add(allFragment);
        fragments.add(goodsFragment);
        fragments.add(mediumFragment);
        fragments.add(badFragment);
        fragments.add(photoFragment);

        PagerAdapter PagerAdapter = new PagerAdapter(getChildFragmentManager());
        commentVp.setAdapter(PagerAdapter);
        commentVp.setOffscreenPageLimit(5);
        for (int i = 0; i < mSegmentTLTitles.length; i++) {
            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        commentTablayout.setTabData(mTabEntities);
        commentTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                commentVp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        commentVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commentTablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        commentVp.setCurrentItem(0);
    }

    @Override
    protected void initData() {

    }


    //ViewPager适配器
    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("goods_id", goods_id);
            switch (position) {
                case 0:
                    args.putInt("commentType", Constants.COMMENT_GRADE_ALL);
                    allFragment.setArguments(args);
                    return allFragment;
                case 1:
                    args.putInt("commentType",  Constants.COMMENT_GRADE_GOOD);
                    goodsFragment.setArguments(args);
                    return goodsFragment;
                case 2:
                    args.putInt("commentType", Constants.COMMENT_GRADE_NORMAL);
                    mediumFragment.setArguments(args);
                    return mediumFragment;
                case 3:
                    args.putInt("commentType", Constants.COMMENT_GRADE_BAD);
                    badFragment.setArguments(args);
                    return badFragment;
                case 4:
                    args.putInt("commentType", Constants.COMMENT_GRADE_PHOTO);
                    photoFragment.setArguments(args);
                    return photoFragment;
                default:
                    args.putInt("commentType", Constants.COMMENT_GRADE_ALL);
                    allFragment.setArguments(args);
                    return allFragment;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }



    public void updateTabTitle() {
        String[] temp = {getResources().getString(R.string.all_evaluation)+"\n" +commentNumAll , getResources().getString(R.string.good_evaluation)+"\n" + commentNumGoods , getResources().getString(R.string.moddle_evaluation)+"\n" +commentNumNormal, getResources().getString(R.string.bad_evaluation)+"\n"+commentNumBad, getResources().getString(R.string.picture)+"\n"+commentNumPhoto};
        L.e(TAG + " updateTabTitle 全部评价 " +commentNumAll + " 好评 " + commentNumGoods +" 中评 " +commentNumNormal+" 差评 "+commentNumBad+ " 晒图 "+commentNumPhoto);
        this.mSegmentTLTitles = temp;
        mTabEntities.clear();
        for (int i = 0; i < mSegmentTLTitles.length; i++) {
            mTabEntities.add(new TabEntity(mSegmentTLTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        commentTablayout.setTabData(mTabEntities);
    }

    public void setCommentNumAll(String commentNumAll) {
        this.commentNumAll = commentNumAll;
    }

    public void setCommentNumGoods(String commentNumGoods) {
        this.commentNumGoods = commentNumGoods;
    }

    public void setCommentNumNormal(String commentNumNormal) {
        this.commentNumNormal = commentNumNormal;
    }

    public void setCommentNumBad(String commentNumBad) {
        this.commentNumBad = commentNumBad;
    }

    public void setCommentNumPhoto(String commentNumPhoto) {
        this.commentNumPhoto = commentNumPhoto;
        if(this.commentNumPhoto.equals(""))this.commentNumPhoto ="0";
    }






    @Override
    protected void loadData() {
    }

}
