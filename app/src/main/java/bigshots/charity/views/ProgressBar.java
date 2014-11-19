package bigshots.charity.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import bigshots.charity.R;

/**
 * Created by root on 19/11/14.
 */
public class ProgressBar extends View {
    private final Paint paint = new Paint();
    private int progress, w, h;
    private float max;
    private int backgroundColor, progressColor;

    public ProgressBar(Context context) {
        super(context);
        progress = 50;
        max = 100;
        progressColor = getResources().getColor(R.color.material_blue);
        backgroundColor = getResources().getColor(R.color.light_grey);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        w = width;
        h = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int stopStart = Math.round((progress / max) * w);

        paint.setColor(progressColor);
        canvas.drawRect(0, h, stopStart, 0, paint);

        paint.setColor(backgroundColor);
        canvas.drawRect(stopStart, h, w, 0, paint);

    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
