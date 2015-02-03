package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.LeaderBoardAdapter;
import bigshots.people_helping_people.utilities.Utility;

public class LeaderboardFragment extends BaseFragment {
    private static ListView listView;
    private static View message;
    private static TextView myRank, points;
    private static LeaderBoardAdapter adapter;

    public LeaderboardFragment() {
    }

    public static void refreshList() {
        //Todo test
        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    message.setVisibility(View.GONE);
                    adapter = new LeaderBoardAdapter(MainMenu.context, MainMenu.stats, false);
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
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
        final View view = inflater.inflate(R.layout.leader_board, container, false);
        myRank = (TextView) view.findViewById(R.id.my_rank);
        message = view.findViewById(R.id.message);
        listView = (ListView) view.findViewById(R.id.list);
        points = (TextView) view.findViewById(R.id.points_money);
        points.setText(String.format("%spts", Utility.formatNumber(Utility.getTotalScore(MainMenu.context))));
        // listView.setScrollListener(scrollListener);
        return view;
    }

    @Override
    protected void update() {
        if (adapter == null) {
            MainMenu.downloadData();
            return;
        } else {
            message.setVisibility(View.GONE);
        }
        if (listView == null) {
            try {
                listView = (ListView) getView().findViewById(R.id.list);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (listView.getAdapter() == null || listView.getAdapter().getCount() != adapter.getCount())
                    listView.setAdapter(adapter);
            }
        });

        if (points != null)
            points.setText(String.format("%spts", Utility.formatNumber(Utility.getTotalScore(MainMenu.context))));

        if (myRank != null)
            myRank.setText(String.format("%d. Me", MainMenu.rank));

//        getView().invalidate();
    }
}
