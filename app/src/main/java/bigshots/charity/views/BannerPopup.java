package bigshots.charity.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import bigshots.charity.R;
import bigshots.charity.Vote;
import bigshots.charity.io.AdManager;
import bigshots.charity.services.BannerPopupService;

/**
 * Created by root on 18/11/14.
 */
public class BannerPopup extends ViewGroup {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static final int mainViewHeight = 64, adHeight = 50, adWidth = 350;
    private static int duration = 750;
    final Intent service = new Intent(getContext(), BannerPopupService.class);
    private final OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            click(v);
        }
    };
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private State state = State.SHOWING_AD;
    //private final WindowManager windowManager;
    private MenuItem closeBanner, openApp, minimise;
    private MainBannerView mainView;
    // private BannerPopup popup;
    private Direction direction;
    private AdManager adManager;
    private CurrentAnimation currentAnimation = CurrentAnimation.NONE;
    private View adView;
    private float adDistance = 0.425f;
    //private WindowManager.LayoutParams params;
    private float animated_value;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            runAnimations();
            invalidatePoster();
        }
    };
    private int[] mainViewLocation;
    private OnTouchListener mainViewOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("Touch", "view");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = (int) getX();
                    initialY = (int) getY();
                    initialTouchX = (int) event.getRawX();
                    initialTouchY = (int) event.getRawY();

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    break;
                case MotionEvent.ACTION_MOVE:
                    setX(initialX + (int) (event.getRawX() - initialTouchX));
                    setY(initialY + (int) (event.getRawY() - initialTouchY));

                    mainView.getLocationInWindow(mainViewLocation);

                    if (mainViewLocation[0] > (screenWidth / 2))
                        direction = Direction.RIGHT;
                    else
                        direction = Direction.LEFT;

                    invalidate();
                    break;

            }
            return true;
        }
    };
    private int spacing, initialX, initialY, initialTouchX, initialTouchY;
    private int snapToX, snapTpY, x, y, w, h, screenHeight, screenWidth;
    private CurrentAnimation[] currentAnimations;

    public BannerPopup(Context context, WindowManager windowManager, WindowManager.LayoutParams params) {
        super(context);
        //  this.windowManager = windowManager;
        //  this.params = params;
        init();
    }


    public BannerPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //Todo check the screenSize, and scale the ad accordingly

    private void init() {
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);

        adManager = new AdManager(getContext());
        adManager.loadBannerAd();

        mainView = new MainBannerView(getContext());
        mainView.setState(State.SHOWING_AD);
        mainView.setId(R.id.main_view);

        closeBanner = new MenuItem(getContext(), R.drawable.fa_default_background_default);
        closeBanner.setId(R.id.close_banner);

        minimise = new MenuItem(getContext(), R.drawable.white_arrow);
        minimise.setId(R.id.minimise);

        openApp = new MenuItem(getContext(), R.drawable.hands);
        openApp.setId(R.id.open_app);

        adView = adManager.getBannerAd();

        //todo windowManager.updateViewLayout(bubbleView, params);
        //todo windowManager.addView(bubbleView, params);
        //Todo  maxHeight, maxWidth
        //Todo addView()


//        ViewGroup.LayoutParams layoutParams = bubbleView.findViewById(R.id.bubble_id).getLayoutParams();
//        layoutParams.height = Utils.getInstance(null).getPixelsFromDp(48);
//        layoutParams.width = Utils.getInstance(null).getPixelsFromDp(48);
        setParameters();
//        addView(closeBanner);
//        addView(minimise);
//        addView(openApp);
//        addView(adView);
//        addView(mainView);
        invalidate();


        //windowManager.updateViewLayout(this, params);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    private void resolveAdSize() {
        final DisplayMetrics metrics = new DisplayMetrics();
        try {
            //windowManager.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // w = (int) (screenWidth * 0.66f);
        // w = Math.min(w,adWidth);
        h = dpToPixels(mainViewHeight);
        w = dpToPixels(adWidth + (int) (mainViewHeight * adDistance));
        final int adH = dpToPixels(adHeight);

        //Todo might have to mess around here   adView = adManager.getBannerAd();
//        mainView.setLayoutParams();
//        mainView.setBackgroundColor(0x4432dddd);
//        closeBanner.setLayoutParams();
//        closeBanner.setBackgroundColor(0x44fff3dd);
//        minimise.setLayoutParams();
//        minimise.setBackgroundColor(0x4467e2d);
//        openApp.setLayoutParams();
//        openApp.setBackgroundColor(0x44d35d2e);

        addView(closeBanner, new LayoutParams(adH, adH));
        closeBanner.setOnClickListener(clickListener);
        addView(minimise, new LayoutParams(adH, adH));
        minimise.setOnClickListener(clickListener);
        addView(openApp, new LayoutParams(adH, adH));
        openApp.setOnClickListener(clickListener);
        addView(adView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(mainView, new LayoutParams(h, h));
        mainView.setOnTouchListener(mainViewOnTouchListener);
        mainView.setOnClickListener(clickListener);
        // windowManager.updateViewLayout(this, params);
        this.setLayoutParams(new LayoutParams(w, h));

    }

    private void setParameters() {
        resolveAdSize();
        //Todo fill in

    }


    private void click(View v) {
        switch (v.getId()) {
            case R.id.main_view:
                Log.e("Click", "main");
                break;
            case R.id.close_banner:
                try {
                    getContext().stopService(service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.minimise:
                currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_MENU, CurrentAnimation.SNAP_TO};
                if (direction == direction.LEFT)
                    snapToX = -((int) (0.2f * getMeasuredHeight()));
                else
                    snapToX = screenWidth - ((int) (0.2f * getMeasuredHeight()));


                break;
            case R.id.open_app:
                final Intent intent = new Intent(getContext(), Vote.class);
                getContext().startActivity(intent);
                break;
        }
    }

    private void longClick(View view) {

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if ((child instanceof MenuItem) && (child.getVisibility() != GONE && state == State.SHOWING_MENU)) {
                //Todo fill this in
                //Log.e("instance of", String.format("w,h,x,y : %d, %d, %d, %d", child.getMeasuredWidth(), child.getMeasuredHeight(), (int) child.getX(), (int) child.getY()));
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }

        int adViewPadding = (getMeasuredHeight() - adView.getMeasuredHeight()) / 2;
        adViewPadding = adViewPadding < 0 ? 0 : adViewPadding;

        if (state == State.SHOWING_AD)
            adView.layout((int) (getMeasuredHeight() * adDistance), adViewPadding, getMeasuredWidth(), getMeasuredHeight() - adViewPadding);

        mainView.layout(0, 0, getMeasuredHeight(), getMeasuredHeight());
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

//        params.height = height;
//        params.width = width;
//        windowManager.updateViewLayout(this, params);

    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

//    private void setWidth(int width) {
//        resize(width, params.height);
//    }
//
//    private void setHeight(int height) {
//        resize(params.width, height);
//    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
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

//    private void setPosition(int x, int y) {
//        params.x = x;
//        params.y = y;
//        windowManager.updateViewLayout(this, params);
//    }

    private void setSize(int width, int height) {
        resize(width, height);
    }

    private void runAnimations() {
        if (animated_value > 0.999f) {
            currentAnimation = CurrentAnimation.NONE;
            return;
        }

        if (animated_value > 0.5f)
            currentAnimation = currentAnimations[1];
        else
            currentAnimation = currentAnimations[0];

    }

    public enum State {
        SHOWING_MENU, SHOWING_AD, MINIMISED
    }

    private enum Direction {
        LEFT, RIGHT
    }

    private enum CurrentAnimation {
        SHOW_AD, HIDE_AD, SHOW_MENU, HIDE_MENU, MINIMISE, MAXIMISE, NONE, SNAP_TO
    }
}
