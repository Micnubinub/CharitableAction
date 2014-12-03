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
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
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
    private static boolean loadAd;
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams params;

    public static void scheduleNext(Context context, boolean load) {
        loadAd = load;
        try {
            // Toast.makeText(context, "LOAD_AD sched" + String.valueOf(loadAd), Toast.LENGTH_LONG).show();
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (load) {
                Log.e("Scheduling", String.valueOf(loadAd));
                int mins = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utils.FULLSCREEN_AD_FREQUENCY_MINUTES, 0);
                Intent i = new Intent(context, AlarmReceiver.class);
                if (mins == 0) {
                    alarmManager.cancel(alarmIntent);
                    return;
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC, (System.currentTimeMillis() + (mins * 60000)) - 10000, PendingIntent.getBroadcast(context, 0, i, 0));
                    } else
                        alarmManager.set(AlarmManager.RTC, (System.currentTimeMillis() + (mins * 60000)) - 10000, PendingIntent.getBroadcast(context, 0, i, 0));
                }
            } else {
                final Intent intent = new Intent(context, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 10000, alarmIntent);
                } else
                    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, alarmIntent);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BannerPopup getBannerPopup() {
        return bannerPopup;
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

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Utils.LOOP_SCHEDULE, false) &&
                PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Utils.ENABLE_SCHEDULED_ADS, false))
            scheduleNext(this, true);
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
            if (isServiceRunning) {
                try {
                    if (loadAd) {
                        bannerPopup.loadFullScreenAd();
                        scheduleNext(context, false);
                        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.TOAST_BEFORE_BOOL, true))
                            Toast.makeText(context, "Showing Ad in 10 secs", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        bannerPopup.showFullScreenAd();
                        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.LOOP_SCHEDULE, false))
                            scheduleNext(context, true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
