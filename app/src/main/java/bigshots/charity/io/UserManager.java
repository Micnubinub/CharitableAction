package bigshots.charity.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class UserManager {

    public UserManager() {

    }

    public void insertUser(String useremail) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
        nvp.add(new BasicNameValuePair("USER_EMAIL", useremail));
        AsyncConnector
                .makeConnection(nvp, "USER_INSERT.php", "USER_INSERT");
    }
}