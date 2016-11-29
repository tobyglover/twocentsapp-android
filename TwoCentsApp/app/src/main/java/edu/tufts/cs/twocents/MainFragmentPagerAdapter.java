package edu.tufts.cs.twocents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by John on 10/23/16.
 * MainFragmentPagerAdapter
 */
class MainFragmentPagerAdapter extends FragmentPagerAdapter {


    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public UpdatableFragment getItem(int position) {
        switch (position) {
            case 0:
                ListPollsFragment allPollsFragment = new ListPollsFragment();
                allPollsFragment.setType(ListPollsFragment.ListPollType.ALL);
                return allPollsFragment;
            case 1:
                ListPollsFragment userPollsFragment = new ListPollsFragment();
                userPollsFragment.setType(ListPollsFragment.ListPollType.USER);
                return userPollsFragment;
            case 2:
                return new SettingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


}