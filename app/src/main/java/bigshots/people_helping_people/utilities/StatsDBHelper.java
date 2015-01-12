package bigshots.people_helping_people.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 25/12/14.
 */
public class StatsDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Stats.db";
    public static final int DB_V = 1;
    public static final String STATS_TABLE = "Stats";
    public static final String ID = "_id";
    public static final String TIME_LONG = "TIME_LONG";
    public static final String POINTS_INT = "POINTS_INT";


    public StatsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_V);
    }

    @Override
    public void onCreate(SQLiteDatabase profiledb) {
        final String sqlStatement = "create table " + STATS_TABLE
                + " (" + ID + " integer primary key autoincrement,"
                + TIME_LONG + " string,"
                + POINTS_INT + " string"
                + ");";
        profiledb.execSQL(sqlStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }
}
