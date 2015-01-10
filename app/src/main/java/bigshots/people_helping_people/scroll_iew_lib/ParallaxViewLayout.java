package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 10/01/15.
 */
public class ParallaxViewLayout extends ViewGroup implements ScrollListener {
    //Don't need nothin
    int scrollY;

    //Already have set up methods
    private ParallaxViewPager pager;
    private Fragment[] fragments;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private PagerAdapter pagerAdapter;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private KenBurnsSupportView kenBurnsSupportView;

    //Todo need setUpMethods
    private TextView title;
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
    }

    public void setUpFragments() {
        //Todo
    }

    public void setUpKenBurnsSupportView() {
        //Todo
    }

    public void setUpPagerAdapter() {
        //Todo
    }

    public void setUpPager() {
        //Todo
    }

    public void setUpPagerSlidingTabStrip() {
        //Todo
    }

    public void setUpFragmentPagerAdapter() {
        //Todo
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
//Todo


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

    }

    @Override
    public void onScrollY(int scrollViewTop, int firstVisibleChildPos, int firstVisibleChildTop, int scrollY) {
//Todo
    }

    @Override
    public void setScrollY(int scrollY) {
        scrollY = scrollY <= scrollYMax ? scrollY : scrollYMax;
        this.scrollY = scrollY;
        setViewsY();
    }

    private void setViewsY() {
        //Todo
        //Todo  ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));

    }


}
