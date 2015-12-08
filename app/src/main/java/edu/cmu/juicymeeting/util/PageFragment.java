package edu.cmu.juicymeeting.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import edu.cmu.juicymeeting.database.model.ChatGroup;
import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.CreateEventActivity;
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
            case 0: //create event page
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

                createEvent = (LinearLayout)view.findViewById(R.id.event_create);
                createEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                        startActivity(intent);
                    }
                });

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

            case 2:
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

            case 1:
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
}