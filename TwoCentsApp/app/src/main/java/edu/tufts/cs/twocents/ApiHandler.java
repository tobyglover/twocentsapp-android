package edu.tufts.cs.twocents;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
 * Api Handler
 */


public class ApiHandler implements Requestable {

    private static final String TAG = "ApiHandler";
    private static final String BASE_URL = "http://2cnts.com/api/";
    private final Context context;
    private final User user;
    private final StoredSettings storedSettings;
    private Location currentLocation;
    private LocationManager locationManager;


    public ApiHandler(Context context) {
        this.context = context;
        this.user = new User(context);
        this.storedSettings = new StoredSettings(context);
    }

    private void makeRequestHelper(ApiMethods apiMethod, Map<String, String> params, Map<String, String> urlParams) {
        String url = BASE_URL;
        JSONObject postParams = null;
        int method = Request.Method.GET;

        if (isLocationRequired(apiMethod)) {
            if (params == null) {
                params = new HashMap<>();
            }

            params.put("lat", Double.toString(currentLocation.getLatitude()));
            params.put("lng", Double.toString(currentLocation.getLongitude()));
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

    public void makeRequest(final ApiMethods apiMethod, final Map<String, String> params, final Map<String, String> urlParams) {
        if (currentLocation == null && isLocationRequired(apiMethod)) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Log.v(TAG, location + " <- New location");
                    currentLocation = location;
                    makeRequestHelper(apiMethod, params, urlParams);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            try {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            } catch (SecurityException e) {
                Log.v(TAG, "Security exception!  Location permission not granted");
            } catch (Exception e) {
                Log.v(TAG, "Generic exception!" + e.getMessage());
            }
        } else {
            makeRequestHelper(apiMethod, params, urlParams);
        }
    }

    private Boolean isLocationRequired(ApiMethods method) {
        return method == ApiMethods.GET_POLLS || method == ApiMethods.GET_POLLS_FOR_USER || method == ApiMethods.CREATE_NEW_POLL;
    }


    public void onCompleted(JSONObject response) {}

    public void onError() {}

}
