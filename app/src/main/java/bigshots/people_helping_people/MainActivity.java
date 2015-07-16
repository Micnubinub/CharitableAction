package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.fragments.CreditFragment;
import bigshots.people_helping_people.fragments.CurrentCharityFragment;
import bigshots.people_helping_people.fragments.DonationsFragment;
import bigshots.people_helping_people.fragments.MainFragment;
import bigshots.people_helping_people.fragments.VoteFragment;
import bigshots.people_helping_people.graph.LeaderBoardFragment;
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

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainActivity extends FragmentActivity {
    public static Activity context;
    public static FragmentActivity fragmentActivity;
    public static int rank, userScore, totalCash;
    public static long totalScore;
    public static ArrayList<Charity> charities;
    public static ArrayList<UserStats> stats;
    public static Charity charity, pedestal;
    public static String email;
    public static AdManager adManager;
    public static boolean videoClicked, fullScreenClicked;
    public static UserManager userManager;
    public static MainActivity mainActivity;
    public static Fragment fragment;
    private static SharedPreferences prefs;
    private static CharityManager charityManager;
    private static final Runnable refreshVoteList = new Runnable() {
        @Override
        public void run() {
            charityManager.getCharities();
            charityManager.currentCharity(email);
        }
    };
    //TODO maybe add links to show receipts for the respective charity in the history
    private static final Runnable downloadData = new Runnable() {
        @Override
        public void run() {
            charityManager.monthlyCharity();
            charityManager.getHistory();
            charityManager.getCredits();
            charityManager.getTotalScore();
            userManager.getScore(email);
            charityManager.getCharities();
            charityManager.currentCharity(email);
            refreshLeaderBoard();
        }
    };
    private static View view;
    public static final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCharityMonth(Charity charity) {

        }

        @Override
        public void onCompleteCredits(String[] credits) {
            for (Fragment fragment : ParallaxViewLayout.fragments) {
                if (fragment instanceof CreditFragment) {
                    ((CreditFragment) fragment).setCredits(credits);
                    return;
                }
            }
        }

        @Override
        public void onCompleteTotalScore(long score) {
            totalScore = score;
        }

        @Override
        public void onCurrentCharity(Charity charity) {
            VoteCharityAdapter.setVotedFor(charity.getUrl());
            VoteFragment.refreshList();
            for (Fragment fragment : ParallaxViewLayout.fragments) {
                if (fragment instanceof VoteFragment) {
                    ((VoteFragment) fragment).update();
                    return;
                }
            }
        }

        @Override
        public void onDonationsArray(ArrayList<Charity> charities) {
            DonationsFragment.charities = charities;
            loop:
            for (Charity charity : charities) {
                if (charity.isCurrent()) {
                    MainActivity.charity = charity;
                    CurrentCharityFragment.refreshCharity();
                    break loop;
                }
            }
            DonationsFragment.refreshList();
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities, Charity pedestal) {
            MainActivity.charities = charities;
            MainActivity.pedestal = pedestal;
            VoteFragment.refreshList();
            for (Fragment fragment : ParallaxViewLayout.fragments) {
                if (fragment instanceof VoteFragment) {
                    ((VoteFragment) fragment).update();
                }
            }
        }

        @Override
        public void onCompleteRank(final int rank) {
            MainActivity.rank = rank + 1;
            LeaderBoardFragment.refreshList();
        }

        @Override
        public void onCompleteLeaderBoardList(final ArrayList<UserStats> stats) {
            MainActivity.stats = stats;
            LeaderBoardFragment.refreshList();
        }

        @Override
        public void onCompleteCurrentScore(int score) {
            Utility.initScore(context, score);
        }
    };
    private static boolean closeActivity;

    public static FragmentManager getFragManager() {
        return fragment.getChildFragmentManager();
    }

    public static void downloadData() {
        if (view != null)
            view.post(downloadData);
        else new Thread(downloadData).start();
    }

    public static void getCurrentCharity() {
        new Thread(refreshVoteList).start();
    }

    public static void refreshLeaderBoard() {
        try {
            if (email.length() > 3) {
                final UserManager manager1 = new UserManager();
                userManager.getScoreRank(email);
                userScore = Utility.getTotalScore(MainActivity.context);
                manager1.postStats(email, userScore, Utility.getRate(MainActivity.context));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userManager.getLeaderboardListScore(50);
    }

    public static void toast(final String msg) {
        if (mainActivity != null) {
            try {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity, msg, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        adManager = AdManager.getAdManager(context);
        adManager.loadFullscreenAd();
        adManager.loadVideoAd();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getEmail();
        AsyncConnector.setListener(aSyncListener);
        init();
        setUpFragment();
        downloadData();
        mainActivity = this;
    }

    private void init() {
        setContentView(R.layout.material_main_menu);
        charityManager = new CharityManager();
        fragmentActivity = this;
        userManager = new UserManager();
        userManager.insertUser(email);

    }

    private void setUpFragment() {
        fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        MainFragment.parallaxViewLayout.setUpPagerAndAdapter();
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
