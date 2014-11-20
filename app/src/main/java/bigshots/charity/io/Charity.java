package bigshots.charity.io;

import bigshots.charity.utilities.Interfaces;

public class Charity implements Interfaces.Charity {

    private String name;
    private String link;
    private int votes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
