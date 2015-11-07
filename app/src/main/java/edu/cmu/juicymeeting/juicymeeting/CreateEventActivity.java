package edu.cmu.juicymeeting.juicymeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateEventActivity extends AppCompatActivity {
    private ImageButton confirmButton;
    private EditText toEditText;
    private EditText eventNameEditText;
    private EditText dateTimeEditText;
    private EditText locationEditText;
    private EditText notesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // just for viewing the correctness of the ui
        setContentView(R.layout.activity_create_event);

        // get all the resources for reading input
        confirmButton = (ImageButton) findViewById(R.id.confirm_btn);
        toEditText = (EditText) findViewById(R.id.toEditText);
        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        dateTimeEditText = (EditText) findViewById(R.id.dateTimeEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);

        // read the contents
        String toWho = toEditText.getText().toString();
        String eventName = eventNameEditText.getText().toString();
        String date = dateTimeEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String notes = notesEditText.getText().toString();

        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeView = new Intent(CreateEventActivity.this, MainPageActivity.class);
                startActivity(seeView);
            }
        });
    }
}
