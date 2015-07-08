package bigshots.people_helping_people.views;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import bigshots.people_helping_people.MainActivity;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AdManager;
import bigshots.people_helping_people.services.BannerPopupService;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class BannerPopup extends FrameLayout {
    //hidemenu-showmenu in next update
    //google plus

    private static final int mainViewHeight = 64, adHeight = 50, adWidth = 350;
    private static final int duration = 1100;
    private static final int[] screenPos = new int[2];
    private static int touchSlop;
    private static Runnable runnable;
    private static View fullScreenAd, videoAd;
    private static View mainView;
    private final Intent service = new Intent(getContext(), BannerPopupService.class);
    private final WindowManager windowManager;
    private final float adDistance = 0.625f;
    private final WindowManager.LayoutParams params;
    private boolean settingUp = true;
    private long downTime;
    private int viewTouchX;
    // private BannerPopup popup;
    private Direction direction = Direction.LEFT;
    private AdManager adManager;//, fullScreenAdmanager;
    private final OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            click(v);
        }
    };
    private boolean animationFinished;
    private int lastMenuItemDistance, menuItemWidth;
    private int x, y;
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
                    break;
                case MotionEvent.ACTION_MOVE:
                    setPosition((initialX + (int) (event.getRawX() - initialTouchX)), (initialY + (int) (event.getRawY() - initialTouchY)));
                    mainView.getLocationOnScreen(screenPos);
                    if ((screenPos[0] + (mainView.getWidth() / 2)) > (screenWidth / 2))
                        direction = Direction.RIGHT;
                    else
                        direction = Direction.LEFT;
                    break;
            }
            invalidate();
            return true;
        }
    };
    private int spacing, initialX, initialY, initialTouchX, initialTouchY;
    //implement x,t
    private int w, h, screenHeight, screenWidth;
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
        touchSlop = dpToPixels(4);
        final int viewW = dpToPixels(208);
        final int viewH = dpToPixels(70);
        setLayoutParams(new LayoutParams(viewW, viewH));

        adManager = new AdManager(getContext());
        adManager.loadFullscreenAd(false);
        adManager.loadVideoAd(false);

        final View container = View.inflate(getContext(), R.layout.banner_popup, null);

        mainView = container.findViewById(R.id.main_view);
        videoAd = container.findViewById(R.id.video_ad);
        fullScreenAd = container.findViewById(R.id.full_screen);

        mainView.setOnTouchListener(mainViewOnTouchListener);
        videoAd.setOnClickListener(clickListener);
        fullScreenAd.setOnClickListener(clickListener);

        settingUp = false;
        addView(container, new LayoutParams(viewW, viewH));
        resize(viewW, viewH);
    }

    private void reset() {
        int resetX = screenWidth - params.width;
        resetX = resetX < 0 ? 0 : resetX;
        params.x = resetX;
        update();
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    private void click(View v) {
        switch (v.getId()) {
            case R.id.open_app:
                final Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            case R.id.video_ad:
                showVideoAd();
                break;
            case R.id.full_screen:
                showFullScreenAd();
                break;
        }
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

    @Override
    public void invalidate() {
        update();
        super.invalidate();
    }

    private void update() {
        try {
            setXY();
            windowManager.updateViewLayout(this, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPosition(int x, int y) {
        params.x = ((x < 0) || (x + params.width > screenWidth)) ? params.x : x;
        if (y > 0 && y + mainView.getHeight() < screenHeight)
            params.y = y;
        update();
    }

    public void loadFullScreenAd() {
        adManager.loadFullscreenAd(false);
    }

    public void showFullScreenAd() {
        adManager.showFullscreenAd();
    }

    public void loadVideoAd() {
        adManager.loadVideoAd(false);
    }

    public void showVideoAd() {
        adManager.showVideoAd();
    }

    public void refresh() {
        setXY();
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        minX = (int) (0 - (mainView.getWidth() * 0.2f));
        maxX = (int) (screenWidth - mainView.getWidth() * 0.8f);

        setPosition(((int) (x * screenWidth)), ((int) (y * screenHeight)));
    }

    private void setXY() {
        x = Math.round(params.x / (float) screenWidth);
        y = Math.round(params.y / (float) screenHeight);
    }

    private boolean outOfBounds() {
        return ((x < 0) || (x + params.width > screenWidth));
    }

    private enum Direction {
        LEFT, RIGHT
    }

}
