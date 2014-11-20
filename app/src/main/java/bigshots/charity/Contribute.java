package bigshots.charity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;

import bigshots.charity.io.AdManager;

/**
 * Created by root on 18/11/14.
 */
public class Contribute extends Activity {
    //Fullscreen ads
    //Video
    //Banner popup
    //Link to this months charity

    /*
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
    startActivity(browserIntent);
    */
    private AdManager adManager;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.full_screen:
                    adManager.loadFullscreenAd();
                    adManager.getFullscreenAd().setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            adManager.getFullscreenAd().show();
                        }
                    });
                    break;
                case R.id.video_ad:
                    adManager.loadVideoAd();
                    adManager.getVideoAd().setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            adManager.getVideoAd().show();
                        }
                    });
                    break;
                case R.id.current_charity:
                    //Todo get current charity
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    startActivity(browserIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute);
        adManager = new AdManager(this);
        findViewById(R.id.full_screen).setOnClickListener(listener);
        findViewById(R.id.current_charity).setOnClickListener(listener);
        findViewById(R.id.video_ad).setOnClickListener(listener);
    }
}
