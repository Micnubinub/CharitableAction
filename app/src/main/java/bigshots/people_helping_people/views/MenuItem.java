package bigshots.people_helping_people.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by root on 22/11/14.
 */
@SuppressWarnings("ALL")
public class MenuItem extends ImageView {
    private int xPos;

    public MenuItem(Context context, int resID) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
        setImageResource(resID);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDistanceScale(float scale) {
        scale = scale < 0.01f ? 0.01f : scale;
        scale = scale > 1 ? 1 : scale;
        setScaleX(scale);
        setScaleY(scale);
    }

    @Override
    public float getX() {
        return xPos;
    }

    @Override
    public void setX(float x) {
        this.xPos = (int) x;
        super.setX(x);
    }
}
