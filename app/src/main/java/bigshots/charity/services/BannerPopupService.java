package bigshots.charity.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;

import bigshots.charity.views.BannerPopup;

/**
 * Created by root on 18/11/14.
 */
public class BannerPopupService extends Service {
    public static boolean isServiceRunning;
    public static BannerPopup bannerPopup;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

//        params.width = w;
//        params.height = h;

        isServiceRunning = true;
        if (bannerPopup == null)
            bannerPopup = new BannerPopup(this, windowManager, params);

        bannerPopup.setBackgroundColor(0xffffbb00);
        params = new WindowManager.LayoutParams(bannerPopup.getW(), bannerPopup.getH(), WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;
        windowManager.addView(bannerPopup, params);
        windowManager.updateViewLayout(bannerPopup, params);
    }

    @Override
    public boolean stopService(Intent name) {
        isServiceRunning = false;
        return super.stopService(name);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        isServiceRunning = true;
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
        try {
            windowManager.removeViewImmediate(bannerPopup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
