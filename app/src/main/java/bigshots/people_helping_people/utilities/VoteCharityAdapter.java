package bigshots.people_helping_people.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.CharityManager;
import bigshots.people_helping_people.views.CharityListItem;
import bigshots.people_helping_people.views.ProgressBar;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class VoteCharityAdapter extends BaseAdapter {
    private static int max;
    private static String votedFor;


    private Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(Charity charity) {
            votedFor = charity.getUrl();
            CharityListItem.setCurrentVote(votedFor);
            notifyDataSetChanged();

            Toast.makeText(context, charity.getUrl(), Toast.LENGTH_SHORT).show();

            AsyncConnector.setListener(null);
            notify();
        }

        @Override
        public void onCompleteArray(ArrayList<Charity> charities) {

        }
    };
    private static ArrayList<Charity> charities;
    private final Context context;
    private int height = 100;

    public VoteCharityAdapter(Context context, ArrayList<Charity> charities) {
        this.charities = charities;
        getMax();
        this.context = context;
        height = dpToPixels(68);
//new VoteCharityAdapter()
        getVotedFor();
    }

    public static ArrayList<Charity> getCharities() {
        return charities;
    }

    public static void setVotedFor(String votedFor) {
        VoteCharityAdapter.votedFor = votedFor;
    }

    void getVotedFor() {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts;
        accounts = manager.getAccounts();
        for (Account account : accounts) {
            if (account.name.contains("@")) {
                new CharityManager().currentCharity(account.name);
                AsyncConnector.setListener(aSyncListener);
                break;
            }
        }
    }

    private void getMax() {
        for (Charity charity : charities) {
            max = Math.max(max, charity.getVotes());
        }
        ProgressBar.setMax(max);
    }


    @Override
    public int getCount() {
        return charities == null ? 0 : charities.size();
    }

    @Override
    public Object getItem(int position) {
        return charities == null ? null : charities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (votedFor == null)
            getVotedFor();

        final CharityListItem view = new CharityListItem(context, this);
        view.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height));
        view.setPos(position);
        view.setLink(charities.get(position).getUrl());
        view.setPrimaryText(charities.get(position).getName());
        view.setSecondaryText(charities.get(position).getVotes());

        if ((votedFor != null) && (charities.get(position).getUrl().equals(votedFor)))
            view.setVotedFor(true);
        else
            view.setVotedFor(false);

        return view;
    }


    int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
