package bigshots.people_helping_people;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Statistics extends Activity {
    //How much they donated over time >>> graph
    //Leader board

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
    }
}