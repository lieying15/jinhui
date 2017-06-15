package com.thlh.viewlib.ripple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.thlh.baselib.R;
/**
 * Created by Administrator on 2016/4/15.
 */
public class RippleRelativeLayout extends RelativeLayout {
    private final int     DEFAULT_DURATION       = 300;
    private final int     DEFAULT_FADE_DURATION  = 15; //每次刷新距离
    private final int     DEFAULT_ALPHA          = 90;
    private final int     DEFAULT_COLOR          = Color.BLACK;

    private int layout_width;
    private int layout_height;
    private int frameRate ;
    private int rippleDuration;
    private int rippleAlpha;
    private Handler canvasHandler;
    private float radiusMax = 0;
    private float radiusTemp = 0;
    private boolean animationRunning = false;
    private int timer = 0;
    private float touch_x = -1;
    private float touch_y = -1;
    private Paint paint;
    private int rippleColor;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private OnRippleCompleteListener onCompletionListener;

    public RippleRelativeLayout(Context context) {
        super(context);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        rippleColor = typedArray.getColor(R.styleable.RippleView_ripColor, DEFAULT_COLOR);
        rippleAlpha = typedArray.getInteger(R.styleable.RippleView_ripAlpha, DEFAULT_ALPHA);
        rippleDuration = typedArray.getInteger(R.styleable.RippleView_ripDuration, DEFAULT_DURATION);
        frameRate = typedArray.getInteger(R.styleable.RippleView_ripFramerate, DEFAULT_FADE_DURATION);
        canvasHandler = new Handler();
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(rippleColor);
        paint.setAlpha(rippleAlpha);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (animationRunning) {
//            if (timer == 0) canvas.save();
            radiusTemp = (float) timer * frameRate / rippleDuration;
            if ( radiusTemp >1) {
                animationRunning = false;
                timer = 0;
//                canvas.save();
//                canvas.restore();
                invalidate();
                if (onCompletionListener != null) onCompletionListener.onComplete(this);
                return;
//            }
            } else
                canvasHandler.postDelayed(runnable, frameRate);


            paint.setColor(rippleColor);
            paint.setAlpha((int) (rippleAlpha - ((rippleAlpha) * (((float) timer * frameRate) / rippleDuration))));
            canvas.drawCircle(touch_x, touch_y, radiusMax * radiusTemp, paint);
            timer ++ ;
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        layout_width = width;
        layout_height = height;
    }




    public boolean onTouchRippleEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                rippleAnimationInit(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        onTouchRippleEvent(event);
        return super.onInterceptTouchEvent(event);
    }

    private void rippleAnimationInit(MotionEvent event){
        if (this.isEnabled() && !animationRunning) {
//            radiusMax = Math.max(layout_width, layout_height);
            radiusMax = (float) Math.sqrt(Math.pow(layout_width,2) + Math.pow(layout_height,2));
            this.touch_x = event.getX();
            this.touch_y = event.getY();
            animationRunning = true;
            invalidate();
        }
    }


    public void setRLRippleCompleteListener(OnRippleCompleteListener listener) {
        this.onCompletionListener = listener;
    }

    public interface OnRippleCompleteListener {
        void onComplete(RippleRelativeLayout rippleRelativeLayout);
    }


}
