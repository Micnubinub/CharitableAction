package bigshots.people_helping_people;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.utilities.Interfaces;

/**
 * Created by root on 30/11/14.
 */
public class CurrentCharity extends Activity {

    private TextView description, raised, name, linkr;
    private String link;
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(final Charity charity) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    description.setText(charity.getDescription());
                    raised.setText(String.format("$%d raised", charity.getWorth()));
                    name.setText(charity.getName());
                    link = charity.getUrl();
                    linkr.setText("More Info");
                    linkr.setEnabled(true);
                    findViewById(R.id.message).setVisibility(View.GONE);
                }
            });

        }

        @Override
        public void onCompleteArray(ArrayList<Charity> charities) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_charity);

        description = (TextView) findViewById(R.id.description);
        raised = (TextView) findViewById(R.id.raised);
        name = (TextView) findViewById(R.id.name);
        linkr = (TextView) findViewById(R.id.link);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linkr.setEnabled(false);
        linkr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link != null && link.length() > 1) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                } else {
                    Toast.makeText(CurrentCharity.this, "No link to display", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AsyncConnector.setListener(aSyncListener);
        new CharityManager().monthlyCharity();
    }
}
