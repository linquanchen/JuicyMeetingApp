package edu.cmu.juicymeeting.juicymeeting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class CreateEventActivity extends AppCompatActivity {
    private ImageButton confirmButton;
    private EditText toEditText;
    private EditText eventNameEditText;
    private EditText dateTimeEditText;
    private EditText locationEditText;
    private EditText notesEditText;

    private ImageView createEventsButton;
    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.create_event_image);
                // Set the Image in ImageView after decoding the String
                // Get the directory for the user's public pictures directory.
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // just for viewing the correctness of the ui

        //setContentView(R.layout.activity_create_event);
        setContentView(R.layout.create_event);

        verifyStoragePermissions(this);

        createEventsButton = (ImageView)findViewById(R.id.create_event_image);
        createEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

//        // get all the resources for reading input
//        confirmButton = (ImageButton) findViewById(R.id.confirm_btn);
//        toEditText = (EditText) findViewById(R.id.toEditText);
//        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
//        dateTimeEditText = (EditText) findViewById(R.id.dateTimeEditText);
//        locationEditText = (EditText) findViewById(R.id.locationEditText);
//        notesEditText = (EditText) findViewById(R.id.notesEditText);
//
//        // read the contents
//        String toWho = toEditText.getText().toString();
//        String eventName = eventNameEditText.getText().toString();
//        String date = dateTimeEditText.getText().toString();
//        String location = locationEditText.getText().toString();
//        String notes = notesEditText.getText().toString();
//
//        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent seeView = new Intent(CreateEventActivity.this, MainPageActivity.class);
//                startActivity(seeView);
//            }
//        });
    }
}
