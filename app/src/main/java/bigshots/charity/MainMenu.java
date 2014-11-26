package bigshots.charity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import bigshots.charity.io.AsyncConnector;
import bigshots.charity.io.Charity;
import bigshots.charity.schedule_wheel.AbstractWheel;
import bigshots.charity.schedule_wheel.OnWheelChangedListener;
import bigshots.charity.schedule_wheel.OnWheelClickedListener;
import bigshots.charity.schedule_wheel.adapters.NumericWheelAdapter;
import bigshots.charity.utilities.Interfaces;

/**
 * Created by root on 18/11/14.
 */
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

        final TextView textView = (TextView) findViewById(R.id.text);
        final AbstractWheel hours = (AbstractWheel) findViewById(R.id.wheel);
        hours.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
        hours.setCyclic(true);

        // set current time
        Calendar c = Calendar.getInstance();
        int curHours = c.get(Calendar.HOUR_OF_DAY);
        hours.setCurrentItem(curHours);
        textView.setText(String.valueOf(curHours));

        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.valueOf(hours.getCurrentItem()));
                    }
                });

            }
        };
        hours.addChangingListener(wheelListener);
        OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(AbstractWheel wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);












        /*AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        for (Account account : accounts){
        if(account.name.endsWith("gmail.com")){
              String gmailAddress = account.name;
              String password = manager.getPassword(account);
           }
         }
        con.getUserManager().insertUser("Steve@gmail.com");*/
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
                        ((TextView) findViewById(R.id.contribute)).setText(charity.getName().toString());
                    }
                });
            }

            @Override
            public void onCompleteArray(ArrayList<Charity> charities) {

            }
        });


    }

}
