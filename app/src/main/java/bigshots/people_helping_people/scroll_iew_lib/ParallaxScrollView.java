package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by root on 8/01/15.
 */
public class ParallaxScrollView extends ScrollView implements ScrollListener {
    protected final int[] firstVisibleChildVals = new int[2];
    private ScrollListener scrollListener;
    private int initX, currX;

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN:
                        initX = Math.round(event.getX());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currX = Math.round(event.getX());
                        break;
                }
                return false;
            }
        });
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

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //Log.e("overScrolledBy:", "" + scrollY + ", " + (currX - initX));
    }

}
