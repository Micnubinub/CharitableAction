package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

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
public class ParallaxViewLayout extends ViewGroup implements ScrollListener {

    private static final int logoBackground = 0xaa000000;
    private static int w, h;
    private final Fragment[] fragments = new Fragment[7];
    //Don't need nothing
    private int scrollY;
    private int currentCharityLogoHeight;
    private int currentPos;
    //Already have set up methods
    private ParallaxViewPager pager;
    private MyPagerAdapter fragmentPagerAdapter;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private KenBurnsSupportView kenBurnsSupportView;
    private int scrollYMax;

    //Todo need setUpMethods
    private TitleTextButton title;
    private ImageView currentCharityLogo;
    private LayoutParams pagerParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

    public ParallaxViewLayout(Context context) {
        super(context);
        init();
    }

    public ParallaxViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getYScrollMax();
        setUpTitle();
        setUpKenBurnsSupportView();
        setUpFragments();
        setUpCurrentCharityLogo();
        addViews();
        scrollY = scrollYMax;
    }

    private void getYScrollMax() {
        scrollYMax = dpToPixels(250);
        final DisplayMetrics metrics = new DisplayMetrics();
        MainMenu.fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w = metrics.widthPixels;
        h = metrics.heightPixels;
        scrollYMax = Math.min(h / 2, scrollYMax);
        Log.e("ScrollYMax", String.valueOf(scrollYMax));
    }

    public void setUpCurrentCharityLogo() {
        currentCharityLogo = new ImageView(getContext());
        currentCharityLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        currentCharityLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Open Current Charity", Toast.LENGTH_LONG).show();
            }
        });
        final int p = dpToPixels(8);
        currentCharityLogo.setPadding(p, p, p, p);
        currentCharityLogo.setBackgroundColor(logoBackground);
        currentCharityLogo.setImageResource(R.drawable.logo);
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

//        fragments[0] = new AboutFragment();
//        fragments[1] = new AboutFragment();
//        fragments[2] = new AboutFragment();
//        fragments[3] = new AboutFragment();
//        fragments[4] = new AboutFragment();
//        fragments[5] = new AboutFragment();
//        fragments[6] = new AboutFragment();

        setUpPagerAndAdapter();
    }

    public void setUpKenBurnsSupportView() {
        kenBurnsSupportView = new KenBurnsSupportView(getContext());
        kenBurnsSupportView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, scrollYMax));
    }

    public void setUpPagerAndAdapter() {
        //Todo
        fragmentPagerAdapter = new MyPagerAdapter(MainMenu.fragmentActivity.getSupportFragmentManager());
        setUpPager();
    }

    public void setUpPager() {
        //Todo
        pager = new ParallaxViewPager(getContext());
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(8);
        pager.setScrollListener(this);
        pager.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, h - scrollYMax));
        setUpPagerSlidingTabStrip();
    }

    public void setUpPagerSlidingTabStrip() {
        //Todo
        pagerSlidingTabStrip = new PagerSlidingTabStrip(getContext());
        pagerSlidingTabStrip.setViewPager(pager);
        pagerSlidingTabStrip.setBackgroundColor(logoBackground);
        pagerSlidingTabStrip.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, dpToPixels(56)));
        pagerSlidingTabStrip.setTextColor(0xfffffff);
        final int fourDP = dpToPixels(4);
        pagerSlidingTabStrip.setPadding(fourDP, fourDP, fourDP, fourDP);
    }

    public void setUpTitle() {
        title = new TitleTextButton(getContext());
        final View titleView = title.getView();
        titleView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, dpToPixels(52)));
        final int eDP = dpToPixels(8);
        titleView.setPadding(eDP, eDP, eDP, eDP);
        //Todo make drawable that turn brighter pink when clicked titleView.setBackground();
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
        currentCharityLogo.layout(0, titleBottom, getMeasuredWidth(), pagerSlidingTabStrip.getTop());
        pager.layout(0, scrollY, getMeasuredWidth(), getMeasuredHeight());
        invalidatePager();
    }

    private void invalidatePager() {
        pagerParams.width = w;
        pagerParams.height = Math.abs(pager.getTop() - pager.getBottom());
        pager.setLayoutParams(pagerParams);
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
    @Override
    public void onScrollX(int posX, float amount) {
        if (posX != currentPos)
            //Todo
            ;
        currentPos = posX;
        Log.e("scrollX", String.valueOf(posX + amount));
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
        Log.e("setScrollY", String.valueOf(scrollY));
    }

    private void setViewsY() {
        //Todo
        ViewHelper.setTranslationY(kenBurnsSupportView, scrollY - scrollYMax);

    }

    private void addViews() {
        addView(kenBurnsSupportView);
        addView(pager);
        addView(pagerSlidingTabStrip);
        addView(currentCharityLogo);
        addView(title.getView());
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
