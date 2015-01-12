package bigshots.people_helping_people;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import bigshots.people_helping_people.graph.charts.BarChart;
import bigshots.people_helping_people.graph.models.BarModel;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Statistics extends Activity {

    private static BarChart myStatsBarGraph, globalStatsBarGraph;
    int shown = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myStatsBarGraph = (BarChart) findViewById(R.id.my_stats);
        globalStatsBarGraph = (BarChart) findViewById(R.id.global_stats);
        //Todo stat_description android:text="$18 raised at 90c a day"
        //Todo new UserManager().postStats("sidney@cyberkomm.ch", 500, 2.15f);
        //Todo new UserManager().getLeaderboardListRate(5);
        //Todo new UserManager().getLeaderboardListScore(5);
        addValues();
    }

    private void addValues() {
        plotMyStatsPoints();

        addGlobalStatsBar("mon", 36.9f);
        addGlobalStatsBar("tue", 23.41f);
        addGlobalStatsBar("wed", 42.4f);
        addGlobalStatsBar("thur", 20.3f);
        addGlobalStatsBar("fri", 9.4f);
        addGlobalStatsBar("sat", 52.2f);
        addGlobalStatsBar("sun", 11.4f);

    }

    private void plotMyStatsPoints() {
//        final ArrayList<Point> points = Utils.getPoints(this);
//        if (points.size() < 1) {
//            Toast.makeText(this, "No data to display", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Mode mode = Utils.getScope(this);
//        for (int i = 0; i < points.size(); i++) {
//            final Point point = points.get(i);
//            if (mode == Mode.MONTH)
//                addMyStatsBar(point.getLegendTitle() + String.valueOf(i + 1), point.getY());
//            else
//                addMyStatsBar(point.getLegendTitle(), point.getY());
//
//        }
    }

    private void addMyStatsBar(String label, float value) {
        int color = color = getResources().getColor(R.color.material_blue);
//        if (value > 5) {
//
//        } else if (value > 3) {
//            color = getResources().getColor(R.color.material_green_light);
//        } else {
//            color = getResources().getColor(R.color.material_red);
//        }
        myStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    private void addGlobalStatsBar(String label, float value) {
        int color = color = getResources().getColor(R.color.material_blue);
//        if (value > 5) {
//
//        } else if (value > 3) {
//            color = getResources().getColor(R.color.material_green_light);
//        } else {
//            color = getResources().getColor(R.color.material_red);
//        }
        globalStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    public enum Mode {
        DAY, WEEK, MONTH, YEAR
    }
}
