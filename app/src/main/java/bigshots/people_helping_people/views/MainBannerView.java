package bigshots.people_helping_people.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import bigshots.people_helping_people.R;

/**
 * Created by root on 22/11/14.
 */
@SuppressWarnings("ALL")
public class MainBannerView extends ImageView {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
    private static final int duration = 710;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private BannerPopup.State state;
    private float animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            invalidatePoster();
        }
    };
    private boolean animateRipple;
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            animateRipple = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animated_value = 0;
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
    private int rippleR, cx, cy;
    private int rippleColor = 0x35ffffff;

    public MainBannerView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.addListener(animatorListener);
        animator.addUpdateListener(animatorUpdateListener);
        setWillNotDraw(false);
        paint.setColor(rippleColor);
    }

    public void setState(BannerPopup.State state) {
        if (this.state != state) {
            this.state = state;
            switch (state) {
                case MINIMISED:
                    give();
                    break;
                case SHOWING_AD:
                    giving();
                    break;
                case SHOWING_MENU:
                    menu();
                    break;
            }
        }
    }

    private void give() {
        setImageResource(R.drawable.icon_orange);
        invalidate();
    }

    private void giving() {
        setImageResource(R.drawable.icon_blue);
        invalidate();
    }

    private void menu() {
        setImageResource(R.drawable.back);
        invalidate();
    }

    public void rippleOut() {
        try {
            animator.cancel();
            animator.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
        animator.start();
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
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (animateRipple) {
            canvas.drawCircle(cx, cy, rippleR * animated_value, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w / 2;
        cy = h / 2;
        rippleR = Math.min(cx, cy);
    }
}
