package bigshots.charity.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

/**
 * Created by root on 19/11/14.
 */
public class PlusButton extends Button {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 600;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            animateRipple = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {

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
    private int cx, cy;
    private boolean votedFor = false, animateRipple;
    private float ripple_animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            ripple_animated_value = (Float) (animation.getAnimatedValue());

            invalidatePoster();
        }
    };
    private int rippleR;

    private OnClickListener listener, onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            click();
            if (listener != null)
                listener.onClick(v);
        }
    };

    public PlusButton(Context context) {
        super(context);
        init();
    }

    public PlusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setOnClickListener(onClickListener);
        animator.setDuration(duration);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setInterpolator(interpolator);
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animateRipple && animator.isRunning()) {
            paint.setColor(getColor(!votedFor));
            canvas.drawCircle(cx, cy, rippleR, paint);
            paint.setColor(getColor(votedFor));
            canvas.drawCircle(cx, cy, rippleR * ripple_animated_value, paint);
        } else {
            paint.setColor(getColor(votedFor));
            canvas.drawCircle(cx, cy, rippleR, paint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w / 2;
        cy = h / 2;
        w = w - getPaddingLeft() - getPaddingRight();
        h = h - getPaddingTop() - getPaddingBottom();

        rippleR = Math.min(w, h) / 2;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        listener = l;
    }

    public void setIsVotedFor(boolean votedFor) {
        this.votedFor = votedFor;
    }

    private void startAnimator() {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }

        animator.start();
    }

    private void click() {
        setIsVotedFor(!votedFor);
        startAnimator();
    }

    private int getColor(boolean b) {
        return b ? 0x42bd41 : 0xe51c23;
    }
}
