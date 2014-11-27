package bigshots.people_helping_people.utilities;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import bigshots.people_helping_people.io.Charity;

/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class CharityListClickListener implements AdapterView.OnItemClickListener {

    private final ArrayList<Charity> charities;

    public CharityListClickListener(ArrayList<Charity> charities) {
        this.charities = charities;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Todo vote>
        // charities.get(position).getLink();
    }
}
