package bigshots.charity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import bigshots.charity.io.AsyncConnector;
import bigshots.charity.io.Charity;
import bigshots.charity.io.CharityManager;
import bigshots.charity.io.Connector;
import bigshots.charity.utilities.Interfaces;
import bigshots.charity.utilities.VoteCharityAdapter;

/**
 * Created by root on 18/11/14.
 */
public class Vote extends Activity {
    //Our Own voting system
    //Google plus
    //Facebook
    //Show current stats (total of all votes)
    //Suggest Charity

    /*
Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
startActivity(browserIntent);
 */

    private ListView listView;
    //private CharityListClickListener charityListClickListener;
    private VoteCharityAdapter adapter;
    private ArrayList<Charity> charities;
    private Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(Charity charity) {

        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {
            Vote.this.charities = charities;
            // charityListClickListener = new CharityListClickListener(charities);
            //Todo is this necessary
//            if (listView!=null)
//                listView.setOnItemClickListener(charityListClickListener);
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
        listView = (ListView) findViewById(R.id.list);
        new CharityManager().getCharities();
        AsyncConnector.setListener(aSyncListener);
    }


    private void showSuggestionDialog() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.email);
        final EditText charity = (EditText) dialog.findViewById(R.id.suggested_charity);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send:
                        String charityName = charity.getText().toString();
                        if (charityName != null && charityName.length() > 3) {
                            new Connector().getCharityManager().suggestCharity(charityName);
                            dialog.dismiss();
                        }
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
}
