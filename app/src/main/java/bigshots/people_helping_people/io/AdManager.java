package bigshots.people_helping_people.io;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

public class AdManager {
    private static final String sdkKey = "B9guoaCTTWCzlI4IqkgnPwEC5NQvXogdwTKmwVZU5DDYx039PGQozfEXY199";
    private static TJPlacement fullscreenAd, videoAd, autoFullscreenAd, autoVideoAd, bannerAd;
    private static Activity context;
    private static final TJConnectListener connectionListener = new TJConnectListener() {
        @Override
        public void onConnectSuccess() {

            toast("Connected");
        }

        @Override
        public void onConnectFailure() {
            toast("Failed to connect");
        }
    };
    //Todo might have to make 5 different placement listeners
    private static final TJPlacementListener placementListener = new TJPlacementListener() {
        @Override
        public void onRequestSuccess(TJPlacement placement) {
            toast("request success");
        }

        @Override
        public void onRequestFailure(TJPlacement placement, TJError tjError) {
            toast("request failed");
        }

        @Override
        public void onContentReady(TJPlacement placement) {
            toast("content ready");

        }

        @Override
        public void onContentShow(TJPlacement placement) {
            toast("content showing");
        }

        @Override
        public void onContentDismiss(TJPlacement placement) {
            toast("content dismissed");
        }

        @Override
        public void onPurchaseRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s) {

        }

        @Override
        public void onRewardRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s, int i) {

        }
    };

    public AdManager(Activity context) {
        this.context = context;
        Tapjoy.connect(context, sdkKey, null, connectionListener);
        //Todo remove this value
        Tapjoy.setDebugEnabled(true);
    }

    public static void toast(final String msg) {
        Log.e("AdManager > ", msg);

        if (context == null)
            return;

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void checkConnection() {

    }

    public static TJPlacement getAutoFullscreenAd() {
        if (autoFullscreenAd == null)
            autoFullscreenAd = new TJPlacement(context, "AUTOMATIC_FULLSCREEN", placementListener)
        return autoFullscreenAd;
    }

    public static TJPlacement getBannerAd() {
        if (bannerAd == null)
            bannerAd = new TJPlacement(context, "BANNER", placementListener);
        return bannerAd;
    }

    public static TJPlacement getAutoVideoAd() {
        if (autoVideoAd == null)
            autoVideoAd = new TJPlacement(context, "AUTOMATIC_VIDEO", placementListener);
        return autoVideoAd;
    }

    public static TJPlacement getFullscreenAd() {
        if (fullscreenAd == null)
            fullscreenAd = new TJPlacement(context, "FULLSCREEN_AD", placementListener);
        return fullscreenAd;
    }

    public static TJPlacement getVideoAd() {
        if (videoAd == null)
            videoAd = new TJPlacement(context, "VIDEO_AD", placementListener);
        return videoAd;
    }

    public void loadBannerAd() { // Get And Load Banner Ad

    }

    public void loadFullscreenAd() { // Get And Load Fullscreen Ad
        fullscreenAd.loadAd(new AdRequest.Builder().build());
    }

    public void loadVideoAd() { // Get And Load Video Ad
        videoAd.loadAd(new AdRequest.Builder().build());
    }
}
