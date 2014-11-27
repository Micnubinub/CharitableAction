package bigshots.people_helping_people;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;
import bigshots.people_helping_people.views.RippleButton;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Vote extends Activity {

    private ListView listView;
    private VoteCharityAdapter adapter;
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(Charity charity) {

        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new VoteCharityAdapter(Vote.this, charities);
                    if (listView != null)
                        listView.setAdapter(adapter);
                }
            });


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuggestionDialog();
            }
        });
        ((RippleButton) findViewById(R.id.fab)).setIsCircle(true);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.list);
        new CharityManager().getCharities();
        AsyncConnector.setListener(aSyncListener);
    }


    private void showSuggestionDialog() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.suggest_charity);
        final EditText charity = (EditText) dialog.findViewById(R.id.suggested_charity);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.submit:
                        String charityName = charity.getText().toString();
                        if (charityName != null && charityName.length() > 3) {
                            new CharityManager().suggestCharity(charityName);
                            Toast.makeText(getApplicationContext(), "Thank you for your suggestion.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);

        dialog.show();
    }
}
