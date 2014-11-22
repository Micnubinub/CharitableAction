package bigshots.charity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by root on 22/11/14.
 */
public class MainBannerView extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private BannerPopup.State state = BannerPopup.State.SHOWING_AD;
    private Bitmap bitmap;
    private int w, h;

    public MainBannerView(Context context) {
        super(context);
    }

    public void setState(BannerPopup.State state) {
        this.state = state;

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
    }

    private void giving() {
//Todo fill in
    }

    private void menu() {
//Todo fill in
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }
}
