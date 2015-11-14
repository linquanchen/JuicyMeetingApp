package edu.cmu.juicymeeting.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.juicymeeting.juicymeeting.EventMainChatActivity;
import edu.cmu.juicymeeting.juicymeeting.EventMainPageActivity;
import edu.cmu.juicymeeting.juicymeeting.GroupChatActivity;
import edu.cmu.juicymeeting.juicymeeting.R;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

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
                android.support.v7.widget.CardView cardViewEvent = (android.support.v7.widget.CardView)
                        view.findViewById(R.id.cardViewEvent);
                cardViewEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EventMainPageActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 2:
                view = inflater.inflate(R.layout.group_chat, container, false);
                android.support.v7.widget.CardView cardView = (android.support.v7.widget.CardView)
                        view.findViewById(R.id.cardView);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
            default:
                view = inflater.inflate(R.layout.explore, container, false);
                android.support.v7.widget.CardView cardViewEvent1 = (android.support.v7.widget.CardView)
                        view.findViewById(R.id.cardViewNearby);
                cardViewEvent1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EventMainPageActivity.class);
                        startActivity(intent);
                    }
                });

                break;
        }

        return view;
    }
}