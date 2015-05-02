package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.LeaderBoardAdapter;

public class LeaderboardFragment extends BaseFragment {
    private static ListView listView;
    private static View message;
    private static TextView myRank, raised;
    private static LeaderBoardAdapter adapter;
    private static int index, top;

    public LeaderboardFragment() {
    }

    public static void refreshList() {
        //Todo test
        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    message.setVisibility(View.GONE);
                    final double money = (MainMenu.userScore / (double) MainMenu.totalScore) * MainMenu.totalCash;
                    raised.setText(String.format("$%.2f", money));
                    StatisticsFragment.setRaised(money);
                    StatisticsFragment.invalidate();
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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                index = listView.getFirstVisiblePosition();
                final View v = listView.getChildAt(0);
                top = (v == null) ? 0 : v.getTop();
            }
        });
        raised = (TextView) view.findViewById(R.id.raised);
        return view;
    }

    @Override
    protected void update() {
        MainMenu.refreshLeaderBoard();

        if (MainMenu.stats == null) {
            MainMenu.downloadData();
            return;
        }

        if ((myRank != null) && (MainMenu.email.length() > 3))
            myRank.setText(String.format("%d. Me", MainMenu.rank));

        refreshList();
        listView.setSelectionFromTop(index, top);
    }
}
