package bigshots.people_helping_people.io;

import android.util.Log;

import bigshots.people_helping_people.fragments.VoteFragment;

public class Charity {

    private String name;
    private String url;
    private int votes;
    private int worth;
    private boolean current, trusted;


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
        Log.e(name, String.format("trusted : %d, %b", trusted, trusted == 1));
        VoteFragment.refreshList();
        this.trusted = trusted == 1;
    }
}
