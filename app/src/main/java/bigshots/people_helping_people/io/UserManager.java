package bigshots.people_helping_people.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class UserManager {
    public void insertUser(String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "USER_INSERT.php", "USER_INSERT");
    }

    public void postStats(String useremail, int score, float rate) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(3);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        nvp.add(new BasicNameValuePair("USER_SCORE", String.valueOf(score)));
        nvp.add(new BasicNameValuePair("USER_RATE", String.valueOf(rate)));
        AsyncConnector.makeConnection(nvp, "USER_STATS.php", "USER_STATS");
    }

    public void getLeaderboardListScore(int amount) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("AMOUNT", String.valueOf(amount)));
        AsyncConnector.makeConnection(nvp, "GET_LEADER_S.php", "GET_LEADER");
    }

    public void getLeaderboardListRate(int amount) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("AMOUNT", String.valueOf(amount)));
        AsyncConnector.makeConnection(nvp, "GET_LEADER_R.php", "GET_LEADER");
    }

    public void getScoreRank(String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "GET_SCORE_RANK.php", "GET_RANK");
    }

    public void getRateRank(String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector.makeConnection(nvp, "GET_RATE_RANK.php", "GET_RANK");
    }
}