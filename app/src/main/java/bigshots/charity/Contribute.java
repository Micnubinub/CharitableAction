package bigshots.charity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import bigshots.charity.io.AdManager;
import bigshots.charity.io.AsyncConnector;
import bigshots.charity.io.Charity;
import bigshots.charity.io.Connector;
import bigshots.charity.utilities.Interfaces;

/**
 * Created by root on 18/11/14.
 */
public class Contribute extends Activity {
    //Fullscreen ads
    //Video
    //Banner popup
    //Link to this months charity

    private AdManager adManager;
    private String currentCharity;
    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.full_screen:
                    adManager.getFullscreenAd().setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            adManager.getFullscreenAd().show();
                        }
                    });
                    break;
                case R.id.video_ad:
                    adManager.getVideoAd().setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            adManager.getVideoAd().show();
                        }
                    });
                    break;
                case R.id.current_charity:
                    if (currentCharity != null && currentCharity.length() > 3) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentCharity));
                        startActivity(browserIntent);
                    }

                    break;
            }
        }
    };
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(Charity charity) {
            currentCharity = charity.getUrl();
        }

        @Override
        public void onCompleteArray(ArrayList<Charity> charities) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute);
        adManager = new AdManager(this);
        adManager.loadFullscreenAd();
        adManager.loadVideoAd();

        findViewById(R.id.full_screen).setOnClickListener(listener);
        findViewById(R.id.current_charity).setOnClickListener(listener);
        findViewById(R.id.video_ad).setOnClickListener(listener);

        AsyncConnector.setListener(aSyncListener);
        new Connector().getCharityManager().monthlyCharity();
    }
}
