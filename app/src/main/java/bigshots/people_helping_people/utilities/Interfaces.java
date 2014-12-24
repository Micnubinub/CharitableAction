package bigshots.people_helping_people.utilities;


import java.util.ArrayList;

import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.UserStats;


/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class Interfaces {
//Todo userManager.postStats("sidney@cyberkomm.ch", 500, 2.15f);
//Todo userManager.getLeaderboardListRate(5);
//Todo serManager.getLeaderboardListScore(5);
//Todo userManager.getScoreRank("lindelwe.ncube@my.jcu.edu.au");

    public interface ASyncListener {
        void onCompleteSingle(Charity charity);

        void onCompleteRank(int rank);

        void onCompleteArray(ArrayList<Charity> charities);

        void onCompleteLeaderBoardList(ArrayList<UserStats> stats);
    }

    public interface suggestCharity {
        //returns false if the string isn't a proper link
        boolean suggestCharity(String link);
    }
}
