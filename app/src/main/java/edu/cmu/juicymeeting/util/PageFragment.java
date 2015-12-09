package edu.cmu.juicymeeting.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
                            // transition animation
//                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    getActivity(), view.findViewById(R.id.event_list_card_image), "event_list_card_image_transition");
                            //ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
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
                new HttpAsyncTask(mAdapter, events, getContext()).execute(RESTfulAPI.upcomingEventURL + "zxq@cmu.edu");
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
    private void publish(View v) {
        EditText name = (EditText)v.findViewById(R.id.create_event_name);
        EditText location = (EditText)v.findViewById(R.id.create_event_location);
        EditText time = (EditText)v.findViewById(R.id.create_event_time);
        EditText description = (EditText)v.findViewById(R.id.create_event_description);

        //((TextView)(v.findViewById(R.id.create_event_description_copy))).setText("");

        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("eventDateTime", "2015-11-27 11:25:51.0");
            eventObject.put("imgStr", Utility.convertImgToStr(imgDecodableString));
            eventObject.put("imgFormat", "jpg");
            eventObject.put("creatorEmail", Data.userEmail);
            eventObject.put("name", name.getText().toString());
            eventObject.put("description", description.getText().toString());
            eventObject.put("lon", -122.0819);
            eventObject.put("lat", 37.3894);
            eventObject.put("imageContextColor", imageContextColor);
            eventObject.put("titleContextColor", textContextColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostTask(RESTfulAPI.creatEventURL, eventObject).execute();
    }
}