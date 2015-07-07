package bigshots.people_helping_people.io;

import android.content.Context;
import android.util.Log;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import java.util.ArrayList;

import bigshots.people_helping_people.utilities.Utility;

public class AdManager {
    private static final String sdkKey = "B9guoaCTTWCzlI4IqkgnPwEC5NQvXogdwTKmwVZU5DDYx039PGQozfEXY199";
    private static final ArrayList<Runnable> onConnectedRunnables = new ArrayList<>();
    //    private static TJPlacement autoFullscreenAd, autoVideoAd;
    private static boolean shouldOpenFullScreen, shouldOpenVid;
//    private static boolean shouldOpenAutoFullScreen, shouldOpenAutoVid;
    private static TJPlacement fullscreenAd, videoAd;
    private static Context context;
    private static final TJConnectListener connectionListener = new TJConnectListener() {
        @Override
        public void onConnectSuccess() {
            toast("Connected");
            for (Runnable runnable : onConnectedRunnables) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            onConnectedRunnables.clear();
//            getAutoVideoAd().requestContent();
        }

        @Override
        public void onConnectFailure() {
            toast("Failed to connect");
            //Todo count the number of retries and cut them off at like 20
            connect();
        }
    };

    private static final TJPlacementListener fullScreenListener = new TJPlacementListener() {
        @Override
        public void onRequestSuccess(TJPlacement placement) {
            toast("fs request success");
        }

        @Override
        public void onRequestFailure(TJPlacement placement, TJError tjError) {
            toast("fs request failed");
            loadFullscreenAd(shouldOpenFullScreen);
        }

        @Override
        public void onContentReady(TJPlacement placement) {
            toast("fs content ready");
            if (shouldOpenFullScreen)
                placement.showContent();
        }

        @Override
        public void onContentShow(TJPlacement placement) {
            toast("fs content showing");
        }

        @Override
        public void onContentDismiss(TJPlacement placement) {
            Utility.addScore(context, 15);
            loadFullscreenAd(false);
            toast("fs content dismissed");
        }

        @Override
        public void onPurchaseRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s) {

        }

        @Override
        public void onRewardRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s, int i) {

        }
    };

    private static final TJPlacementListener videoListener = new TJPlacementListener() {
        @Override
        public void onRequestSuccess(TJPlacement placement) {
            toast("vid request success");
        }

        @Override
        public void onRequestFailure(TJPlacement placement, TJError tjError) {
            toast("vid request failed");
            loadVideoAd(shouldOpenVid);
        }

        @Override
        public void onContentReady(TJPlacement placement) {
            toast("vid content ready");
            if (shouldOpenVid)
                placement.showContent();
        }

        @Override
        public void onContentShow(TJPlacement placement) {
            toast("vid content showing");
        }

        @Override
        public void onContentDismiss(TJPlacement placement) {
            Utility.addScore(context, 20);
            loadVideoAd(false);
            toast("vid content dismissed");
        }

        @Override
        public void onPurchaseRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s) {

        }

        @Override
        public void onRewardRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s, int i) {

        }
    };
/*    private static final TJPlacementListener autoFullScreenListener = new TJPlacementListener() {
        @Override
        public void onRequestSuccess(TJPlacement placement) {
            toast("autoFS request success");
        }

        @Override
        public void onRequestFailure(TJPlacement placement, TJError tjError) {
            toast("autoFS request failed");
            loadAutoFullscreenAd(shouldOpenAutoFullScreen);
        }

        @Override
        public void onContentReady(TJPlacement placement) {
            toast("autoFS content ready");
            if (shouldOpenAutoFullScreen)
                placement.showContent();
        }

        @Override
        public void onContentShow(TJPlacement placement) {
            toast("autoFS content showing");
        }

        @Override
        public void onContentDismiss(TJPlacement placement) {
            Utility.addScore(context, 15);
            loadAutoFullscreenAd(false);
            toast("autoFS content dismissed");
        }

        @Override
        public void onPurchaseRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s) {

        }

        @Override
        public void onRewardRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s, int i) {

        }
    };

    private static final TJPlacementListener autoVideoListener = new TJPlacementListener() {
        @Override
        public void onRequestSuccess(TJPlacement placement) {
            toast("autoVid request success");
        }

        @Override
        public void onRequestFailure(TJPlacement placement, TJError tjError) {
            toast("autoVid request failed");

            loadAutoVideoAd(shouldOpenAutoVid);
        }

        @Override
        public void onContentReady(TJPlacement placement) {
            toast("autoVid content ready");
            if (shouldOpenAutoVid)
                placement.showContent();
        }

        @Override
        public void onContentShow(TJPlacement placement) {
            toast("autoVid content showing");
        }

        @Override
        public void onContentDismiss(TJPlacement placement) {
            Utility.addScore(context, 20);
            loadAutoVideoAd(false);
            toast("autoVid content dismissed");
        }

        @Override
        public void onPurchaseRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s) {

        }

        @Override
        public void onRewardRequest(TJPlacement placement, TJActionRequest tjActionRequest, String s, int i) {

        }
    };
    */

    public AdManager(Context context) {
        this.context = context;
        connect();
    }

    public static void toast(final String msg) {
        Log.e("AdManager > ", msg);
        if (context == null)
            return;

//        MainActivity.context.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public static void connect() {
        Tapjoy.connect(context, sdkKey, null, connectionListener);
        //TODO remove this value
        Tapjoy.setDebugEnabled(true);
    }

//    private static TJPlacement getAutoFullscreenAd() {
//        if (autoFullscreenAd == null)
//            autoFullscreenAd = new TJPlacement(context, "AUTOMATIC_FULLSCREEN", autoFullScreenListener);
//        return autoFullscreenAd;
//    }
//
//    private static TJPlacement getAutoVideoAd() {
//        if (autoVideoAd == null)
//            autoVideoAd = new TJPlacement(context, "AUTOMATIC_VIDEO", autoVideoListener);
//        return autoVideoAd;
//    }

    private static TJPlacement getFullscreenAd() {
        if (fullscreenAd == null)
            fullscreenAd = new TJPlacement(context, "FULLSCREEN_AD", fullScreenListener);
        return fullscreenAd;
    }

    private static TJPlacement getVideoAd() {
        if (videoAd == null)
            videoAd = new TJPlacement(context, "VIDEO_AD", videoListener);
        return videoAd;
    }


    public static void loadFullscreenAd(final boolean openImmediatelyAfter) { // Get And Load Fullscreen Ad
        shouldOpenFullScreen = openImmediatelyAfter;
        if (Tapjoy.isConnected()) {
            getFullscreenAd().requestContent();
        } else {
            toast("TapJoy not connected yet, please wait. Request will be run once connected");
            onConnectedRunnables.add(new Runnable() {
                @Override
                public void run() {
                    loadFullscreenAd(openImmediatelyAfter);
                }
            });
        }
    }

    public static void loadVideoAd(final boolean openImmediatelyAfter) { // Get And Load Video Ad
        shouldOpenVid = openImmediatelyAfter;
        if (Tapjoy.isConnected()) {
            getVideoAd().requestContent();
        } else {
            toast("TapJoy not connected yet, please wait. Request will be run once connected");
            onConnectedRunnables.add(new Runnable() {
                @Override
                public void run() {
                    loadVideoAd(openImmediatelyAfter);
                }
            });
        }
    }

//    public static void loadAutoFullscreenAd(final boolean openImmediatelyAfter) { // Get And Load Fullscreen Ad
//        shouldOpenAutoFullScreen = openImmediatelyAfter;
//        if (Tapjoy.isConnected()) {
//            getAutoFullscreenAd().requestContent();
//        } else {
//            toast("TapJoy not connected yet, please wait. Request will be run once connected");
//            onConnectedRunnables.add(new Runnable() {
//                @Override
//                public void run() {
//                    loadAutoFullscreenAd(openImmediatelyAfter);
//                }
//            });
//        }
//    }
//
//    public static void loadAutoVideoAd(final boolean openImmediatelyAfter) { // Get And Load Video Ad
//        shouldOpenAutoFullScreen = openImmediatelyAfter;
//        if (Tapjoy.isConnected()) {
//            getAutoVideoAd().requestContent();
//        } else {
//            toast("TapJoy not connected yet, please wait. Request will be run once connected");
//            onConnectedRunnables.add(new Runnable() {
//                @Override
//                public void run() {
//                    loadAutoVideoAd(openImmediatelyAfter);
//                }
//            });
//        }
//    }

    public static void showFullscreenAd() { // Get And show Fullscreen Ad
        toast("show FS");
        if (getFullscreenAd().isContentReady()) {
            getFullscreenAd().showContent();
        } else {
            loadFullscreenAd(true);
            toast("Content not ready yet, please wait. Request will be run once connected");
            //Todo
        }
    }

    public static void showVideoAd() { // Get And show Video Ad
        toast("show Vid");
        if (getVideoAd().isContentReady()) {
            getVideoAd().showContent();
        } else {
            loadVideoAd(true);
            toast("Content not ready yet, please wait. Request will be run once connected");
            //Todo
        }
    }

//    public static void showAutoFullscreenAd() { // Get And show Fullscreen Ad
//        toast("show AutoFS");
//        if (getAutoFullscreenAd().isContentReady()) {
//            getAutoFullscreenAd().showContent();
//        } else {
//            loadAutoFullscreenAd(true);
//            toast("Content not ready yet, please wait. Request will be run once connected");
//            //Todo
//        }
//    }
//
//    public static void showAutoVideoAd() { // Get And show Video Ad
//        toast("show AutoVid");
//        if (getAutoVideoAd().isContentReady()) {
//            getAutoVideoAd().showContent();
//        } else {
//            loadAutoVideoAd(true);
//            toast("Content not ready yet, please wait. Request will be run once connected");
//            //Todo
//        }
//    }


}
