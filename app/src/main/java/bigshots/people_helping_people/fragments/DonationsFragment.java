package bigshots.people_helping_people.fragments;

import android.os.Bundle;
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


public class DonationsFragment extends BaseFragment {
    public static ArrayList<Charity> charities;
    private static TextView raised;
    private static ListView listView;
    private static Dapter adapter;


    public DonationsFragment() {
    }

    public static void refreshList() {
        if (raised != null) {
            int total = 2400;
            if (charities != null) {
                total = 0;
                for (Charity charity : charities) {
                    total += charity.getWorth();
                }
            }
            raised.setText(String.format("Total raised : %d", total));
        }

        if (listView != null) {
            if (adapter == null)
                adapter = new Dapter();
            else
                adapter.notifyDataSetChanged();
            listView.setAdapter(new Dapter());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.donations, container, false);
        raised = (TextView) view.findViewById(R.id.raised);
        listView = (ListView) view.findViewById(R.id.list);


        return view;
    }

    @Override
    protected void update() {
    }

    static class Dapter extends BaseAdapter {
        @Override
        public int getCount() {
            return charities == null ? 0 : charities.size();
        }

        @Override
        public Object getItem(int position) {
            return charities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = View.inflate(MainMenu.context, R.layout., null);
            final Charity charity = charities.get(position);
            //Todo
            return null;
        }
    }
}
