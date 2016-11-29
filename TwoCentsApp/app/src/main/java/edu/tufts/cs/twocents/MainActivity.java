package edu.tufts.cs.twocents;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;

import static java.security.AccessController.getContext;


/**
 * Created by John on 10/23/16.
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private final int [] ICON_IDS = {R.drawable.all_icon, R.drawable.me_icon, R.drawable.settings_icon};

    private ViewPager viewPager;

    private static final int MY_PERMISSIONS_ACCESS_LOCATION_FINE = 1;

    private static final String TAG = "MainActivity";

    private LocationListener locationListener;
    private StoredSettings storedSettings;

    MainFragmentPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupTabs();

        storedSettings = new StoredSettings(this.getApplicationContext());
        storedSettings.clearLocation();

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.v(TAG, "Location changed " + location.toString());
                storedSettings.setMostRecentLocation(location);
                if (viewPager != null) {
                    UpdatableFragment currentFragment = adapter.getItem(0);
                    if (currentFragment == null) {
                        Log.v(TAG, "Current fragment is null");
                    } else if (currentFragment.getContext() == null){
                        Log.v(TAG, "Current fragment context is null");
                    } else {
                        currentFragment.onLocationUpdate();
                    }
                    Log.v(TAG, "Fragment Name: " + currentFragment.fragmentName);
                } else {
                    Log.v(TAG, "View pager is null");
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void setupTabs() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSIONS_ACCESS_LOCATION_FINE );
        } else {
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            viewPager = (ViewPager) findViewById(R.id.pager);
            tabLayout.setupWithViewPager(viewPager);

            adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(ICON_IDS[i]);
            }

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition(), true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            User user = new User(getApplicationContext());
            if (!user.isUserKeySet()) {
                Intent intent = new Intent(this, CreateUserActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_LOCATION_FINE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupTabs();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Log.v(TAG, "Called onResume");
            LocationManager locationManager = (LocationManager) this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            Log.v(TAG, "Security exception!  Location permission not granted");
        } catch (Exception e) {
            Log.v(TAG, "Generic exception!" + e.getMessage());
        }
    }
}


