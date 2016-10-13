package edu.tufts.cs.twocents;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by John on 10/12/16.
 */
public class Poll {

    private String username, pollId, question;
    private JSONObject votes;
    private Long createdAt;

    public String getUsername() {
        return username;
    }

    public String getPollId() {
        return pollId;
    }

    public String getQuestion() {
        return question;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public JSONObject getVotes() {
        return votes;
    }

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
            createdAt = new Long(poll.getInt("created"));

        } catch (JSONException e) {
        }
    }

    @Override
    public String toString() {
        return "Hello";
    }

}
