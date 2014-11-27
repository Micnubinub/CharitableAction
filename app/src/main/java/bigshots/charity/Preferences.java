package bigshots.charity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Preferences extends Activity {
    //Banner autostart
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
