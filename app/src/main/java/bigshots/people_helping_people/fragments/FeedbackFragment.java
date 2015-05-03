package bigshots.people_helping_people.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Connector;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;


public class FeedbackFragment extends BaseFragment {

    private static final Connector connector = new Connector();

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.email:
                    showEmailDialog();
                    break;
                case R.id.rate:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=bigshots.people_helping_people")));
                    break;
                case R.id.gplus:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/b/117110729119624919274/117110729119624919274/posts")));
                    break;
                case R.id.fb:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/theBigShotsForThePeople?ref=bookmarks")));
                    break;
                case R.id.direct:
                    showDirectMessageDialog();
                    break;
            }
        }
    };

    public FeedbackFragment() {
    }

    private void showDirectMessageDialog() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.direct_message);
        final EditText editText = (EditText) dialog.findViewById(R.id.message);

        final View.OnClickListener directMessageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send:
                        final String message = editText.getText().toString();
                        if (message != null && message.length() > 1)
                            connector.getMessageManager().sendMessage(message, MainMenu.email);
                        Toast.makeText(MainMenu.context, "Your message is being sent", Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
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
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"sidney@cyberkomm.ch", "lindelwencube.ln@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.feedback, container, false);
        view.findViewById(R.id.email).setOnClickListener(listener);
        view.findViewById(R.id.gplus).setOnClickListener(listener);
        view.findViewById(R.id.fb).setOnClickListener(listener);
        view.findViewById(R.id.direct).setOnClickListener(listener);
        view.findViewById(R.id.rate).setOnClickListener(listener);
        //((ParallaxScrollView) view.findViewById(R.id.scroll_view)).setScrollListener(scrollListener);
        return view;
    }

    @Override
    public void update() {

    }
}
