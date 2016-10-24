package edu.tufts.cs.twocents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;


/**
 * Created by John on 10/12/16.
 */
public class PollArrayAdapter extends ArrayAdapter<Poll> {

    private static final String TAG = "PollArrayAdapter";

    private ArrayList<Poll> polls;

    public PollArrayAdapter(Context context, int listViewResourceId, ArrayList<Poll> polls) {
        super(context, listViewResourceId, polls);
        this.polls = polls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.poll_template, parent, false);
        }

        Poll poll = polls.get(position);
        if (poll != null) {
            ((TextView) convertView.findViewById(R.id.poll_question)).
                    setText(poll.getQuestion());

            ((TextView) convertView.findViewById(R.id.poll_created_at)).
                    setText(poll.formatNumSecondsCreatedAgo());

            ((TextView) convertView.findViewById(R.id.poll_username)).
                    setText(poll.getUsername());

            Iterator<String> votes_itr = poll.getVotes().keys();
            LinearLayout voteButtonContainer =
                    (LinearLayout) convertView.findViewById(R.id.poll_vote_button_container);

            //Make sure that all child elements (i.e. voting buttons)
            //are removed before adding the new ones.
            voteButtonContainer.removeAllViews();

            while (votes_itr.hasNext()) {
                String vote = votes_itr.next();
                int vote_count = 0;
                try {
                    vote_count = poll.getVote(vote).getInt("count");
                    Button b = new Button(convertView.getContext());
                    b.setText(vote + " (" + vote_count + ")");
                    b.setOnClickListener(new HandleVoteButtonClick(poll.getPollId(),
                            poll.getVote(vote).getString("optionId")));
                    voteButtonContainer.addView(b);
                } catch (JSONException e) {
                    Log.v(TAG, "Couldn't find vote count");
                }

            }
            return convertView;
        } else {
            Log.v(TAG, "Poll is NULL");
        }

        return convertView;
    }

    private class HandleVoteButtonClick implements View.OnClickListener {
        private String pollId, optionId;

        public HandleVoteButtonClick(String pollId, String optionId) {
            this.pollId = pollId;
            this.optionId = optionId;
        }

        @Override
        public void onClick(View view) {
            String userKey = "9bac4d6226350f34ffa2c0dd77922b78";
            String [] getParams = {};
            MakeRequest request = new MakeRequest("/voteOnPoll" + "/" + userKey +  "/" + pollId + "/" + optionId, getParams);
            request.execute();
            view.refreshDrawableState();
        }
    }
}

