package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import bigshots.people_helping_people.fragments.CurrentCharityFragment;
import bigshots.people_helping_people.fragments.DonationsFragment;
import bigshots.people_helping_people.fragments.LeaderboardFragment;
import bigshots.people_helping_people.fragments.MainFragment;
import bigshots.people_helping_people.fragments.VoteFragment;
import bigshots.people_helping_people.io.AdManager;
import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.Utility;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;
import bigshots.people_helping_people.views.CharityListItem;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainMenu extends FragmentActivity {
    public static Context context;
    private static final AdListener fullScreen = new AdListener() {
        @Override
        public void onAdOpened() {
            fullScreenClicked = false;
            super.onAdOpened();
        }

        @Override
        public void onAdLoaded() {

            if (fullScreenClicked) {
                adManager.getFullscreenAd().show();
            }
            super.onAdLoaded();
        }

        @Override
        public void onAdClosed() {
            Log.e("show", "fS");
            Utility.addScore(context, 15);
            adManager.loadFullscreenAd();
            super.onAdClosed();
        }
    };
    public static FragmentActivity fragmentActivity;
    public static int rank;
    public static ArrayList<Charity> charities;
    public static ArrayList<UserStats> stats;
    public static Charity charity;
    public static final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCharityMonth(Charity charity) {
            MainMenu.charity = charity;
            CurrentCharityFragment.refreshCharity();
        }

        @Override
        public void onCurrentCharity(Charity charity) {
            VoteCharityAdapter.setVotedFor(charity.getUrl());
            CharityListItem.setCurrentVote(charity.getUrl());
            VoteFragment.refreshList();
        }

        @Override
        public void onDonationsArray(ArrayList<Charity> charities) {
            DonationsFragment.charities = charities;
            DonationsFragment.refreshList();
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {
            MainMenu.charities = charities;
            VoteFragment.refreshList();
        }

        @Override
        public void onCompleteRank(final int rank) {
            MainMenu.rank = rank + 1;
            LeaderboardFragment.refreshList();
        }

        @Override
        public void onCompleteLeaderBoardList(final ArrayList<UserStats> stats) {
            MainMenu.stats = stats;
            LeaderboardFragment.refreshList();
        }

        @Override
        public void onCompleteCurrentScore(int score) {
            Utility.initScore(score);
        }
    };
    public static String email;
    //Todo consider making curent charity hard coded
    private static final Runnable downloadData = new Runnable() {
        @Override
        public void run() {
            getVotedFor();
            charityManager.getCharities();
            charityManager.monthlyCharity();
            try {
                final UserManager manager1 = new UserManager();
                userManager.getScoreRank(email);
                manager1.postStats(email, Utility.getTotalScore(MainMenu.context), Utility.getRate(MainMenu.context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            userManager.getLeaderboardListScore(25);
        }
    };
    public static AdManager adManager;
    public static boolean videoClicked, fullScreenClicked;
    private final AdListener video = new AdListener() {
        @Override
        public void onAdOpened() {
            videoClicked = false;
            super.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            if (videoClicked) {

                adManager.getVideoAd().show();
            }
            super.onAdLoaded();
        }

        @Override
        public void onAdClosed() {
            Log.e("show", "vid");
            Utility.addScore(context, 20);
            adManager.loadVideoAd();
            super.onAdClosed();
        }
    };
    public static UserManager userManager;
    private static CharityManager charityManager;
    private static Fragment fragment;
    private static View view;

    public static FragmentManager getFragManager() {
        return fragment.getChildFragmentManager();
    }

    private static void getVotedFor() {
        charityManager.currentCharity(email);
    }

    public static void downloadData() {
        if (view != null)
            view.post(downloadData);
        else new Thread(downloadData).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getEmail();
        AsyncConnector.setListener(aSyncListener);
        init();
        setUpFragment();
        downloadData();
    }

    private void init() {
        setContentView(R.layout.material_main_menu);
        charityManager = new CharityManager();
        context = this;
        fragmentActivity = this;
        userManager = new UserManager();
        adManager = new AdManager(context);
        adManager.loadFullscreenAd();
        adManager.loadVideoAd();
        adManager.getFullscreenAd().setAdListener(fullScreen);
        adManager.getVideoAd().setAdListener(video);
    }

    private void setUpFragment() {
        fragment = new MainFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.commit();
        view = fragment.getView();
    }

    public void getEmail() {
        final AccountManager manager = AccountManager.get(this);
        for (Account account : manager.getAccounts()) {
            if (account.name.contains("@")) {
                email = account.name;
                break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
