package bigshots.people_helping_people.scroll_iew_lib;

/**
 * Created by root on 8/01/15.
 */
public interface Scrollable {
    void onScrollX(int posX, float amount);

    void onScrollY(int scrollViewTop, int firstVisibleChildPos, int firstVisibleChildTop, int scrollY);
}
