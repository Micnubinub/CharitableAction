package bigshots.charity.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import bigshots.charity.Contribute;
import bigshots.charity.R;
import bigshots.charity.io.AdManager;
import bigshots.charity.services.BannerPopupService;

/**
 * Created by root on 18/11/14.
 */
public class BannerPopup extends ViewGroup {
    private static final int mainViewHeight = 64, adHeight = 50, adWidth = 350;
    private static int duration = 750, touchSlop;
    final Intent service = new Intent(getContext(), BannerPopupService.class);
    private final OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            click(v);
        }
    };
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            finishAnimation();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            finishAnimation();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private final WindowManager windowManager;
    private State state;
    private MenuItem closeBanner, openApp, fullScreen, minimise;
    private MainBannerView mainView;
    private long downTime;
    // private BannerPopup popup;
    private Direction direction = Direction.LEFT;
    private AdManager adManager;
    private CurrentAnimation currentAnimation = CurrentAnimation.NONE;
    private View adView;
    private float adDistance = 0.425f;
    private int lastMenuItemDistance, menuItemWidth;
    private WindowManager.LayoutParams params;
    private float animated_value;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            runAnimations();
            invalidatePoster();
        }
    };
    private int spacing, initialX, initialY, initialTouchX, initialTouchY;
    //Todo implement x,t
    private int toX, fromX, x, y, w, h, screenHeight, screenWidth;
    private CurrentAnimation[] currentAnimations;
    private OnTouchListener mainViewOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = (int) event.getRawX();
                    initialTouchY = (int) event.getRawY();
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (((System.currentTimeMillis() - downTime) < 92) && ((event.getRawX() - initialTouchX) < touchSlop) && ((event.getRawY() - initialTouchY) < touchSlop))
                        click(mainView);

                    if (state == State.MINIMISED) {
                        currentAnimations = new CurrentAnimation[]{CurrentAnimation.SNAP_TO};
                        setToX();
                        fromX = x;
                        startAnimator();
                        setState(State.MINIMISED);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    setPosition((initialX + (int) (event.getRawX() - initialTouchX)), (initialY + (int) (event.getRawY() - initialTouchY)));
                    if (initialX + (int) (event.getRawX() - initialTouchX) > (screenWidth / 2))
                        direction = Direction.RIGHT;
                    else
                        direction = Direction.LEFT;
                    break;

            }
            invalidate();
            return true;
        }
    };

    public BannerPopup(Context context, WindowManager windowManager, WindowManager.LayoutParams params) {
        super(context);
        this.windowManager = windowManager;
        this.params = params;
        init();
    }


    //Todo check the screenSize, and scale the ad accordingly

    private void init() {
        animator.setDuration(duration);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);

        touchSlop = dpToPixels(4);

        adManager = new AdManager(getContext());
        adManager.loadBannerAd();

        mainView = new MainBannerView(getContext());
        mainView.setState(State.SHOWING_AD);
        mainView.setId(R.id.main_view);

        closeBanner = new MenuItem(getContext(), R.drawable.fa_default_background_default);
        closeBanner.setId(R.id.close_banner);

        minimise = new MenuItem(getContext(), R.drawable.white_arrow);
        minimise.setId(R.id.minimise);

        fullScreen = new MenuItem(getContext(), R.drawable.white_arrow);
        fullScreen.setId(R.id.full_screen);

        openApp = new MenuItem(getContext(), R.drawable.open_app);
        openApp.setId(R.id.open_app);

        adView = adManager.getBannerAd();
        adView.setPivotX(0);

        setState(State.SHOWING_AD);


        //todo windowManager.updateViewLayout(bubbleView, params);
        //todo windowManager.addView(bubbleView, params);
        //Todo  maxHeight, maxWidth
        //Todo addView()
        //Todo set direction

        setParameters();
        invalidate();
    }

    private void finishAnimation() {
        animated_value = 1;
        runAnimations();
        currentAnimation = CurrentAnimation.NONE;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    private void resolveAdSize() {
        final DisplayMetrics metrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        // w = (int) (screenWidth * 0.66f);
        // w = Math.min(w,adWidth);
        h = dpToPixels(mainViewHeight);
        w = dpToPixels(adWidth + (int) (mainViewHeight * adDistance));
        final int adW = dpToPixels(adWidth);
        final int adH = dpToPixels(adHeight);
        lastMenuItemDistance = adW - adH;
        menuItemWidth = adH;

        //Todo might have to mess around here   adView = adManager.getBannerAd();
        adView.setBackgroundColor(0xffbbff00);
        final int padding = (h - adH) / 2;
        addView(closeBanner, new LayoutParams(adH, adH));
        closeBanner.setOnClickListener(clickListener);
        closeBanner.setY(padding);

        addView(minimise, new LayoutParams(adH, adH));
        minimise.setOnClickListener(clickListener);
        minimise.setY(padding);

        addView(fullScreen, new LayoutParams(adH, adH));
        fullScreen.setOnClickListener(clickListener);
        fullScreen.setY(padding);

        addView(openApp, new LayoutParams(adH, adH));
        openApp.setOnClickListener(clickListener);
        openApp.setY(padding);

        spacing = (adW - (adH * 4)) / 4;
        addView(adView, new LayoutParams(adH, adW));
        addView(mainView, new LayoutParams(h, h));
        mainView.setOnTouchListener(mainViewOnTouchListener);

        currentAnimation = CurrentAnimation.HIDE_MENU;

        updateMenuItemDistance(1);
        resetMenuItemPositions();
        currentAnimation = CurrentAnimation.NONE;

        params.width = w;
        params.height = h;
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMenuItemVisibility(int visibility) {
        openApp.setVisibility(visibility);
        closeBanner.setVisibility(visibility);
        fullScreen.setVisibility(visibility);
        minimise.setVisibility(visibility);
    }

    private void setParameters() {
        resolveAdSize();
        //Todo fill in

    }

    public void setState(State state) {
        this.state = state;
    }

    private void click(View v) {
        Toast.makeText(getContext(), String.valueOf(state), Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.main_view:
                switch (state) {
                    case SHOWING_MENU:
                        setState(State.SHOWING_AD);
                        currentAnimation = CurrentAnimation.HIDE_MENU;
                        currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_MENU, CurrentAnimation.SHOW_AD};
                        break;
                    case SHOWING_AD:
                        setState(State.SHOWING_MENU);
                        currentAnimation = CurrentAnimation.HIDE_AD;
                        currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_AD, CurrentAnimation.SHOW_MENU};
                        break;
                    case MINIMISED:
                        setState(State.SHOWING_AD);
                        currentAnimation = CurrentAnimation.SNAP_TO;
                        currentAnimations = new CurrentAnimation[]{CurrentAnimation.SNAP_TO, CurrentAnimation.SHOW_AD};
                        toX = x;
                        break;
                }
                startAnimator();
                break;
            case R.id.close_banner:
                Toast.makeText(getContext(), "close", Toast.LENGTH_SHORT).show();
                try {
                    getContext().stopService(service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.open_app:
                Toast.makeText(getContext(), "Open app", Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(getContext(), Contribute.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            case R.id.minimise:
                Toast.makeText(getContext(), "minimise", Toast.LENGTH_SHORT).show();
                currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_MENU, CurrentAnimation.SNAP_TO};
                setToX();
                fromX = x;
                startAnimator();
                setState(State.MINIMISED);
                break;
            case R.id.full_screen:
                Toast.makeText(getContext(), "Full screen", Toast.LENGTH_SHORT).show();
                adManager.loadFullscreenAd();
                adManager.getFullscreenAd();
                break;
        }

    }

    public void setToX() {
        if (direction == direction.LEFT)
            toX = -((int) (0.2f * getMeasuredHeight()));
        else
            toX = screenWidth - ((int) (0.2f * getMeasuredHeight()));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int adViewPadding = (getMeasuredHeight() - adView.getMeasuredHeight()) / 2;
        adViewPadding = adViewPadding < 0 ? 0 : adViewPadding;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if ((child instanceof MenuItem) && (state == State.SHOWING_MENU)) {
                child.layout((int) child.getX(), adViewPadding, (int) child.getX() + child.getMeasuredWidth(), child.getMeasuredHeight() - adViewPadding);
            }
        }

        if ((state == State.SHOWING_AD) && (adView.getScaleX() > 0.02f))
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
            //Todo don't measure menu items if not showing menu, don't measure ad if not showing ad
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredHeight += child.getMeasuredHeight();
            measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth());
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
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void setDistance() {
        if (animator.isRunning()) {
            Log.e("setting", "distance");
            closeBanner.setDistance(getDistance(closeBanner));
            openApp.setDistance(getDistance(openApp));
            minimise.setDistance(getDistance(minimise));
            fullScreen.setDistance(getDistance(fullScreen));
        }
    }

    private float getDistance(View v) {
        if (v instanceof MainBannerView)
            return 0;

        final float base = (mainView.getWidth() / 2) + spacing;
        final int mainViewCenter = mainView.getLeft() + (mainView.getWidth() / 2);

        switch (direction) {
            case LEFT:
                return (v.getLeft() - mainViewCenter) / base;
            case RIGHT:
                return (mainViewCenter - v.getLeft()) / base;
        }

        return 0;
    }

    private void resetMenuItemPositions() {

        switch (direction) {
            case LEFT:
                closeBanner.setX(0);
                openApp.setX(0 - spacing - menuItemWidth);
                fullScreen.setX(0 - spacing - menuItemWidth - spacing - menuItemWidth);
                minimise.setX(0 - spacing - menuItemWidth - spacing - menuItemWidth - spacing - menuItemWidth);
                break;
            case RIGHT:
                closeBanner.setX(0);
                openApp.setX(0 + spacing + menuItemWidth);
                fullScreen.setX(0 + spacing + menuItemWidth + spacing + menuItemWidth);
                minimise.setX(0 + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth);
                break;
        }

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
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void invalidate() {
        super.invalidate();
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invalidate(Rect dirty) {
        super.invalidate(dirty);
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSize(int width, int height) {
        resize(width, height);
    }

    private void runAnimations() {
        if (currentAnimations.length > 1) {
            if (animated_value > 0.5f)
                currentAnimation = currentAnimations[1];
            else
                currentAnimation = currentAnimations[0];

            if (animated_value == 1)
                animated_value = 0.999f;
        } else {
            if (animated_value > 0.5f) {
                animator.cancel();
                return;
            }
        }
        final float val = animated_value % 0.5f;
        switch (currentAnimation) {
            //Todo use interpolator from hunid jumps
            case SHOW_AD:
                //Todo check
                adView.setScaleX(val / 0.5f);
                break;
            case HIDE_AD:
                adView.setScaleX((0.5f - val) / 0.5f);
                break;
            case SHOW_MENU:
                updateMenuItemDistance(val / 0.5f);
                break;
            case HIDE_MENU:
                updateMenuItemDistance((0.5f - val) / 0.5f);
                break;
            case SNAP_TO:
                snap(val / 0.5f);
                break;
        }

    }

    private void snap(float progress) {
        setPosition(fromX + Math.round((toX - fromX) * progress), params.y);
    }

    private void updateMenuItemDistance(float distance) {
        setMenuItemsX((getHeight() / 2) + Math.round(distance * lastMenuItemDistance));
        setDistance();
    }

    private void setMenuItemsX(int x) {
        switch (direction) {
            case LEFT:
                closeBanner.setX(x);
                openApp.setX(x - spacing - menuItemWidth);
                fullScreen.setX(x - spacing - menuItemWidth - spacing - menuItemWidth);
                minimise.setX(x - spacing - menuItemWidth - spacing - menuItemWidth - spacing - menuItemWidth);
                break;
            case RIGHT:
                closeBanner.setX(x + spacing + menuItemWidth);
                openApp.setX(x + spacing + menuItemWidth + spacing + menuItemWidth);
                fullScreen.setX(x + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth);
                minimise.setX(x + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth);
                break;
        }

    }

    public enum State {
        SHOWING_MENU, SHOWING_AD, MINIMISED
    }

    private enum Direction {
        LEFT, RIGHT
    }

    private enum CurrentAnimation {
        SHOW_AD, HIDE_AD, SHOW_MENU, HIDE_MENU, NONE, SNAP_TO
    }

}
