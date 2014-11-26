package bigshots.charity.views;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by root on 22/11/14.
 */
public class MenuItem extends ImageView {
    private int xPos;

    public MenuItem(Context context, int resID) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
        setImageResource(resID);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
