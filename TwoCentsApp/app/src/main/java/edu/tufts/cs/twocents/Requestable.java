package edu.tufts.cs.twocents;

import org.json.JSONObject;

/**
 * Created by toby on 10/27/16.
 */

public interface Requestable {
    public void onCompleted(JSONObject response);
    public void onError();
}
