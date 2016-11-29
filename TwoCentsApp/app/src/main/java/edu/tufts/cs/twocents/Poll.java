package edu.tufts.cs.twocents;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by John on 10/12/16.
 * Poll
 */
class Poll {

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
            //Log.v(TAG, "Couldn't parse JSON");
        }
    }

    @Override
    public String toString() {
        return "Hello";
    }

    public int getVoteCount() {
        Iterator<String> i = votes.keys();
        int count = 0;
        while (i.hasNext()) {
            count += getVoteCount(i.next());
        }
        return count;
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

    public JSONObject getVotes() {
        return votes;
    }

    private int getVoteCount(String vote) {
        try {
            return getVote(vote).getInt("count");
        } catch (JSONException e) {
            return 0;
        }
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
