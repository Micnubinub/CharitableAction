package bigshots.people_helping_people.utilities;


import java.util.ArrayList;

import bigshots.people_helping_people.feeds.Message;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.UserStats;


/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class Interfaces {

    public interface ASyncListener {
        void onCharityMonth(Charity charity);

        void onCurrentCharity(Charity charity);

        void onCompleteRank(int rank);

        void onCompleteArray(ArrayList<Charity> charities);

        void onDonationsArray(ArrayList<Charity> charities);

        void onCompleteLeaderBoardList(ArrayList<UserStats> stats);

        void onCompleteCurrentScore(int score);

        void onCompleteTotalScore(long score);
    }

    public interface FeedClient {
        void onReceiveMessages(ArrayList<Message> messages);

        void onReceiveMessages(Message... messages);
    }

    public interface suggestCharity {
        boolean suggestCharity(String link);
    }
}
