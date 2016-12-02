package edu.tufts.cs.twocents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListPollsFragment extends UpdatableFragment {
    private static final String TAG = "ListPollsFragment";

    public enum ListPollType {ALL, USER}

    private ListView pollListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Poll> polls;
    private ListPollType type;

    public void setType(ListPollType type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentName = TAG;
        //Log.v(TAG, "Just set fragment Name: " + fragmentName);
        return inflater.inflate(R.layout.activity_all, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Log.v(TAG, "onViewCreated running in ListPollsFragment");
        polls = new ArrayList<>();
        pollListView = (ListView) view.findViewById(R.id.poll_list_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        FloatingActionButton newPollButton = (FloatingActionButton) view.findViewById(R.id.new_post_button);
        newPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostNewPollActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Log.v(TAG, "onRefresh called from SwipeRefreshLayout");
                    fetchPolls();
                }
            }
        );

        fetchPolls();
    }

    private void fetchPolls() {
        swipeRefreshLayout.setRefreshing(true);
        ApiHandler apiHandler = new ApiHandler(getContext()) {
            @Override
            public void onCompleted(JSONObject response) {
                //Log.v(TAG, response.toString());
                displayPolls(response);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        if (this.type == ListPollType.ALL) {
            apiHandler.makeRequest(ApiMethods.GET_POLLS, null, null);
        } else {
            apiHandler.makeRequest(ApiMethods.GET_POLLS_FOR_USER, null, null);
        }
    }

    private void displayPolls(JSONObject JSONPolls) {
        try {
            JSONArray pollArray = JSONPolls.getJSONArray("polls");
            polls.clear();
            for (int i = 0; i < pollArray.length(); i++) {
                polls.add(new Poll(pollArray.getJSONObject(i)));
            }
            if (getView() == null) {
                //Log.v(TAG, "View is NULL?");
            }
            PollArrayAdapter adapter = new PollArrayAdapter(getView().getContext(), R.id.poll_list_view, polls);
            pollListView.setAdapter(adapter);

        } catch (JSONException e) {
            //Log.v(TAG, e.getMessage());
        }

    }

    @Override
    public void onLocationUpdate() {
        //Log.v(TAG, "Location updated in ListPollsFragment");
        fetchPolls();
    }
}
