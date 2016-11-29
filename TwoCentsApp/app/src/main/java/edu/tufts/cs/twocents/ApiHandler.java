package edu.tufts.cs.twocents;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.analytics.internal.zzy.n;
import static com.google.android.gms.analytics.internal.zzy.p;
import static com.google.android.gms.analytics.internal.zzy.w;
import static edu.tufts.cs.twocents.ApiMethods.CREATE_NEW_USER;


/**
 * Created by toby on 10/27/16.
 * Api Handler
 */


public class ApiHandler implements Requestable {

    private static final String TAG = "ApiHandler";
    private static final String BASE_URL = "http://2cnts.com/api/";
    private final Context context;
    private final User user;
    private final StoredSettings storedSettings;

    public ApiHandler(Context context) {
        this.context = context;
        this.user = new User(context);
        this.storedSettings = new StoredSettings(context);
    }

    public void makeRequest(ApiMethods apiMethod, Map<String, String> params, Map<String, String> urlParams) {
        String url = BASE_URL;
        JSONObject postParams = null;
        int method = Request.Method.GET;
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy();

        if (apiMethod == ApiMethods.GET_POLLS || apiMethod == ApiMethods.GET_POLLS_FOR_USER || apiMethod == ApiMethods.CREATE_NEW_POLL) {
            if (params == null) {
                params = new HashMap<>();
            }


            if (!storedSettings.hasLocation()) {
                // not ready yet
                return;
            }

            double latitude = storedSettings.getMostRecentLat();
            double longitude = storedSettings.getMostRecentLng();
            int radius = storedSettings.getRadius();

            params.put("lat", Double.toString(latitude));
            params.put("lng", Double.toString(longitude));
            params.put("radius", Integer.toString(radius));
        }

        switch (apiMethod) {
            case CREATE_NEW_USER:
                url += "createNewUser";
                break;
            case IS_USERNAME_AVAILABLE:
                url += "isUsernameAvailable";
                break;
            case CREATE_NEW_POLL:
                method = Request.Method.POST;
                postParams = new JSONObject(params);
                url += "createNewPoll/" + this.user.getUserKey() + "/";
                retryPolicy = new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                break;
            case VOTE_ON_POLL:
                url += "voteOnPoll/" + this.user.getUserKey() + "/" + urlParams.get("pollId") + "/" + urlParams.get("optionId") + "/";
                break;
            case GET_POLLS:
                url += "getPolls";
                break;
            case GET_POLLS_FOR_USER:
                url += "getPollsForUser/" + this.user.getUserKey() + "/";
                break;
            default:
                return;
        }

        // getParams() is overridden by JsonObjectRequest, can't use it
        if (method == Request.Method.GET && params != null) {
            url += "?";
            for (String key : params.keySet()) {
                url += key + "=" + params.get(key) + "&";
            }
        }

        JsonObjectRequest request = new JsonObjectRequest(method, url, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v(TAG, "Received response:\n" + response.toString());
                onCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                onError();
            }
        });

        request.setRetryPolicy(retryPolicy);

        RequestSingleton.getInstance(this.context).addToRequestQueue(request);

        if (postParams != null) {
            Log.v(TAG, "ApiMethodNum: " + apiMethod.getName() + "\nUrl: " + url + "\nPost Params: " + postParams.toString());
        } else {
            Log.v(TAG, "ApiMethodNum: " + apiMethod.getName() + "\nUrl: " + url);
        }

    }

    public void onCompleted(JSONObject response) {}

    public void onError() {}

}
