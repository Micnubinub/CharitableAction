package bigshots.people_helping_people.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;


public class CreditFragment extends BaseFragment {

    private static final ArrayList<String> credits = new ArrayList<>();
    private static Activity context;
    private static ListView listView;
    private static CreditAdapter adapter;

    public static void setCredits(final String[] vals) {
        Log.e("setting credits", Arrays.toString(vals));
        credits.clear();
        credits.ensureCapacity(vals.length);
        for (String val : vals) {
            credits.add(val);
        }


        if (listView != null)
            listView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listView.invalidate();
                }
            });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.credits_view, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CreditAdapter();
        update();
        return view;
    }

    @Override
    public void update() {
        Log.e("credits update", credits.toString());
        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    listView.setAdapter(adapter);
                    listView.setSelected(true);
                    listView.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class CreditAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return credits == null ? 0 : credits.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(context, R.layout.credit_list_item, null);
            }
            ((TextView) view.findViewById(R.id.text)).setText(credits.get(position));
            return view;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }
}
