package bigshots.charity.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by root on 19/11/14.
 */
public class PlusButton extends View {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 800;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private Paint paint = new Paint();
    private int circleColor = 0xffdcdcdc, plusMinusColor = 0xff454545;
    private int[] verticalLinePos = new int[4], horizontalLinePos = new int[4];
    private State state = State.PLUS;
    private boolean touchDown = false, animateRipple;
    private float ripple_animated_value, animated_value;
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

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!touchDown)
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
    private int rippleR;
    private int rippleColor = 0x25000000;
    private int clickedX, clickedY;
    private OnPlusMinusButtonPressed onPlusMinusButtonPressed;
    private int w, h, cx, cy, r, lineR;

    public PlusButton(Context context) {
        super(context);
        init();
    }

    public PlusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });


        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(circleColor);
        canvas.drawCircle(cx, cy, r, paint);
        drawLines(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (animateRipple) {
            paint.setColor(rippleColor);
            canvas.drawCircle(clickedX, clickedY, rippleR * ripple_animated_value, paint);
        }
    }

    @Override
    protected void onSizeChanged(int wi, int he, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        w = wi;
        h = he;
        cx = w / 2;
        cy = h / 2;
        r = Math.min(w, h) / 2;
        lineR = (int) (r * 0.5f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(lineR / 6f);
    }

    private void drawLines(Canvas canvas) {
        calculatePositions();
        paint.setColor(plusMinusColor);
        canvas.drawLine(verticalLinePos[0], verticalLinePos[1], verticalLinePos[2], verticalLinePos[3], paint);
        canvas.drawLine(horizontalLinePos[0], horizontalLinePos[1], horizontalLinePos[2], horizontalLinePos[3], paint);
    }

    private void calculatePositions() {
        if (animator.isRunning()) {

            int base = 0;
            switch (state) {
                case MINUS:
                    base = (int) ((animated_value * 270) - 90);
                    break;
                case PLUS:
                    base = (int) ((animated_value * 90) + 180);
                    break;
            }

            injectPositions(base);
        } else {
            Log.e("animator", "not runing");
            switch (state) {
                case MINUS:
                    verticalLinePos[0] = cx - lineR;
                    verticalLinePos[1] = cy;
                    verticalLinePos[2] = cx + lineR;
                    verticalLinePos[3] = cy;

                    horizontalLinePos[0] = cx - lineR;
                    horizontalLinePos[1] = cy;
                    horizontalLinePos[2] = cx + lineR;
                    horizontalLinePos[3] = cy;

                    break;
                case PLUS:
                    verticalLinePos[0] = cx;
                    verticalLinePos[1] = cy - lineR;
                    verticalLinePos[2] = cx;
                    verticalLinePos[3] = cy + lineR;

                    horizontalLinePos[0] = cx - lineR;
                    horizontalLinePos[1] = cy;
                    horizontalLinePos[2] = cx + lineR;
                    horizontalLinePos[3] = cy;
                    break;
            }
        }

    }

    private void injectPositions(int baseDeg) {
        switch (state) {
            case PLUS:
                verticalLinePos[0] = cx + (int) (Math.cos(Math.toRadians(baseDeg)) * lineR);
                verticalLinePos[1] = cy + (int) (Math.sin(Math.toRadians(baseDeg)) * lineR);
                verticalLinePos[2] = cx + (int) (Math.cos(Math.toRadians(baseDeg - 180)) * lineR);
                verticalLinePos[3] = cy + (int) (Math.sin(Math.toRadians(baseDeg - 180)) * lineR);
                baseDeg -= 90;
                horizontalLinePos[0] = cx + (int) (Math.sin(Math.toRadians(baseDeg)) * lineR);
                horizontalLinePos[1] = cy + (int) (Math.cos(Math.toRadians(baseDeg)) * lineR);
                horizontalLinePos[2] = cx + (int) (Math.sin(Math.toRadians(baseDeg - 180)) * lineR);
                horizontalLinePos[3] = cy + (int) (Math.cos(Math.toRadians(baseDeg - 180)) * lineR);

                break;
            case MINUS:
                verticalLinePos[0] = cx + (int) (Math.cos(Math.toRadians(baseDeg)) * lineR);
                verticalLinePos[1] = cy + (int) (Math.sin(Math.toRadians(baseDeg)) * lineR);
                verticalLinePos[2] = cx + (int) (Math.cos(Math.toRadians(baseDeg - 180)) * lineR);
                verticalLinePos[3] = cy + (int) (Math.sin(Math.toRadians(baseDeg - 180)) * lineR);

                baseDeg -= 90;
                horizontalLinePos[0] = cx + (int) (Math.cos(Math.toRadians(baseDeg)) * lineR);
                horizontalLinePos[1] = cy + (int) (Math.sin(Math.toRadians(baseDeg)) * lineR);
                horizontalLinePos[2] = cx + (int) (Math.cos(Math.toRadians(baseDeg - 180)) * lineR);
                horizontalLinePos[3] = cy + (int) (Math.sin(Math.toRadians(baseDeg - 180)) * lineR);
                break;
        }

    }

    private void click() {
        System.out.println("click");
        if (state == State.MINUS)
            state = State.PLUS;
        else
            state = State.MINUS;

        if (onPlusMinusButtonPressed != null)
            onPlusMinusButtonPressed.onButtonPressed(state);

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

    public enum State {
        MINUS, PLUS

    }

    public interface OnPlusMinusButtonPressed {
        void onButtonPressed(State state);
    }
}
