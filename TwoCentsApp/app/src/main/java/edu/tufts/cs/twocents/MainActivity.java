package edu.tufts.cs.twocents;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by John on 10/23/160.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final int [] ICON_IDS = {R.drawable.all_icon, R.drawable.settings_icon};

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
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
    }
}


