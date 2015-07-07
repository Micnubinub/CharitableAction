package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import bigshots.people_helping_people.MainActivity;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.AboutFragment;
import bigshots.people_helping_people.fragments.ContributeFragment;
import bigshots.people_helping_people.fragments.CreditFragment;
import bigshots.people_helping_people.fragments.CurrentCharityFragment;
import bigshots.people_helping_people.fragments.DonationsFragment;
import bigshots.people_helping_people.fragments.FeedbackFragment;
import bigshots.people_helping_people.fragments.LeaderboardFragment;
import bigshots.people_helping_people.fragments.StatisticsFragment;
import bigshots.people_helping_people.fragments.VoteFragment;

/**
 * Created by root on 10/01/15.
 */
public class ParallaxViewLayout {
    public static final Fragment[] fragments = new Fragment[9];
    private static ViewPager pager;
    private static MyPagerAdapter fragmentPagerAdapter;
    private static PagerSlidingTabStrip pagerSlidingTabStrip;
    private static Context context;
    private static View main;

    public ParallaxViewLayout(Context context, View view) {
        main = view;
        ParallaxViewLayout.context = context;
        setUpTitle();
        setUpFragments();
    }

    public static Context getContext() {
        return context;
    }


    void setUpFragments() {
        fragments[0] = new ContributeFragment();
        fragments[1] = new VoteFragment();
        fragments[2] = new CurrentCharityFragment();
        fragments[3] = new DonationsFragment();
        fragments[4] = new StatisticsFragment();
        fragments[5] = new LeaderboardFragment();
        fragments[6] = new FeedbackFragment();
        fragments[7] = new CreditFragment();
        fragments[8] = new AboutFragment();
        setUpPagerAndAdapter();
    }

    void setUpPagerAndAdapter() {
        fragmentPagerAdapter = new MyPagerAdapter(MainActivity.fragment.getChildFragmentManager());
        setUpPager();
    }

    void setUpPager() {
        pager = (ViewPager) main.findViewById(R.id.pager);
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(4);
        setUpPagerSlidingTabStrip();
    }

    void setUpPagerSlidingTabStrip() {
        pagerSlidingTabStrip = (PagerSlidingTabStrip) main.findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(pager);
        pagerSlidingTabStrip.setOnPageChangedListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                try {
                    ((BaseFragment) fragments[position % fragments.length]).update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    void setUpTitle() {
//        title = (TextView) main.findViewById(R.id.title);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"Main", "Vote", "Current Charity", "Donations",
                "Statistics", "Leaderboard", "Feedback", "Credits", "About"};

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
