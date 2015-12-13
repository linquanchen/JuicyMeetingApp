package edu.cmu.juicymeeting.juicymeeting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.juicymeeting.database.chatDB.DatabaseConnector;
import edu.cmu.juicymeeting.juicymeeting.R;

public class CreateJoinGroupActivity extends AppCompatActivity {
    private static final String TAG = CreateJoinGroupActivity.class.getSimpleName();
    private String chatAction;
    private EditText groupCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        //set toolbar and menu
        TextView tv = (TextView)findViewById(R.id.toolbar_title);
        tv.setText("GROUP INFO");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //must set, otherwise menu is not shown
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // find the group code view
        groupCodeEditText = (EditText) findViewById(R.id.join_group_password);
        // get whether user wants to create/join an group chat
        chatAction = getIntent().getExtras().getString(ChatroomActivity.CHAT_ACTION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w("inflate", "menu");
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.check_menu:
                confirm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // invoke when ok button clicked
    public void confirm() {
        // pass group code and chat action to the chat room session
        Intent i = new Intent(this, ChatroomActivity.class);
        String groupCode = groupCodeEditText.getText().toString();
        Log.e(TAG, "get chat group code: " + groupCode);
        i.putExtra(ChatroomActivity.CHATROOM_GROUP, groupCode);
        i.putExtra(ChatroomActivity.CHAT_ACTION, chatAction);
        // determine whether is old user
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        String isOldUser;
        if (connector.getGroupByGroupCode(Integer.parseInt(groupCode)) != null)
            isOldUser = "true";
        else
            isOldUser = "false";
        i.putExtra(ChatroomActivity.IS_OLD_USER, isOldUser);
        startActivity(i);

        //finish this activity
        finish();
    }


    public static class ProfileActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            //toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            //set title
            TextView title = (TextView)findViewById(R.id.toolbar_title);
            title.setText("PROFILE");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //set navigation icon, this must be set after set navigation view
            toolbar.setNavigationIcon(R.drawable.profile_pink);
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
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_events) {
                Intent intent = new Intent(this, EventDetailActivity.MainPageActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_myEvents) {

            } else if (id == R.id.nav_chat) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    public static class SignupActivity extends AppCompatActivity {
        private static final String TAG = "SignupActivity";

        EditText _nameText;
        EditText _emailText;
        EditText _passwordText;
        Button _signupButton;
        TextView _loginLink;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            _nameText = (EditText)findViewById(R.id.input_name);
            _emailText = (EditText)findViewById(R.id.input_email);
            _passwordText = (EditText)findViewById(R.id.input_password);
            _signupButton = (Button)findViewById(R.id.btn_signup);
            _loginLink = (TextView)findViewById(R.id.link_login);

            _signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signup();
                }
            });

            _loginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Finish the registration screen and return to the Login activity
                    finish();
                }
            });
        }

        public void signup() {
            Log.d(TAG, "Signup");

            if (!validate()) {
                onSignupFailed();
                return;
            }

            _signupButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            // TODO: Implement your own signup logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }


        public void onSignupSuccess() {
            _signupButton.setEnabled(true);
            setResult(RESULT_OK, null);
            finish();
        }

        public void onSignupFailed() {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _signupButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (name.isEmpty() || name.length() < 3) {
                _nameText.setError("at least 3 characters");
                valid = false;
            } else {
                _nameText.setError(null);
            }

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
}
