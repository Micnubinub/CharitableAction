package bigshots.people_helping_people.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.fragments.StatisticsFragment;

/**
 * Created by root on 27/11/14.
 */
@SuppressWarnings("ALL")
public class Utility {
    public static final String FULLSCREEN_AD_FREQUENCY_MINUTES = "FULLSCREEN_AD_FREQUENCY_MINUTES";
    public static final String INTRO_SHOWN = "INTRO_SHOWN";
    public static final String LOAD_AD = "LOAD_AD";
    public static final String ENABLE_SCHEDULED_ADS = "ENABLE_SCHEDULED_ADS";
    public static final String SCHEDULE = "SCHEDULE";
    public static final String AUTO_START_BOOL = "AUTO_START_BOOL";
    public static final String ADS_AT_START_BOOL = "ADS_AT_START_BOOL";
    public static final String LOOP_SCHEDULE = "LOOP_SCHEDULE";
    public static final String TOAST_BEFORE_BOOL = "TOAST_BEFORE_BOOL";
    public static final String TOAST_TIME_SECS = "TOAST_TIME_SECS";
    public static final String ENABLE_REMINDER = "ENABLE_REMINDER";
    public static final String REMINDER_TIME_MINS_INT = "REMINDER_TIME_MINS_INT";
    public static final String REMINDER_TIME_HOURS_INT = "REMINDER_TIME_HOURS_INT";
    public static final String SAVED_EMAIL = "SAVED_EMAIL";
    public static final long MONTH_MILLIS = 2714400000l;

    public static String getDay(long date) {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("EEE");
        calendar.setTimeInMillis(date);
        return formatter.format(calendar.getTime());
    }

    public static String getMonth(long date) {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("MMM");
        calendar.setTimeInMillis(date);
        return formatter.format(calendar.getTime());
    }

    public static int getHours(long date) {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("HH");
        calendar.setTimeInMillis(date);
        return Integer.parseInt(formatter.format(calendar.getTime()));
    }

    public static String getHour(long date) {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("hhaa");
        calendar.setTimeInMillis(date);
        String out = formatter.format(calendar.getTime());
        if (out.startsWith("0")) {
            out = out.split("0")[1];
        }
        return out;
    }

    public static int getMinutes(long date) {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("mm");
        calendar.setTimeInMillis(date);
        return Integer.parseInt(formatter.format(calendar.getTime()));
    }

    public static int getDif(long date) {
        final Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("ss");
        calendar.setTimeInMillis(date);
        int sec = Integer.parseInt(formatter.format(calendar.getTime()));
        formatter = new SimpleDateFormat("SSS");
        int milli = Integer.parseInt(formatter.format(calendar.getTime()));
        return milli + sec;
    }


    public static StatisticsFragment.Mode getScope(Context context) {
        try {
            final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
            final SQLiteDatabase statsDB = statsDBHelper.getReadableDatabase();

            final Cursor cursor = statsDB.query(StatsDBHelper.STATS_TABLE, new String[]{StatsDBHelper.TIME_LONG}, null, null, null, null, null);

            cursor.moveToFirst();
            long first = Long.parseLong(cursor.getString(0));

            cursor.moveToLast();
            long last = Long.parseLong(cursor.getString(0));

            statsDB.close();

            long diff = last - first;

            if (diff <= 86400000l)
                return StatisticsFragment.Mode.DAY;

            if (diff <= 604800000l)
                return StatisticsFragment.Mode.WEEK;

            if (diff < 2419200000l)
                return StatisticsFragment.Mode.MONTH;

            return StatisticsFragment.Mode.YEAR;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StatisticsFragment.Mode.DAY;
    }

    public static int getTotalScore(Context context) {
        int total = 0;
        try {
            final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
            final SQLiteDatabase statsDB = statsDBHelper.getReadableDatabase();

            final Cursor cursor = statsDB.query(StatsDBHelper.STATS_TABLE, new String[]{StatsDBHelper.POINTS_INT}, null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                total += Integer.parseInt(cursor.getString(0));
                cursor.moveToNext();
            }

            statsDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public static float getRate(Context context) {
        float rate = 0;
        float total = 0;
        float first, last;
        try {
            final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
            final SQLiteDatabase statsDB = statsDBHelper.getReadableDatabase();

            final Cursor cursor = statsDB.query(StatsDBHelper.STATS_TABLE, new String[]{StatsDBHelper.POINTS_INT, StatsDBHelper.TIME_LONG}, null, null, null, null, null);
            cursor.moveToFirst();
            first = Long.parseLong(cursor.getString(1));
            while (!cursor.isAfterLast()) {
                total += Integer.parseInt(cursor.getString(0));
                cursor.moveToNext();
            }

            cursor.moveToLast();
            last = Long.parseLong(cursor.getString(1));
            statsDB.close();
            rate = total / (last - first);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    public static void addScore(Context context, long when, int points) {
        final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
        final SQLiteDatabase statsDB = statsDBHelper.getWritableDatabase();
        final String time = String.valueOf(when);
        final String pointsString = String.valueOf(points);

        final ContentValues values = new ContentValues();
        values.put(StatsDBHelper.POINTS_INT, pointsString);
        values.put(StatsDBHelper.TIME_LONG, time);
        statsDB.insert(StatsDBHelper.STATS_TABLE, "", values);
        statsDB.close();
    }

    public static void addScore(Context context, int points) {
        final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
        final SQLiteDatabase statsDB = statsDBHelper.getWritableDatabase();
        final String time = String.valueOf(System.currentTimeMillis());
        final String pointsString = String.valueOf(points);

        final ContentValues values = new ContentValues();
        values.put(StatsDBHelper.POINTS_INT, pointsString);
        values.put(StatsDBHelper.TIME_LONG, time);
        statsDB.insert(StatsDBHelper.STATS_TABLE, "", values);
        statsDB.close();
    }

    public static ArrayList<Point> getPoints(Context context) {
        final ArrayList<Point> graphPoints = new ArrayList<Point>();
        final StatisticsFragment.Mode mode = getScope(context);
        String lengend = "";
        long steps = 0;
        //Todo tabstrip colors
        switch (mode) {
            case DAY:
                lengend = "hour";
                steps = 3600000L;
                break;
            case WEEK:
                lengend = "day";
                steps = 86400000L;
                break;
            case MONTH:
                lengend = "week";
                steps = 604800000L;
                break;
            case YEAR:
                lengend = "month";
                steps = 2419200000L;
                break;

        }
        final ArrayList<DataEntry> entries = new ArrayList<DataEntry>();
        try {
            final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
            final SQLiteDatabase statsDB = statsDBHelper.getReadableDatabase();

            final Cursor cursor = statsDB.query(StatsDBHelper.STATS_TABLE, new String[]{StatsDBHelper.POINTS_INT, StatsDBHelper.TIME_LONG}, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entries.add(new DataEntry(Long.parseLong(cursor.getString(1)), Integer.parseInt(cursor.getString(0))));
                cursor.moveToNext();
            }
            statsDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (entries.size() < 1)
            return graphPoints;

        long currTime = entries.get(0).getDate();
        long stopTime = entries.get(entries.size() - 1).getDate();
        while (currTime <= stopTime) {
            switch (mode) {
                case DAY:
                    lengend = getHour(currTime);
                    break;
                case WEEK:
                    lengend = getDay(currTime);
                    break;

                case YEAR:
                    lengend = getMonth(currTime);
                    break;
            }
            graphPoints.add(new Point(lengend, addFromTo(entries, currTime, currTime + steps)));
            currTime += steps;
        }

        return graphPoints;
    }

    private static int addFromTo(final ArrayList<DataEntry> entries, long from, long to) {
        int total = 0;
        for (int i = 0; i < entries.size(); i++) {
            final int points = entries.get(i).getPoints();
            final long date = entries.get(i).getDate();

            if (date < from)
                continue;
            if (date > to)
                break;

            total += points;
        }

        return total;
    }


    public static int initScore(Context context, int external) {
        final int local = getTotalScore(MainMenu.context);

        if (local < external) {
            Utility.addScore(context, System.currentTimeMillis() - MONTH_MILLIS, external);
            MainMenu.downloadData();
        }

        final int max = Math.max(local, external);
        //Todo post max here
        MainMenu.userManager.postStats(MainMenu.email, max, Utility.getRate(MainMenu.context));
        return max;
    }

    public static String formatNumber(int i) {
        //Todo use
        return NumberFormat.getIntegerInstance().format(i);
    }


    public static void hasActiveInternetConnection(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if (activeNetworkInfo != null) {
                    try {
                        final HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        MainMenu.toast((urlc.getResponseCode() == 200) ? "Ad failed to load" : "Please connect to the internet");
                    } catch (IOException e) {
                        Log.e("outterConnected", e.toString());
                    }
                } else {
                }
            }
        }).start();

    }

}
