package bigshots.people_helping_people.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

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
            try {
                if (myIntent.getAction().equals(ROTATION_BROADCAST)) {
                    bannerPopup.rotate(getResources().getConfiguration().orientation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public static boolean isServiceRunning;
    private static BannerPopup bannerPopup;
    private static PendingIntent alarmIntent;
    private static AlarmManager alarmManager;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    public static void scheduleNext(Context context) {

        try {
            int mins = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utils.FULLSCREEN_AD_FREQUENCY_MINUTES, 0);
            long when = System.currentTimeMillis() + (mins * 3000);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final Intent intent = new Intent(context, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            if (mins == 0) {
                alarmManager.cancel(alarmIntent);
                return;
            } else
                alarmManager.set(AlarmManager.RTC, when, alarmIntent);

//            Intent i = new Intent(context, AlarmReceiver.class);
//            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.TOAST_BEFORE_BOOL, true))
//            i.putExtra(Utils.SCHEDULE, Utils.SCHEDULE);
//            alarmManager.set(AlarmManager.RTC, when - 10000, PendingIntent.getBroadcast(context, 0, i, 0));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;

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
            try {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

                //Todo implement this on the next update
                //Toast.makeText(context, String.format("Connected : %b", isConnected), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class BootUp extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.AUTO_START_BOOL, false)) {
                    Intent myIntent = new Intent(context, BannerPopupService.class);
                    context.startService(myIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Toast.makeText(context, "show", Toast.LENGTH_LONG).show();
                if (intent.getStringExtra(Utils.LOAD_AD).equals(Utils.LOAD_AD)) {
                    Toast.makeText(context, "load", Toast.LENGTH_LONG).show();
                    bannerPopup.loadFullScreenAd();

                    if (intent.getStringExtra(Utils.SCHEDULE).equals(Utils.SCHEDULE))
                        Toast.makeText(context, "Showing Ad in 10 secs", Toast.LENGTH_LONG).show();
                    return;
                }


                bannerPopup.showFullScreenAd();
                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.LOOP_SCHEDULE, false))
                    scheduleNext(context);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
