package bigshots.charity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import bigshots.charity.io.AdManager;
import bigshots.charity.io.AsyncConnector;
import bigshots.charity.io.Charity;
import bigshots.charity.io.Connector;
import bigshots.charity.schedule_wheel.AbstractWheel;
import bigshots.charity.schedule_wheel.OnWheelChangedListener;
import bigshots.charity.schedule_wheel.adapters.NumericWheelAdapter;
import bigshots.charity.utilities.Interfaces;
import bigshots.charity.utilities.Utils;

/**
 * Created by root on 18/11/14.
 */
public class Contribute extends Activity {
    //Fullscreen ads
    //Video
    //Banner popup
    //Link to this months charity

    private int frequencyMinutes;
    private Dialog dialog;
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
                    try {
                        if (currentCharity != null && currentCharity.length() > 3) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.savethechildren.org.au/"));//currentCharity));
                            startActivity(browserIntent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.scheduled_ads:
                    dialog = getScheduledAds();
                    dialog.show();
                    break;
                case R.id.save:
                    save();
                case R.id.cancel:
                    if (dialog != null)
                        dialog.dismiss();
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
        findViewById(R.id.scheduled_ads).setOnClickListener(listener);
        findViewById(R.id.current_charity).setOnClickListener(listener);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent(Contribute.this, MainMenu.class);
                startActivity(i);
            }
        });

        AsyncConnector.setListener(aSyncListener);
        new Connector().getCharityManager().monthlyCharity();
    }

    private Dialog getScheduledAds() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.scheduled_dialog);

        dialog.findViewById(R.id.save_cancel).findViewById(R.id.save).setOnClickListener(listener);
        dialog.findViewById(R.id.save_cancel).findViewById(R.id.cancel).setOnClickListener(listener);

        final TextView frequency = (TextView) dialog.findViewById(R.id.frequency);
        final AbstractWheel hours = (AbstractWheel) dialog.findViewById(R.id.hours);
        final AbstractWheel minutes = (AbstractWheel) dialog.findViewById(R.id.minutes);

        hours.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
        hours.setCyclic(true);

        minutes.setViewAdapter(new NumericWheelAdapter(this, 0, 59));
        minutes.setCyclic(true);

        // set current time

        hours.setCurrentItem(0);
        minutes.setCurrentItem(20);
        final String prefix = "A full screen Ad will be show every : ";

        frequency.setText(prefix + "20 minutes");
        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                frequencyMinutes = (hours.getCurrentItem() * 60) + minutes.getCurrentItem();
                frequency.post(new Runnable() {
                    @Override
                    public void run() {
                        frequency.setText(prefix + String.valueOf(frequencyMinutes) + " minutes");
                    }
                });
            }
        };

        hours.addChangingListener(wheelListener);
        minutes.addChangingListener(wheelListener);

        return dialog;
    }

    private void save() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Utils.FULLSCREEN_AD_FREQUENCY_MINUTES, frequencyMinutes).commit();
    }
}
