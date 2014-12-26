package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.Utils;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainMenu extends Activity {
    private final View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainMenu.this, Contribute.class);

            switch (v.getId()) {
                case R.id.about:
                    i = new Intent(MainMenu.this, About.class);
                    break;
                case R.id.prefs:
                    i = new Intent(MainMenu.this, Preferences.class);
                    break;
                case R.id.feedback:
                    i = new Intent(MainMenu.this, Feedback.class);
                    break;
                case R.id.vote:
                    i = new Intent(MainMenu.this, Vote.class);
                    break;
                case R.id.statistics:
                    i = new Intent(MainMenu.this, Statistics.class);
                    break;
                case R.id.leader_board:
                    i = new Intent(MainMenu.this, LeaderBoard.class);
                    break;
                case R.id.current_charity:
                    i = new Intent(MainMenu.this, CurrentCharity.class);
                    break;
            }
            startActivity(i);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        try {
            final AccountManager manager = AccountManager.get(this);
            final Account[] accounts = manager.getAccounts();
            for (Account account : accounts) {
                if (account.name.contains("@")) {
                    final UserManager manager1 = new UserManager();
                    manager1.insertUser(account.name);
                    manager1.postStats(account.name, Integer.parseInt(Utils.getTotalScore(this)), Utils.getRate(this));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.about).setOnClickListener(listener);
        findViewById(R.id.prefs).setOnClickListener(listener);
        findViewById(R.id.feedback).setOnClickListener(listener);
        findViewById(R.id.vote).setOnClickListener(listener);
        findViewById(R.id.contribute).setOnClickListener(listener);
        findViewById(R.id.statistics).setOnClickListener(listener);
        findViewById(R.id.leader_board).setOnClickListener(listener);
        findViewById(R.id.current_charity).setOnClickListener(listener);
        AsyncConnector.setListener(new Interfaces.ASyncListener() {
            @Override
            public void onCompleteSingle(final Charity charity) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.contribute)).setText(charity.getName());
                    }
                });
            }

            @Override
            public void onCompleteArray(ArrayList<Charity> charities) {

            }

            @Override
            public void onCompleteRank(int rank) {

            }

            @Override
            public void onCompleteLeaderBoardList(ArrayList<UserStats> stats) {

            }
        });

    }

}
