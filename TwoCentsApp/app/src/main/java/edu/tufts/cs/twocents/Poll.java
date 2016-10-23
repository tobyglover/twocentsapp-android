package edu.tufts.cs.twocents;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by John on 10/12/16.
 */
public class Poll {

    private static final String TAG = "Poll";

    private String username, pollId, question;
    private JSONObject votes;
    private int numSecondsCreatedAgo;


    public Poll (JSONObject poll) {

        try {
            username = poll.getString("username");
        } catch (JSONException e) {
            username = "";
        }

        try {
            votes = poll.getJSONObject("votes");
            pollId = poll.getString("pollId");
            question = poll.getString("question");
            numSecondsCreatedAgo = poll.getInt("createdAgo");

        } catch (JSONException e) {
        }
    }

    @Override
    public String toString() {
        return "Hello";
    }


    public String getUsername() {
        return username;
    }

    public String getPollId() {
        return pollId;
    }

    public String getQuestion() {
        return question;
    }

    public int getNumSecondsCreatedAgo() {
        return numSecondsCreatedAgo;
    }

    public JSONObject getVotes() {
        return votes;
    }

    public JSONObject getVote(String vote) {
        try {
            return votes.getJSONObject(vote);
        } catch (JSONException e) {
            return null;
        }
    }

    public String formatNumSecondsCreatedAgo() {
        if (numSecondsCreatedAgo < 0) {
            return "error";
        } else if (numSecondsCreatedAgo < 60) {
            return "a few seconds ago";
        } else if (numSecondsCreatedAgo < 3600) {
            return (numSecondsCreatedAgo / 60) + " m";
        } else if (numSecondsCreatedAgo < 86400) {
            return (numSecondsCreatedAgo / 3600) + " h";
        } else {
            return (numSecondsCreatedAgo / 86400) + "d";
        }
    }
}
