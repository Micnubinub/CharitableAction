package bigshots.people_helping_people.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.utilities.Interfaces;


public class CurrentCharityFragment extends Fragment {


    private TextView description, raised, name, linkr;
    private String link;
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(final Charity charity) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    description.setText(charity.getDescription());
                    raised.setText(String.format("$%d raised", charity.getWorth()));
                    name.setText(charity.getName());
                    link = charity.getUrl();
                    linkr.setText("More Info");
                    linkr.setEnabled(true);
                    getView().findViewById(R.id.message).setVisibility(View.GONE);
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
    };


    public CurrentCharityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.current_charity, container, false);
        description = (TextView) view.findViewById(R.id.description);
        raised = (TextView) view.findViewById(R.id.raised);
        name = (TextView) view.findViewById(R.id.name);
        linkr = (TextView) view.findViewById(R.id.link);

        linkr.setEnabled(false);
        linkr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link != null && link.length() > 1) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainMenu.context, "No link to display", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AsyncConnector.setListener(aSyncListener);
        new CharityManager().monthlyCharity();
        return view;
    }


}


