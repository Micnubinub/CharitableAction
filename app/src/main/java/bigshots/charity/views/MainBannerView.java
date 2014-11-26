package bigshots.charity.views;

import android.content.Context;
import android.widget.ImageView;

import bigshots.charity.R;

/**
 * Created by root on 22/11/14.
 */
public class MainBannerView extends ImageView {
    private BannerPopup.State state;


    public MainBannerView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    public void setState(BannerPopup.State state) {
        if (this.state != state) {
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
    }

    private void give() {
//Todo fill in
        setImageResource(R.drawable.icon_orange);
        invalidate();
    }

    private void giving() {
//Todo fill in
        setImageResource(R.drawable.icon_blue);
        invalidate();
    }

    private void menu() {
//Todo fill in
    }


}
