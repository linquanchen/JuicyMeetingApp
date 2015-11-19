package edu.cmu.juicymeeting.juicymeeting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

@SuppressWarnings("deprecation")
public class EventMainPageActivity extends AppCompatActivity {
    private EditText eventNameEditText;
    private EditText dateTimeEditText;
    private EditText locationEditText;
    private EditText notesEditText;

    public static final String EVENT_ID = "eventID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_event_main_page);
        setContentView(R.layout.e_list_card);

//        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
//        dateTimeEditText = (EditText) findViewById(R.id.dateTimeEditText);
//        locationEditText = (EditText) findViewById(R.id.locationEditText);
//        notesEditText = (EditText) findViewById(R.id.notesEditText);
//
//        eventNameEditText.setEnabled(false);
//        dateTimeEditText.setEnabled(false);
//        locationEditText.setEnabled(false);
//        notesEditText.setEnabled(false);
//
//        // flate tabs
//        TabHost tabHost = (TabHost)findViewById(R.id.eventMainTabHost);
//
//        tabHost.setup();
//
//        TabHost.TabSpec tabSpec = tabHost.newTabSpec("eventInfo");
//        tabSpec.setContent(R.id.mainEventInfoTab);
//        tabSpec.setIndicator("Info");
//        tabHost.addTab(tabSpec);
//
//        tabSpec = tabHost.newTabSpec("eventChat");
//        tabSpec.setContent(R.id.mainEventChatTab);
//        tabSpec.setIndicator("Chat");
//        tabHost.addTab(tabSpec);
    }
}
