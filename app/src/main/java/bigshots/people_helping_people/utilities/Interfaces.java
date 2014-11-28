package bigshots.people_helping_people.utilities;


import java.util.ArrayList;

import bigshots.people_helping_people.io.Charity;


/**
 * Created by root on 19/11/14.
 */
@SuppressWarnings("ALL")
public class Interfaces {

    public interface ASyncListener {
        void onCompleteSingle(Charity charity);

        void onCompleteArray(ArrayList<Charity> charities);

    }

    public interface suggestCharity {
        //returns false if the string isn't a proper link
        boolean suggestCharity(String link);
    }
}