package edu.tufts.cs.twocents;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

/**
 * Created by toby on 10/30/16.
 * Stored Settings
 */

class StoredSettings {
    private static final String FILE_PREF_NAME = "Settings";

    private static final String RADIUS_PREF = "Radius";
    private static final int RADIUS_DEFAULT = 3;

    private static final String LOCATION_LAT_PREF = "LocationLat";
    private static final String LOCATION_LNG_PREF = "LocationLng";
    public static final float LOCATION_LAT_DEFAULT = 270;
    public static final float LOCATION_LNG_DEFAULT = 270;

    private final SharedPreferences sharedPrefs;

    public StoredSettings(Context context) {
        sharedPrefs = context.getSharedPreferences(FILE_PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getRadius() {
        return sharedPrefs.getInt(RADIUS_PREF, RADIUS_DEFAULT);
    }

    public void setRadius(int radius) {
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        editor.putInt(RADIUS_PREF, radius);
        editor.apply();
    }

    public double getMostRecentLat() {
        return (double) sharedPrefs.getFloat(LOCATION_LAT_PREF, LOCATION_LAT_DEFAULT);
    }

    public double getMostRecentLng() {
        return (double) sharedPrefs.getFloat(LOCATION_LNG_PREF, LOCATION_LNG_DEFAULT);
    }

    public void setMostRecentLocation(Location l) {
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        editor.putFloat(LOCATION_LAT_PREF, (float) l.getLatitude());
        editor.putFloat(LOCATION_LNG_PREF, (float) l.getLongitude());
        editor.apply();
    }

    public void clearLocation() {
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        editor.remove(LOCATION_LAT_PREF);
        editor.remove(LOCATION_LNG_PREF);
        editor.apply();
    }
}
