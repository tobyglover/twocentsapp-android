package edu.tufts.cs.twocents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllActivity extends Fragment {
    private static final String TAG = "AllActivity";

    private ViewGroup container;

    private ListView pollListView;
    private ArrayList<Poll> polls;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        polls = new ArrayList<>();
        pollListView = (ListView) container.findViewById(R.id.poll_list_view);
        fetchPolls();
        return inflater.inflate(R.layout.activity_all, container, false);
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
            PollArrayAdapter adapter = new PollArrayAdapter(container.getContext(), R.id.poll_list_view, polls);
            pollListView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }

    }
}
