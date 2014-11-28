package bigshots.people_helping_people.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;

import bigshots.people_helping_people.utilities.Utils;
import bigshots.people_helping_people.views.BannerPopup;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class BannerPopupService extends Service {
    private static final String ROTATION_BROADCAST = "android.intent.action.CONFIGURATION_CHANGED";
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent myIntent) {
            if (myIntent.getAction().equals(ROTATION_BROADCAST)) {
                bannerPopup.rotate(getResources().getConfiguration().orientation);
            }
        }
    };
    public static boolean isServiceRunning;
    private static BannerPopup bannerPopup;
    private static PendingIntent alarmIntent;
    private static AlarmManager alarmManager;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private static void scheduleNext(Context context) {
        try {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int mins = preferences.getInt(Utils.FULLSCREEN_AD_FREQUENCY_MINUTES, 0);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(Utils.SCHEDULE, Utils.SCHEDULE);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            if (mins == 0)
                alarmManager.cancel(alarmIntent);
            else
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + (mins * 60000), alarmIntent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;

        scheduleNext(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        bannerPopup = new BannerPopup(this, windowManager, params);

        windowManager.addView(bannerPopup, params);
        windowManager.updateViewLayout(bannerPopup, params);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ROTATION_BROADCAST);
        registerReceiver(broadcastReceiver, filter);

    }

    @Override
    public boolean stopService(Intent name) {
        isServiceRunning = false;
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return START_STICKY;
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
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

            //Todo implement this on the next update
            //Toast.makeText(context, String.format("Connected : %b", isConnected), Toast.LENGTH_SHORT).show();
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            scheduleNext(context);
        }
    }
}
