package com.thlh.jhmjmw.business.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.photo.PhotoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by HQ on 2015/12/17.
 */
public class PhotoPagerActivity extends BaseActivity {

    @BindView(R.id.header_nomal_left_fl)
    FrameLayout headerNomalLeftFl;

    private ViewPager mPager;
    private TextView tv_photonum;
    private List<String> mPhotosListData = new ArrayList<String>();
    private int initPosition;
    private String url;

    public static void activityStart(Context context, ArrayList<String> imagesUrl, int position) {
        Intent intent = new Intent(context, PhotoPagerActivity.class);
        intent.putStringArrayListExtra("imagesUrl", imagesUrl);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }


    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_photo_pager);
        ButterKnife.bind(this);
        tv_photonum = (TextView) findViewById(R.id.photopager_photonum_tv);
        tv_photonum.setText(initPosition + 1 + "/" + mPhotosListData.size());
        mPager = (ViewPager) findViewById(R.id.photopager_viewpager);

        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));

        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mPhotosListData.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                PhotoView view = new PhotoView(PhotoPagerActivity.this);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                view.enable();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                if (mPhotosListData.get(position).contains("http")){
                    url = mPhotosListData.get(position);
                }else {
                    url = Deployment.IMAGE_PATH + mPhotosListData.get(position);
                }
                ImageLoader.display(url,view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                int tvposition = position + 1;
                tv_photonum.setText(tvposition + "/" + mPhotosListData.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mPager.setCurrentItem(initPosition);
    }

    @Override
    protected void initVariables() {
        mPhotosListData = getIntent().getStringArrayListExtra("imagesUrl");
        initPosition = getIntent().getIntExtra("position", 0);
        if (mPhotosListData == null) finish();
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.header_nomal_left_fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_nomal_left_fl:
                PhotoPagerActivity.this.finish();
                break;
        }
    }
}
