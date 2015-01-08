package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

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
import bigshots.people_helping_people.new_ui.astuetz.PagerSlidingTabStrip;
import bigshots.people_helping_people.new_ui.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import bigshots.people_helping_people.new_ui.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import bigshots.people_helping_people.new_ui.kmshack.newsstand.ScrollTabHolder;
import bigshots.people_helping_people.new_ui.kmshack.newsstand.ScrollTabHolderFragment;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.Utils;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainMenu extends ActionBarActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener {
    //Todo remove money from leaderboard
    //Todo make slidding toolbar with link to preferencess
    //Todo Consider putting the points++ to onAdShown
    //Todo fix y scroll
    //Todo fix onClick listener in switches
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
    private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();
    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int headerHeight;
    private int mMinHeaderTranslation;
    private ImageView mHeaderLogo;
    private RectF rect1 = new RectF();
    private RectF rect2 = new RectF();
    private TypedValue mTypedValue = new TypedValue();
    private SpannableString spannableString;
    private AlphaForegroundColorSpan alphaForegroundColorSpan;
    private KenBurnsSupportView mHeaderPicture;
    private View mHeader;

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();
        setContentView(R.layout.material_main_menu);

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
        setUpFragments();
    }

    private void setUpFragments() {
        fragments[0] = new ContributeFragment();
        fragments[1] = new VoteFragment();
        fragments[2] = new CurrentCharityFragment();
        fragments[3] = new StatisticsFragment();
        fragments[4] = new LeaderboardFragment();
        fragments[5] = new FeedbackFragment();
        fragments[6] = new AboutFragment();

        mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);
        //Todo add multiple images
        mHeaderPicture.setResourceIds(R.drawable.people, R.drawable.people);
        mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
        mHeader = findViewById(R.id.header);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(7);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setTabHolderScrollingContent(this);
        pager.setAdapter(pagerAdapter);

        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(this);
        spannableString = new SpannableString("Title");
        alphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
        // ViewHelper.setAlpha(getActionBarIconView(), 0f);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // nothing
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = pagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);

        currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper.getTranslationY(mHeader)));
    }

    @Override
    public void onScroll(ScrollView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (pager.getCurrentItem() == pagePosition) {
            int scrollY = getScrollY(view);
            ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
            float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
            interpolate(mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio));
            setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }

    public int getScrollY(ScrollView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getScrollY();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = this.headerHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        } else {
            getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
        }

        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());

        return mActionBarHeight;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    private void setTitleAlpha(float alpha) {
        alphaForegroundColorSpan.setAlpha(alpha);
        spannableString.setSpan(alphaForegroundColorSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ImageView getActionBarIconView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (ImageView) findViewById(android.R.id.home);
        }

        return (ImageView) findViewById(android.support.v7.appcompat.R.id.home);
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(rect1, view1);
        getOnScreenRect(rect2, view2);

        float scaleX = 1.0F + interpolation * (rect2.width() / rect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (rect2.height() / rect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (rect2.left + rect2.right - rect1.left - rect1.right));
        float translationY = 0.5F * (interpolation * (rect2.top + rect2.bottom - rect1.top - rect1.bottom));

        ViewHelper.setTranslationX(view1, translationX);
        ViewHelper.setTranslationY(view1, translationY - ViewHelper.getTranslationY(mHeader));
        ViewHelper.setScaleX(view1, scaleX);
        ViewHelper.setScaleY(view1, scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"Free Donation", "Vote", "Current Charity",
                "Statistics", "Leaderboard", "Feedback", "About"};
        private SparseArrayCompat<ScrollTabHolder> scrollTabHolders;
        private ScrollTabHolder listener;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            scrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            this.listener = listener;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return scrollTabHolders;
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
            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) fragments[position];

            scrollTabHolders.put(position, fragment);
            if (listener != null) {
                fragment.setScrollTabHolder(listener);
            }
            return fragments[position];
        }
    }


}
