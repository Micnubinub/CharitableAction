package bigshots.charity.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 18/11/14.
 */
public class BannerPopup extends ViewGroup {

    private static final OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public BannerPopup(Context context) {
        super(context);
    }

    public BannerPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public BannerPopup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                measuredHeight += child.getMeasuredHeight();
                measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth());
            }
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }
}
