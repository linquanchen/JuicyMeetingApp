package edu.cmu.juicymeeting.juicymeeting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.R;
import edu.cmu.juicymeeting.juicymeeting.adapter.EventDetailPageAdapter;
import edu.cmu.juicymeeting.juicymeeting.adapter.SampleFragmentPagerAdapter;
import edu.cmu.juicymeeting.util.Constants;
import edu.cmu.juicymeeting.util.Data;
import edu.cmu.juicymeeting.util.JuicyFont;
import edu.cmu.juicymeeting.util.ZoomOutPageTransformer;
import edu.cmu.juicymeeting.ws.HttpGetTask;
import edu.cmu.juicymeeting.ws.HttpPostTask;
import edu.cmu.juicymeeting.ws.RESTfulAPI;

public class EventDetailActivity extends AppCompatActivity {
    EventDetailPageAdapter eventDetailPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_wrapper);

        //get all events and current event position
        Bundle extras = getIntent().getExtras();
        Parcelable[] parcelables = extras.getParcelableArray(Constants.ALL_EVENTS);
        Event[] events = new Event[parcelables.length];
        for(int i = 0; i < events.length; i++) {
            events[i] = (Event)parcelables[i];
        }
        int position = extras.getInt(Constants.EVENT_INDEX);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        eventDetailPagerAdapter = new EventDetailPageAdapter(getSupportFragmentManager(), events, this);
        mViewPager = (ViewPager) findViewById(R.id.event_detail_wrapper);
        mViewPager.setAdapter(eventDetailPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setCurrentItem(position);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //necessary for transition animation
        //this.supportFinishAfterTransition();
    }

    public static class LoginActivity extends AppCompatActivity {
        private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;

        EditText _emailText;
        EditText _passwordText;
        Button _loginButton;
        TextView _signupLink;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            _emailText = (EditText)findViewById(R.id.input_email);
            _passwordText = (EditText)findViewById(R.id.input_password);
            _loginButton = (Button)findViewById(R.id.btn_login);
            _signupLink = (TextView)findViewById(R.id.link_signup);

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    //login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), CreateJoinGroupActivity.SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
        }

        public void login() {
            Log.d(TAG, "Login");

            if (!validate()) {
                onLoginFailed();
                return;
            }

            _loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_SIGNUP) {
                if (resultCode == RESULT_OK) {

                    // TODO: Implement successful signup logic here
                    // By default we just finish the Activity and log them in automatically
                    this.finish();
                }
            }
        }

        @Override
        public void onBackPressed() {
            // disable going back to the MainActivity
            moveTaskToBack(true);
        }

        public void onLoginSuccess() {
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed() {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("enter a valid email address");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                _passwordText.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }
    }

    public static class MainPageActivity extends AppCompatActivity
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

            new HttpGetTask(this).execute(RESTfulAPI.upcomingEventURL + Data.userEmail);

            JSONObject eventObject = new JSONObject();
            try {
                eventObject.put("lat", Data.lat);
                eventObject.put("lon", Data.log);
                eventObject.put("distance", Data.distance);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new HttpPostTask(RESTfulAPI.exploreEventURL, eventObject, "explore").execute();

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            android.support.v7.widget.RecyclerView listView =
                    (android.support.v7.widget.RecyclerView) findViewById(R.id.group_list);
            switch(requestCode) {
                case (CREATE_GROUP_ACTIVITY) : {

                    break;
                }
                case (JOIN_GROUP_ACTIVITY) : {

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

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                Intent intent = new Intent(this, CreateJoinGroupActivity.ProfileActivity.class);
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

    /**
     * Created by chenlinquan on 11/19/15.
     */
    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
