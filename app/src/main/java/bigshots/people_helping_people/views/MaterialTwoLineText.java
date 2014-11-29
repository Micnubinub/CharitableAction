package bigshots.people_helping_people.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bigshots.people_helping_people.R;

/**
 * Created by root on 20/10/14.
 */
@SuppressWarnings("ALL")
public class MaterialTwoLineText extends ViewGroup {
    private TextView primaryTextView, secondaryTextView;
    private String primaryText, secondaryText;
    private int width;
    private int height;
    private int clickedX, clickedY;

    public MaterialTwoLineText(Context context) {
        super(context);
        init();
    }


    public void setPrimaryText(String text) {
        primaryText = text;
        primaryTextView.setText(text);
    }

    public void setSecondaryText(String text) {
        secondaryText = text;
        secondaryTextView.setText(text);

    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        final int padding = dpToPixels(12);
        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        primaryTextView = new TextView(getContext());
        primaryTextView.setTextColor(getResources().getColor(R.color.dark_grey));
        primaryTextView.setTypeface(null, Typeface.BOLD);
        primaryTextView.setTextSize(22);
        primaryTextView.setMaxLines(1);
        primaryTextView.setLayoutParams(params);
        primaryTextView.setEllipsize(TextUtils.TruncateAt.END);
        primaryTextView.setPadding(padding, 0, 0, 0);

        secondaryTextView = new TextView(getContext());
        secondaryTextView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        secondaryTextView.setTextSize(18);
        secondaryTextView.setMaxLines(1);
        secondaryTextView.setLayoutParams(params);
        secondaryTextView.setEllipsize(TextUtils.TruncateAt.END);
        secondaryTextView.setPadding(padding, 0, 0, 0);

        setBackground(getResources().getDrawable(R.drawable.white_button_selector));

        addView(primaryTextView);
        addView(secondaryTextView);
    }

    public void setSecondaryTextColor(int color) {
        secondaryTextView.setTextColor(color);
    }

    public void setSecondaryTextMaxLines(int maxLines) {
        secondaryTextView.setMaxLines(maxLines);
    }

    public void setSecondaryTextSize(int sp) {
        secondaryTextView.setTextSize(sp);
    }

    public void setPrimaryTextSize(int sp) {
        primaryTextView.setTextSize(sp);
    }

    public void setPrimaryTextColor(int color) {
        primaryTextView.setTextColor(color);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int textViewPadding = ((getMeasuredHeight() - primaryTextView.getMeasuredHeight() - secondaryTextView.getMeasuredHeight()) / 3);
        primaryTextView.layout(getPaddingLeft(), textViewPadding,
                getMeasuredWidth() - getPaddingRight(),
                primaryTextView.getMeasuredHeight() + textViewPadding);

        checkViewParams(primaryTextView);

        secondaryTextView.layout(getPaddingLeft(),
                primaryTextView.getMeasuredHeight() + textViewPadding + textViewPadding,
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - textViewPadding
        );

        checkViewParams(secondaryTextView);
    }

    private void checkViewParams(final View view, final int layoutWidth, final int layoutHeight) {
        final int width = view.getMeasuredWidth();
        final int height = view.getMeasuredHeight();
        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            measureChild(view, MeasureSpec.AT_MOST, MeasureSpec.AT_MOST);
            view.requestLayout();
            view.invalidate();
            requestLayout();

        }
    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getRight() - view.getLeft();
        final int layoutHeight = view.getBottom() - view.getTop();

        checkViewParams(view, layoutWidth, layoutHeight);


    }


    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= 2)
            return;
        super.addView(child, index, params);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {

            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredHeight += child.getMeasuredHeight();
            measuredWidth = Math.max(child.getMeasuredWidth(), measuredWidth);
        }

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));


    }
}
