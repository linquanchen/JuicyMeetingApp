package edu.cmu.juicymeeting.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import edu.cmu.juicymeeting.database.model.ChatGroup;
import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.EventDetailActivity;
import edu.cmu.juicymeeting.chat.GroupChatActivity;
import edu.cmu.juicymeeting.juicymeeting.OnItemClickListener;
import edu.cmu.juicymeeting.juicymeeting.R;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    protected static final String TAG = "PageFragment";

    public static final String ARG_PAGE = "ARG_PAGE";

    private static final int CREATE_GROUP_ACTIVITY = 0;
    private static final int JOIN_GROUP_ACTIVITY = 1;

    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;

    private int mPage;

    private RecyclerView mRecyclerView;
    private CardViewDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeContainer;

    private RecyclerView exploreRecyclerView;
    private CardViewDataAdapter exploreAdapter;
    private RecyclerView.LayoutManager exploreLayoutManager;

    private RecyclerView groupRecyclerView;
    private ChatGroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    private LinearLayout createEvent;

    //create event variables
    private View createEventSelectButton;
    private TextView cancelButton;
    private TextView publishButton;
    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;
    private int imageContextColor;
    private int textContextColor;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /** Provides the entry point to Google Play services.*/
    protected GoogleApiClient mGoogleApiClient;

    private EditText name;
    private EditText location;
    private EditText time;
    private EditText description;

    /** Store my location information*/
    protected Location mLastLocation;
    private double latitude, longitude;

    Event[] events = null;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        switch(mPage) {
            //create event
            case 0:
                Toolbar t = (Toolbar)(((AppCompatActivity)(getActivity())).findViewById(R.id.toolbar));
                t.inflateMenu(R.menu.menu_publish);
                t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        publish();
                        return true;
                    }
                });
                view = inflater.inflate(R.layout.create_event, container, false);
                verifyStoragePermissions(getActivity());

                name = (EditText)view.findViewById(R.id.create_event_name);
                location = (EditText)view.findViewById(R.id.create_event_location);
                time = (EditText)view.findViewById(R.id.create_event_time);
                description = (EditText)view.findViewById(R.id.create_event_description);

                //((TextView)(v.findViewById(R.id.create_event_description_copy))).setText("");
                buildGoogleApiClient();
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.disconnect();
                        }
                        mGoogleApiClient.connect();
                    }
                });

                time.requestFocus();
                time.setInputType(InputType.TYPE_NULL);
                time.setOnClickListener(new View.OnClickListener() {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    @Override
                    public void onClick(View v) {


                        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                mYear = selectedyear;
                                mMonth = selectedmonth;
                                mDay = selectedday;
                                time.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                            }
                        }, mYear, mMonth, mDay);
                        mDatePicker.show();
                    }
                });

                createEventSelectButton = (View)view.findViewById(R.id.create_event_select);
                createEventSelectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create intent to Open Image applications like Gallery, Google Photos
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                    }
                });
                break;

            //upcoming event
            case 1:
                view = inflater.inflate(R.layout.upcoming_event, container, false);

                mRecyclerView = (RecyclerView) view.findViewById(R.id.upcoming_event_list);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);

                if (Data.upComingEvents != null) {
                    events = Utility.getAllUpcomingEvent(Data.upComingEvents, getContext());

                    // specify an adapter
                    mAdapter = new CardViewDataAdapter(events, getContext());
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setmItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.v("LISTENER", "Position:" + position);
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            intent.putExtra(Constants.ALL_EVENTS, events);
                            intent.putExtra(Constants.EVENT_INDEX, position);//
                            startActivity(intent);
                        }
                    });
                }

                //setup refresh action
                swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
                // Setup refresh listener which triggers new data loading
                swipeContainer.setOnRefreshListener(this);
                // Configure the refreshing colors
                swipeContainer.setColorSchemeResources(
                        android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
                break;

            //chat room
            case 3:
                view = inflater.inflate(R.layout.group_chat, container, false);

                groupRecyclerView = (RecyclerView) view.findViewById(R.id.group_list);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView

                //mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                groupLayoutManager = new LinearLayoutManager(getActivity());
                groupRecyclerView.setLayoutManager(groupLayoutManager);
                ChatGroup[] chatGroups = new ChatGroup[4];
                chatGroups[0] = new ChatGroup("First Meeting", "Mountain View", 1);
                chatGroups[1] = new ChatGroup("Third Meeting", "San Francisco", 2);
                chatGroups[2] = new ChatGroup("Sixth Meeting", "New York", 2);
                chatGroups[3] = new ChatGroup("Eight Meeting", "Boston", 2);
                // specify an adapter (see also next example)
                groupAdapter = new ChatGroupAdapter(chatGroups);
                groupRecyclerView.setAdapter(groupAdapter);

                groupAdapter.setmItemClickListener(new OnItemClickListener() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getActivity(), position, Toast.LENGTH_LONG);
                        Log.v("LISTENER", "Position:" + position);
                        Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                        startActivity(intent);
                    }
                });

                break;

            //explore event
            case 2:
                view = inflater.inflate(R.layout.explore, container, false);

                exploreRecyclerView = (RecyclerView) view.findViewById(R.id.exploreList);
                // use a linear layout manager
                exploreLayoutManager = new LinearLayoutManager(getActivity());
                exploreRecyclerView.setLayoutManager(exploreLayoutManager);

                if (Data.exploreEvents != null) {
                    final Event[] exploreEvents = Utility.getAllUpcomingEvent(Data.exploreEvents, getContext());
                    // specify an adapter (see also next example)
                    exploreAdapter = new CardViewDataAdapter(exploreEvents, getContext());
                    exploreRecyclerView.setAdapter(exploreAdapter);

                    exploreAdapter.setmItemClickListener(new OnItemClickListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onItemClick(View view, int position) {
                            //create event detail page that can swipe to navigate
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            intent.putExtra(Constants.ALL_EVENTS, exploreEvents);
                            intent.putExtra(Constants.EVENT_INDEX, position);
                            startActivity(intent);
                        }
                    });
                }

                //setup refresh action
                swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
                // Setup refresh listener which triggers new data loading
                swipeContainer.setOnRefreshListener(this);
                // Configure the refreshing colors
                swipeContainer.setColorSchemeResources(
                        android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);

                break;
            default:
                break;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            Log.v("onResume", "update.........");
        }

    }

    @Override public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new HttpAsyncTask(mAdapter, events, getContext()).execute(RESTfulAPI.upcomingEventURL + Data.userEmail);
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put("lat", Data.lat);
                    eventObject.put("lon", Data.log);
                    eventObject.put("distance", Data.distance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new PostTask(RESTfulAPI.exploreEventURL, eventObject, "explore").execute();
                swipeContainer.setRefreshing(false);
            }
        }, 5000);
    }

    //create event need check permission
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //create event when user picked a image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                Log.v("image", imgDecodableString);
                Log.v("image", Utility.convertImgToStr(imgDecodableString));
                cursor.close();
                ImageView imgView = (ImageView)getView().findViewById(R.id.create_event_image);
                // Set the Image in ImageView after decoding the String
                // Get the directory for the user's public pictures directory.
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                imgView.setImageBitmap(bitmap);

                //collapse color
                Palette.from(bitmap).maximumColorCount(16).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // Get the "vibrant" color swatch based on the bitmap
                        Palette.Swatch vibrant = palette.getDarkVibrantSwatch();
                        if (vibrant != null) {
                            imageContextColor = vibrant.getRgb();
                            Log.v("imageContextColor", String.valueOf(imageContextColor));
                            textContextColor = vibrant.getTitleTextColor();
                            Log.v("textContextColor", String.valueOf(textContextColor));
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    //create event publish, need implement later
    public void publish() {
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("eventDateTime", time.getText().toString());
            eventObject.put("imgStr", Utility.convertImgToStr(imgDecodableString));
            eventObject.put("imgFormat", "jpg");
            eventObject.put("creatorEmail", Data.userEmail);
            eventObject.put("name", name.getText().toString());
            eventObject.put("description", description.getText().toString());
            eventObject.put("lon", longitude);
            eventObject.put("lat", latitude);
            eventObject.put("imageContextColor", imageContextColor);
            eventObject.put("titleContextColor", textContextColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostTask(RESTfulAPI.creatEventURL, eventObject).execute();
    }


    /**
     * Get the current location
     */
    private void getLocation() {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = Double.parseDouble(String.valueOf(mLastLocation.getLatitude()));
            longitude = Double.parseDouble(String.valueOf(mLastLocation.getLongitude()));
            location.setText("Latitude: " + latitude + ", Longitude: " + longitude);
        } else {
            Toast.makeText(getActivity(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                // GET the current location
//                getLocation();
//            } else {
//                // Should we show an explanation?
//                if (shouldShowRequestPermissionRationale(
//                        Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    // Explain to the user why we need to read the contacts
//                    Toast.makeText(getActivity(), R.string.permission_rationale, Toast.LENGTH_LONG).show();
//                }
//
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        REQUEST_ACCESS_FINE_LOCATION);
//            }
//        else {
//            getLocation();
//        }
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}
