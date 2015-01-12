package bigshots.people_helping_people.scroll_iew_lib;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import bigshots.people_helping_people.R;

/**
 * Created by root on 10/01/15.
 */
public class TitleTextButton {
    private static View view;
    private TextView title;
    private FrameLayout container;
    private Context context;

    public TitleTextButton(Context context, View view) {
        this.context = context;
        TitleTextButton.view = view;
        title = (TextView) view.findViewById(R.id.title);
        container = (FrameLayout) view.findViewById(R.id.container);
        //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, dpToPixels(56)));
    }

    public View getView() {
        return view;
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public void setText(String text) {
        title.setText(text);
    }

    public void setTextColor(int color) {
        title.setTextColor(color);
    }

    public void setTextSize(int sp) {
        title.setTextSize(sp);
    }

    private void setButtonView(View view) {
        if (container == null)
            return;

        try {
            container.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(view);
    }

    public void setAlpha(float alpha) {
        view.setAlpha(alpha);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        //Todo
        view.setOnClickListener(listener);
    }

    public void setBackgroundResourse(int drawable) {
        view.setBackgroundResource(drawable);
    }

    public void setBackground(int color) {
        view.setBackgroundColor(color);
    }
}
