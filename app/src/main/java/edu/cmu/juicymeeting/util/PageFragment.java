package edu.cmu.juicymeeting.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.juicymeeting.database.model.ChatGroup;
import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.EventDetailActivity;
import edu.cmu.juicymeeting.chat.GroupChatActivity;
import edu.cmu.juicymeeting.juicymeeting.OnItemClickListener;
import edu.cmu.juicymeeting.juicymeeting.R;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String ARG_PAGE = "ARG_PAGE";

    private static final int CREATE_GROUP_ACTIVITY = 0;
    private static final int JOIN_GROUP_ACTIVITY = 1;

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
                view = inflater.inflate(R.layout.create_event, container, false);
                verifyStoragePermissions(getActivity());
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

                publishButton = (TextView)view.findViewById(R.id.create_event_publish);
                publishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Successfully create event!", Toast.LENGTH_SHORT).show();
                        publish(getView());
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
                    final Event[] events = Utility.getAllUpcomingEvent(Data.upComingEvents, getContext());

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
                            // transition animation
//                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    getActivity(), view.findViewById(R.id.event_list_card_image), "event_list_card_image_transition");
                            //ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                            startActivity(intent);
                        }
                    });
                }

//                createEvent = (LinearLayout)view.findViewById(R.id.event_create);
//                createEvent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), CreateEventActivity.class);
//                        startActivity(intent);
//                    }
//                });

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
            default:
                view = inflater.inflate(R.layout.explore, container, false);
                exploreRecyclerView = (RecyclerView) view.findViewById(R.id.exploreList);

                // use a linear layout manager
                exploreLayoutManager = new LinearLayoutManager(getActivity());
                exploreRecyclerView.setLayoutManager(exploreLayoutManager);
//                final Event[] exploreEvents = new Event[6];
//                exploreEvents[0] = new Event("Third Meeting", "Mountain View", "2015/07/10");
//                exploreEvents[1] = new Event("Four Meeting", "San Francisco", "2015/08/15");
//                exploreEvents[2] = new Event("Nine Meeting", "New York", "2015/08/22");
//                exploreEvents[3] = new Event("Third Meeting", "Mountain View", "2015/09/12");
//                exploreEvents[4] = new Event("Four Meeting", "San Francisco", "2015/12/12");
//                exploreEvents[5] = new Event("Nine Meeting", "New York", "2016/01/12");
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
                            //                        //transition animation
                            //                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            //                                getActivity(), view.findViewById(R.id.event_list_card_image), "event_list_card_image_transition");
                            //                        getActivity().startActivity(intent, options.toBundle());
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
        }

        return view;
    }

    @Override public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new HttpAsyncTask().execute(RESTfulAPI.upcomingEventURL + "zxq@cmu.edu");
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
                            textContextColor = vibrant.getTitleTextColor();
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
    private void publish(View v) {
        ((EditText)(v.findViewById(R.id.create_event_name))).setText("");
        ((EditText)(v.findViewById(R.id.create_event_description))).setText("");
        ((TextView)(v.findViewById(R.id.create_event_description_copy))).setText("");
        ((EditText)(v.findViewById(R.id.create_event_location))).setText("");
        ((EditText)(v.findViewById(R.id.create_event_time))).setText("");
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("eventDateTime", "2015-11-27 11:25:51.0");
            eventObject.put("imgStr", "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAG9AZADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD5/ooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiig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r/xS8NaHvjW5N7cKP9Xb8jPu3SvKfEXxe17WFaGyK6dbHjER+cj3avaweQYvEatcq8zCeIhE9o8R+N9D8MRMb67V7gdLeI7pD+Hb8a8S8W/FPWfEZe2tWNjYHjy42+Zx/tN/hXCzTSTyGSV2d2OSzHJNRV9ll+R4bCe9bml3Zw1MRKemwrEnknJptLSV7JzhRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFTwzy28iyQyNG6nhlOCKgpaLJ7grnb6P8AFTxTpJVTe/a4RxsuRu/Xr+td1pnx0s5Nq6npUkR7vA+R+Rrw6ivOxGU4PEazgr+WhtGvOOzPqDT/AIneEr8LjUxAzfwzoVrorXWNMvRm11C1lGP4ZRXx7mnpLJGcpIyn2OK8irwrhpawk0bLGSW6PstcOPlIYf7JzS4I7V8hQa/q9tjydTu4x6LMw/rV+Lxz4nhxs1q7H1fP864Z8JS+zU/AtYxdUfV2DRg+lfLI+I3i4dNcuf8Ax3/CmN8QfFjjDa5dY9iB/So/1Sq/8/EV9dj2Pqnaf7pqOSaGIEyTRIB13OBXydN4t8QXGfN1i9P/AG2YfyrOm1C8uDma7nk/3pCa1p8J/wA9T8CXjeyPqq98Y+HNPB+1azaKR1CvuP6Vy+p/GXwzZBltftF446bF2j8zXzrkk8kmkr0KPDGEhrNuRnLFzex6vqvxw1W4UppljBaA9Hf52/wrgdX8U63rrE6lqM847IWwv5DisYdaU17OHwGGw/8ADgkYSqyluxD1pKD1orrMwooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=");
            eventObject.put("imgFormat", "jpg");
            eventObject.put("creatorEmail", Data.userEmail);
            eventObject.put("name", "ShenDeng");
            eventObject.put("description", "I am about to start an event");
            eventObject.put("lon", 37.3894);
            eventObject.put("lat", -122.0819);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new GetJsonTask(RESTfulAPI.creatEventURL, eventObject).execute();
    }
}