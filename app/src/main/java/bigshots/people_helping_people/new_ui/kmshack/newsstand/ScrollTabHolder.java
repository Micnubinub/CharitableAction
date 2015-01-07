package bigshots.people_helping_people.new_ui.kmshack.newsstand;

import android.widget.AbsListView;

public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight);

    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
