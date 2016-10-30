package edu.tufts.cs.twocents;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by toby on 10/28/16.
 */

public class User {
    private static final String PREF_NAME = "UserKey";
    private SharedPreferences sharedPrefs;

    public User(Context context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
    }

    public Boolean isUserKeySet() {
        return sharedPrefs.contains(PREF_NAME);
    }

    public String getUserKey() {
        return sharedPrefs.getString(PREF_NAME, null);
    }

    // only works if the key has not already been set
    public void setUserKey(String userKey) {
        if (!isUserKeySet()) {
            SharedPreferences.Editor editor = this.sharedPrefs.edit();
            editor.putString(PREF_NAME, userKey);
            editor.commit();
        }
    }
}