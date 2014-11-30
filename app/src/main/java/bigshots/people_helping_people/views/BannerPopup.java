package bigshots.people_helping_people.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import bigshots.people_helping_people.Contribute;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AdManager;
import bigshots.people_helping_people.services.BannerPopupService;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class BannerPopup extends ViewGroup {
    //Todo add intro slides 1/2 done
    //hidemenu-showmenu in next update
    //google plus

    private static final int mainViewHeight = 64, adHeight = 50, adWidth = 350;
    private static final int duration = 1100;
    private static int touchSlop;
    private static InterstitialAd fullScreenAd;
    private static Runnable runnable;
    private final Intent service = new Intent(getContext(), BannerPopupService.class);
    private final OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            click(v);
        }
    };
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final WindowManager windowManager;
    private final float adDistance = 0.625f;
    private final WindowManager.LayoutParams params;
    private State state;
    private MenuItem closeBanner, fullScreen, minimise;
    private MainBannerView mainView;
    private boolean settingUp = true;
    private long downTime;
    private int viewTouchX;
    private int[] screenPos = new int[2];
    private final OnTouchListener mainViewOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = (int) event.getRawX();
                    initialTouchY = (int) event.getRawY();
                    viewTouchX = (int) event.getX();
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    setXY();

                    if (((System.currentTimeMillis() - downTime) < 180) && ((event.getRawX() - initialTouchX) < touchSlop) && ((event.getRawY() - initialTouchY) < touchSlop)) {
                        click(mainView);
                        mainView.rippleOut();
                    }

                    if (state == State.MINIMISED)
                        minimise();

                    break;
                case MotionEvent.ACTION_MOVE:
                    setPosition((initialX + (int) (event.getRawX() - initialTouchX)), (initialY + (int) (event.getRawY() - initialTouchY)));
                    mainView.getLocationOnScreen(screenPos);
                    if ((screenPos[0] + (mainView.getWidth() / 2)) > (screenWidth / 2))
                        setDirection(Direction.RIGHT);
                    else
                        setDirection(Direction.LEFT);
                    break;
            }
            invalidate();
            return true;
        }
    };
    // private BannerPopup popup;
    private Direction direction = Direction.LEFT;
    private AdManager adManager;//, fullScreenAdmanager;
    private boolean animationFinished;
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            animationFinished = false;
            if (state == State.SHOWING_MENU) {
                addMenuItems();
            } else if (state == State.SHOWING_AD) {
                adAdView();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!animationFinished)
                finishAnimation();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (!animationFinished)
                finishAnimation();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private CurrentAnimation currentAnimation = CurrentAnimation.NONE;
    private View adView;
    private int lastMenuItemDistance, menuItemWidth;
    private float animated_value, unit, x, y;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            runAnimations();
            invalidatePoster();
        }
    };
    private int spacing, initialX, initialY, initialTouchX, initialTouchY;
    //implement x,t
    private int toX, fromX, padding, adH, adW, w, h, screenHeight, screenWidth;
    private CurrentAnimation[] currentAnimations;
    private int maxX, minX;

    public BannerPopup(Context context, WindowManager windowManager, WindowManager.LayoutParams params) {
        super(context);
        this.windowManager = windowManager;
        this.params = params;
        init();
    }

    public static void setRunnable(Runnable runnable) {
        BannerPopup.runnable = runnable;
    }

    private void init() {
        animator.setDuration(duration);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);

        touchSlop = dpToPixels(4);

        adManager = new AdManager(getContext());
        adManager.loadBannerAd();
        adView = adManager.getBannerAd();
        adView.setPivotX(0);

        mainView = new MainBannerView(getContext());
        mainView.setId(R.id.main_view);
        setState(State.SHOWING_AD);

        closeBanner = new MenuItem(getContext(), R.drawable.close);
        closeBanner.setId(R.id.close_banner);

        minimise = new MenuItem(getContext(), R.drawable.minimise);
        minimise.setId(R.id.minimise);

        fullScreen = new MenuItem(getContext(), R.drawable.full_screen_ad);
        fullScreen.setId(R.id.full_screen);

        setParameters();
        invalidate();
        settingUp = false;


    }

    private void finishAnimation() {
        animationFinished = true;
        animated_value = 1;
        runAnimations();


        if (state == State.SHOWING_AD)
            removeMenuItems();
        else if (state == State.SHOWING_MENU)
            removeAdView();
        else if (state == State.MINIMISED) {
            removeMenuItems();
            removeAdView();
        }

        if (currentAnimation == CurrentAnimation.SHOW_AD && outOfBounds())
            reset();
        currentAnimation = CurrentAnimation.NONE;

    }

    private void reset() {
        int resetX = screenWidth - params.width;
        resetX = resetX < 0 ? 0 : resetX;
        params.x = resetX;
        update();
    }

    private void addMenuItems() {
        try {
            final int menuItemX = mainView.getLeft() + ((mainView.getWidth() - menuItemWidth) / 2);
            addView(closeBanner, new LayoutParams(adH, adH));
            closeBanner.setOnClickListener(clickListener);
            closeBanner.setX(menuItemX);

            addView(minimise, new LayoutParams(adH, adH));
            minimise.setOnClickListener(clickListener);
            minimise.setX(menuItemX);

            addView(fullScreen, new LayoutParams(adH, adH));
            fullScreen.setOnClickListener(clickListener);
            fullScreen.setX(menuItemX);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainView.bringToFront();
    }

    private void removeMenuItems() {
        try {
            removeView(closeBanner);
            removeView(minimise);
            removeView(fullScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainView.bringToFront();
    }

    private void adAdView() {
        try {
            adView.setScaleX(0);
            addView(adView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            adView.setScaleX(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainView.bringToFront();
    }

    private void removeAdView() {
        try {
            removeView(adView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainView.bringToFront();
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    private void resolveAdSize() {
        refresh();

        h = dpToPixels(mainViewHeight);
        w = dpToPixels(adWidth + (int) (mainViewHeight * adDistance));

        adW = dpToPixels(adWidth);
        adH = dpToPixels(adHeight);

        final float scale = getScale(screenWidth, w, adW);
        if (scale < 0.9999f) {
            h = (int) (h * scale);
            w = (int) (w * scale);
            adW = (int) (adW * scale);
            adH = (int) (adH * scale);
        }

        lastMenuItemDistance = (int) (adH * 3.5f) + (h / 2);
        menuItemWidth = adH;
        unit = spacing + h;

        padding = (h - adH) / 2;
        spacing = adH / 5;
        addView(adView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(mainView, new LayoutParams(h, h));
        mainView.setOnTouchListener(mainViewOnTouchListener);
        setPosition(0, screenHeight - h - spacing - spacing - spacing);

        params.width = w;
        params.height = h;

        refresh();
        update();
    }

    private float getScale(int screenWidth, int viewWidth, int adWidth) {
        //fill in in next update
        return 1f;
    }

    private void setParameters() {
        resolveAdSize();

    }

    public void setState(State state) {
        this.state = state;
        mainView.setState(state);
    }

    private void click(View v) {
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
                        fromX = direction == Direction.LEFT ? minX : maxX;
                        toX = getFromX();
                        break;
                }
                startAnimator();
                break;
            case R.id.close_banner:
                try {
                    getContext().stopService(service);
                    Toast.makeText(getContext(), "Thanks. We appreciate your time", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.open_app:
                final Intent intent = new Intent(getContext(), Contribute.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            case R.id.minimise:
                currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_MENU, CurrentAnimation.SNAP_TO};
                setXY();
                setToX();
                fromX = getFromX();
                startAnimator();
                setState(State.MINIMISED);
                break;
            case R.id.full_screen:
                showFullScreenAd();
                break;
        }

    }

    public int getFromX() {
        return ((int) (x * screenWidth));
    }

    public void setToX() {
        if (direction == Direction.LEFT)
            toX = -((int) (0.2f * mainView.getMeasuredWidth()));
        else
            toX = screenWidth - ((int) (0.8f * mainView.getMeasuredWidth()));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int adViewPadding = (getMeasuredHeight() - adH) / 2;
        adViewPadding = adViewPadding < 0 ? 0 : adViewPadding;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if ((child instanceof MenuItem) && (state == State.SHOWING_MENU)) {
                child.layout((int) child.getX(), adViewPadding, (int) child.getX() + adH, getMeasuredHeight() - adViewPadding);
            }
        }

        if ((state == State.SHOWING_AD) && (adView.getScaleX() > 0.01f))
            adView.layout((int) (getMeasuredHeight() * adDistance), adViewPadding, getMeasuredWidth(), getMeasuredHeight() - adViewPadding);
        mainView.layout(0, 0, getMeasuredHeight(), getMeasuredHeight());

//        switch (direction) {
//            case LEFT:
//                if ((state == State.SHOWING_AD) && (adView.getScaleX() > 0.01f))
//                    adView.layout((int) (getMeasuredHeight() * adDistance), adViewPadding, getMeasuredWidth(), getMeasuredHeight() - adViewPadding);
//                mainView.layout(0, 0, getMeasuredHeight(), getMeasuredHeight());
//                break;
//            case RIGHT:
//                if ((state == State.SHOWING_AD) && (adView.getScaleX() > 0.01f))
//                    adView.layout(0, adViewPadding, getWidth() - (int) (getMeasuredHeight() * adDistance), getMeasuredHeight() - adViewPadding);
//                mainView.layout(getWidth() - getHeight(), 0, getMeasuredWidth(), getMeasuredHeight());
//                break;
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);

            if (state != State.SHOWING_MENU && child instanceof MenuItem)
                continue;

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
        params.height = height;
        params.width = width;
        update();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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
            closeBanner.setDistanceScale(getDistance(closeBanner));
            minimise.setDistanceScale(getDistance(minimise));
            fullScreen.setDistanceScale(getDistance(fullScreen));
        }
    }

    private float getDistance(View v) {
        if (!(v instanceof MenuItem))
            return 0;

//        switch (direction) {
//            case LEFT:
//                return (v.getX() - mainView.getLeft()) / unit;
//            case RIGHT:
//                return (mainView.getRight() - v.getRight()) / unit;
//        }

        return (v.getX() - mainView.getLeft()) / unit;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;

//        switch (state) {
//            case SHOWING_AD:
//                currentAnimation = CurrentAnimation.HIDE_AD;
//                currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_AD, CurrentAnimation.SHOW_AD};
//
//                break;
//            case SHOWING_MENU:
//                currentAnimation = CurrentAnimation.HIDE_MENU;
//                currentAnimations = new CurrentAnimation[]{CurrentAnimation.HIDE_MENU, CurrentAnimation.SHOW_MENU};
//
//                break;
//        }
//        startAnimator();
    }

    private void startAnimator() {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }
        animator.start();
    }

    private void update() {
        try {
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPosition(int x, int y) {
//        switch (direction) {
//            case LEFT:
//                params.x = (x >= minX) ? x : minX;
//                break;
//            case RIGHT:
//                x = (x <= maxX) ? x : maxX;
//                x -= (getWidth() - (mainView.getWidth() / 2));
//                params.x = x;
//                break;
//        }


        params.x = ((x < 0) || (x + params.width > screenWidth)) ? params.x : x;

        if (y > 0 && y + mainView.getHeight() < screenHeight)
            params.y = y;
        update();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (runnable != null)
            runnable.run();
        runnable = null;
        if (!settingUp)
            switch (state) {
                case MINIMISED:
                    resize(mainView.getWidth(), mainView.getHeight());
                    break;
                case SHOWING_AD:
                    resize((int) ((mainView.getWidth() * adDistance) + adView.getWidth()), mainView.getHeight());
                    break;
                case SHOWING_MENU:
                    resize(mainView.getWidth() + ((spacing + minimise.getWidth()) * 3), getHeight());
                    break;
            }
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        if (!settingUp)
            switch (state) {
                case MINIMISED:
                    resize(mainView.getWidth(), mainView.getHeight());
                    break;
                case SHOWING_AD:
                    resize((int) ((mainView.getWidth() * adDistance) + adView.getWidth()), mainView.getHeight());
                    break;
                case SHOWING_MENU:
                    resize(mainView.getWidth() + ((spacing + minimise.getWidth()) * 3), getHeight());
                    break;
            }
    }

    @Override
    public void invalidate(Rect dirty) {
        super.invalidate(dirty);
        if (!settingUp)
            switch (state) {
                case MINIMISED:
                    resize(mainView.getWidth(), mainView.getHeight());
                    break;
                case SHOWING_AD:
                    resize((int) ((mainView.getWidth() * adDistance) + adView.getWidth()), mainView.getHeight());
                    break;
                case SHOWING_MENU:
                    resize(mainView.getWidth() + ((spacing + minimise.getWidth()) * 3), getHeight());
                    break;
            }
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
            currentAnimation = currentAnimations[0];
            if (animated_value >= 0.5f) {
                if (animator.isRunning()) {
                    animator.cancel();
                }
                animated_value = 0.4998f;
            }
        }
        final float val = animated_value % 0.5f;
        switch (currentAnimation) {
            //use interpolator from hunid jumps
            case SHOW_AD:
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
        Log.e("snapto", String.format("prog,val : %f, %d", progress, fromX + Math.round((toX - fromX) * progress)));
    }

    private void updateMenuItemDistance(float distance) {
        distance = Math.abs(distance);
        setMenuItemsX(Math.round(distance * lastMenuItemDistance));
        setDistance();
        if ((distance < 0.1f) && (currentAnimation == CurrentAnimation.HIDE_MENU))
            finishAnimation();

    }

    private void setMenuItemsX(int x) {

//        switch (direction) {
//            case LEFT:
        int left = (mainView.getWidth() / 2) + x - menuItemWidth;
        closeBanner.setX(left);
        fullScreen.setX(left - spacing - menuItemWidth);
        minimise.setX(left - spacing - menuItemWidth - spacing - menuItemWidth);
//                break;
//            case RIGHT:
//                int right = (mainView.getLeft() + (mainView.getWidth() / 2)) - x;
//                closeBanner.setX(right + spacing + menuItemWidth);
//                openApp.setX(right + spacing + menuItemWidth + spacing + menuItemWidth);
//                fullScreen.setX(right + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth);
//                minimise.setX(right + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth + spacing + menuItemWidth);
//                break;
        // }

    }

    public void loadFullScreenAd() {
        adManager.loadFullscreenAd();
    }

    public void showFullScreenAd() {
        if (adManager.getFullscreenAd().isLoaded())
            adManager.getFullscreenAd().show();
        else {
            loadFullScreenAd();
            fullScreenAd = adManager.getFullscreenAd();
            fullScreenAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    fullScreenAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    Toast.makeText(getContext(), "Failed to load ad", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void rotate(int orientation) {
        refresh();
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:

                break;
            default:

                break;
        }
    }

    private void refresh() {
        final DisplayMetrics metrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        minX = (int) (0 - (mainView.getWidth() * 0.2f));
        maxX = (int) (screenWidth - mainView.getWidth() * 0.8f);

        setPosition(((int) (x * screenWidth)), ((int) (y * screenHeight)));
    }

    public void forceMinimise() {
        Toast.makeText(getContext(), "force minimise", Toast.LENGTH_SHORT).show();
        removeMenuItems();
        removeAdView();
        setState(State.MINIMISED);
    }

    public void minimise() {
        currentAnimations = new CurrentAnimation[]{CurrentAnimation.SNAP_TO};
        setToX();
        fromX = getFromX();
        startAnimator();
        setState(State.MINIMISED);
    }

    private void setXY() {
        x = params.x / (float) screenWidth;
        y = params.y / (float) screenHeight;
    }

    private boolean outOfBounds() {
        return ((x < 0) || (x + params.width > screenWidth));
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
