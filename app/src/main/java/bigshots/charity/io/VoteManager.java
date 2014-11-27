package bigshots.charity.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class VoteManager {

    public VoteManager() {

    }

    public Charity getCurrentCharity() {
        return new Charity();
    }

    public void castVote(String url, String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<>(2);
        nvp.add(new BasicNameValuePair("URL", url));
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "VOTE_CAST.php", "VOTE_CAST");
    }

    public void removeVote(String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<>(1);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "VOTE_REMOVE.php", "VOTE_REMOVE");
    }

}
