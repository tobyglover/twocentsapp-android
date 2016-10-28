package edu.tufts.cs.twocents;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import java.util.Map;


/**
 * Created by toby on 10/27/16.
 */



public class ApiHandler implements Requestable {

    private static final String TAG = "ApiHandler";
    private static final String BASE_URL = "http://2cnts.com/api/";
    private Context context;

    public ApiHandler(Context context) {
        this.context = context;
    }


    public void makeRequest(ApiMethods apiMethod, final Map<String, String> params, Map<String, String> urlParams) {

        String url = BASE_URL;
        JSONObject parameters = null;
        int method = Request.Method.GET;

        switch (apiMethod) {
            case CREATE_NEW_USER:
                url += "createNewUser";
                break;
            case IS_USERNAME_AVAILABLE:
                url += "isUsernameAvailable";
                break;
            case CREATE_NEW_POLL:
                method = Request.Method.POST;
                parameters = new JSONObject(params);
                Log.v(TAG, parameters.toString());
                url += "createNewPoll/" + urlParams.get("userKey") + "/";
                break;
            case VOTE_ON_POLL:
                url += "voteOnPoll/" + urlParams.get("userKey") + "/" + urlParams.get("pollId") + "/" + urlParams.get("optionId") + "/";
                break;
            case GET_POLLS:
                url += "getPolls";
                break;
        }

        // getParams() is overridden by JsonObjectRequest, can't use it
        if (method == Request.Method.GET && params != null) {
            url += "?";
            for (String key : params.keySet()) {
                url += key + "=" + params.get(key) + "&";
            }
        }

        JsonObjectRequest request = new JsonObjectRequest(method, url, parameters, new Response.Listener<JSONObject>() {
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
