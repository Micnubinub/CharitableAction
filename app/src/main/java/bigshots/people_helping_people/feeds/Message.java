package bigshots.people_helping_people.feeds;

/**
 * Created by Michael on 2/6/2015.
 */
public class Message {
    private final String user;
    private final String id;
    private final String message;
    private final String rank;
    private final long when;

    public Message(String id, String user, String message, String rank, long when) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.rank = rank;
        this.when = when;

    }
}
