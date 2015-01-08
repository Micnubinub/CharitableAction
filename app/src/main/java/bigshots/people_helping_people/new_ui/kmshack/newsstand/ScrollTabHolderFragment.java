package bigshots.people_helping_people.new_ui.kmshack.newsstand;

import android.support.v4.app.Fragment;
import android.widget.ScrollView;

public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {

    protected ScrollTabHolder mScrollTabHolder;

    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
        mScrollTabHolder = scrollTabHolder;
    }

    @Override
    public void onScroll(ScrollView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        // nothing
    }

}