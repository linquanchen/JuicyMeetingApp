package edu.cmu.juicymeeting.juicymeeting.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.fragment.EventDetailFragment;
import edu.cmu.juicymeeting.util.Constants;

//public class EventDetailPageAdapter extends FragmentStatePagerAdapter {
public class EventDetailPageAdapter extends FragmentPagerAdapter {

    private Event[] events;
    private AppCompatActivity activity;

    public EventDetailPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public EventDetailPageAdapter(FragmentManager fm, Event[] events, AppCompatActivity activity) {
        super(fm);
        this.events = events;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        //args.putInt(EventDetailFragment.ARG_OBJECT, i + 1);
        args.putParcelable(Constants.EVENT, events[i]);
        //args.putParcelable(Constants.ACTIVITY, (Parcelable)activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return events.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "EVENT " + (position + 1);
    }
}
