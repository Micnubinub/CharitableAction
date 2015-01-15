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
    private int height = 100;


    public VoteCharityAdapter(Context context, ArrayList<Charity> charities) {
        this.charities = charities;
        this.context = context;
        height = dpToPixels(68);

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
        view.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height));
        view.setPos(position);
        view.setLink(charities.get(position).getUrl());
        view.setPrimaryText(charities.get(position).getName());
        view.setVotes(charities.get(position).getVotes());
        view.setSecondaryText(charities.get(position).getVotes());
        view.setVotedFor((votedFor != null) && (charities.get(position).getUrl().equals(votedFor)));
        return view;
    }


    int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
