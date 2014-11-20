package bigshots.charity.io;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Connector con = new Connector();
        // con.getMessageManager().sendMessage("You are Gods");
        // con.getMessageManager().sendMessage("You are Awesome");
        // con.getCharityManager().getCharities();
        // con.getCharityManager().suggestCharity("ww.ruefg.com");
        // con.getCharityManager().currentCharity("Steve@gmail.com");
        // con.getUserManager().insertUser("Steve@gmail.com");
        // con.getVoteManager().castVote("www.charity.com", "Steve@gmail.com");
        // con.getVoteManager().removeVote("Sidney");
        final AdManager adManager = new AdManager(this);
        adManager.loadVideoAd();
        adManager.getVideoAd().setAdListener(new AdListener() {
            public void onAdLoaded() {
                adManager.getVideoAd().show();
            }
        });
    }
}
