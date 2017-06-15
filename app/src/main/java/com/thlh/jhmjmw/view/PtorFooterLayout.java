package com.thlh.jhmjmw.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.viewlib.pulltorefresh.LoadingLayoutBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;


/**
 * Created by HQ on 2016/5/9.
 */
public class PtorFooterLayout extends LoadingLayoutBase {

    private FrameLayout mInnerLayout;


    private final TextView mFooterText;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;


    private ImageView mPersonImage;
    private AnimationDrawable animP;

    public PtorFooterLayout(Context context) {
        this(context, PullToRefreshBase.Mode.PULL_FROM_END);
    }

    public PtorFooterLayout(Context context, PullToRefreshBase.Mode mode) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_ptor_footer_loadinglayout, this);

        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        mFooterText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_footer_text);

        mPersonImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_footer_logo);

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();
        lp.gravity = mode == PullToRefreshBase.Mode.PULL_FROM_END ? Gravity.TOP : Gravity.BOTTOM;

        // Load in labels
        mPullLabel = getResources().getString(R.string.laoding_all_things);
        mRefreshingLabel = getResources().getString(R.string.laoding_all_things);
        mReleaseLabel = getResources().getString(R.string.laoding_all_things);

        reset();
    }

    @Override
    public final int getContentSize() {
        return mInnerLayout.getHeight();
    }

    @Override
    public final void pullToRefresh() {
        mFooterText.setText(mPullLabel);
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
        mFooterText.setText(mRefreshingLabel);

        if (animP == null) {
            mPersonImage.setImageResource(R.drawable.refreshing_anim);
            animP = (AnimationDrawable) mPersonImage.getDrawable();
        }
        animP.start();

    }

    @Override
    public final void releaseToRefresh() {
        mFooterText.setText(mReleaseLabel);
    }

    @Override
    public final void reset() {
        if (animP != null) {
            animP.stop();
            animP = null;
        }
        mPersonImage.setImageResource(R.drawable.hompage_logo1);

    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        mFooterText.setText(label);
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


}