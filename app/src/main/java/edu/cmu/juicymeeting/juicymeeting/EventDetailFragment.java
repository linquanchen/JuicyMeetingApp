package edu.cmu.juicymeeting.juicymeeting;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.util.Constants;
import edu.cmu.juicymeeting.util.JuicyFont;
import edu.cmu.juicymeeting.util.PermissionUtils;
import edu.cmu.juicymeeting.util.RImageView;


public class EventDetailFragment extends Fragment implements
        //GoogleMap.OnMyLocationButtonClickListener,
        //OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{
    //public static final String ARG_OBJECT = "object";

    //necessary information to select event
    private Event event;
    //private AppCompatActivity activity;

    //used to show layout
    private RImageView userPortrait;
    private TextView userName;
    private TextView joinLeave;
    private ImageView image;
    private TextView name;
    private TextView location;
    private TextView date;
    private TextView description;

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

    //private GoogleMap mMap;

    private View rootView;

    public EventDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.event_detail, container, false);
        }
        //get necessary variable
        Bundle args = getArguments();
        //activity = (AppCompatActivity)args.getParcelable(Constants.ACTIVITY);
        event = (Event)args.getParcelable(Constants.EVENT);


        //insert event detail information into layout
        userPortrait = (RImageView)rootView.findViewById(R.id.event_detail_portrait);
        userName = (TextView)rootView.findViewById(R.id.event_detail_user_name);

        joinLeave = (TextView)rootView.findViewById(R.id.event_detail_join_leave_switch);

        image = (ImageView)rootView.findViewById(R.id.event_detail_image);
        name = (TextView)rootView.findViewById(R.id.event_detail_event_name);
        location = (TextView)rootView.findViewById(R.id.event_detail_location);
        date = (TextView)rootView.findViewById(R.id.event_detail_date);
        description = (TextView)rootView.findViewById(R.id.event_detail_description);

        //image
        name.setText(event.getEventName());
        JuicyFont.getInstance().setFont(name, JuicyFont.OPEN_SANS_REGULAR);
        location.setText(event.getLocation());
        date.setText(event.getDate());
        description.setText(event.getDescription());
        JuicyFont.getInstance().setFont(description, JuicyFont.OPEN_SANS_REGULAR);

        joinLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        // Map
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.event_detail_map);
//        mapFragment.getMapAsync(this);


        return rootView;
    }
//    @Override
//    public void onMapReady(GoogleMap map) {
//        mMap = map;
//
//        mMap.setOnMyLocationButtonClickListener(this);
//        enableMyLocation();
//    }

//    /**
//     * Enables the My Location layer if the fine location permission has been granted.
//     */
//    private void enableMyLocation() {
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission to access the location is missing.
//            //need AppCompatActivity but could only get FragmentActivity here, need to fix later
////            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
////                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        } else if (mMap != null) {
//            // Access to the location has been granted to the app.
//            mMap.setMyLocationEnabled(true);
//        }
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
//        // Return false so that we don't consume the event and the default behavior still occurs
//        // (the camera animates to the user's current position).
//        return false;
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
//            return;
//        }
//
//        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
//                Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Enable the my location layer if the permission has been granted.
//            enableMyLocation();
//        } else {
//            // Display the missing permission error dialog when the fragments resume.
//            mPermissionDenied = true;
//        }
//    }

//    @Override
//    protected void onResumeFragments() {
//        //super.onResumeFragments();
//        if (mPermissionDenied) {
//            // Permission was not granted, display error dialog.
//            showMissingPermissionError();
//            mPermissionDenied = false;
//        }
//    }
//
//    /**
//     * Displays a dialog with error message explaining that the location permission is missing.
//     */
//    private void showMissingPermissionError() {
//        PermissionUtils.PermissionDeniedDialog
//                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        //necessary for transition animation
//        getActivity().supportFinishAfterTransition();
    }
}