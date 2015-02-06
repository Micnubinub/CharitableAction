package bigshots.people_helping_people.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import bigshots.people_helping_people.R;

/**
 * Created by root on 20/10/14.
 */
@SuppressWarnings("ALL")
public class MaterialTwoLineText {
    private TextView primaryTextView, secondaryTextView, trustedView;
    private View view;
    private String primaryText, secondaryText;
    private Context context;
    private int width;
    private int height;
    private int clickedX, clickedY;

    public MaterialTwoLineText(Context context) {
        init(context);
    }

    public View getView() {
        return view;
    }

    public void setPrimaryText(String text) {
        primaryText = text;
        primaryTextView.setText(text);
    }

    public Context getContext() {
        return context;
    }

    private Resources getResources() {
        return context.getResources();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init(Context context) {
        this.context = context;
        final int padding = dpToPixels(12);
        view = View.inflate(context, R.layout.two_line, null);

        primaryTextView = (TextView) view.findViewById(R.id.primary);
        secondaryTextView = (TextView) view.findViewById(R.id.secondary);
        trustedView = (TextView) view.findViewById(R.id.trusted);

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


    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String text) {
        secondaryText = text;
        secondaryTextView.setText(text);

    }

    public void setTrustedViewText(String text) {
        trustedView.setText(text);
    }

}
