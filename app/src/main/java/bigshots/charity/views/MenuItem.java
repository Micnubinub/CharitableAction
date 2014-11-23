package bigshots.charity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by root on 22/11/14.
 */
public class MenuItem extends View {
    private static int w, h;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int resId;
    private Bitmap bitmap, scaledBitmap;
    private int calcX;
    private int calcY;
    private int calcW;
    private int calcH;

    public MenuItem(Context context, int resId) {
        super(context);
        this.resId = resId;
        bitmap = BitmapFactory.decodeResource(getResources(), resId);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (scaledBitmap != null)
            canvas.drawBitmap(scaledBitmap, calcX, calcY, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        generateScaledBitmap(w, h);
    }

    public void setDistance(float dist) {
        if (dist > 1)
            return;

        if (dist < 0) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        calcW = (int) (w * dist);
        calcH = (int) (h * dist);
        calcX = (w - calcW) / 2;
        calcY = (h - calcH) / 2;

        generateScaledBitmap(calcW, calcH);
    }

    private void generateScaledBitmap(int width, int height) {
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
