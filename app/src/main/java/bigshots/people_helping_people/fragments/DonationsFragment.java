package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.Utility;


public class DonationsFragment extends BaseFragment {
    public static ArrayList<Charity> charities;
    private static TextView raised;
    private static ListView listView;
    private static Dapter adapter;


    public DonationsFragment() {
    }

    public static void refreshList() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (raised != null) {
                    int total = 0;
                    if (charities != null) {
                        for (Charity charity : charities) {
                            total += charity.getWorth();
                        }
                        MainMenu.totalCash = total;
                        raised.setText(String.format("Total raised : $%s", Utility.formatNumber(total)));
                    } else {

                        raised.setText("Loading...");
                    }

                }

                if (listView != null) {
                    if (adapter == null)
                        adapter = new Dapter();
                    else
                        adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.donations, container, false);
        raised = (TextView) view.findViewById(R.id.raised);
        listView = (ListView) view.findViewById(R.id.list);
        refreshList();
        return view;
    }

    @Override
    protected void update() {
    }

    private static class Dapter extends BaseAdapter {
        @Override
        public int getCount() {
            return charities == null ? 0 : charities.size();
        }

        @Override
        public Object getItem(int position) {
            return charities == null ? null : charities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = View.inflate(MainMenu.context, R.layout.donations_list_item, null);
            final Charity charity = charities.get(position);
            //Todo
            Log.e("charity:", charity.getName() + " " + charity.getWorth());
            ((TextView) view.findViewById(R.id.raised)).setText(String.format("$%d", charity.getWorth()));
            ((TextView) view.findViewById(R.id.charity)).setText(charity.getName());
            return view;
        }
    }
}
