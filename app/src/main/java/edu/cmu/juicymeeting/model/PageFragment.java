package edu.cmu.juicymeeting.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import edu.cmu.juicymeeting.database.model.ChatGroup;
import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.CardViewDataAdapter;
import edu.cmu.juicymeeting.juicymeeting.ChatGroupAdapter;
import edu.cmu.juicymeeting.juicymeeting.EventMainChatActivity;
import edu.cmu.juicymeeting.juicymeeting.EventMainPageActivity;
import edu.cmu.juicymeeting.juicymeeting.GroupChatActivity;
import edu.cmu.juicymeeting.juicymeeting.OnItemClickListener;
import edu.cmu.juicymeeting.juicymeeting.R;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private RecyclerView mRecyclerView;
    private CardViewDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView exploreRecyclerView;
    private RecyclerView.Adapter exploreAdapter;
    private RecyclerView.LayoutManager exploreLayoutManager;

    private RecyclerView groupRecyclerView;
    private ChatGroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

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
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView

                //mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);
                Event[] events = new Event[4];
                events[0] = new Event("First Meeting", "Mountain View", "07/2016");
                events[1] = new Event("Third Meeting", "San Francisco", "08/2016");
                events[2] = new Event("Sixth Meeting", "New York", "09/2016");
                events[3] = new Event("Eight Meeting", "Boston", "10/2016");
                // specify an adapter (see also next example)
                mAdapter = new CardViewDataAdapter(events);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setmItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.v("LISTENER", "Position:" + position);
                        Intent intent = new Intent(getActivity(), EventMainPageActivity.class);
                        startActivity(intent);
                    }
                });


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

                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getActivity(), position, Toast.LENGTH_LONG);
                        Log.v("LISTENER", "Position:" + position);
                        Intent intent = new Intent(getActivity(), EventMainPageActivity.class);
                        startActivity(intent);
                    }
                });

                break;
            case 1:
            default:
                view = inflater.inflate(R.layout.explore, container, false);
                exploreRecyclerView = (RecyclerView) view.findViewById(R.id.exploreList);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView

                //mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                exploreLayoutManager = new LinearLayoutManager(getActivity());
                exploreRecyclerView.setLayoutManager(exploreLayoutManager);
                Event[] exploreEvents = new Event[3];
                exploreEvents[0] = new Event("Third Meeting", "Mountain View", "07/2016");
                exploreEvents[1] = new Event("Four Meeting", "San Francisco", "08/2016");
                exploreEvents[2] = new Event("Nine Meeting", "New York", "09/2016");

                // specify an adapter (see also next example)
                exploreAdapter = new CardViewDataAdapter(exploreEvents);
                exploreRecyclerView.setAdapter(exploreAdapter);
                break;
        }

        return view;
    }
}