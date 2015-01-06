package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.fragments.AboutFragment;
import bigshots.people_helping_people.fragments.ContributeFragment;
import bigshots.people_helping_people.fragments.CurrentCharityFragment;
import bigshots.people_helping_people.fragments.FeedbackFragment;
import bigshots.people_helping_people.fragments.LeaderboardFragment;
import bigshots.people_helping_people.fragments.StatisticsFragment;
import bigshots.people_helping_people.fragments.VoteFragment;
import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.Utils;
import bigshots.people_helping_people.views.PagerSlidingTabStrip;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainMenu extends FragmentActivity {

    //    private final View.OnClickListener listener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            Intent i = new Intent(MainMenu.this, Contribute.class);
//
//            switch (v.getId()) {
//                case R.id.about:
//                    i = new Intent(MainMenu.this, About.class);
//                    break;
//                case R.id.prefs:
//                    i = new Intent(MainMenu.this, Preferences.class);
//                    break;
//                case R.id.feedback:
//                    i = new Intent(MainMenu.this, Feedback.class);
//                    break;
//                case R.id.vote:
//                    i = new Intent(MainMenu.this, Vote.class);
//                    break;
//                case R.id.statistics:
//                    i = new Intent(MainMenu.this, Statistics.class);
//                    break;
//                case R.id.leader_board:
//                    i = new Intent(MainMenu.this, LeaderBoard.class);
//                    break;
//                case R.id.current_charity:
//                    i = new Intent(MainMenu.this, CurrentCharity.class);
//                    break;
//            }
//            startActivity(i);
//
//        }
//    };
    private static final Fragment[] fragments = new Fragment[7];
    public static Context context;
    private static TextView title;
    private static TextView coinText;
    private static PagerSlidingTabStrip tabs;
    private static ViewPager pager;
    private static MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_menu);
        setContentView(R.layout.material_main);
        context = this;

        try {
            final AccountManager manager = AccountManager.get(this);
            final Account[] accounts = manager.getAccounts();
            for (Account account : accounts) {
                if (account.name.contains("@")) {
                    final UserManager manager1 = new UserManager();
                    manager1.insertUser(account.name);
                    manager1.postStats(account.name, Integer.parseInt(Utils.getTotalScore(this)), Utils.getRate(this));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        findViewById(R.id.about).setOnClickListener(listener);
//        findViewById(R.id.prefs).setOnClickListener(listener);
//        findViewById(R.id.feedback).setOnClickListener(listener);
//        findViewById(R.id.vote).setOnClickListener(listener);
//        findViewById(R.id.contribute).setOnClickListener(listener);
//        findViewById(R.id.statistics).setOnClickListener(listener);
//        findViewById(R.id.leader_board).setOnClickListener(listener);
//        findViewById(R.id.current_charity).setOnClickListener(listener);
        AsyncConnector.setListener(new Interfaces.ASyncListener() {
            @Override
            public void onCompleteSingle(final Charity charity) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.contribute)).setText(charity.getName());
                    }
                });
            }

            @Override
            public void onCompleteArray(ArrayList<Charity> charities) {

            }

            @Override
            public void onCompleteRank(int rank) {

            }

            @Override
            public void onCompleteLeaderBoardList(ArrayList<UserStats> stats) {

            }
        });

    }

    private void setUpFragments() {
        fragments[0] = new ContributeFragment();
        fragments[1] = new VoteFragment();
        fragments[2] = new CurrentCharityFragment();
        fragments[3] = new StatisticsFragment();
        fragments[4] = new LeaderboardFragment();
        fragments[5] = new FeedbackFragment();
        fragments[6] = new AboutFragment();


        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(5);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);

        tabs.setViewPager(pager);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Free Donation", "Vote", "Current Charity",
                "Statistics", "Leaderboard", "Feedback", "About"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }
}
