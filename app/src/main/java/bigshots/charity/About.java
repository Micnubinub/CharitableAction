package bigshots.charity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by root on 18/11/14.
 */
public class About extends Activity {
    //TBS > Giving hunids away (a hunnet percent)
    //Giving users choices

    /*
Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
startActivity(browserIntent);
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}
