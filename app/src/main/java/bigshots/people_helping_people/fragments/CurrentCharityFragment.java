package bigshots.people_helping_people.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.Utility;


public class CurrentCharityFragment extends BaseFragment {

    private static TextView raised, linkr;
    private static Charity charity;


    public CurrentCharityFragment() {
    }

    private static void setCharityDescription() {
        charity = MainMenu.charity;
        raised.post(new Runnable() {
            @Override
            public void run() {
                if (charity == null) {
                    return;
                }
                raised.setText(String.format("Total raised : $ %s raised", Utility.formatNumber(charity.getWorth())));

            }
        });
    }

    public static void refreshCharity() {
        setCharityDescription();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.current_charity, container, false);
        raised = (TextView) view.findViewById(R.id.raised);
        linkr = (TextView) view.findViewById(R.id.link);
        linkr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.worldwildlife.org.au/"));
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    protected void update() {
        if (charity == null) {
            MainMenu.downloadData();
            return;
        }
        setCharityDescription();
        Log.e("update", "cc");
    }
}
