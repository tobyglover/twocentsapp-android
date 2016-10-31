package edu.tufts.cs.twocents;

import org.json.JSONObject;

/**
 * Created by toby on 10/27/16.
 * Requestable
 */

interface Requestable {
    void onCompleted(JSONObject response);
    void onError();
}
