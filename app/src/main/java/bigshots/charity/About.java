package bigshots.charity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class About extends Activity {
    //TBS > Giving hunids away (a hunnet percent)
    //Giving users choices

    /*
Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
startActivity(browserIntent);
 */

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playstore_link:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=The+Big+Shots"));
                    startActivity(browserIntent);
                    break;
                case R.id.website_link:
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.thebigshots.net/"));
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        findViewById(R.id.playstore_link).setOnClickListener(listener);
        findViewById(R.id.website_link).setOnClickListener(listener);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
