package bigshots.people_helping_people.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import bigshots.people_helping_people.R;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class ProgressBar extends View {
    private static final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private static final int duration = 750;
    private static float max;
    private final Paint paint = new Paint();
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private int progress, w, h;
    private int backgroundColor, progressColor;
    private boolean animateIn = true;
    private final ValueAnimator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animateIn = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private float animated_value;
    private final ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
        }
    };

    public ProgressBar(Context context) {
        super(context);
        init();
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        animator.setDuration(duration);
        animator.addUpdateListener(updateListener);
        animator.addListener(listener);
        animator.setInterpolator(interpolator);
        max = 100;
        progressColor = getResources().getColor(R.color.current_charity_color);
        backgroundColor = getResources().getColor(R.color.light_grey);
        animator.start();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        w = width;
        h = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int stopStart = Math.round((progress / max) * w);

        if (animateIn)
            stopStart = (int) (stopStart * animated_value);

        paint.setColor(progressColor);
        canvas.drawRect(0, 0, stopStart, h, paint);

        paint.setColor(backgroundColor);
        canvas.drawRect(stopStart, 0, w, h, paint);

    }

    public float getMax() {
        return max;
    }

    public static void setMax(float max) {
        ProgressBar.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


}
