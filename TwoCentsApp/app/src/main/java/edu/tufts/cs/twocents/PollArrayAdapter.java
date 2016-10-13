package edu.tufts.cs.twocents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by John on 10/12/16.
 */
public class PollArrayAdapter extends ArrayAdapter<Poll> {

    private ArrayList<Poll> polls;

    public PollArrayAdapter(Context context, int listViewResourceId, ArrayList<Poll> polls) {
        super(context, listViewResourceId, polls);
        this.polls = polls;
        Log.v("PollArrayAdapter", "POLL ARRAY SIZE: " + polls.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.poll_template, parent, false);
        }

        Poll poll = polls.get(position);
        if (poll != null) {
            ((TextView) convertView.findViewById(R.id.poll_question)).setText(poll.getQuestion());
            ((TextView) convertView.findViewById(R.id.poll_created_at)).setText(poll.getCreatedAt().toString());
            ((TextView) convertView.findViewById(R.id.poll_username)).setText(poll.getUsername());
            Log.v("PollArrayAdapter", "Poll is not NULL");

        } else {
            Log.v("PollArrayAdapter", "Poll is NULL");
        }

        return convertView;
    }
}

