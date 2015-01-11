package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by root on 8/01/15.
 */
public class ParallaxScrollView extends ScrollView implements ScrollListener {
    protected final int[] firstVisibleChildVals = new int[2];
    private ScrollListener scrollListener;

    //Todo fill in
    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    protected void onScrollChanged(int horizontalScroll, int verticalScroll, int oldHorizontalScroll, int oldVerticalScroll) {
        super.onScrollChanged(horizontalScroll, verticalScroll, oldHorizontalScroll, oldVerticalScroll);
        getFirstVisibleChildVals();
        onScrollY(getTop(), firstVisibleChildVals[0], firstVisibleChildVals[1], verticalScroll);

    }

    private void getFirstVisibleChildVals() {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getTop() <= 0 && child.getBottom() >= 0) {
                firstVisibleChildVals[0] = i;
                firstVisibleChildVals[1] = child.getTop();
                return;
            }
        }
    }

    @Override
    public void onScrollX(int posX, float amount) {

    }

    @Override
    public void onScrollY(int scrollViewTop, int firstVisibleChildPos, int firstVisibleChildTop, int scrollY) {
        if (scrollListener != null)
            scrollListener.onScrollY(scrollViewTop, firstVisibleChildPos, firstVisibleChildTop, scrollY);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }


}
