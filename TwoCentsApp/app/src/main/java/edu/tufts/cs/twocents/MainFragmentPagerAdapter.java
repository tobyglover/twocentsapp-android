package edu.tufts.cs.twocents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.tufts.cs.twocents.AllActivity;
import edu.tufts.cs.twocents.SettingsActivity;

/**
 * Created by John on 10/23/16.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {


    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllActivity allActivity = new AllActivity();
                return allActivity;
            case 1:
                SettingsActivity settingsActivity = new SettingsActivity();
                return settingsActivity;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }



}