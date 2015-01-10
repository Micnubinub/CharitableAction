package bigshots.people_helping_people.new_ui.kmshack.newsstand;

import android.widget.ScrollView;

public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight);

    void onScroll(ScrollView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
