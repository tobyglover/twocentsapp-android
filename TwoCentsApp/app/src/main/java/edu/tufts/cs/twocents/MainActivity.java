package edu.tufts.cs.twocents;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupTabs();
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

            MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
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
}


