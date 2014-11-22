package bigshots.charity.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import bigshots.charity.io.AdManager;

/**
 * Created by root on 18/11/14.
 */
public class BannerPopup extends ViewGroup {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 750;
    private final OnLongClickListener longClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            longClick(v);
            return false;
        }
    };
    private final OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            click(v);
        }
    };
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private MenuItem closeBanner, openApp, minimise;
    private int closeBannerID = 1, openAppID = 2, minimiseID = 3, mainViewID = 4;
    private MainBannerView mainView;
    private BannerPopup popup;
    private Direction direction;
    private AdManager adManager;
    private CurrentAnimation currentAnimation = CurrentAnimation.NONE;
    private View adView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private float animated_value;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            invalidatePoster();
        }
    };
    private int spacing;
    private int x, y, w, h, maxHeight, maxWidth;

    public BannerPopup(Context context) {
        super(context);

    }

    private void click(View view) {
//        adManager.getFullscreenAd().setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                adManager.getVideoAd().show();
//            }
//        });
    }

    private void longClick(View view) {

    }

    private void touch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) (event.getRawX() - mainView.getWidth() / 2);
                int y = (int) (event.getRawY() - mainView.getHeight() / 2);
                setPosition(x, y);
                break;

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                measuredHeight += child.getMeasuredHeight();
                measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth());
            }
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0), resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    private void resize(int width, int height) {
        getLayoutParams().height = height;
        getLayoutParams().width = width;

        params.height = height;
        params.width = width;
        windowManager.updateViewLayout(this, params);
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void setWidth(int width) {
        resize(width, params.height);
    }

    private void setHeight(int height) {
        resize(params.width, height);
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }


    private void init() {
        windowManager = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE);
//        ViewGroup.LayoutParams layoutParams = bubbleView.findViewById(R.id.bubble_id).getLayoutParams();
//        layoutParams.height = Utils.getInstance(null).getPixelsFromDp(48);
//        layoutParams.width = Utils.getInstance(null).getPixelsFromDp(48);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        // Todo int closeBannerID = 1, openAppID = 2, minimiseID = 3, mainViewID = 4;
        //todo windowManager.updateViewLayout(bubbleView, params);
        //todo windowManager.addView(bubbleView, params);
        //Todo  maxHeight, maxWidth
        //Todo addView()

    }

    private void setDistance() {
        closeBanner.setDistance(getDistance(closeBanner));
        openApp.setDistance(getDistance(openApp));
        minimise.setDistance(getDistance(minimise));
    }

    private float getDistance(View v) {
        if (v instanceof MainBannerView)
            return 0;

        int mainViewR = (mainView.getWidth() / 2);
        float base = mainViewR + spacing;
        int mainViewCenter = mainView.getLeft() + mainViewR;
        switch (direction) {
            case LEFT:
                return (v.getLeft() - mainViewCenter) / base;
            case RIGHT:
                return (mainViewCenter - v.getLeft()) / base;
        }

        return 0;
    }

    private void startAnimator() {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }
        animator.start();
    }

    private void setPosition(int x, int y) {
        params.x = x;
        params.y = y;
        windowManager.updateViewLayout(this, params);
    }

    private void setSize(int width, int height) {
        resize(width, height);
    }

    public enum State {
        SHOWING_MENU, SHOWING_AD, MINIMISED
    }

    private enum Direction {
        LEFT, RIGHT
    }

    private enum CurrentAnimation {
        SHOW_AD, HIDE_AD, SHOW_MENU, HIDE_MENU, MINIMISE, MAXIMISE, NONE
    }
}
