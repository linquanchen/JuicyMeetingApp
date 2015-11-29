package edu.cmu.juicymeeting.juicymeeting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cmu.juicymeeting.util.SampleFragmentPagerAdapter;

public class MainPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CREATE_GROUP_ACTIVITY = 0;
    private static final int JOIN_GROUP_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);        // Get the ViewPager and set it's PagerAdapter so that it can display items


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainPageActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabLayout.Tab tab = tabLayout.getTabAt(1);//second tab as default
        tab.select();

        //stub
        ListView listView;
        ArrayList<String> list;
        ArrayAdapter<String> adapter;


    }

    public void createEvent(View view) {
        //intent Viewer to show result
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    public void createGroup(View view) {
        //intent Viewer to show result
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivityForResult(intent, CREATE_GROUP_ACTIVITY);
    }

    public void joinGroup(View view) {
        //intent Viewer to show result
        Intent intent = new Intent(this, JoinGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        android.support.v7.widget.RecyclerView listView =
                (android.support.v7.widget.RecyclerView) findViewById(R.id.group_list);
        switch(requestCode) {
            case (CREATE_GROUP_ACTIVITY) : {
//                if (resultCode == Activity.RESULT_OK) {
//                    String number = data.getStringExtra(CreateGroupActivity.PASS);
//                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(number));
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                            android.R.layout.simple_list_item_1, android.R.id.text1, list);
//                    listView.setAdapter(adapter);
//                }
                break;
            }
            case (JOIN_GROUP_ACTIVITY) : {
//                if (resultCode == Activity.RESULT_OK) {
//                    String number = data.getStringExtra(JoinGroupActivity.PASS);
//                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(number));
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                            android.R.layout.simple_list_item_1, android.R.id.text1, list);
//                    listView.setAdapter(adapter);
//                }
                break;
            }
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pastEvents) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
