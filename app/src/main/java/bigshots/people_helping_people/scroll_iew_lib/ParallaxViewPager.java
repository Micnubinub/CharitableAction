package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by root on 8/01/15.
 */
public class ParallaxViewPager extends ViewPager {
    private ScrollListener scrollListener;

    public ParallaxViewPager(Context context) {
        super(context);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        if (scrollListener != null)
            scrollListener.onScrollX(position, offset);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("pagerSizeChanged", String.format("%d, %d, %d, %d", oldw, oldh, w, h));
    }

    @Override
    public void invalidate() {
        super.invalidate();
//        Log.e("invalidate", String.format("x,y : %f, %f...\n" +
//                "childCound, currentItem %d, %d", getX(), getY(),
//                ((ParallaxViewLayout.MyPagerAdapter)getAdapter()).getItem(getCurrentItem()).getView().getHeight(), getCurrentItem()));
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
        Log.e("received", "set:" + item);
    }
}
