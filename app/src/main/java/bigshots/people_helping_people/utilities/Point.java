package bigshots.people_helping_people.utilities;

/**
 * Created by root on 25/12/14.
 */
public class Point {
    private static String legendTitle;
    private int y;

    public Point(String legendTitle, int y) {
        this.legendTitle = legendTitle;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public String getLegendTitle() {
        return legendTitle;
    }
}
