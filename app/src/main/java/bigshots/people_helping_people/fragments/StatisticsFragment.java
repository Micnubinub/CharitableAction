package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.graph.charts.BarChart;
import bigshots.people_helping_people.graph.models.BarModel;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.scroll_iew_lib.ParallaxScrollView;
import bigshots.people_helping_people.utilities.Point;
import bigshots.people_helping_people.utilities.Utils;


public class StatisticsFragment extends BaseFragment {

    private static BarChart myStatsBarGraph, globalStatsBarGraph;
    int shown = 0;

    public StatisticsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics, container, false);
        myStatsBarGraph = (BarChart) view.findViewById(R.id.my_stats);
        globalStatsBarGraph = (BarChart) view.findViewById(R.id.global_stats);
        ((ParallaxScrollView) view.findViewById(R.id.scroll_view)).setScrollListener(scrollListener);
        //Todo stat_description android:text="$18 raised at 90c a day"
        //Todo new UserManager().postStats("sidney@cyberkomm.ch", 500, 2.15f);
        //Todo new UserManager().getLeaderboardListRate(5);
        //Todo new UserManager().getLeaderboardListScore(5);
        addValues();
        return view;
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
        final ArrayList<Point> points = Utils.getPoints(MainMenu.context);
        if (points.size() < 1) {
            Toast.makeText(MainMenu.context, "No data to display", Toast.LENGTH_LONG).show();
            return;
        }
        //Mode mode = Utils.getScope(MainMenu.context);
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
        globalStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    public enum Mode {
        DAY, WEEK, MONTH, YEAR
    }
}
