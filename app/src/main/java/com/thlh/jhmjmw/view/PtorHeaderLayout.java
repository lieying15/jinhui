package com.thlh.jhmjmw.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.viewlib.pulltorefresh.LoadingLayoutBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;


/**
 * Created by HQ on 2016/5/9.
 */
public class PtorHeaderLayout extends LoadingLayoutBase {

    private FrameLayout mInnerLayout;

    private final TextView mHeaderText;
    private final TextView mSubHeaderText;
    private final LinearLayout mHeaderBottomLl,mHeaderTopLl;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;


    private ImageView mPersonImage;
    private AnimationDrawable animP;
    private LinearLayout mHeaderLoadingLl;
    private ImageView mHeaderLoadingIv;

    public PtorHeaderLayout(Context context) {
        this(context, PullToRefreshBase.Mode.PULL_FROM_START);
    }

    public PtorHeaderLayout(Context context, PullToRefreshBase.Mode mode) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_ptor_header_loadinglayout, this);

        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
        mSubHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
        mHeaderTopLl = (LinearLayout) mInnerLayout.findViewById(R.id.pull_to_refresh_top_ll);
        mHeaderBottomLl = (LinearLayout) mInnerLayout.findViewById(R.id.pull_to_refresh_bottom_ll);
        mHeaderLoadingLl = (LinearLayout) mInnerLayout.findViewById(R.id.pull_to_loading_ll);
        mHeaderLoadingIv = (ImageView) mInnerLayout.findViewById(R.id.pull_to_loading_iv);

        mPersonImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_logo);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();
        lp.gravity = mode == PullToRefreshBase.Mode.PULL_FROM_END ? Gravity.TOP : Gravity.BOTTOM;

        // Load in labels
        mPullLabel = getResources().getString(R.string.down_fresh);
        mRefreshingLabel = getResources().getString(R.string.downing);
        mReleaseLabel = getResources().getString(R.string.return_top);
        reset();

    }

    @Override
    public final int getContentSize() {
        return mInnerLayout.getHeight();
    }

    @Override
    public final void pullToRefresh() {
        mPersonImage.setImageResource(R.drawable.img_loading_arrow);
        mSubHeaderText.setText(mPullLabel);
        mHeaderTopLl.setVisibility(VISIBLE);
        mHeaderBottomLl.setVisibility(VISIBLE);
        mHeaderLoadingLl.setVisibility(GONE);
    }

    @Override
    public final void onPull(float scaleOfLayout) {
        scaleOfLayout = scaleOfLayout > 1.0f ? 1.0f : scaleOfLayout;



        //透明度动画
        ObjectAnimator animAlphaP = ObjectAnimator.ofFloat(mPersonImage, "alpha", -1, 1).setDuration(300);
        animAlphaP.setCurrentPlayTime((long) (scaleOfLayout * 300));

        ObjectAnimator animPX = ObjectAnimator.ofFloat(mPersonImage, "scaleX", 0, 1).setDuration(300);
        animPX.setCurrentPlayTime((long) (scaleOfLayout * 300));
        ObjectAnimator animPY = ObjectAnimator.ofFloat(mPersonImage, "scaleY", 0, 1).setDuration(300);
        animPY.setCurrentPlayTime((long) (scaleOfLayout * 300));

    }

    @Override
    public final void refreshing() {
        mSubHeaderText.setText(mRefreshingLabel);
        mHeaderTopLl.setVisibility(GONE);
        mHeaderBottomLl.setVisibility(GONE);
        mHeaderLoadingLl.setVisibility(VISIBLE);
        if (animP == null) {
            mHeaderLoadingIv.setImageResource(R.drawable.refreshing_anim);
            animP = (AnimationDrawable) mHeaderLoadingIv.getDrawable();
        }
        animP.start();

    }

    @Override
    public final void releaseToRefresh() {
        mSubHeaderText.setText(mReleaseLabel);
    }

    @Override
    public final void reset() {
        if (animP != null) {
            animP.stop();
            animP = null;
        }
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        mSubHeaderText.setText(label);
//        mHeaderBottomLl.setVisibility(VISIBLE);
//        mHeaderBottomLl.setGravity(Gravity.RIGHT);
    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    @Override
    public void setTextTypeface(Typeface tf) {
        mHeaderText.setTypeface(tf);
    }
}