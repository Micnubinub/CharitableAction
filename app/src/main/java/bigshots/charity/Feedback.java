package bigshots.charity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import bigshots.charity.io.Connector;

/**
 * Created by root on 18/11/14.
 */
public class Feedback extends Activity {
    //Email
    //Facebook page
    //G+ page
    private static final Connector connector = new Connector();

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.email:
                    showEmailDialog();
                    break;
                case R.id.gplus:
                    //Todo make gPlus age
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://plus.google.com"));
                    startActivity(browserIntent);
                    break;
                case R.id.fb:
                    //Todo make facebook page
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                    startActivity(intent);
                    break;
                case R.id.direct:
                    showDirectMessageDialog();
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
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent(Feedback.this, MainMenu.class);
                startActivity(i);
            }
        });
    }

    private void showDirectMessageDialog() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.direct_message);
        final EditText editText = (EditText) dialog.findViewById(R.id.message);

        final View.OnClickListener directMessageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send:
                        String message = editText.getText().toString();
                        if (message != null && message.length() > 1)
                            connector.getMessageManager().sendMessage(message);
                        Toast.makeText(Feedback.this.getApplicationContext(), "Your message is being sent", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        dialog.findViewById(R.id.send_cancel).findViewById(R.id.send).setOnClickListener(directMessageListener);
        dialog.findViewById(R.id.send_cancel).findViewById(R.id.cancel).setOnClickListener(directMessageListener);
        dialog.show();
    }


    private void showEmailDialog() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.email);
        final EditText subject = (EditText) dialog.findViewById(R.id.subject);
        final EditText body = (EditText) dialog.findViewById(R.id.body);
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send:
                        String bodyString = body.getText().toString();
                        String subjectString = subject.getText().toString();
                        sendMessage(subjectString, bodyString);
                        dialog.dismiss();
                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        dialog.findViewById(R.id.send_cancel).findViewById(R.id.send).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.send_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);

        dialog.show();
    }

    private void sendMessage(String subject, String body) {
        Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
        emailintent.setType("plain/text");
        emailintent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"sidney@cyberkomm.ch", "lindelwencube.ln@gmail.com"});
        emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailintent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailintent, "Send mail..."));
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
