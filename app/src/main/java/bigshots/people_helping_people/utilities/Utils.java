package bigshots.people_helping_people.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import bigshots.people_helping_people.Statistics;

/**
 * Created by root on 27/11/14.
 */
@SuppressWarnings("ALL")
public class Utils {
    public static final String FULLSCREEN_AD_FREQUENCY_MINUTES = "FULLSCREEN_AD_FREQUENCY_MINUTES";
    public static final String INTRO_SHOWN = "INTRO_SHOWN";
    public static final String LOAD_AD = "LOAD_AD";
    public static final String ENABLE_SCHEDULED_ADS = "ENABLE_SCHEDULED_ADS";
    public static final String SCHEDULE = "SCHEDULE";
    public static final String AUTO_START_BOOL = "AUTO_START_BOOL";
    public static final String LOOP_SCHEDULE = "LOOP_SCHEDULE";
    //public static final String LOOP_SCHEDULE = "LOOP_SCHEDULE";
    public static final String TOAST_BEFORE_BOOL = "TOAST_BEFORE_BOOL";
    public static final String TOAST_TIME_SECS = "TOAST_TIME_SECS";
    public static final String ENABLE_REMINDER = "ENABLE_REMINDER";
    public static final String REMINDER_TIME_MINS_INT = "REMINDER_TIME_MINS_INT";
    public static final String REMINDER_TIME_HOURS_INT = "REMINDER_TIME_HOURS_INT";

    public static String getDay(long date) {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("ddd");
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


    public static Statistics.Mode getScope(Context context) {
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
                return Statistics.Mode.DAY;

            if (diff <= 604800000l)
                return Statistics.Mode.WEEK;

            if (diff < 2419200000l)
                return Statistics.Mode.MONTH;

            return Statistics.Mode.YEAR;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Statistics.Mode.DAY;
    }

    public static String getTotalScore(Context context) {
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
        return String.valueOf(total);
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
        final Statistics.Mode mode = getScope(context);
        String lengend = "";
        long steps = 0;
//Todo use scope to detemine legend and points y ** lots of work to be done
        switch (mode) {
            case DAY:
                lengend = "hour";
                steps = 3600000l;
                break;
            case WEEK:
                lengend = "day";
                steps = 86400000l;
                break;

            case MONTH:
                lengend = "week";
                steps = 6048000000l;
                break;

            case YEAR:
                lengend = "month";
                steps = 24192000000l;
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

}
