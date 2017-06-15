package com.thlh.viewlib.ripple;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.thlh.baselib.R;


public class RippleButton extends Button {
    private final int     DEFAULT_DURATION       = 300;
    private final int     DEFAULT_FADE_DURATION  = 15; //每次刷新距离
    private final int     DEFAULT_ALPHA          = 90;
    private final int     DEFAULT_COLOR          = Color.BLACK;
    private final boolean     DEFAULT_TOUCH_RETURN    = true;
    private float mDownX;
    private float mDownY;
    private float mAlphaFactor;
    private float mRadius;
    private float mMaxRadius;
    private Rect mRect;
    private int mRippleColor;
    private boolean mIsAnimating = false;
    private RadialGradient mRadialGradient;
    private Paint mPaint;
    private ObjectAnimator mRadiusAnimator;
    private Path mPath = new Path();


    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        mRippleColor = a.getColor(R.styleable.RippleView_ripColor, DEFAULT_COLOR);
        mAlphaFactor = a.getFloat(R.styleable.RippleView_ripAlpha, DEFAULT_ALPHA)/255;
        a.recycle();
        
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAlpha(DEFAULT_ALPHA);
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        mMaxRadius = (float) Math.sqrt(width * width + height * height);
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(!this.isEnabled())return true;
        mDownX = event.getX();
        mDownY = event.getY();
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                touchDownEvent();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return DEFAULT_TOUCH_RETURN;
    }

    public void touchDownEvent(){
        mRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        mRadiusAnimator = ObjectAnimator.ofFloat(this, "radius", 0,mMaxRadius);
        mRadiusAnimator.setDuration(DEFAULT_DURATION);
        mRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mRadiusAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIsAnimating = true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                setRadius(0);
                mIsAnimating = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        mRadiusAnimator.start();
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public void setRadius(final float radius) {
        mRadius = radius;
        if (mRadius > 0) {
            mRadialGradient = new RadialGradient(mDownX, mDownY, mRadius, adjustAlpha(mRippleColor, mAlphaFactor), mRippleColor, Shader.TileMode.MIRROR);
            mPaint.setShader(mRadialGradient);
        }
        invalidate();
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode()) {
            return;
        }

        canvas.save(Canvas.CLIP_SAVE_FLAG);
        mPath.reset();
        mPath.addCircle(mDownX, mDownY, mRadius, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.restore();
        canvas.drawCircle(mDownX, mDownY, mRadius, mPaint);
    }

}
