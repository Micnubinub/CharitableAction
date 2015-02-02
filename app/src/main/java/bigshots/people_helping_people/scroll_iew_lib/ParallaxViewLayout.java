package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.AboutFragment;
import bigshots.people_helping_people.fragments.ContributeFragment;
import bigshots.people_helping_people.fragments.CurrentCharityFragment;
import bigshots.people_helping_people.fragments.FeedbackFragment;
import bigshots.people_helping_people.fragments.LeaderboardFragment;
import bigshots.people_helping_people.fragments.StatisticsFragment;
import bigshots.people_helping_people.fragments.VoteFragment;

/**
 * Created by root on 10/01/15.
 */
public class ParallaxViewLayout {
    private static final Fragment[] fragments = new Fragment[7];
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


    public void setUpFragments() {
        BaseFragment.scrollListener = this;
        fragments[0] = new ContributeFragment();
        fragments[1] = new VoteFragment();
        fragments[2] = new CurrentCharityFragment();
        fragments[3] = new StatisticsFragment();
        fragments[4] = new LeaderboardFragment();
        fragments[5] = new FeedbackFragment();
        fragments[6] = new AboutFragment();
        setUpPagerAndAdapter();
    }

    public void setUpPagerAndAdapter() {
        fragmentPagerAdapter = new MyPagerAdapter(MainMenu.getFragManager());
        setUpPager();
    }

    public void setUpPager() {
        pager = (ViewPager) main.findViewById(R.id.pager);
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(6);
        setUpPagerSlidingTabStrip();
    }

    public void setUpPagerSlidingTabStrip() {
        pagerSlidingTabStrip = (PagerSlidingTabStrip) main.findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(pager);
        pagerSlidingTabStrip.setTextColor(0xffffffff);
        updatePages();
    }

    public void setUpTitle() {
//        title = (TextView) main.findViewById(R.id.title);
    }

    private void updatePages() {
        ((BaseFragment) ((MyPagerAdapter) pager.getAdapter()).getItem(pager.getCurrentItem())).update();

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"Main", "Vote", "Current Charity",
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
