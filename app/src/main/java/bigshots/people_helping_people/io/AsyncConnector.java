package bigshots.people_helping_people.io;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import bigshots.people_helping_people.utilities.Interfaces;

public class AsyncConnector {
    static Interfaces.ASyncListener listener;

    public static void setListener(Interfaces.ASyncListener listener) {
        AsyncConnector.listener = listener;
    }

    public static void makeConnection(ArrayList<NameValuePair> pairs,
                                      String nm, String act) {
        new ConnectorTask(act, pairs, nm).execute();
    }

    public static void interpretResponse(String resp, String action) {
        if (resp.contains("Failed")) {
            Log.e("Async", "Failed to perform action: " + action);
            Log.e("Async", "Original Error Message: " + resp);
        } else {
            if (action.equals("SEND_MESSAGE")) {
                Log.e("Async", resp);
            } else if (action.equals("GET_CHARITIES")) {
                Log.e("Async", resp);
                ArrayList<Charity> charities = new ArrayList<Charity>();
                if (!resp.equals("")) {
                    resp = resp.substring(0, resp.length() - 1);
                    String[] tmp1 = resp.split("\\|");
                    for (String s : tmp1) {
                        String[] tmp = s.split("\\^", -1);
                        Charity charity = new Charity();
                        charity.setUrl(tmp[0]);
                        charity.setName(tmp[1]);
                        try {
                            charity.setVotes(Integer.valueOf(tmp[2]));
                        } catch (ClassCastException e) {
                            charity.setVotes(0);
                        }
                        charities.add(charity);
                    }
                }
                if (listener != null)
                    listener.onCompleteArray(charities);
            } else if (action.equals("GET_HISTORY")) {
                Log.e("Async", resp);
                ArrayList<Charity> charities = new ArrayList<Charity>();
                if (!resp.equals("")) {
                    resp = resp.substring(0, resp.length() - 1);
                    String[] tmp1 = resp.split("\\|");
                    for (String s : tmp1) {
                        String[] tmp = s.split("\\^", -1);
                        Charity charity = new Charity();
                        charity.setName(tmp[0]);
                        try {
                            charity.setWorth(Integer.parseInt(tmp[1]));
                        } catch (ClassCastException e) {
                            charity.setWorth(0);
                        }

                        charity.setUrl(tmp[3]);
                        charities.add(charity);
                    }
                }
                if (listener != null)
                    listener.onDonationsArray(charities);
            } else if (action.equals("VOTE_CAST")) {
                Log.e("Async", resp);
            } else if (action.equals("VOTE_REMOVE")) {
                Log.e("Async", resp);
            } else if (action.equals("CHARITY_SUGGEST")) {
                Log.e("Async", resp);
            } else if (action.equals("CHARITY_MONTH")) {
                Log.e("Async", resp);
                Charity charity = new Charity();
                if (!resp.equals("")) {
                    resp = resp.substring(0, resp.length() - 1);
                    String[] tmp1 = resp.split("\\|");
                    String[] tmp = tmp1[0].split("\\^", -1);
                    charity.setName(tmp[0]);
                    charity.setUrl(tmp[1]);
                    try {
                        charity.setWorth(Integer.valueOf(tmp[2]));
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                    try {
                        // charity.setDescription(tmp[3]); // TO DO MICHAEL
                    } catch (ClassCastException e) {
                        // charity.setDescription("No description"); // TO DO
                        // MICHAEL
                    }

                    if (listener != null)
                        listener.onCharityMonth(charity);
                } else {
                    Log.e("Async", "Charity of the month not selected yet");
                }
            } else if (action.equals("CHARITY_CURRENT")) {
                Log.e("resp : ", String.valueOf(resp));
                Charity charity = new Charity();
                charity.setUrl(resp);
                if (listener != null)
                    listener.onCurrentCharity(charity);
            } else if (action.equals("USER_INSERT")) {
                Log.e("Async", resp);
            } else if (action.equals("USER_STATS")) {
                Log.e("Async", resp);
            } else if (action.equals("GET_LEADER")) {
                Log.e("Async", resp);
                ArrayList<UserStats> users = new ArrayList<UserStats>();
                if (!resp.equals("")) {
                    resp = resp.substring(0, resp.length() - 1);
                    String[] tmp1 = resp.split("\\|");
                    int inc = 1;
                    for (String s : tmp1) {
                        String[] tmp = s.split("\\^", -1);
                        UserStats user = new UserStats();
                        user.setEmail(tmp[0]);
                        user.setRank(inc);
                        try {
                            user.setScore(Integer.valueOf(tmp[1]));
                        } catch (ClassCastException e) {
                            user.setScore(0);
                        }
                        try {
                            user.setRate(Float.valueOf(tmp[2]));
                        } catch (ClassCastException e) {
                            user.setRate(0);
                        }
                        inc += 1;
                        users.add(user);
                    }
                }
                System.out.println(users.get(0).getName() + " has Raised "
                        + users.get(0).getRaised() + "$");
                // if (listener != null)
                // listener.onCompleteArray(users); // TO DO MICHAEL
            } else if (action.equals("GET_RANK")) {
                Log.e("Async", resp);
                System.out.println("TEST");
                int rank = 0;
                try {
                    rank = Integer.valueOf(resp);
                } catch (ClassCastException e) {
                    rank = 0;
                }
                // listener.onCompleteRank(rank); // TO DO MICHAEL
            } else {
                Log.e("Async", "Invalid Action specified!");
            }
        }
    }
}

class ConnectorTask extends AsyncTask<Void, Void, Boolean> {

    private final String action;
    private final ArrayList<NameValuePair> nvp;
    private final String fileName;

    public ConnectorTask(String action, ArrayList<NameValuePair> nvp, String fn) {
        this.action = action;
        this.nvp = nvp;
        this.fileName = fn;
    }

    private void postData() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://cyberkomm.ch/sidney/php/"
                + this.fileName);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(this.nvp));
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            String responseString = sb.toString();
            if (responseString.length() > 0) {
                AsyncConnector.interpretResponse(responseString, this.action);
            } else {
                Log.e("Async", "No Response. Empty String");
            }
        } catch (Exception e) {
            Log.e("Async", "Failed to perform action: " + this.action);
            Log.e("Async", "Error:  " + e.toString());
        }

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        postData();
        return null;
    }

}
