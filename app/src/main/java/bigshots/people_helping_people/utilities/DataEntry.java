package bigshots.people_helping_people.utilities;

/**
 * Created by root on 25/12/14.
 */
public class DataEntry {
    private final int points;
    private final long date;

    public DataEntry(long date, int points) {
        this.date = date;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public long getDate() {
        return date;
    }
}
