package bigshots.charity.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by root on 18/11/14.
 */
public class BannerPopupService extends Service {
    public static boolean isServiceRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
        System.out.println("start");
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
        System.out.println("stop");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
