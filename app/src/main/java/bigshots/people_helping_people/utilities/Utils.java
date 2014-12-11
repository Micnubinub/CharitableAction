package bigshots.people_helping_people.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public static final String REMINDER_TIME_MINS_INT = "REMINDER_TIME_INT";
    public static final String REMINDER_TIME_HOURS_INT = "REMINDER_TIME_INT";

    public static int getHours(long date) {
        final Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("hh");
        calendar.setTimeInMillis(date);
        return Integer.parseInt(formatter.format(calendar.getTime()));
    }

    public static int getMinutes(long date) {
        final Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("mm");
        calendar.setTimeInMillis(date);
        return Integer.parseInt(formatter.format(calendar.getTime()));
    }
}
