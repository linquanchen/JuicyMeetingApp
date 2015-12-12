package edu.cmu.juicymeeting.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cmu.juicymeeting.juicymeeting.PageFragment;
import edu.cmu.juicymeeting.juicymeeting.R;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    private Context context;
    private static String tabTitles[] = new String[]{"CREATE", "MY", "EXPLORE", "CHAT"};
    private int[] imageResId = {R.drawable.create, R.drawable.star_outline, R.drawable.explore, R.drawable.chat};

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
        Log.w("get new tab view", "normal");
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        tv.setTextColor(Color.rgb(0,0,0));
//        ImageView img = (ImageView)v.findViewById(R.id.imgView);
//        img.setBackgroundResource(imageResId[position]);
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, imageResId[position], 0, 0);
        return v;
    }
}