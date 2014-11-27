package bigshots.charity.utilities;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import bigshots.charity.io.Charity;
import bigshots.charity.io.VoteManager;
import bigshots.charity.views.CharityListItem;

/**
 * Created by root on 19/11/14.
 */
public class VoteCharityAdapter extends BaseAdapter {
    private static int max;
    private final Context context;
    private final ArrayList<Charity> charities;
    private final VoteManager voteManager = new VoteManager();
    //Todo implement this

    private int height = 100;
    private String votedFor;
    //Todo implement this con.getCharityManager().currentCharity("Steve@gmail.com");
    private Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(Charity charity) {
            votedFor = charity.getUrl();
            notifyDataSetChanged();
        }

        @Override
        public void onCompleteArray(ArrayList<Charity> charities) {

        }
    };

    public VoteCharityAdapter(Context context, ArrayList<Charity> charities) {
        this.charities = charities;
        getMax();
        this.context = context;
        height = dpToPixels(76);
    }


    private void getMax() {
        max = 0;
        for (Charity charity : charities) {
            max = Math.max(max, charity.getVotes());
        }
    }

    @Override
    public void notifyDataSetChanged() {
        getMax();
        super.notifyDataSetChanged();
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
        final CharityListItem view = new CharityListItem(context);
        view.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height));
        view.setPos(position);
        view.setLink(charities.get(position).getUrl());
        view.setText(charities.get(position).getName());
        if (votedFor != null && charities.get(position).getUrl() == votedFor)
            view.setVotedFor(true);
        else
            view.setVotedFor(false);
        return view;
    }

    //Todo implement this
    private void castVote(String charity) {
        voteManager.castVote("www.charity.com", "Steve@gmail.com");
        voteManager.removeVote("Sidney");
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
