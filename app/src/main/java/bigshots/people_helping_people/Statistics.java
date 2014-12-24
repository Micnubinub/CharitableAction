package bigshots.people_helping_people;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import bigshots.people_helping_people.graph.charts.ValueLineChart;
import bigshots.people_helping_people.graph.communication.IOnPointFocusedListener;
import bigshots.people_helping_people.graph.models.ValueLinePoint;
import bigshots.people_helping_people.graph.models.ValueLineSeries;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Statistics extends Activity {
    //How much they donated over time >>> graph
    //Leader board
    ValueLineChart statsLineGraph;

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
        statsLineGraph = (ValueLineChart) findViewById(R.id.stats_line_graph);
        addValues();
    }

    private void addValues() {

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(getResources().getColor(R.color.material_blue));

        series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", .4f));
        series.addPoint(new ValueLinePoint("Apr", 1.2f));
        series.addPoint(new ValueLinePoint("Mai", 2.6f));
        series.addPoint(new ValueLinePoint("Jun", 1.0f));
        series.addPoint(new ValueLinePoint("Jul", 3.5f));
        series.addPoint(new ValueLinePoint("Aug", 2.4f));
        series.addPoint(new ValueLinePoint("Sep", 2.4f));
        series.addPoint(new ValueLinePoint("Oct", 3.4f));
        series.addPoint(new ValueLinePoint("Nov", .4f));
        series.addPoint(new ValueLinePoint("Dec", 1.0f));
        series.addPoint(new ValueLinePoint("Jan", 1.2f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", 2.0f));
        series.addPoint(new ValueLinePoint("Apr", 1.0f));
        series.addPoint(new ValueLinePoint("Mai", 3.5f));
        series.addPoint(new ValueLinePoint("Jun", 2.4f));
        series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));

//        mCubicValueLineChart.addSeries(series1);
        statsLineGraph.addSeries(series);
        statsLineGraph.addStandardValue(2.3f);
        statsLineGraph.addStandardValue(1.3f);
        statsLineGraph.addStandardValue(4.3f);
        statsLineGraph.setOnPointFocusedListener(new IOnPointFocusedListener() {
            @Override
            public void onPointFocused(int _PointPos) {
                Log.d("Test", "Pos: " + _PointPos);
            }
        });

    }
}
