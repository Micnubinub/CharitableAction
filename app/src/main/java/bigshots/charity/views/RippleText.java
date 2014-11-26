package bigshots.charity.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import bigshots.charity.R;

/**
 * Created by root on 27/11/14.
 */
public class RippleText extends TextView {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 850;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private TextView primaryTextView, secondaryTextView;
    private String primaryText, secondaryText;
    private int width;
    private int height;
    private float animated_value = 0;
    private float scaleTo = 1.065f;
    private int clickedX, clickedY;
    private boolean touchDown = false, animateRipple;
    private float ripple_animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            ripple_animated_value = animated_value;
            invalidatePoster();
        }
    };
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            animateRipple = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!touchDown) {
                ripple_animated_value = 0;
                return;
            }
            animateRipple = false;
            invalidatePoster();
        }

        @Override
        public void onAnimationCancel(Animator animator) {


        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
    private int rippleR;
    private int rippleColor = 0x25000000;

    public RippleText(Context context) {
        super(context);
        init();
    }

    public RippleText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleText, 0, 0);
        setRippleColor(a.getColor(R.attr.ripple_color, 0x25000000));
        a.recycle();
        init();
    }

    private void init() {
        animator.setDuration(duration);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setInterpolator(interpolator);
        paint.setColor(rippleColor);
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void setRippleColor(int color) {
        rippleColor = color;
        paint.setColor(color);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (animateRipple) {
            canvas.drawCircle(clickedX, clickedY, rippleR * ripple_animated_value, paint);
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                rippleR = (int) (Math.sqrt(Math.pow(Math.max(width - clickedX, clickedX), 2) + Math.pow(Math.max(height - clickedY, clickedY), 2)) * 1.15);

                animator.start();

                touchDown = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                callOnClick();
                touchDown = false;

                if (!animator.isRunning()) {
                    animateRipple = false;
                    ripple_animated_value = 0;
                    invalidatePoster();
                }
                break;
        }
        return true;
    }


}
