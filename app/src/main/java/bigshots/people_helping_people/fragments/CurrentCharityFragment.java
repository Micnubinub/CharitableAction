package bigshots.people_helping_people.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;


public class CurrentCharityFragment extends BaseFragment {

    private static TextView description, raised, name, linkr;
    private static String link;
    private static Charity charity;
    private static View message;


    public CurrentCharityFragment() {
    }

    private static void setCharityDescription() {
        charity = MainMenu.charity;
        raised.post(new Runnable() {
            @Override
            public void run() {
//                if (charity == null) {
//                    return;
//                }
//                message.setVisibility(View.GONE);
//                if (description.getText().toString().length() < 10) {
//                    description.setText(charity.getDescription());
//                    description.setMaxLines(20);
//                    description.setMinLines(3);
//                }
                raised.setText(String.format("$%d raised", charity.getWorth()));
//                name.setText(charity.getName());
//                if (!linkr.isEnabled()) {
//                    link = charity.getUrl();
//                    linkr.setEnabled(true);
//                }
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
        description = (TextView) view.findViewById(R.id.description);
        // ((ParallaxScrollView) view.findViewById(R.id.scroll_view)).setScrollListener(scrollListener);
        raised = (TextView) view.findViewById(R.id.raised);
        name = (TextView) view.findViewById(R.id.name);
        linkr = (TextView) view.findViewById(R.id.link);
        //message = view.findViewById(R.id.message);
//        linkr.setEnabled(false);
        linkr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link != null && link.length() > 1) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nbcf.org.au/"));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainMenu.context, "No link to display", Toast.LENGTH_SHORT).show();
                }
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
    }
}
