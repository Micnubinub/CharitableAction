package bigshots.charity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by root on 22/11/14.
 */
public class MenuItem extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int resID;
    private Bitmap bitmap;

    public MenuItem(Context context, int resID) {
        super(context);
        this.resID = resID;
        paint.setColor(0xffffff);
    }

    private void getBitmap(int resID, int r) {
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), resID), r, r, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            canvas.drawBitmap(bitmap, canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
        } catch (Exception e) {
            e.printStackTrace();

            canvas.drawCircle(canvas.getWidth() / 2,
                    canvas.getHeight() / 2,
                    Math.min(canvas.getWidth(), canvas.getHeight()) / 2,
                    paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        try {
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Math.min(w, h) > 1)
            setVisibility(VISIBLE);

        getBitmap(resID, Math.min(w, h));
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setDistance(float dist) {
        Log.e("setDist", String.valueOf(dist));
        dist = Math.abs(dist);

        if (dist > 1 || dist < 0)
            return;
        setScaleX(dist);
        setScaleY(dist);
    }
}
