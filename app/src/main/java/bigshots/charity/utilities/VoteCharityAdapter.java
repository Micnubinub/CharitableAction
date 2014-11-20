package bigshots.charity.utilities;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by root on 19/11/14.
 */
public class VoteCharityAdapter extends BaseAdapter {
    private static int max;
    private ArrayList<Charity> charities;

    public VoteCharityAdapter(ArrayList<Charity> charities) {
        this.charities = charities;
        getMax();
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
        return null;
    }

}
