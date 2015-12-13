package edu.cmu.juicymeeting.juicymeeting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import edu.cmu.juicymeeting.juicymeeting.R;
import edu.cmu.juicymeeting.juicymeeting.fragment.PageFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    private Context context;
    private static String tabTitles[] = new String[]{"CREATE", "MY", "EXPLORE", "CHAT"};
    private int[] imageResId = {R.drawable.create, R.drawable.star_outline, R.drawable.explore, R.drawable.chat};
    private int[] imageSelectedResId = {R.drawable.create_pink, R.drawable.star_outline_pink, R.drawable.explore, R.drawable.chat};

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @SuppressLint("NewApi")
    public View getNormalTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        tv.setTextColor(Color.rgb(0,0,0));
        tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, imageResId[position], 0, 0);
        return v;
    }

    @SuppressLint("NewApi")
    public void setNormalTabView(View v, int position) {
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, imageResId[position], 0, 0);
    }

    @SuppressLint("NewApi")
    public void setSelectedTabView(View v, int position) {
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setTextColor(ContextCompat.getColor(context, R.color.sexy_pink));
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, imageSelectedResId[position], 0, 0);
    }
}