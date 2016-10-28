package edu.tufts.cs.twocents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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
                Intent intent = new Intent(getActivity(), PostNewPollActivity.class);
                startActivity(intent);
            }
        });
        fetchPolls();
    }

    private void fetchPolls() {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("lat", "-5");
        getParams.put("lng", "-5");
        getParams.put("radius", "5");

        ApiHandler apiHandler = new ApiHandler(getContext().getApplicationContext()) {
            @Override
            public void onCompleted(JSONObject response) {
                Log.v(TAG, response.toString());
                displayPolls(response);
            }
        };

        apiHandler.makeRequest(ApiMethods.GET_POLLS, getParams, null);
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
