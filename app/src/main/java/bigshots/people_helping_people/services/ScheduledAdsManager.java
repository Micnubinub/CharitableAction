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

import bigshots.people_helping_people.MainMenu;
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
    private static int SCHEDULED_ADS_NOTIFICATION_ID = 455129802;
    private static int REMINDER_NOTIFICATION_ID = 455129854;
    private static boolean serviceRunning = false;

    public static void loadFullScreenAd() {
        adManager.loadFullscreenAd();
    }

    public static void showFullScreenAd() {
        if (adManager == null)
            getAds();

        if (adManager.getFullscreenAd().isLoaded()) {
            adManager.getFullscreenAd().show();
            Utils.addScore(context, 15);
        } else {
            loadFullScreenAd();
            fullScreenAd = adManager.getFullscreenAd();
            fullScreenAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    //Todo move to opened
                    try {
                        Utils.addScore(context, 15);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fullScreenAd.show();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    Log.e("schedules", "opened");
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
                Intent i = new Intent(context, AdAlarmReceiver.class);
                if (mins == 0) {
                    cancelNotification(context);
                    return;
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC, (System.currentTimeMillis() + (mins * 60000)) - 10000, PendingIntent.getBroadcast(context, 0, i, 0));
                    } else
                        alarmManager.set(AlarmManager.RTC, (System.currentTimeMillis() + (mins * 60000)) - 10000, PendingIntent.getBroadcast(context, 0, i, 0));
                }
            } else {
                final Intent intent = new Intent(context, AdAlarmReceiver.class);
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
                            .setSmallIcon(R.drawable.small_notification_icon)
                            .setContentTitle("Scheduled Ads Active")
                            .setContentText("Click to manage")
                            .setOngoing(true);

            final Intent intent = new Intent(context, MainMenu.class);
            builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(SCHEDULED_ADS_NOTIFICATION_ID, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelNotification(Context context) {
        try {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(SCHEDULED_ADS_NOTIFICATION_ID);
            context.stopService(new Intent(context, ScheduledAdsManager.class));
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AdAlarmReceiver.class), 0);
            alarmManager.cancel(alarmIntent);
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

    public static void scheduleNextReminder(Context context) {
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utils.ENABLE_REMINDER, false))
            return;

        final int hr = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utils.REMINDER_TIME_HOURS_INT, 12);
        final int mins = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utils.REMINDER_TIME_MINS_INT, 30);

        final long now = System.currentTimeMillis();
        final int nowHr = Utils.getHours(now);
        final int nowMin = Utils.getMinutes(now);

        final boolean hrLessThan = (hr < nowHr);
        final boolean minLessThan = (mins <= nowMin);
        int difHr, difMin;

        if (hrLessThan && minLessThan) {
            difHr = (hr - nowHr + 24) * 60;
            difMin = (mins - nowMin + 60);
        } else if (hrLessThan) {
            difHr = (hr - nowHr + 24) * 60;
            difMin = (mins - nowMin);
        } else if (minLessThan) {
            difHr = (hr - nowHr) * 60;
            difMin = (mins - nowMin + 60);
        } else {
            difHr = (hr - nowHr) * 60;
            difMin = (mins - nowMin);
        }

        final long time = System.currentTimeMillis() - Utils.getDif(now) + (difHr + difMin) * 60000;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent inten = new Intent(context, ReminderAlarmReceiver.class);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, time, PendingIntent.getBroadcast(context, 0, inten, 0));
        } else
            alarmManager.set(AlarmManager.RTC, time, PendingIntent.getBroadcast(context, 0, inten, 0));


    }

    private static void showReminder(Context context) {

        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("People helping people")
                        .setContentText("Donation reminder")
                        .setOngoing(false);

        final Intent intent = new Intent(context, MainMenu.class);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(REMINDER_NOTIFICATION_ID, builder.build());
    }

    public static void cancelReminder(Context context) {
        try {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(REMINDER_NOTIFICATION_ID);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, ReminderAlarmReceiver.class), 0);
            alarmManager.cancel(alarmIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    public static class ReminderAlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showReminder(context);
            scheduleNextReminder(context);
        }
    }

    public static class AdAlarmReceiver extends BroadcastReceiver {
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
