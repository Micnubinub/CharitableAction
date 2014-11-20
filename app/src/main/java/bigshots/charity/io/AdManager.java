package bigshots.charity.io;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager {
    private static final String BANNER_ID = "ca-app-pub-7957464690974751/7567893025";
    private static final String FULLSCREEN_ID = "ca-app-pub-7957464690974751/1521359425";
    private static final String VIDEO_ID = "ca-app-pub-7957464690974751/9044626224";
    private AdView bannerAd;
    private InterstitialAd fullscreenAd;
    private InterstitialAd videoAd;

    public AdManager(Context context) {
        bannerAd = new AdView(context);
        bannerAd.setAdUnitId(BANNER_ID);
        fullscreenAd = new InterstitialAd(context);
        fullscreenAd.setAdUnitId(FULLSCREEN_ID);
        videoAd = new InterstitialAd(context);
        videoAd.setAdUnitId(VIDEO_ID);
    }

    public void loadBannerAd() { // Get And Load Banner Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.setAdSize(AdSize.BANNER);
        bannerAd.loadAd(adRequest);
    }

    public void loadFullscreenAd() { // Get And Load Fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        fullscreenAd.loadAd(adRequest);
    }

    public void loadVideoAd() { // Get And Load Video Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        videoAd.loadAd(adRequest);
    }

    public AdView getBannerAd() {
        return bannerAd;
    }

    public InterstitialAd getFullscreenAd() {
        return fullscreenAd;
    }

    public InterstitialAd getVideoAd() {
        return videoAd;
    }
}
