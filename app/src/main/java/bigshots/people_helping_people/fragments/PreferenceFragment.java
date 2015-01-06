package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tbs.jumpsnew.R;
import tbs.jumpsnew.ui.Adapter;


public class PreferenceFragment extends Fragment {

    private static ListView listView;

    public PreferenceFragment() {
    }

    public static void setListAdapter(Adapter adapter) {
        PreferenceFragment.adapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        if (adapter != null)
            listView.setAdapter(adapter);
        return view;
    }
}
