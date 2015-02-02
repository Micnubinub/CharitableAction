package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;


public class DonationsFragment extends BaseFragment {
    static TextView raised;
    static ListView listView;


    public DonationsFragment() {
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
}
