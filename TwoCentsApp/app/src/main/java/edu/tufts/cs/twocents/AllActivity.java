package edu.tufts.cs.twocents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllActivity extends Fragment {
    private static final String TAG = "AllActivity";

    private ListView pollListView;
    private FloatingActionButton newPollButton;
    private ArrayList<Poll> polls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_all, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        polls = new ArrayList<>();
        pollListView = (ListView) getView().findViewById(R.id.poll_list_view);
        newPollButton = (FloatingActionButton) getView().findViewById(R.id.new_post_button);
        newPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "New Poll Button Clicked");
            }
        });
        fetchPolls();
    }

    private void fetchPolls() {
        String [] getParams = {"lat=2", "lng=-5", "radius=5"};
        MakeRequest mr = new MakeRequest("getPolls", getParams) {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    displayPolls(result);
                    Log.v(TAG, result.toString());
                } else {
                    Log.v(TAG, "JSON Object was NULL");
                }
            }
        };
        mr.execute();
    }

    private void displayPolls(JSONObject JSONPolls) {
        try {
            JSONArray pollArray = JSONPolls.getJSONArray("polls");
            polls.clear();
            for (int i = 0; i < pollArray.length(); i++) {
                polls.add(new Poll(pollArray.getJSONObject(i)));
            }
            PollArrayAdapter adapter = new PollArrayAdapter(getView().getContext(), R.id.poll_list_view, polls);
            pollListView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }

    }
}
