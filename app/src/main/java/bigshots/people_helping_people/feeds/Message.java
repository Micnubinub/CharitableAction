package bigshots.people_helping_people.feeds;

/**
 * Created by Michael on 2/6/2015.
 */
public class Message {
    public final String user;
    public final String id;
    public final String message;
    public final String rank;
    public final long when;

    public Message(String id, String user, String message, String rank, long when) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.rank = rank;
        this.when = when;

    }
}
