package bigshots.charity.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import bigshots.charity.io.AdManager;

/**
 * Created by root on 20/11/14.
 */
public class ExpandableView extends ViewGroup {

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 600;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    int videoHeight;
    private State state = State.CONTRACTED;
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            if (state == State.EXPANDED)
                container.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (state == State.CONTRACTED)
                container.setVisibility(View.GONE);
            invalidatePoster();
        }

        @Override
        public void onAnimationCancel(Animator animator) {


        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleState();
            if (state == State.EXPANDED)
                adManager.getVideoAd();
        }
    };
    private FrameLayout container;
    private View toggle;
    private int width, height, minHeight;
    private float animated_value;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            update();
        }
    };
    private AdManager adManager;
    private boolean touchDown = false;

    public ExpandableView(Context context) {
        super(context);
        init();
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        final int padding = dpToPixels(16);
        minHeight = dpToPixels(72);
        toggle = new View(getContext());
        toggle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, minHeight));
        toggle.setBackgroundColor(0xffbb00);
        toggle.setOnClickListener(listener);

        container = new FrameLayout(getContext());
        container.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT));
        container.setBackgroundColor(0xffffff);

        adManager = new AdManager(getContext());

        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);
        paint.setColor(0x25000000);

        //Todo addView();
        addView(toggle);
        addView(container);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;

        if (state == State.EXPANDED) {
            container.measure(widthMeasureSpec, heightMeasureSpec);
            measuredHeight += container.getMeasuredHeight();
            measuredWidth = Math.max(container.getMeasuredWidth(), measuredWidth);
        }
        toggle.measure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight += toggle.getMeasuredHeight();
        measuredWidth = Math.max(toggle.getMeasuredWidth(), measuredWidth);

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0), resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        toggle.layout(0, 0, getMeasuredWidth(), toggle.getMeasuredHeight());
        if (state == State.EXPANDED)
            container.layout(0, toggle.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());

    }

    private void toggleState() {
        if (state == State.CONTRACTED)
            state = State.EXPANDED;
        else
            state = State.CONTRACTED;
    }

    private void update() {
        if (animator.isRunning()) {
            switch (state) {
                case EXPANDED:
                    getLayoutParams().height = Math.round(minHeight + (animated_value * videoHeight));
                    break;
                case CONTRACTED:
                    getLayoutParams().height = Math.round(minHeight + ((1 - animated_value) * videoHeight));
                    break;
            }
        } else {
            switch (state) {
                case EXPANDED:
                    getLayoutParams().height = minHeight + videoHeight;
                    break;
                case CONTRACTED:
                    getLayoutParams().height = minHeight;
                    break;
            }
        }
        invalidatePoster();
    }

    private enum State {
        EXPANDED, CONTRACTED
    }
}
