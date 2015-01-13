package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import bigshots.people_helping_people.fragments.LeaderboardFragment;
import bigshots.people_helping_people.fragments.MainFragment;
import bigshots.people_helping_people.fragments.VoteFragment;
import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.utilities.Utils;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainMenu extends FragmentActivity {
    public static Context context;
    public static FragmentActivity fragmentActivity;
    private static CharityManager charityManager;
    private static final Runnable charitySetUp = new Runnable() {
        @Override
        public void run() {
            getVotedFor();
            charityManager = new CharityManager();
            charityManager.getCharities();
        }
    };
    private static Fragment fragment;
    private static View view;
    private static String email;
    private static final Runnable leaderboardSetUp = new Runnable() {
        @Override
        public void run() {
            AsyncConnector.setListener(LeaderboardFragment.aSyncListener);
            try {
                final UserManager manager1 = new UserManager();
                userManager.getScoreRank(email);
                manager1.postStats(email, Integer.parseInt(Utils.getTotalScore(MainMenu.context)), Utils.getRate(MainMenu.context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            userManager.getLeaderboardListScore(25);
        }
    };
    private static UserManager userManager;

    public static FragmentManager getFragManager() {
        return fragment.getChildFragmentManager();
    }

    public static void setUpCharities() {
        if (view != null)
            view.post(charitySetUp);
        else new Thread(charitySetUp).start();
    }

    private static void getVotedFor() {
        new CharityManager().currentCharity(email);
        AsyncConnector.setListener(VoteFragment.aSyncListener);
    }

    public static void setUpLeaderBoard() {
        if (view != null)
            view.post(leaderboardSetUp);
        else new Thread(leaderboardSetUp).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setUpFragment();
        setUpCharities();
        setUpLeaderBoard();
    }

    private void init() {
        setContentView(R.layout.material_main_menu);
        context = this;
        getEmail();
        fragmentActivity = this;
        userManager = new UserManager();
    }

    private void setUpFragment() {
        fragment = new MainFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.commit();
        view = fragment.getView();
    }

    public void getEmail() {
        final AccountManager manager = AccountManager.get(MainMenu.context);
        for (Account account : manager.getAccounts()) {
            if (account.name.contains("@")) {
                email = account.name;
                break;
            }
        }
    }
}
