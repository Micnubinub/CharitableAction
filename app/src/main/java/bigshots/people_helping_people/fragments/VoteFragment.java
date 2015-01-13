package bigshots.people_helping_people.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.scroll_iew_lib.ParallaxListView;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.VoteCharityAdapter;
import bigshots.people_helping_people.views.CharityListItem;

public class VoteFragment extends BaseFragment {
    private static ParallaxListView listView;
    private static VoteCharityAdapter adapter;
    private static View message;
    private static Charity charity;
    public static final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(final Charity charity) {
            VoteFragment.charity = charity;
            listView.post(new Runnable() {
                @Override
                public void run() {
                    VoteCharityAdapter.setVotedFor(charity.getUrl());
                    CharityListItem.setCurrentVote(charity.getUrl());
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                    listView.invalidate();
                }
            });
        }

        @Override
        public void onCompleteArray(final ArrayList<Charity> charities) {

            listView.post(new Runnable() {
                @Override
                public void run() {
                    message.setVisibility(View.GONE);
                    adapter = new VoteCharityAdapter(MainMenu.context, charities);
                    listView.setAdapter(adapter);
                }
            });
        }

        @Override
        public void onCompleteRank(int rank) {

        }

        @Override
        public void onCompleteLeaderBoardList(ArrayList<UserStats> stats) {

        }
    };

    public VoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vote, container, false);
        message = view.findViewById(R.id.message);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuggestionDialog();
            }
        });
        listView = (ParallaxListView) view.findViewById(R.id.list);
        //listView.setScrollListener(scrollListener);

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


    @Override
    protected void update() {
        if (adapter == null || charity == null) {
            MainMenu.setUpCharities();
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

                    VoteCharityAdapter.setVotedFor(charity.getUrl());
                    CharityListItem.setCurrentVote(charity.getUrl());

                    adapter.notifyDataSetChanged();
                    listView.invalidateViews();
                }
            }
        });

    }
}
