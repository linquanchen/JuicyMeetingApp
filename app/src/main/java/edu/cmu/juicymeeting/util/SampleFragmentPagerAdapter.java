package edu.cmu.juicymeeting.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cmu.juicymeeting.juicymeeting.R;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private Context context;
    private String tabTitles[] = new String[] { "create", "upcoming", "explore", "chat" };
    private int[] imageNormalResId = { R.drawable.create, R.drawable.star_outline, R.drawable.search, R.drawable.chat};
    private int[] imageSelectedResId = { R.drawable.create_pink, R.drawable.star_outline_pink,
            R.drawable.search_pink, R.drawable.chat_pink};
    private int[] imageResId;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        imageResId = imageNormalResId;
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
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);
        return v;
    }

    private boolean selected = false;
    public void switchTab() {
        if(!selected)
            imageResId = imageNormalResId;
        else
            imageResId = imageSelectedResId;
        selected = !selected;
    }

    @SuppressLint("NewApi")
    public View getSelectedTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        tv.setTextColor(context.getColor(R.color.sexy_pink));
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageSelectedResId[position]);
        return v;
    }
}