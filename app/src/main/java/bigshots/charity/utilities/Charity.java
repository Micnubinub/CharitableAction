package bigshots.charity.utilities;

/**
 * Created by root on 19/11/14.
 */
public class Charity implements Interfaces.charity {
    private int votes;
    private String link;
    private String name;

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
