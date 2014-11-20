package bigshots.charity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by root on 18/11/14.
 */
public class Feedback extends Activity {
    //Email
    //Facebook page
    //G+ page


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.email:
                    // sidney@cyberkomm.ch
                    break;
                case R.id.gplus:


                    break;
                case R.id.fb:


                    break;
                case R.id.direct:


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        findViewById(R.id.email).setOnClickListener(listener);
        findViewById(R.id.gplus).setOnClickListener(listener);
        findViewById(R.id.fb).setOnClickListener(listener);
        findViewById(R.id.direct).setOnClickListener(listener);

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
