package bigshots.charity.utilities;

/**
 * Created by root on 19/11/14.
 */
public class Interfaces {

    public interface sendDirectMessage {
        void sendMessage(String message);
    }

    public interface Charity {
        String getLink();

        String getName();

        int getVotes();
    }

    public interface vote {
        //get the charity the user has currently cast a vote for using their gmail
        String currentCharity();

        void castVote(String link);

        void removeVote(String link);
    }

    public interface suggestCharity {
        //returns false if the string isn't a proper link
        boolean suggestCharity(String link);
    }
}
