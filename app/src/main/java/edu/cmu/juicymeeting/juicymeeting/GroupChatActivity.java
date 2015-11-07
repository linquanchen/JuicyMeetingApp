package edu.cmu.juicymeeting.juicymeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class GroupChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        // just for viewing the ui
        ImageButton backBtn = (ImageButton) findViewById(R.id.group_chat_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeView = new Intent(GroupChatActivity.this, EventMainPageActivity.class);
                startActivity(seeView);
            }
        });
    }
}
