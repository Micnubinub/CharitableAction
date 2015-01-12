package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.util.Log;
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
import bigshots.people_helping_people.utilities.Point;
import bigshots.people_helping_people.utilities.Utils;


public class StatisticsFragment extends BaseFragment {

    private static BarChart myStatsBarGraph, globalStatsBarGraph;
    private static int color;
    int shown = 0;

    public StatisticsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        color = getResources().getColor(R.color.current_charity_color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics, container, false);
        myStatsBarGraph = (BarChart) view.findViewById(R.id.my_stats);
        globalStatsBarGraph = (BarChart) view.findViewById(R.id.global_stats);
        // ((ParallaxScrollView) view.findViewById(R.id.scroll_view)).setScrollListener(scrollListener);
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
        Mode mode = Utils.getScope(MainMenu.context);
        for (int i = 0; i < points.size(); i++) {
            final Point point = points.get(i);
            if (mode == Mode.MONTH)
                addMyStatsBar(point.getLegendTitle() + String.valueOf(i + 1), point.getY());
            else
                addMyStatsBar(point.getLegendTitle(), point.getY());

        }
    }

    private void addMyStatsBar(String label, float value) {
        Log.e("adding", label);
        myStatsBarGraph.addBar(new BarModel(label, value, color));
    }


    private void addGlobalStatsBar(String label, float value) {
        int color = color = getResources().getColor(R.color.material_blue);
        globalStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    @Override
    protected void update() {
        //Todo
    }

    public enum Mode {
        DAY, WEEK, MONTH, YEAR
    }
}
