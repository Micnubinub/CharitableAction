package bigshots.charity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by root on 18/11/14.
 */
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
                final Intent i = new Intent(Statistics.this, MainMenu.class);
                startActivity(i);
            }
        });
    }
}
