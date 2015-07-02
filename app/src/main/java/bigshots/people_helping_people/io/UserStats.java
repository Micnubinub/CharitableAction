package bigshots.people_helping_people.io;

public class UserStats {
    private int worldRank;
    private String email;
    private int score;
    private float rate;

    public int getWorldRank() {
        return worldRank;
    }

    public void setRank(int rank) {
        this.worldRank = rank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return email.split("@")[0];
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRaised() {
        return (float) (score) * (0.0001875f);
    }
}