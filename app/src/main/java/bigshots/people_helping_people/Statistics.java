package bigshots.people_helping_people;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import bigshots.people_helping_people.graph.charts.BarChart;
import bigshots.people_helping_people.graph.models.BarModel;
import bigshots.people_helping_people.io.AdManager;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Statistics extends Activity {

    private static BarChart myStatsBarGraph, globalStatsBarGraph;
    int show = 0;

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
        //Todo new UserManager().postStats("sidney@cyberkomm.ch", 500, 2.15f);
        //Todo new UserManager().getLeaderboardListRate(5);
        //Todo new UserManager().getLeaderboardListScore(5);
        addValues();
        testAds();
    }

    private void testAds() {
        AdManager adManager = new AdManager(this);
        AdView bannerAd = adManager.getBannerAd();
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
                show++;
                Log.e("shown, showVal", String.format("%d, %d", System.currentTimeMillis() / 1000, show));
            }
        });
        ((LinearLayout) findViewById(R.id.test)).addView(bannerAd);
    }

    private void addValues() {
        addMyStatsBar("mon", 3.9f);
        addMyStatsBar("tue", 2.41f);
        addMyStatsBar("wed", 5.4f);
        addMyStatsBar("thur", 2.3f);
        addMyStatsBar("fri", .4f);
        addMyStatsBar("sat", 5.2f);
        addMyStatsBar("sun", 1.4f);
        addMyStatsBar("mon", .7f);

        addGlobalStatsBar("mon", 36.9f);
        addGlobalStatsBar("tue", 23.41f);
        addGlobalStatsBar("wed", 52.4f);
        addGlobalStatsBar("thur", 20.3f);
        addGlobalStatsBar("fri", 9.4f);
        addGlobalStatsBar("sat", 52.2f);
        addGlobalStatsBar("sun", 11.4f);
        addGlobalStatsBar("mon", 22.7f);
        addGlobalStatsBar("mon", 31.9f);
        addGlobalStatsBar("tue", 32.41f);
        addGlobalStatsBar("wed", 51.4f);
        addGlobalStatsBar("thur", 22.3f);
        addGlobalStatsBar("fri", 9.4f);
        addGlobalStatsBar("sat", 15.2f);
        addGlobalStatsBar("sun", 19.4f);
        addGlobalStatsBar("mon", 7.7f);
    }

    private void addMyStatsBar(String label, float value) {
        int color = 1;
        if (value > 5) {
            color = getResources().getColor(R.color.material_blue);
        } else if (value > 3) {
            color = getResources().getColor(R.color.material_green_light);
        } else {
            color = getResources().getColor(R.color.material_red);
        }
        myStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    private void addGlobalStatsBar(String label, float value) {
        int color = 1;
        if (value > 5) {
            color = getResources().getColor(R.color.material_blue);
        } else if (value > 3) {
            color = getResources().getColor(R.color.material_green_light);
        } else {
            color = getResources().getColor(R.color.material_red);
        }
        globalStatsBarGraph.addBar(new BarModel(label, value, color));
    }

    public enum Mode {
        DAY, WEEK, MONTH, YEAR
    }
}
