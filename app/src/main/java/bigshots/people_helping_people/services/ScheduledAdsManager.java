package bigshots.people_helping_people.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import bigshots.people_helping_people.Contribute;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AdManager;
import bigshots.people_helping_people.utilities.Utils;

/**
 * Created by root on 11/12/14.
 */
public class ScheduledAdsManager extends Service {
    private static PendingIntent alarmIntent;
    private static AlarmManager alarmManager;
    private static InterstitialAd fullScreenAd;
    private static AdManager adManager;
    private static boolean loadAd;
    private static Context context;

    private static int NOTIFICATION_ID = 455129802;
    private static boolean serviceRunning = false;

    public static void loadFullScreenAd() {
        adManager.loadFullscreenAd();
    }

    public static void showFullScreenAd() {
        if (adManager == null)
            getAds();

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
                    try {
                        Toast.makeText(context, "Failed to load ad", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public static void scheduleNext(Context context, boolean load) {
        loadAd = load;
        try {
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

    public static void showNotification(Context context) {
        try {
            if (!serviceRunning)
                context.startService(new Intent(context, ScheduledAdsManager.class));

            final NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.icon_blue)
                            .setContentTitle("Scheduled Ads Active")
                            .setContentText("Click to manage");
            final Intent intent = new Intent(context, Contribute.class);
            builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelNotification(Context context) {
        try {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
            context.stopService(new Intent(context, ScheduledAdsManager.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getAds() {
        try {
            adManager = new AdManager(context);
            adManager.loadFullscreenAd();
            fullScreenAd = adManager.getFullscreenAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isServiceRunning() {
        return serviceRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "service started", Toast.LENGTH_LONG).show();
        serviceRunning = true;
        context = this;
        getAds();
        showNotification(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        serviceRunning = false;
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showNotification(context);
            try {
                if (loadAd) {
                    loadFullScreenAd();
                    scheduleNext(context, false);
                    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.TOAST_BEFORE_BOOL, true))
                        Toast.makeText(context, "Showing Ad in 10 secs", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    showFullScreenAd();
                    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.LOOP_SCHEDULE, false))
                        scheduleNext(context, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
