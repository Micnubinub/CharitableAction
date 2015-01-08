package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by root on 8/01/15.
 */
public class ParallaxListView extends ListView implements Scrollable {
    protected final int[] firstVisibleChildVals = new int[2];
    private Scrollable scrollListener;

    public ParallaxListView(Context context) {
        super(context);
        init();


    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (getChildCount() < 1)
                    return;
                Log.e("fvi :" + firstVisibleItem, "after :" + view.getChildAt(0).getTop());
                // getFirstVisibleChildVals();
                onScrollY(getTop(), firstVisibleItem, view.getChildAt(0).getTop(), view.getScrollY());
            }
        });
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    protected void onScrollChanged(int horizontalScroll, int verticalScroll, int oldHorizontalScroll, int oldVerticalScroll) {
        super.onScrollChanged(horizontalScroll, verticalScroll, oldHorizontalScroll, oldVerticalScroll);

    }

//    private void getFirstVisibleChildVals() {
//        for (int i = 0; i < getChildCount(); i++) {
//            final View child = getChildAt(i);
//            if (child.getTop() <= 0 && child.getBottom() >= 0) {
//                firstVisibleChildVals[0] = i;
//                firstVisibleChildVals[1] = child.getTop();
//                return;
//            }
//        }
//    }

    @Override
    public void onScrollX(int posX, float amount) {

    }

    @Override
    public void onScrollY(int scrollViewTop, int firstVisibleChildPos, int firstVisibleChildTop, int scrollY) {
        if (scrollListener != null)
            scrollListener.onScrollY(scrollViewTop, firstVisibleChildPos, firstVisibleChildTop, scrollY);
    }

    public void setScrollListener(Scrollable scrollListener) {
        this.scrollListener = scrollListener;
    }
}