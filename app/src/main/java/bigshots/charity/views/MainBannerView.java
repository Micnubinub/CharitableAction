package bigshots.charity.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import bigshots.charity.R;

/**
 * Created by root on 22/11/14.
 */
public class MainBannerView extends ImageView {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 650;
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
    private float ripple_animated_value = 0;
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            animateRipple = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            ripple_animated_value = 0;
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
    private int rippleColor = 0x25ffffff;

    public MainBannerView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
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
//Todo fill in
        setImageResource(R.drawable.icon_orange);
        invalidate();
    }

    private void giving() {
//Todo fill in
        setImageResource(R.drawable.icon_blue);
        invalidate();
    }

    private void menu() {
//Todo fill in
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
            paint.setColor(rippleColor);
            canvas.drawCircle(cx, cy, rippleR * ripple_animated_value, paint);
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
