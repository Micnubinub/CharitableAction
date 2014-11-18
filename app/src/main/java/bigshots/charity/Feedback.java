package bigshots.charity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by root on 18/11/14.
 */
public class Feedback extends Activity {
    //Email
    //Facebook page
    //G+ page


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
    Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
    emailintent.setType("plain/text");
    emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"mailk@gmail.com" });
    emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
    emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
    startActivity(Intent.createChooser(emailintent, "Send mail..."));
     */

    /*
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
startActivity(browserIntent);
     */
}
