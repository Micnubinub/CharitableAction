package bigshots.people_helping_people.io;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class CharityManager {
    //Todo
    public CharityManager() {

    }

    public void getTotalScore() {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("DUMMY", "."));
        AsyncConnector.makeConnection(nvp, "GET_SCORE_TOTAL.php", "GET_SCORE_TOTAL");
    }

    public void getCharities() {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("DUMMY", "X"));
        AsyncConnector
                .makeConnection(nvp, "GET_CHARITIES.php", "GET_CHARITIES");
    }

    public void getHistory() {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("DUMMY", "X"));
        AsyncConnector
                .makeConnection(nvp, "GET_HISTORY.php", "GET_HISTORY");
    }

    public void suggestCharity(String name, String url, String desc) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(3);
        nvp.add(new BasicNameValuePair("NAME", name));
        nvp.add(new BasicNameValuePair("URL", url));
        nvp.add(new BasicNameValuePair("DESC", desc));
        AsyncConnector.makeConnection(nvp, "CHARITY_SUGGEST.php",
                "CHARITY_SUGGEST");
    }

    public void currentCharity(String useremail) {
        Log.e("getCurrentCharity", useremail);
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "CHARITY_CURRENT.php",
                "CHARITY_CURRENT");
    }

    public void monthlyCharity() {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("DUMMY", "X"));
        AsyncConnector.makeConnection(nvp, "CHARITY_MONTH.php",
                "CHARITY_MONTH");
    }
}
