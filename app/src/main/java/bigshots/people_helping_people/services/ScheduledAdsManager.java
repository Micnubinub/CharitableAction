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
import android.widget.Toast;

import java.util.Random;

import bigshots.people_helping_people.MainActivity;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AdManager;
import bigshots.people_helping_people.utilities.Utility;

/**
 * Created by root on 11/12/14.
 */
public class ScheduledAdsManager extends Service {
    private static final int SCHEDULED_ADS_NOTIFICATION_ID = 455129802;
    private static final int REMINDER_NOTIFICATION_ID = 455129854;
    private static final Random random = new Random();
    private static PendingIntent alarmIntent;
    private static AlarmManager alarmManager;
    private static AdManager adManager;
    private static boolean loadAd;
    private static Context context;
    private static boolean serviceRunning = false;


    private static void showFullScreenAd() {
        getAdManager().showFullScreenAd();
    }

    private static void showVideoAd() {
        getAdManager().showVideoAd();
    }

    public static void scheduleNext(Context context, boolean load) {
        loadAd = load;
        try {
            final Intent intent = new Intent(context, AdAlarmReceiver.class);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (load) {
                int mins = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utility.FULLSCREEN_AD_FREQUENCY_MINUTES, 0);
                if (mins == 0) {
                    cancelNotification(context);
                    return;
                } else {
                    final long when = (System.currentTimeMillis() + (mins * 60000)) - 10000;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC, when, PendingIntent.getBroadcast(context, 0, intent, 0));
                    } else
                        alarmManager.set(AlarmManager.RTC, when, PendingIntent.getBroadcast(context, 0, intent, 0));
                }
            } else {
                final long when = System.currentTimeMillis() + 10000;
                alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC, when, alarmIntent);
                } else
                    alarmManager.set(AlarmManager.RTC, when, alarmIntent);
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

            final Intent intent = new Intent(context, MainActivity.class);
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

    private static void getAds(Context context) {
        try {
            if (getAdManager() == null) {
                final Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AdManager getAdManager() {
        if (adManager == null) adManager = AdManager.getAdManager();
        return adManager;
    }

    public static boolean isServiceRunning() {
        return serviceRunning;
    }

    public static void scheduleNextReminder(Context context) {
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utility.ENABLE_REMINDER, false))
            return;

        final int hr = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utility.REMINDER_TIME_HOURS_INT, 12);
        final int mins = PreferenceManager.getDefaultSharedPreferences(context).getInt(Utility.REMINDER_TIME_MINS_INT, 30);

        final long now = System.currentTimeMillis();
        final int nowHr = Utility.getHours(now);
        final int nowMin = Utility.getMinutes(now);

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

        final long time = System.currentTimeMillis() - Utility.getDif(now) + (difHr + difMin) * 60000;

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

        final Intent intent = new Intent(context, MainActivity.class);
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

        getAds(getApplication());
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
//                    show = false;
                    scheduleNext(context, false);
                    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utility.TOAST_BEFORE_BOOL, true))
                        Toast.makeText(context, "Showing Ad in 10 secs", Toast.LENGTH_LONG).show();
                    return;
                } else {
//                    show = true;
                    if (random.nextInt(4) < 1) {
                        showVideoAd();
                    } else {
                        showFullScreenAd();
                    }
                    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Utility.LOOP_SCHEDULE, false))
                        scheduleNext(context, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
