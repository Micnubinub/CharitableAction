package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.graph.charts.BarChart;
import bigshots.people_helping_people.graph.models.BarModel;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.Point;
import bigshots.people_helping_people.utilities.Utility;


public class StatisticsFragment extends BaseFragment {
    private static String raised;
    //Todo check the saving
    private static BarChart myStatsBarGraph;
    private static int color;
    private static TextView textView;

    public StatisticsFragment() {
    }

    public static void invalidate() {
        textView.setText((raised == null || raised.length() < 2) ? "Loading..." : raised);
    }

    public static void setRaised(double moneyRaised) {
        raised = String.format("Your points equate to about $%.2f", moneyRaised);
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(raised);
            }
        });
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
        textView = (TextView) view.findViewById(R.id.stat_description);
        update();
        return view;
    }

    private void plotMyStatsPoints() {
        myStatsBarGraph.clearChart();
        final ArrayList<Point> points = Utility.getPoints(MainMenu.context);
        if (points.size() < 1) {
            return;
        }
        final Mode mode = Utility.getScope(MainMenu.context);
        for (int i = 0; i < points.size(); i++) {
            final Point point = points.get(i);
            if (mode == Mode.MONTH)
                addMyStatsBar(point.getLegendTitle() + String.valueOf(i + 1), point.getY());
            else
                addMyStatsBar(point.getLegendTitle(), point.getY());
        }
        myStatsBarGraph.invalidate();
    }

    private void addMyStatsBar(String label, float value) {
        if (value == 0)
            return;
        myStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    @Override
    protected void update() {
        textView.setText((raised == null || raised.length() < 2) ? "Loading..." : raised);
        myStatsBarGraph.post(new Runnable() {
            @Override
            public void run() {
                plotMyStatsPoints();
            }
        });

    }

    public enum Mode {
        DAY, WEEK, MONTH, YEAR
    }
}
