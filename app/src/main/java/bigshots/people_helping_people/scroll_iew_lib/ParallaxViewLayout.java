package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

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
public class ParallaxViewLayout extends ViewGroup implements ScrollListener {

    //Don't need nothing
    private int scrollY;
    private int currentCharityLogoHeight;
    private int currentPos;

    //Already have set up methods
    private ParallaxViewPager pager;
    private Fragment[] fragments;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private KenBurnsSupportView kenBurnsSupportView;

    //Todo need setUpMethods
    private TitleTextButton title;
    private ImageView currentCharityLogo;
    private int scrollYMax;

    public ParallaxViewLayout(Context context) {
        super(context);
        init();
    }

    public ParallaxViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    public void setScrollListeners() {
        //Todo
        pager.setScrollListener(this);
    }

    public void setUpFragments() {
        //Todo
        BaseFragment.scrollListener = this;

        fragments[0] = new ContributeFragment();
        fragments[1] = new VoteFragment();
        fragments[2] = new CurrentCharityFragment();
        fragments[3] = new StatisticsFragment();
        fragments[4] = new LeaderboardFragment();
        fragments[5] = new FeedbackFragment();
        fragments[6] = new AboutFragment();
    }

    public void setUpKenBurnsSupportView() {
        //Todo
        kenBurnsSupportView = new KenBurnsSupportView(getContext());
        kenBurnsSupportView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, dpToPixels(240)));
        //Todo add more
        kenBurnsSupportView.setResourceIds(R.drawable.people);
    }

    public void setUpPagerAdapter() {
        //Todo
        pager = new ParallaxViewPager(getContext());
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(8);

    }

    public void setUpPager() {
        //Todo

    }

    public void setUpPagerSlidingTabStrip() {
        //Todo
        pagerSlidingTabStrip = new PagerSlidingTabStrip(getContext());
        pagerSlidingTabStrip.setViewPager(pager);

    }

    public void setUpFragmentPagerAdapter() {
        //Todo
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        int titleBottom = title.getView().getMeasuredHeight();
        //Todo
        title.getView().layout(0, 0, getMeasuredWidth(), titleBottom);
        //Todo
        int pagerSlidingTabStripY = scrollY - pagerSlidingTabStrip.getMeasuredHeight();
        pagerSlidingTabStripY = clamp(pagerSlidingTabStripY, titleBottom, scrollYMax - pagerSlidingTabStrip.getMeasuredHeight());
        pagerSlidingTabStrip.layout(0, pagerSlidingTabStripY, getMeasuredWidth(), pagerSlidingTabStripY + pagerSlidingTabStrip.getMeasuredHeight());
        kenBurnsSupportView.layout(0, 0, getMeasuredWidth(), scrollYMax);
        currentCharityLogo.layout(0, titleBottom, getMeasuredWidth(), titleBottom + getCurrentCharityLogoHeight());
        pager.layout(0, scrollY, getMeasuredWidth(), getMeasuredHeight());

    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;

        try {
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight());
                measuredWidth += child.getMeasuredWidth();
            }
        } catch (Exception ignored) {
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    public int getCurrentCharityLogoHeight() {
        currentCharityLogoHeight = scrollY - title.getView().getHeight();
        currentCharityLogoHeight = currentCharityLogoHeight > scrollYMax - title.getView().getHeight() ? scrollYMax - title.getView().getHeight() : currentCharityLogoHeight;
        return currentCharityLogoHeight;
    }

    @Override
    public void onScrollX(int posX, float amount) {
        if (posX != currentPos)
            //Todo
            ;
        currentPos = posX;
    }

    @Override
    public void onScrollY(int scrollViewTop, int firstVisibleChildPos, int firstVisibleChildTop, int scrollY) {
//Todo
        setScrollY(scrollYMax - scrollY);
    }

    @Override
    public void setScrollY(int scrollY) {
        scrollY = scrollY <= scrollYMax ? scrollY : scrollYMax;
        this.scrollY = scrollY;
        setViewsY();

    }

    private void setViewsY() {
        //Todo
        ViewHelper.setTranslationY(kenBurnsSupportView, scrollY - scrollYMax);

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
