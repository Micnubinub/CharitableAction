package bigshots.charity.io;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class MessageManager {

    public MessageManager() {

    }

    public void sendMessage(String msg) {
        ArrayList<NameValuePair> nvp = new ArrayList<>(1);
        nvp.add(new BasicNameValuePair("MESSAGE", msg));
        AsyncConnector.makeConnection(nvp, "SEND_MESSAGE.php", "SEND_MESSAGE");
    }
}
