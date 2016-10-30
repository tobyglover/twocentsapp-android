package edu.tufts.cs.twocents;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by toby on 10/30/16.
 */

public class StoredSettings {
    private static final String FILE_PREF_NAME = "Settings";
    private static final String RADIUS_PREF = "Radius";
    private static final int RADIUS_DEFAULT = 3;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    public StoredSettings(Context context) {
        sharedPrefs = context.getSharedPreferences(FILE_PREF_NAME, context.MODE_PRIVATE);
    }

    public int getRadius() {
        return sharedPrefs.getInt(RADIUS_PREF, RADIUS_DEFAULT);
    }

    public void setRadius(int radius) {
        if (this.editor == null) {
            this.editor = this.sharedPrefs.edit();
        }

        editor.putInt(RADIUS_PREF, radius);
        editor.commit();
    }
}
