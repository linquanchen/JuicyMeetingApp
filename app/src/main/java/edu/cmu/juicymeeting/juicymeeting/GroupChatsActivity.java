package edu.cmu.juicymeeting.juicymeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.Date;
import java.util.List;

import edu.cmu.juicymeeting.database.chatDB.DatabaseConnector;
import edu.cmu.juicymeeting.database.chatDB.Group;
import edu.cmu.juicymeeting.database.chatDB.Message;
import edu.cmu.juicymeeting.helper.SimpleItemTouchHelperCallback;
import edu.cmu.juicymeeting.util.GroupRecyclerListAdapter;

public class GroupChatsActivity extends AppCompatActivity {
    private final String TAG = GroupChatsActivity.class.getSimpleName();
    private GroupRecyclerListAdapter mGroupRecyclerListAdapter;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private List<Group> mGroups;

    // invoked when join button clicked
    public  void joinGroup(View vie) {
        Intent i = new Intent(this, JoinGroupActivity.class);
        i.putExtra(ChatroomActivity.CHAT_ACTION, "join");
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_chat);

//        // insert some dummy data in database
//        insertDummyData();
        // query groups information
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        mGroups = connector.getAllGroupsOrderByCreateTime();
        // update the adapter for group list view
        mGroupRecyclerListAdapter = new GroupRecyclerListAdapter(mGroups, this);

        // configure the recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mGroupRecyclerListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // attach callbacks to recycler view for movement
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mGroupRecyclerListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    // invoked when createGroup button clicked
    public void createGroup(View view) {
        Intent i = new Intent(this, JoinGroupActivity.class);
        i.putExtra(ChatroomActivity.CHAT_ACTION, "create");
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "GroupChats onResume called");
        // refresh the chat group list
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        mGroups.clear();
        mGroups.addAll(connector.getAllGroupsOrderByCreateTime());
        mGroupRecyclerListAdapter.notifyDataSetChanged();
    }

    // TEST SECTION
    // insert some dummy data (group, message)
    private void insertDummyData() {
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        // delete all records
        connector.deleteAllRecords();
        // insert groups and messages
        for (int i = 0; i < 3; i++) {
            Group g = new Group(i, new Date().getTime());
            connector.addGroup(g);
            for (int j = 0; j < 4; j++) {
//                try {
//                    Thread.sleep(1000);                 //1000 milliseconds is one second.
//                } catch(InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
                Message m = new Message(g, "test message", "test owner", new Date().getTime(), j % 2);
                connector.addMessage(m);
            }
        }
    }

    private static final String[] STRINGS = new String[]{
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    private static final Group[] GROUPS = new Group[] {
            new Group(1, 1), new Group(2, 2), new Group(3, 3)
    };

    // insert sample data, and simple tests
    private void insertDataAndTest() {
        // TEST BEGIN
        // sample data for testing db
        Group group1 = new Group();
        group1.groupCode = 1;
        group1.createTime = new Date().getTime();
        Group group2 = new Group();
        group2.groupCode = 2;
        group2.createTime = new Date().getTime();

        Message message1 = new Message();
        message1.createTime = new Date().getTime();
        message1.owner = "zhexinq";
        message1.message = "Juicy Meeting is awesome";
        message1.group = group1;
        Message message2 = new Message();
        message2.createTime = new Date().getTime();
        message2.owner = "zhexinq";
        message2.message = "Agreed";
        message2.group = group1;

        // Get singleton instance of database
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        // Add a group
        connector.addGroup(group1);
        connector.addGroup(group2);
        // Add a message
        connector.addMessage(message1);
        connector.addMessage(message2);
        // query groups
        List<Group> groups = connector.getAllGroupsOrderByCreateTime();
        for (Group g : groups) {
            Log.d(TAG, "group: " + g.groupCode + " " + g.createTime);
        }
        // query message by group
        List<Message> messages = connector.getAllMessagesByGroupOrderByTime(group1.groupCode);
        for (Message m : messages) {
            Log.d(TAG, "message: " + m.message + " " + m.createTime);
        }
        // delete group
        connector.deleteChatGroup(group2.groupCode);
        groups = connector.getAllGroupsOrderByCreateTime();
        for (Group g : groups) {
            Log.d(TAG, "group: " + g.groupCode + " " + g.createTime);
        }
        // TEST END
    }

}
