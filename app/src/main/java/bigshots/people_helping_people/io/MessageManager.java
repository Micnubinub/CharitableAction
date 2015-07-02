package bigshots.people_helping_people.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class MessageManager {

    public void sendMessage(String msg, String email) {
        ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
        nvp.add(new BasicNameValuePair("MESSAGE", msg));
        nvp.add(new BasicNameValuePair("EMAIL", email));
        AsyncConnector.makeConnection(nvp, "SEND_MESSAGE.php", "SEND_MESSAGE");
    }
}
