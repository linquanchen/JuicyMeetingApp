package edu.cmu.juicymeeting.juicymeeting.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.R;
import edu.cmu.juicymeeting.util.Constants;
import edu.cmu.juicymeeting.util.Data;
import edu.cmu.juicymeeting.util.JuicyFont;
import edu.cmu.juicymeeting.util.PermissionUtils;
import edu.cmu.juicymeeting.ws.HttpPostTask;
import edu.cmu.juicymeeting.ws.RESTfulAPI;
import edu.cmu.juicymeeting.util.RImageView;

//import android.support.v7.graphics.Palette;


public class EventDetailFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    //public static final String ARG_OBJECT = "object";
    private static final String TAG = "EventDetailFragment";

    //necessary information to select event
    private Event event;
    //private AppCompatActivity activity;

    //used to show layout
    private RImageView userPortrait;
    private TextView userName;
    private FloatingActionButton joinLeave;
//    private TextView joinLeave;
    private View joinLeaveBackground;
    private ImageView image;
    private TextView location;
    private TextView date;
    private TextView description;
    private TextView title;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private View rootView;

    private boolean isJoin;


    public EventDetailFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.event_detail, container, false);
        }

        //toolbar
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //get necessary variable
        Bundle args = getArguments();
        //activity = (AppCompatActivity)args.getParcelable(Constants.ACTIVITY);
        event = (Event)args.getParcelable(Constants.EVENT);

        // Get the join status of every event
        isJoin = Data.isJoinMap.get(event.getId());

        //title
        toolbar.setTitle(event.getEventName());

        //insert event detail information into layout
        userPortrait = (RImageView)rootView.findViewById(R.id.event_detail_portrait);
        userName = (TextView)rootView.findViewById(R.id.event_detail_user_name);

        joinLeave = (FloatingActionButton)rootView.findViewById(R.id.event_detail_join_leave_switch);
        refreshJoinLeaveButtonIcon();

        image = (ImageView)rootView.findViewById(R.id.event_detail_image);
        location = (TextView)rootView.findViewById(R.id.event_detail_location);
        date = (TextView)rootView.findViewById(R.id.event_detail_date);
        description = (TextView)rootView.findViewById(R.id.event_detail_description);
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        //image
        Picasso.with(getContext()).load(event.getEventImage()).resize(800, 1000).centerCrop().into(image);
        collapsingToolbarLayout.setTitle(event.getEventName());
        location.setText(event.getLocation());
        date.setText(event.getDate());
        description.setText(event.getDescription());

        //set content scrim color and title context color
        int imageContextColor = (int)event.getImageContextColor();
        int textContextColor = (int)event.getTitleContextColor();
        collapsingToolbarLayout.setContentScrimColor(Color.argb(
                (0xFF000000 & imageContextColor) >> 24,
                (0x00FF0000 & imageContextColor) >> 16,
                (0x0000FF00 & imageContextColor) >> 8,
                (0x000000FF & imageContextColor)
        ));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.argb(
                (0xFF000000 & textContextColor) >> 24,
                (0x00FF0000 & textContextColor) >> 16,
                (0x0000FF00 & textContextColor) >> 8,
                (0x000000FF & textContextColor)
        ));


        Picasso.with(getContext()).load(event.getCreatorImage()).into(userPortrait);
        userName.setText(event.getCreatorName());

        JuicyFont.getInstance().setFont(description, JuicyFont.OPEN_SANS_REGULAR);

        //join / leave floating button
        joinLeave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                isJoin = !isJoin;
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put("userEmail", Data.userEmail);
                    eventObject.put("eventId", event.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isJoin) {
                    new HttpPostTask(RESTfulAPI.joinEventURL, eventObject).execute();
                    Snackbar.make(v, "You joined this meeting", Snackbar.LENGTH_LONG).show();
                    Data.isJoinMap.put(event.getId(), true );
                }
                else {
                    new HttpPostTask(RESTfulAPI.disjoinEventURL, eventObject).execute();
                    Snackbar.make(v, "You leaved this meeting", Snackbar.LENGTH_LONG).show();
                    Data.isJoinMap.put(event.getId(), false);
                    onDestroy();
                }
                refreshJoinLeaveButtonIcon();
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(this);
        return rootView;
    }
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        Log.v("mMap", map.toString());
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity)getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        //super.onResumeFragments();
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestory()");
//        //necessary for transition animation
//        getActivity().supportFinishAfterTransition();
    }

    @SuppressLint("NewApi")
    private void refreshJoinLeaveButtonIcon() {
        if(isJoin)
            joinLeave.setImageDrawable(getResources().getDrawable(R.drawable.minus, getContext().getTheme()));
        else
            joinLeave.setImageDrawable(getResources().getDrawable(R.drawable.plus, getContext().getTheme()));
//        AppCompatActivity activity = (AppCompatActivity)getActivity();
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics( dm );
//        int statusBarOffset = dm.heightPixels - joinLeaveBackground.getMeasuredHeight();
//
//        int originalPos[] = new int[2];
//        joinLeaveBackground.getLocationOnScreen( originalPos );
//
//        int xDest = dm.widthPixels/2;
//        xDest -= (joinLeaveBackground.getMeasuredWidth()/2);
//        int yDest = dm.heightPixels/2 - (joinLeaveBackground.getMeasuredHeight()/2) - statusBarOffset;
//
//        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
//        anim.setDuration(1000);
//        anim.setFillAfter( true );
//        joinLeaveBackground.startAnimation(anim);

    }
}