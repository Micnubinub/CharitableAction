package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import bigshots.people_helping_people.scroll_iew_lib.ParallaxViewLayout;
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
    public static FragmentActivity fragmentActivity;
    public static int rank, userScore, totalCash;
    public static long totalScore;
    public static ArrayList<Charity> charities;
    public static ArrayList<UserStats> stats;
    public static Charity charity, pedestal;
    public static String email;
    public static AdManager adManager;
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
            Utility.addScore(context, 15);
            adManager.loadFullscreenAd();
            super.onAdClosed();
        }
    };
    public static boolean videoClicked, fullScreenClicked;
    public static UserManager userManager;
    public static MainMenu mainMenu;
    private static SharedPreferences prefs;
    private static CharityManager charityManager;
    //TODO make a pedestle (always ontop and has bigger view than most
    //TODO maybe add links to show receipts for the respective charity in the history
    //TODO fix bug that doesn't allow people to suggest a charity
    //TODO fix add failed to load >> (check if online, if not, toat saying you need the internet
    //Todo upadte statistics onScroll in
    //TODO get Userscore from database and update the db if need be
    //Todo consider adding a chunk of score to local device if you have a higher value on DB
    //Todo consider making curent charity hard coded
    private static final Runnable downloadData = new Runnable() {
        @Override
        public void run() {
            getCurrentCharity();
            charityManager.getCharities();
            charityManager.monthlyCharity();
            charityManager.getHistory();
            charityManager.getTotalScore();
            userManager.getScore(email);
            refreshLeaderBoard();
        }
    };
    private static Fragment fragment;
    private static View view;
    public static final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCharityMonth(Charity charity) {

        }

        @Override
        public void onCompleteTotalScore(long score) {
            totalScore = score;
        }

        @Override
        public void onCurrentCharity(Charity charity) {
            VoteCharityAdapter.setVotedFor(charity.getUrl());
            VoteFragment.refreshList();
            if (CharityListItem.queItems.size() > 0)
                getCurrentCharity();
        }

        @Override
        public void onDonationsArray(ArrayList<Charity> charities) {
            DonationsFragment.charities = charities;
            loop:
            for (Charity charity : charities) {
                if (charity.isCurrent()) {
                    MainMenu.charity = charity;
                    CurrentCharityFragment.refreshCharity();
                    break loop;
                }
            }
            DonationsFragment.refreshList();
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities, Charity pedestal) {
            MainMenu.charities = charities;
            MainMenu.pedestal = pedestal;
            VoteFragment.refreshList();
            for (Fragment fragment : ParallaxViewLayout.fragments) {
                if (fragment instanceof VoteFragment) {
                    ((VoteFragment) fragment).update();
                }
            }
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
            Utility.initScore(context, score);
        }
    };
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
            Utility.addScore(context, 20);
            adManager.loadVideoAd();
            super.onAdClosed();
        }
    };

    public static FragmentManager getFragManager() {
        return fragment.getChildFragmentManager();
    }


    public static void downloadData() {
        if (view != null)
            view.post(downloadData);
        else new Thread(downloadData).start();
    }

    public static void getCurrentCharity() {
        charityManager.currentCharity(email);
    }

    public static void refreshLeaderBoard() {

        try {
            final UserManager manager1 = new UserManager();

            if (email.length() > 3)
                userManager.getScoreRank(email);

            userScore = Utility.getTotalScore(MainMenu.context);

            if (email.length() > 3)
                manager1.postStats(email, userScore, Utility.getRate(MainMenu.context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        userManager.getLeaderboardListScore(50);
    }

    public static void post(Runnable r) {
        if (view != null)
            view.post(r);
    }

    public static void nextQueueItem() {
        if (CharityListItem.queItems != null && CharityListItem.queItems.size() > 0) {
            final CharityListItem.QueItem queItem = CharityListItem.queItems.get(0);

            switch (queItem.type) {
                case CAST:
                    CharityListItem.voteManager.removeVote(charity.getUrl(), email);
                    CharityListItem.voteManager.castVote(queItem.link, email);
                    break;
                case REMOVE:
                    CharityListItem.voteManager.removeVote(charity.getUrl(), email);
                    break;
            }
            CharityListItem.queItems.remove(queItem);
        }
    }

    public static void toast(final String msg) {
        if (mainMenu != null) {
            try {
                mainMenu.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainMenu, msg, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainMenu, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainMenu, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getEmail();
        AsyncConnector.setListener(aSyncListener);
        init();
        setUpFragment();
        downloadData();
        mainMenu = this;
    }

    private void init() {
        setContentView(R.layout.material_main_menu);
        charityManager = new CharityManager();
        fragmentActivity = this;
        userManager = new UserManager();
        userManager.insertUser(email);
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
        email = prefs.getString(Utility.SAVED_EMAIL, "");

        if (email.length() < 4) {
            final AccountManager manager = AccountManager.get(this);
            for (Account account : manager.getAccounts()) {
                if (account.name.contains("@")) {
                    email = account.name;
                    break;
                }
            }

            final Dialog dialog = new Dialog(context, R.style.CustomDialog);
            dialog.setContentView(R.layout.get_email);
            final Button save = (Button) dialog.findViewById(R.id.save_cancel).findViewById(R.id.save);
            final Button cancel = (Button) dialog.findViewById(R.id.save_cancel).findViewById(R.id.cancel);
            final TextView msg = ((TextView) dialog.findViewById(R.id.email_text));
            final EditText emailTextView = ((EditText) dialog.findViewById(R.id.email));
            save.setText("Change");
            cancel.setText("Keep");

            emailTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final String text = emailTextView.getText().toString();
                    save.setEnabled(!(text == null || text.length() < 3 || !(text.contains("@"))));
                    save.setTextColor(save.isEnabled() ? getResources().getColor(R.color.current_charity_color) : getResources().getColor(R.color.light_grey));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            msg.setText(
                    email.length() < 4 ?
                            "Enter you email of preference below" :
                            "The following email will be used in the database to keep track of your progress and to vote for charities : \n\n" + email + "\n\nTo change it enter one manually below");

            final View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.save:
                            email = emailTextView.getText().toString();
                        case R.id.cancel:
                            prefs.edit().putString(Utility.SAVED_EMAIL, email).commit();
                            dialog.dismiss();
                            break;
                    }
                }
            };
            save.setOnClickListener(onClickListener);
            dialog.findViewById(R.id.save_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);
            save.setEnabled(false);
            save.setTextColor(getResources().getColor(R.color.light_grey));
            dialog.show();
        }
        //Todo


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
