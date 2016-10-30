package edu.tufts.cs.twocents;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;


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

            LinearLayout voteButtonContainer =
                    (LinearLayout) convertView.findViewById(R.id.poll_vote_button_container);

            voteButtonContainer.removeAllViews();

            int voteCount, currentVoteCount = 0, totalVoteCount = poll.getVoteCount();
            Iterator<String> i = poll.getVotes().keys();

            while (i.hasNext()) {
                String vote = i.next();
                try {
                    voteCount = poll.getVote(vote).getInt("count");

                    final float voteStartingPointPercentage = ((float)currentVoteCount / totalVoteCount);
                    final float voteEndingPointPercentage = voteStartingPointPercentage +  ((float) voteCount / totalVoteCount);

                    final LinearLayout l = (LinearLayout) LayoutInflater.from(convertView.getContext())
                            .inflate(R.layout.poll_vote_button_template, parent, false);

                    final View v = l.findViewById(R.id.vote_button_line);
                    l.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            //v.getViewTreeObserver().removeGlobalLayoutListener(this);
                            int width  = l.getMeasuredWidth();
                            int height = l.getMeasuredHeight();

                            int startingPoint = Math.round(width * voteStartingPointPercentage);
                            int endingPoint = Math.round(width * voteEndingPointPercentage);

                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params.width = endingPoint - startingPoint;
                            params.leftMargin = startingPoint;
                            v.setLayoutParams(params);
                        }
                    });


                    Button b = (Button) l.findViewById(R.id.poll_vote_button);

                    b.setText(vote);
                    b.setOnClickListener(new HandleVoteButtonClick(poll.getPollId(),
                            poll.getVote(vote).getString("optionId")));
                    b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.buttonColorPrimary));
                    voteButtonContainer.addView(l);
                    currentVoteCount += voteCount;

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
        public void onClick(final View view) {
            Map<String, String> urlParams = new HashMap<>();

            urlParams.put("pollId", this.pollId);
            urlParams.put("optionId", this.optionId);

            ApiHandler apiHandler = new ApiHandler(getContext().getApplicationContext()) {
                @Override
                public void onCompleted(JSONObject response) {
                    requestDone(view);
                }
            };

            apiHandler.makeRequest(ApiMethods.VOTE_ON_POLL, null, urlParams);
        }

        private void requestDone(View view) {
            view.refreshDrawableState();
        }
    }
}

