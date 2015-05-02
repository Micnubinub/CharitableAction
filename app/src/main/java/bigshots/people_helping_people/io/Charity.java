package bigshots.people_helping_people.io;

public class Charity {
    private String name;
    private String url;
    private String description;
    private int votes;
    private int worth;
    private boolean current, trusted;

    @Override
    public String toString() {
        return name + " : " + description + "\n>> " + url;
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

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(int trusted) {
        this.trusted = trusted == 1;
    }
}
