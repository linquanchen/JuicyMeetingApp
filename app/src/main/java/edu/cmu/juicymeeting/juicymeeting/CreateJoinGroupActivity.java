package edu.cmu.juicymeeting.juicymeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import edu.cmu.juicymeeting.database.chatDB.DatabaseConnector;

public class CreateJoinGroupActivity extends AppCompatActivity {
    private static final String TAG = CreateJoinGroupActivity.class.getSimpleName();
    private String chatAction;
    private EditText groupCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        // find the group code view
        groupCodeEditText = (EditText) findViewById(R.id.join_group_password);
        // get whether user wants to create/join an group chat
        chatAction = getIntent().getExtras().getString(ChatroomActivity.CHAT_ACTION);
        Log.e(TAG, "get chat action: " + chatAction);
    }

    // invoked when cancel button clicked
    public void cancel(View view) {
        finish();
    }

    // invoke when ok button clicked
    public void confirm(View view) {
        // pass group code and chat action to the chat room session
        Intent i = new Intent(this, ChatroomActivity.class);
        String groupCode = groupCodeEditText.getText().toString();
        Log.e(TAG, "get chat group code: " + groupCode);
        i.putExtra(ChatroomActivity.CHATROOM_GROUP, groupCode);
        i.putExtra(ChatroomActivity.CHAT_ACTION, chatAction);
        // determine whether is old user
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        String isOldUser;
        if (connector.getGroupByGroupCode(Integer.parseInt(groupCode)) != null)
            isOldUser = "true";
        else
            isOldUser = "false";
        i.putExtra(ChatroomActivity.IS_OLD_USER, isOldUser);
        startActivity(i);
    }


}
