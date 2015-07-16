package bigshots.people_helping_people.io;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;

import bigshots.people_helping_people.utilities.Utility;

public class AdManager implements SessionListener, OnAdEventV2 {
    private static Activity context;
    private static AdManager adManager;
    private static boolean sessionStarted;
    private static boolean showVideoAd, showFullScreenAd, fullScreenReady, videoReady;

    private AdManager(Activity context) {
        adManager.context = context;
        connect();
    }

    public static AdManager getAdManager(Activity context) {
        log("getAdManager");
        if (adManager == null)
            adManager = new AdManager(context);
        return adManager;
    }

    public static AdManager getAdManager() {
        log("getAdMangerNoContext");
        return adManager;
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

    public static void connect() {
        if (context == null) {
            log("Context Null");
            return;
        } else {
            log("Connecting");
        }

        MonetizationManager.createSession(context, "29626", adManager);
    }

    public static void loadFullscreenAd() { // Get And Load Fullscreen Ad
        if (fullScreenReady)
            return;

        if (!sessionStarted) {
            connect();
            return;
        }

        MonetizationManager.fetchAd(context, NativeXAdPlacement.Main_Menu_Screen, adManager);
    }

    public static void loadVideoAd() { // Get And Load Video A
        if (videoReady)
            return;

        if (!sessionStarted) {
            connect();
            return;
        }

        MonetizationManager.fetchAd(context, NativeXAdPlacement.Level_Completed, adManager);
    }

    private static void log(String msg) {
        Log.e("AdManager > ", msg);
    }

    public static void showVideoAd() { // Get And show Video Ad
        if (!sessionStarted) {
            connect();
            return;
        }

        if (videoReady)
            MonetizationManager.showReadyAd(context, NativeXAdPlacement.Level_Completed, adManager);
        else
            showVideoAd = true;
    }

    public static void showFullScreenAd() { // Get And show Fullscreen Ad
        if (!sessionStarted) {
            connect();
            return;
        }

        if (fullScreenReady)
            MonetizationManager.showReadyAd(context, NativeXAdPlacement.Main_Menu_Screen, adManager);
        else
            showFullScreenAd = true;
    }

    private static void loadNextAd(String placement) {
        if (placement.equals(NativeXAdPlacement.Main_Menu_Screen.toString())) {
            loadFullscreenAd();
        } else if (placement.equals(NativeXAdPlacement.Level_Completed.toString())) {
            loadVideoAd();
        }
    }

    private static void onAddFetched(String placement) {
        if (placement == null)
            return;

        if (placement.equals(NativeXAdPlacement.Main_Menu_Screen.toString())) {
            fullScreenReady = true;
            if (showFullScreenAd) {
                showFullScreenAd();
                showFullScreenAd = false;
            }
        } else if (placement.equals(NativeXAdPlacement.Level_Completed.toString())) {
            videoReady = true;
            if (showVideoAd) {
                showVideoAd();
                showVideoAd = false;
            }
        }
    }

    private static void onAwardPoints(String placement) {
        if (placement.equals(NativeXAdPlacement.Main_Menu_Screen.toString())) {
            Utility.addScore(context, 15);
            fullScreenReady = false;
        } else if (placement.equals(NativeXAdPlacement.Level_Completed.toString())) {
            Utility.addScore(context, 20);
            videoReady = false;
        }
    }

    @Override
    public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String s) {
        sessionStarted = success;
        if (success) {
            // A session with our servers was established successfully.
            // The app is ready to show ads.
            log("crate session Success");

            //We are fetching all the ads here as adManager is a one scene app.
            //However, in a typical integration you will want to spread these calls out in your game.
            //It is recommended that you add these calls in an area that would allow around 5 seconds before attempting to show
            loadFullscreenAd();
            loadVideoAd();
        } else {
            log("crate session Failed > " + new String(s));
            connect();
        }
    }

    @Override
    public void onEvent(AdEvent event, AdInfo adInfo, String message) {
        switch (event) {
            case FETCHED:
                // the ad has been loaded and is now ready to display
                //enable the buttons within the sample app
                onAddFetched(adInfo.getPlacement());
                break;
            case NO_AD:
                //no ad is available to show at adManager time
                // if app has a free coins button, make sure that button is hidden when no ads
                // are available for rewards
                //freeCoinsButton.setVisibility(View.GONE);
                toast("no ad available");
                loadNextAd(adInfo.getPlacement());
                break;
            case BEFORE_DISPLAY:
                if (adInfo.willPlayAudio()) {
                    // Ad will play music, so make sure game music is paused
                    //Todo
                    toast("About tp show ad");
                }
                break;
            case DISMISSED:
                //user has closed the ad

                //disable the button within the sample app
                onAwardPoints(adInfo.getPlacement());

                //fetch a new ad
                loadNextAd(adInfo.getPlacement());
                break;
            case VIDEO_COMPLETED:
                // Video has completed; rewards will be rewarded if applicable
                onAwardPoints(adInfo.getPlacement());
                loadNextAd(adInfo.getPlacement());
                break;
            case ERROR:
                //there was an error when attempting to fetch or display the ad
                log("Error: " + message);
                loadNextAd(adInfo.getPlacement());
                break;
            default:
                break;
        }
    }


}
