package bigshots.people_helping_people.utilities;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.views.CharityListItem;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class VoteCharityAdapter extends BaseAdapter {
    private static String votedFor;
    private static ArrayList<Charity> charities;
    private final Context context;

    public VoteCharityAdapter(Context context, ArrayList<Charity> charities) {
        this.charities = charities;
        this.context = context;
    }

    public static ArrayList<Charity> getCharities() {
        return charities;
    }

    public static void setVotedFor(String votedFor) {
        VoteCharityAdapter.votedFor = votedFor;
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
        final CharityListItem view = new CharityListItem(context, this);
        view.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setPos(position);
        final Charity charity = charities.get(position);
        view.setLink(charity.getUrl());
        view.setPrimaryText(charity.getName());
        view.setVotes(charity.getVotes());
        view.setSecondaryText(charity.getVotes());
        view.setTrusted(charity.isTrusted());
        view.setVotedFor((votedFor != null) && (charity.getUrl().equals(votedFor)));
        return view;
    }

    int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
