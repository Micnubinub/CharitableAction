package bigshots.people_helping_people.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import bigshots.people_helping_people.MainActivity;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.fragments.StatisticsFragment;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.LeaderBoardAdapter;

public class LeaderBoardFragment extends BaseFragment {
    private static ListView listView;
    private static View message;
    private static TextView myRank, raised;
    private static LeaderBoardAdapter adapter;

    public LeaderBoardFragment() {
    }

    public static void refreshList() {
        //Todo test
        listView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    message.setVisibility(View.GONE);
                    final double money = (MainActivity.userScore / (double) MainActivity.totalScore) * MainActivity.totalCash;
                    raised.setText(String.format("$%.2f", money));
                    StatisticsFragment.setRaised(money);
                    StatisticsFragment.invalidate();

                    adapter = new LeaderBoardAdapter(MainActivity.context, MainActivity.stats, false);
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
        raised = (TextView) view.findViewById(R.id.raised);
        return view;
    }

    @Override
    public void update() {
        MainActivity.refreshLeaderBoard();

        if (MainActivity.stats == null) {
            MainActivity.downloadData();
            return;
        }

        if ((myRank != null) && (MainActivity.email.length() > 3))
            myRank.setText(String.format("%d. Me", MainActivity.rank));

        refreshList();
    }
}
