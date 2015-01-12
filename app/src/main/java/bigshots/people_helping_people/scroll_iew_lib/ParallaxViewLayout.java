package bigshots.people_helping_people.scroll_iew_lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.AboutFragment;

/**
 * Created by root on 10/01/15.
 */
public class ParallaxViewLayout implements ScrollListener {

    private static final int logoBackground = 0xaa000000;
    private static final Fragment[] fragments = new Fragment[7];
    private static final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
    private static final DecelerateInterpolator decel = new DecelerateInterpolator();
    private static int w, h;
    //Don't need nothing
    private static int scrollY;
    private static int currentCharityLogoHeight;
    private static int currentPos;
    //Already have set up methods
    private static ParallaxViewPager pager;
    private static MyPagerAdapter fragmentPagerAdapter;
    private static PagerSlidingTabStrip pagerSlidingTabStrip;
    private static KenBurnsSupportView kenBurnsSupportView;
    private static int scrollYMax;
    //Todo need setUpMethods
    private static TitleTextButton title;
    private static Context context;
    private static ImageView currentCharityLogo;
    private static LayoutParams pagerParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    private static LayoutParams logoParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    private static LayoutParams titleParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    private static LayoutParams kenBurnsParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    private static LayoutParams tabsParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    private static View main;
    private static View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            onLayout();
        }
    };

    public ParallaxViewLayout(Context context, View view) {
        main = view;
        ParallaxViewLayout.context = context;
        getYScrollMax();

        setUpTitle();
        setUpKenBurnsSupportView();
        setUpFragments();
        setUpCurrentCharityLogo();
        setLayoutListener();
        scrollY = scrollYMax;
    }

    public static Context getContext() {
        return context;
    }

    private static void onLayout() {
//        int titleBottom = title.getView().getMeasuredHeight();
//        //Todo
//        layout(title.getView(),0, 0, w, titleBottom);
//        //Todo
//        int pagerSlidingTabStripY = scrollY - pagerSlidingTabStrip.getMeasuredHeight();
//        pagerSlidingTabStripY = clamp(pagerSlidingTabStripY, titleBottom, scrollYMax - pagerSlidingTabStrip.getMeasuredHeight());
//        layout(pagerSlidingTabStrip, 0, pagerSlidingTabStripY, w, pagerSlidingTabStripY + pagerSlidingTabStrip.getMeasuredHeight());
//        layout(kenBurnsSupportView, 0, 0, w, scrollYMax);
//        layout(currentCharityLogo, 0, titleBottom, w, pagerSlidingTabStrip.getTop());
//        layout(pager, 0, scrollY, w, h);
//        invalidatePager();
    }

    private static void layout(View view, int left, int top, int right, int bottom) {
//    view.setX(left);
//    view.setY(top);
//
//    LayoutParams params = null;
//    if (view instanceof KenBurnsSupportView) {
//        params = kenBurnsParams;
//    } else if (view instanceof ParallaxViewPager) {
//        params = pagerParams;
//    } else if (view instanceof PagerSlidingTabStrip) {
//        params = tabsParams;
//    } else if (view instanceof ImageView) {
//        params = logoParams;
//    } else {
//        params = titleParams;
//    }
//
//    params.width = Math.abs(right - left);
//    params.height = Math.abs(bottom - top);
//
//    view.setLayoutParams(params);
    }

    private static void invalidatePager() {
        pagerParams.width = w;
        pagerParams.height = Math.abs(pager.getTop() - pager.getBottom());
//        pager.setLayoutParams(pagerParams);
    }

    private static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    private static void setLayoutListener() {
        kenBurnsSupportView.addOnLayoutChangeListener(layoutChangeListener);
        pager.addOnLayoutChangeListener(layoutChangeListener);
        pagerSlidingTabStrip.addOnLayoutChangeListener(layoutChangeListener);
        currentCharityLogo.addOnLayoutChangeListener(layoutChangeListener);
        title.getView().addOnLayoutChangeListener(layoutChangeListener);
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
        currentCharityLogo.setOnClickListener(new View.OnClickListener() {
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

//        fragments[0] = new ContributeFragment();
//        fragments[1] = new VoteFragment();
//        fragments[2] = new CurrentCharityFragment();
//        fragments[3] = new StatisticsFragment();
//        fragments[4] = new LeaderboardFragment();
//        fragments[5] = new FeedbackFragment();
//        fragments[6] = new AboutFragment();

        fragments[0] = new AboutFragment();
        fragments[1] = new AboutFragment();
        fragments[2] = new AboutFragment();
        fragments[3] = new AboutFragment();
        fragments[4] = new AboutFragment();
        fragments[5] = new AboutFragment();
        fragments[6] = new AboutFragment();

        setUpPagerAndAdapter();
    }

    public void setUpKenBurnsSupportView() {
        kenBurnsSupportView = (KenBurnsSupportView) main.findViewById(R.id.ken_burns);
    }

    public void setUpPagerAndAdapter() {
        //Todo
        fragmentPagerAdapter = new MyPagerAdapter(MainMenu.getFragManager());
        setUpPager();
    }

    public void setUpPager() {
        //Todo
        pager = (ParallaxViewPager) main.findViewById(R.id.pager);
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(8);
        pager.setScrollListener(this);
        setUpPagerSlidingTabStrip();
    }

    public void setUpPagerSlidingTabStrip() {
        //Todo
        pagerSlidingTabStrip = (PagerSlidingTabStrip) main.findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(pager);
        pagerSlidingTabStrip.setBackgroundColor(logoBackground);
        pagerSlidingTabStrip.setTextColor(0xfffffff);
        final int fourDP = dpToPixels(4);
        pagerSlidingTabStrip.setPadding(fourDP, fourDP, fourDP, fourDP);
    }

    public void setUpTitle() {
        title = new TitleTextButton(getContext(), main.findViewById(R.id.title_with_button));
        //Todo make drawable that turn brighter pink when clicked titleView.setBackground();
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
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
