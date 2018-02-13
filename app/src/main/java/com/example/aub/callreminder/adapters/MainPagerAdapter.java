package com.example.aub.callreminder.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.aub.callreminder.R;
import com.example.aub.callreminder.logsfragment.LogsFragment;
import com.example.aub.callreminder.remindersfragment.RemindersFragment;

/**
 * Created by aub on 12/16/17.
 * Project: CallReminder
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RemindersFragment();
            case 1:
                return new LogsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.reminders_frag_title);
            case 1:
                return mContext.getString(R.string.logs_frag_title);
            default:
                return null;
        }
    }
}
