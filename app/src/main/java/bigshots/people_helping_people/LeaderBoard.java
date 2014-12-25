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
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.LeaderBoardAdapter;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class LeaderBoard extends Activity {
    private static final UserManager userManager = new UserManager();
    private ListView listView;
    private LeaderBoardAdapter adapter;
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(final Charity charity) {
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {

        }

        @Override
        public void onCompleteRank(int rank) {

        }

        @Override
        public void onCompleteLeaderBoardList(final ArrayList<UserStats> stats) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.message).setVisibility(View.GONE);
                            adapter = new LeaderBoardAdapter(LeaderBoard.this, stats, false);
                            listView.setAdapter(adapter);
                        }
                    });
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board);
        AsyncConnector.setListener(aSyncListener);
        getScoreLeaderBoard();

        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list);
        //Todo userManager.getLeaderboardListRate(5);
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

    private void getScoreLeaderBoard() {
        userManager.getLeaderboardListScore(20);
    }

}
