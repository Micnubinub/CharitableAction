package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.AboutFragment;
import bigshots.people_helping_people.fragments.ContributeFragment;
import bigshots.people_helping_people.fragments.CurrentCharityFragment;
import bigshots.people_helping_people.fragments.FeedbackFragment;
import bigshots.people_helping_people.fragments.LeaderboardFragment;
import bigshots.people_helping_people.fragments.PreferencesFragment;
import bigshots.people_helping_people.fragments.StatisticsFragment;
import bigshots.people_helping_people.fragments.VoteFragment;

/**
 * Created by root on 10/01/15.
 */
public class ParallaxViewLayout implements ScrollListener {

    private static final Fragment[] fragments = new Fragment[8];
    //    private static final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//    private static final DecelerateInterpolator decel = new DecelerateInterpolator();
    private static int w, h;
    //Don't need nothing
    private static int scrollY, titleBottom;
    private static int tabStripHeight;
    private static int currentPos;
    //Already have set up methods
    private static ParallaxViewPager pager;
    private static MyPagerAdapter fragmentPagerAdapter;
    private static PagerSlidingTabStrip pagerSlidingTabStrip;
    //    private static KenBurnsSupportView kenBurnsSupportView;
    private static int scrollYMax;
    //    private static TextView title;
    private static Context context;
    private static ImageView currentCharityLogo;
    private static View main;


    public ParallaxViewLayout(Context context, View view) {
        main = view;
        ParallaxViewLayout.context = context;
        getYScrollMax();

        setUpTitle();
        setUpKenBurnsSupportView();
        setUpFragments();
        setUpCurrentCharityLogo();
        scrollY = scrollYMax;
    }

    public static Context getContext() {
        return context;
    }

    private static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        scrollY = scrollY < scrollYMax ? scrollY : scrollYMax;
        ParallaxViewLayout.scrollY = scrollY;
        setViewsY();
    }

    public static void selectPage(int page) {
        try {
            if (pager != null)
                pager.setCurrentItem(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getYScrollMax() {
        scrollYMax = dpToPixels(250);
        final DisplayMetrics metrics = new DisplayMetrics();
        MainMenu.fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        titleBottom = dpToPixels(56);
        tabStripHeight = titleBottom;
        w = metrics.widthPixels;
        h = metrics.heightPixels;
        scrollYMax = Math.min(h / 2, scrollYMax);
//        currentCharityLogoMaxHeight = scrollYMax - titleBottom - tabStripHeight;
//        Log.e("ScrollYMax", String.valueOf(scrollYMax));
    }

    public void setUpCurrentCharityLogo() {
        currentCharityLogo = (ImageView) main.findViewById(R.id.logo);
        currentCharityLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        currentCharityLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nbcf.org.au")));
            }
        });
    }

    public void setUpFragments() {
        BaseFragment.scrollListener = this;
        long tic = System.currentTimeMillis();
        fragments[0] = new ContributeFragment();
        fragments[1] = new VoteFragment();
        fragments[2] = new CurrentCharityFragment();
        fragments[3] = new StatisticsFragment();
        fragments[4] = new LeaderboardFragment();
        fragments[5] = new FeedbackFragment();
        fragments[6] = new PreferencesFragment();
        fragments[7] = new AboutFragment();
        Log.e("setUpFrags tic-toc", String.valueOf(System.currentTimeMillis() - tic));
        setUpPagerAndAdapter();
    }

    public void setUpKenBurnsSupportView() {
        //kenBurnsSupportView = (KenBurnsSupportView) main.findViewById(R.id.ken_burns);
    }

    public void setUpPagerAndAdapter() {
        fragmentPagerAdapter = new MyPagerAdapter(MainMenu.getFragManager());
        setUpPager();
    }

    public void setUpPager() {
        pager = (ParallaxViewPager) main.findViewById(R.id.pager);
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(9);
        pager.setScrollListener(this);
        setUpPagerSlidingTabStrip();
    }

    public void setUpPagerSlidingTabStrip() {
        pagerSlidingTabStrip = (PagerSlidingTabStrip) main.findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(pager);
        pagerSlidingTabStrip.setTextColor(0xfffffff);
        updatePages();
    }

    public void setUpTitle() {
//        title = (TextView) main.findViewById(R.id.title);
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onScrollX(int posX, float amount) {
        if (posX != currentPos)
            updatePages();

        currentPos = posX;
    }

    @Override
    public void onScrollY(int scrollViewTop, int firstVisibleChildPos, int firstVisibleChildTop, int scrollY) {
        setScrollY(scrollYMax - scrollY);
        setSidePageTranslation();
    }

    private void setViewsY() {
//
//        float scaleY = (scrollY - titleBottom) / (float) (scrollYMax - titleBottom);
//        scaleY = scaleY > 1 ? 1 : scaleY;
//        scaleY = scaleY < 0 ? 0 : scaleY;
//        final int imageH = Math.round(scaleY * currentCharityLogoMaxHeight);
//        resizeView(currentCharityLogo, 0, imageH);
//        //resizeView(pager, scrollY + tabStripHeight, h - tabStripHeight - titleBottom - imageH);
//
//        int pagerSlidingTabStripY = titleBottom + imageH;
//        pagerSlidingTabStripY = pagerSlidingTabStripY - (scrollYMax - tabStripHeight);
//
//        ViewHelper.setTranslationY(kenBurnsSupportView, pagerSlidingTabStripY);
//
//        currentCharityLogo.invalidate();
//        ViewHelper.setTranslationY(pagerSlidingTabStrip, pagerSlidingTabStripY);
//        setSidePageTranslation();

    }

    private void setSidePageTranslation() {
//        int translation = scrollY;
//        int item = pager.getCurrentItem();
//        if (item - 1 >= 0) {
//            ViewHelper.setTranslationY(((MyPagerAdapter) pager.getAdapter()).getItem(item - 1).getView(), translation);
//        }
//
//        if (item + 1 < pager.getAdapter().getCount()) {
//            ViewHelper.setTranslationY(((MyPagerAdapter) pager.getAdapter()).getItem(item + 1).getView(), translation);
//        }
//
//        ViewHelper.setTranslationY(((MyPagerAdapter) pager.getAdapter()).getItem(item).getView(), translation);
    }

//    private void resizeView(View view, int y, int height) {
//        final ViewGroup.LayoutParams params = view.getLayoutParams();
//        params.height = height;
//        view.setLayoutParams(params);
//        view.setY(y);
//        view.requestLayout();
//    }

    private void updatePages() {
        int item = pager.getCurrentItem();
        if (item - 1 >= 0) {
            ((BaseFragment) ((MyPagerAdapter) pager.getAdapter()).getItem(item - 1)).update();
        }

        if (item + 1 < pager.getAdapter().getCount()) {
            ((BaseFragment) ((MyPagerAdapter) pager.getAdapter()).getItem(item + 1)).update();
        }

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"Free Donation", "Vote", "Current Charity",
                "Statistics", "Leaderboard", "Feedback", "Preferences", "About"};

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
