package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.scroll_iew_lib.ParallaxListView;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.LeaderBoardAdapter;
import bigshots.people_helping_people.utilities.Utils;


public class LeaderboardFragment extends BaseFragment {
    private static ParallaxListView listView;
    private static int rank;
    private static View message;
    private static TextView myRank, points;
    private static LeaderBoardAdapter adapter;
    public static final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(final Charity charity) {
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {

        }

        @Override
        public void onCompleteRank(final int rank) {
            LeaderboardFragment.rank = rank + 1;
            listView.post(new Runnable() {
                @Override
                public void run() {
                    if (myRank != null)
                        myRank.setText(String.format("%d. Me", rank + 1));
                }
            });
        }

        @Override
        public void onCompleteLeaderBoardList(final ArrayList<UserStats> stats) {
            if (listView != null)
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        message.setVisibility(View.GONE);
                        adapter = new LeaderBoardAdapter(MainMenu.context, stats, false);
                        listView.setAdapter(adapter);
                    }
                });
        }
    };

    public LeaderboardFragment() {
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
        listView = (ParallaxListView) view.findViewById(R.id.list);
        points = (TextView) view.findViewById(R.id.points_money);
        points.setText(String.format("%dpts", Integer.parseInt(Utils.getTotalScore(MainMenu.context))));
        // listView.setScrollListener(scrollListener);
        return view;
    }

//    private void showSuggestionDialog() {
//        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
//        dialog.setContentView(R.layout.suggest_charity);
//        final EditText charity = (EditText) dialog.findViewById(R.id.suggested_charity);
//
//        final View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.submit:
//                        String charityName = charity.getText().toString();
//                        if (charityName != null && charityName.length() > 3) {
//                            new CharityManager().suggestCharity(charityName);
//                            Toast.makeText(MainMenu.context, "Thank you for your suggestion.", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                        break;
//                    case R.id.cancel:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        };
//
//        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit).setOnClickListener(onClickListener);
//        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);
//
//        dialog.show();
//    }

    @Override
    protected void update() {
        if (adapter == null) {
            MainMenu.setUpLeaderBoard();
            return;
        }

        if (listView == null) {
            try {
                listView = (ParallaxListView) getView().findViewById(R.id.list);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (listView != null) {
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.invalidate();
                }
            }
        });

        if (points != null)
            points.setText(String.format("%dpts", Integer.parseInt(Utils.getTotalScore(MainMenu.context))));

        if (myRank != null)
            myRank.setText(String.format("%d. Me", rank));

        getView().invalidate();
    }
}
