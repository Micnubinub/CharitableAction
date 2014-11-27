package bigshots.people_helping_people;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.utilities.Interfaces;

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
                case R.id.stats:
                    i = new Intent(MainMenu.this, Statistics.class);
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
            AccountManager manager = AccountManager.get(this);
            Account[] accounts = manager.getAccountsByType("com.google");
            for (Account account : accounts) {
                if (account.name.contains("@gmail")) {
                    new UserManager().insertUser(account.name);
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
        findViewById(R.id.stats).setOnClickListener(listener);
        AsyncConnector.setListener(new Interfaces.ASyncListener() {
            @Override
            public void onCompleteSingle(final Charity charity) {
                Log.e("Interface", "ds");
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
        });


    }

}
