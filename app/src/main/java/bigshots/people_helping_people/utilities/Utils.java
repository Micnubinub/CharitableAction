package bigshots.people_helping_people.utilities;

import android.content.Context;
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

    public static int getHours(long date) {
        final Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("HH");
        calendar.setTimeInMillis(date);

        return Integer.parseInt(formatter.format(calendar.getTime()));
    }

    public static int getMinutes(long date) {
        final Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("mm");
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

    public static void addShown(int points) {

    }

    public static Statistics.Mode getScope(Context context) {
        Statistics.Mode mode = Statistics.Mode.DAY;
        //Todo read db and use the first and last alues to determine the scope using minus
        //day == 86400000, week == 604800000, month == 2419200000

        return mode;
    }

    public static void addScore(Context context, int points) {
        final StatsDBHelper statsDBHelper = new StatsDBHelper(context);
        final SQLiteDatabase statsDB = statsDBHelper.getReadableDatabase();

//        final Cursor cursor = statsDB.query(StatsDBHelper.PROFILE_TABLE, new String[]{StatsDBHelper.ID, StatsDBHelper.PRIORITY, StatsDBHelper.PROFILE_NAME, ProfileDBHelper.TRIGGERS, ProfileDBHelper.COMMANDS}, null, null, null, null, null);
//        cursor.moveToFirst();
    }


    public static ArrayList<Point> getPoints(Context context) {
        final ArrayList<Point> graphPoints = new ArrayList<Point>();
//Todo use scope to detemine legend and points y ** lots of work to be done

        return graphPoints;
    }

}
