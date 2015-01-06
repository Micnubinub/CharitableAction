package bigshots.people_helping_people.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.io.UserManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.LeaderBoardAdapter;
import bigshots.people_helping_people.utilities.Utils;


public class LeaderboardFragment extends Fragment {

    private static final UserManager userManager = new UserManager();
    private static ListView listView;
    int rank;
    private LeaderBoardAdapter adapter;
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(final Charity charity) {
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {

        }

        @Override
        public void onCompleteRank(final int rank) {
            LeaderboardFragment.this.rank = rank + 1;
            listView.post(new Runnable() {
                @Override
                public void run() {
                    getView().findViewById(R.id.my_rank).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) getView().findViewById(R.id.my_rank)).setText(String.format("%d. Me", rank + 1));
                        }
                    });
                }
            });
        }

        @Override
        public void onCompleteLeaderBoardList(final ArrayList<UserStats> stats) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            getView().findViewById(R.id.message).setVisibility(View.GONE);
                            adapter = new LeaderBoardAdapter(MainMenu.context, stats, false);
                            listView.setAdapter(adapter);
                        }
                    });
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
        listView = (ListView) view.findViewById(R.id.list);
        final int points = Integer.parseInt(Utils.getTotalScore(MainMenu.context));
        try {
            final AccountManager manager = AccountManager.get(MainMenu.context);
            final Account[] accounts = manager.getAccounts();
            for (Account account : accounts) {
                if (account.name.contains("@")) {
                    final UserManager manager1 = new UserManager();
                    userManager.getScoreRank(account.name);
                    manager1.postStats(account.name, points, Utils.getRate(MainMenu.context));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((TextView) view.findViewById(R.id.points_money)).setText(String.format("%dpts | $%.2f", points, (points * (0.0001875f))));
        AsyncConnector.setListener(aSyncListener);
        getScoreLeaderBoard();

        //Todo userManager.getLeaderboardListRate(5);
        return view;
    }

    private void showSuggestionDialog() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.suggest_charity);
        final EditText charity = (EditText) dialog.findViewById(R.id.suggested_charity);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.submit:
                        String charityName = charity.getText().toString();
                        if (charityName != null && charityName.length() > 3) {
                            new CharityManager().suggestCharity(charityName);
                            Toast.makeText(MainMenu.context, "Thank you for your suggestion.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.submit).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.submit_cancel).findViewById(R.id.cancel).setOnClickListener(onClickListener);

        dialog.show();
    }

    private void getScoreLeaderBoard() {
        userManager.getLeaderboardListScore(25);
    }

}
