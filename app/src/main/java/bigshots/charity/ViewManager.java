package bigshots.charity;

import android.widget.TextView;

import java.util.ArrayList;

import bigshots.charity.io.Charity;

/**
 * Created by root on 20/11/14.
 */
public class ViewManager {
    public static TextView textView;
    public static ArrayList<Charity> charities;
    public static Charity charity;

    public ArrayList<bigshots.charity.io.Charity> getCharities() {
        return charities;
    }

    public void setCharities(ArrayList<Charity> charities) {
        this.charities = charities;
    }

    public Charity getCharity() {
        return charity;
    }

    public void setCharity(Charity charity) {
        this.charity = charity;
    }
}
