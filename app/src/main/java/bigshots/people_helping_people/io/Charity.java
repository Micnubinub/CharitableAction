package bigshots.people_helping_people.io;

public class Charity {

    private String name;
    private String url;
    private int votes;
    private int worth;
    private boolean trusted;
    private String description; // Amount of Money Generated: Only for Charity of the
    // Month']


    @Override
    public String toString() {
        return name;
    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
