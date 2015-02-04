package bigshots.people_helping_people.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class CharityManager {

    public CharityManager() {

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

    public void suggestCharity(String url) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("URL", url));
        AsyncConnector.makeConnection(nvp, "CHARITY_SUGGEST.php",
                "CHARITY_SUGGEST");
    }

    public void currentCharity(String useremail) {
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
