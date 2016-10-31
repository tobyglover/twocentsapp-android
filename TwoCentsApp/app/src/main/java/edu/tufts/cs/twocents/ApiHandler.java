package edu.tufts.cs.twocents;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by toby on 10/27/16.
 */



public class ApiHandler implements Requestable {

    private static final String TAG = "ApiHandler";
    private static final String BASE_URL = "http://2cnts.com/api/";
    private Context context;
    private User user;
    private StoredSettings storedSettings;

    public ApiHandler(Context context) {
        this.context = context;
        this.user = new User(context);
        this.storedSettings = new StoredSettings(context);
    }


    public void makeRequest(ApiMethods apiMethod, Map<String, String> params, Map<String, String> urlParams) {

        String url = BASE_URL;
        JSONObject postParams = null;
        int method = Request.Method.GET;

        if (apiMethod == ApiMethods.GET_POLLS || apiMethod == ApiMethods.GET_POLLS_FOR_USER || apiMethod == ApiMethods.CREATE_NEW_POLL) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("lat", "-5"); // need to make this real values
            params.put("lng", "-5");
            params.put("radius", Integer.toString(storedSettings.getRadius()));
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
                onCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                onError();
            }
        });

        RequestSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void onCompleted(JSONObject response) {
        return;
    }

    public void onError() {
        return;
    }

}
