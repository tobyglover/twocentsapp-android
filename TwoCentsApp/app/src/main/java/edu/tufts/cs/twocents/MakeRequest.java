package edu.tufts.cs.twocents;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MakeRequest extends AsyncTask<Void, Void, JSONObject> {

    private static final String TAG = "MakeRequest";

    public static enum HTTP_Methods {GET, POST};

    private String baseUrl = "http://2cnts.com/api/";
    private String pathUrl = "";
    private String[] getParams;
    private HTTP_Methods method;

    public MakeRequest(String pathUrl, String[] getParams, HTTP_Methods method) {
        super();
        this.pathUrl = pathUrl;
        this.getParams = getParams;
        this.method = method;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            String urlString = baseUrl + pathUrl + "?";
            for (int i = 0; i < getParams.length; i++) {
                urlString += getParams[i] + "&";
            }
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            if (this.method == HTTP_Methods.GET) {
                urlConnection.setRequestMethod("GET");
            } else {
                urlConnection.setRequestMethod("GET");
            }
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            JSONObject j = null;
            try {
                j = new JSONObject(buffer.toString());
            } catch(JSONException e) {
                Log.v(TAG, "JSON parsing error: " + e.getMessage());
                return null;
            }
            return j;
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }
}