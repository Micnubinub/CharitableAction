package bigshots.charity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import bigshots.charity.R;

/**
 * Created by root on 22/11/14.
 */
public class MainBannerView extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //private BannerPopup.State state = BannerPopup.State.SHOWING_AD;
    private Bitmap bitmap;
    private int w, h, resID = R.drawable.icon_blue;
    private boolean paramsSet = false;

    public MainBannerView(Context context, int r) {
        super(context);
        this.setLayoutParams(new ViewGroup.LayoutParams(r, r));
        paramsSet = true;
    }

    public void setState(BannerPopup.State state) {
        //this.state = state;
        switch (state) {
            case MINIMISED:
                give();
                break;
            case SHOWING_AD:
                giving();
                break;
            case SHOWING_MENU:
                menu();
                break;
        }
    }

    private void give() {
//Todo fill in
        getBitmap(R.drawable.icon_orange);
        invalidate();
    }

    private void giving() {
//Todo fill in
        getBitmap(R.drawable.icon_blue);
        invalidate();
    }

    private void menu() {
//Todo fill in
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null)
            canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private void getBitmap(int resID) {
        this.resID = resID;
        final int r = Math.min(w, h);

        if (r < 1)
            return;

        try {
            if (bitmap != null)
                bitmap.recycle();

            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), resID), r, r, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;

        if (paramsSet)
            getBitmap(resID);

    }
}
