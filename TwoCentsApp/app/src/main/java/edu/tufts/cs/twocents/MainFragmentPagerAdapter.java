package edu.tufts.cs.twocents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import static android.R.attr.fragment;
import static com.google.android.gms.internal.zzng.fm;

/**
 * Created by John on 10/23/16.
 * MainFragmentPagerAdapter
 */
class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<UpdatableFragment> fragmentList;
    private static final int NUM_FRAGMENTS = 3;

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        for (int i = 0; i < NUM_FRAGMENTS; i++) {
            fragmentList.add(i, null);
        }
    }

    @Override
    public UpdatableFragment getItem(int position) {
        if (fragmentList.get(position) == null) {
            UpdatableFragment fragment;
            switch (position) {
                case 0:
                    ListPollsFragment allPollsFragment = new ListPollsFragment();
                    allPollsFragment.setType(ListPollsFragment.ListPollType.ALL);
                    fragment = allPollsFragment;
                    break;
                case 1:
                    ListPollsFragment userPollsFragment = new ListPollsFragment();
                    userPollsFragment.setType(ListPollsFragment.ListPollType.USER);
                    fragment = userPollsFragment;
                    break;
                case 2:
                    fragment = new SettingsFragment();
                    break;
                default:
                    return null;
            }
            fragmentList.add(position, fragment);
            return fragment;
        } else {
            return fragmentList.get(position);
        }
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }


}