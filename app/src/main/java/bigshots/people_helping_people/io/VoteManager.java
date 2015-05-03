package bigshots.people_helping_people.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;

public class VoteManager {

    public void castVote(String url, String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
        nvp.add(new BasicNameValuePair("URL", url));
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "VOTE_CAST.php", "VOTE_CAST");
        MainMenu.toast("Casting vote");
    }

    public void removeVote(String url, String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        nvp.add(new BasicNameValuePair("URL", url));
        AsyncConnector.makeConnection(nvp, "VOTE_REMOVE.php", "VOTE_REMOVE");
    }
}
