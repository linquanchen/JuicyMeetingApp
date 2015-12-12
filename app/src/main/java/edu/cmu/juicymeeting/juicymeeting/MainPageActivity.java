package edu.cmu.juicymeeting.juicymeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.juicymeeting.util.Data;
import edu.cmu.juicymeeting.util.HttpAsyncTask;
import edu.cmu.juicymeeting.util.JuicyFont;
import edu.cmu.juicymeeting.util.PostTask;
import edu.cmu.juicymeeting.util.RESTfulAPI;
import edu.cmu.juicymeeting.util.SampleFragmentPagerAdapter;

public class MainPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CREATE_GROUP_ACTIVITY = 0;
    private static final int JOIN_GROUP_ACTIVITY = 1;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initial font setter
        JuicyFont.initialize(getApplicationContext());

        setContentView(R.layout.activity_mainpage);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        //tab view pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final SampleFragmentPagerAdapter pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), MainPageActivity.this);
        viewPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getNormalTabView(i));
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        //toolbar, set menu
                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        toolbar.setNavigationIcon(R.drawable.profile_pink);
                        TextView toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
                        toolbarTitle.setText("CREATE");
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        break;
                    case 1:
                        //toolbar, set menu
                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        toolbar.setNavigationIcon(R.drawable.profile_pink);
                        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
                        toolbar.getMenu().clear();
                        toolbarTitle.setText("MY EVENTS");
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        break;
                    case 2:
                        //toolbar, set menu
                        toolbar = (Toolbar)findViewById(R.id.toolbar);
                        toolbar.setNavigationIcon(R.drawable.profile_pink);
                        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
                        toolbar.getMenu().clear();
                        toolbarTitle.setText("EXPLORE");
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        break;
                    case 3:
                        toolbar = (Toolbar)findViewById(R.id.toolbar);
                        toolbar.setNavigationIcon(R.drawable.profile_pink);
                        toolbar.getMenu().clear();
                        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
                        toolbarTitle.setText("CHAT");
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        break;
                }
            }
        });
        TabLayout.Tab tab = tabLayout.getTabAt(2);//second tab as default
        tab.select();

        new HttpAsyncTask(this).execute(RESTfulAPI.upcomingEventURL + Data.userEmail);

        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("lat", Data.lat);
            eventObject.put("lon", Data.log);
            eventObject.put("distance", Data.distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostTask(RESTfulAPI.exploreEventURL, eventObject, "explore").execute();

    }

//    public void createGroup(View view) {
//        Intent intent = new Intent(this, CreateJoinGroupActivity.class);
//        startActivityForResult(intent, CREATE_GROUP_ACTIVITY);
//    }
//
//    public void joinGroup(View view) {
//        Intent intent = new Intent(MainPageActivity.this, CreateJoinGroupActivity.class);
//        startActivity(intent);
//    }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_publish, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.w("click", "menu");
//        ((PageFragment)(getSupportFragmentManager().findFragmentById(R.id.create_event_wrapper))).publish();
//        return true;
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    public void tabClick(View v) {
        Log.w("test", "test");
    }
}
