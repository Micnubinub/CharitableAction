package bigshots.people_helping_people.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.UserStats;

/**
 * Created by root on 25/12/14.
 */
public class LeaderBoardAdapter extends BaseAdapter {
    final ArrayList<UserStats> stats;
    final boolean isRate;
    private final Context context;

    public LeaderBoardAdapter(Context context, ArrayList<UserStats> stats, boolean isRate) {
        this.stats = stats;
        this.isRate = isRate;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stats == null ? 0 : stats.size();
    }

    @Override
    public Object getItem(int position) {
        return stats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = View.inflate(context, R.layout.leader_board_list_item, null);

        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final UserStats stat = stats.get(position);

        ((TextView) view.findViewById(R.id.name_pos)).setText(String.format("%d. %s", position + 1, stat.getName()));
//
//        if (isRate)
//            ((TextView) view.findViewById(R.id.points_money)).setText(String.format("%.3fpts", stat.getRate()));
//        else
        ((TextView) view.findViewById(R.id.points_money)).setText(String.format("%spts", Utility.formatNumber(stat.getScore())));

        return view;
    }
}
