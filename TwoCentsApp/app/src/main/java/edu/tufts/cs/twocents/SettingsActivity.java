package edu.tufts.cs.twocents;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SettingsActivity extends Fragment {
    private static final String TAG = "SettingsActivity";
    private static final int DEFAULT_ZOOM = 10;

    private GoogleMap map;
    private MapView mapView;
    private SeekBar radiusBar;
    private TextView radiusValue;
    private StoredSettings storedSettings;
    private Circle userCircle;
    private LocationManager locationManager;

    private static final int MY_PERMISSIONS_ACCESS_LOCATION_FINE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        storedSettings = new StoredSettings(getContext().getApplicationContext());

        initializeRadius(view);
        initializeMap(view, savedInstanceState);

        return view;
    }

    public void initializeRadius(View view) {
        radiusBar = (SeekBar) view.findViewById(R.id.radius);
        radiusValue = (TextView) view.findViewById(R.id.radius_value);
        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int radius = i + 1;
                radiusValue.setText("Search Radius: " + radius + "km");
                storedSettings.setRadius(radius);
                if (userCircle != null) {
                    userCircle.setRadius(radius * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void initializeUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSIONS_ACCESS_LOCATION_FINE );
        } else {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    storedSettings.setMostRecentLocation(location);

                    LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.v(TAG, "Location: " + myLatLng.toString());

                    map.clear();
                    map.addMarker(new MarkerOptions().position(myLatLng).title("I'm here!"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLatLng).zoom(DEFAULT_ZOOM).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    CircleOptions circleOptions = new CircleOptions().center(myLatLng).radius(storedSettings.getRadius() * 1000);
                    userCircle = map.addCircle(circleOptions);
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void initializeMap(View view, Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();
        //

        try {
            MapsInitializer.initialize(getContext().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                map = gMap;
                LatLng myLatLng = new LatLng(storedSettings.getMostRecentLat(), storedSettings.getMostRecentLng());
                Log.v(TAG, "Saved Location: " + myLatLng.toString());

                map.clear();
                map.addMarker(new MarkerOptions().position(myLatLng).title("I'm here!"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLatLng).zoom(DEFAULT_ZOOM).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                CircleOptions circleOptions = new CircleOptions().center(myLatLng).radius(storedSettings.getRadius() * 1000);
                userCircle = map.addCircle(circleOptions);
                initializeUserLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_LOCATION_FINE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeUserLocation();
                }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        radiusBar.setProgress(storedSettings.getRadius() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
