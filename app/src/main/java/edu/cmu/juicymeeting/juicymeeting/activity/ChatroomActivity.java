package edu.cmu.juicymeeting.juicymeeting.activity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.cmu.juicymeeting.database.chatDB.DatabaseConnector;
import edu.cmu.juicymeeting.database.chatDB.Group;
import edu.cmu.juicymeeting.database.chatDB.Message;
import edu.cmu.juicymeeting.juicymeeting.R;
import edu.cmu.juicymeeting.util.ChatUtil;
import edu.cmu.juicymeeting.juicymeeting.adapter.MessageListAdapter;
import edu.cmu.juicymeeting.ws.WsConfig;

public class ChatroomActivity extends AppCompatActivity {
    public final static String TAG = ChatroomActivity.class.getSimpleName();
    public final static String CHATROOM_GROUP = "groupCode";
    public final static String CHAT_ACTION = "chatAction";
    public final static String IS_OLD_USER = "isOldUser";
    private ListView msgListView;
    private List<Message> mMessages;
    private MessageListAdapter mMessageListAdapter;
    private WebSocketClient client;
    // utils
    private ChatUtil utils;
    // flag to identify the kind of json response on the client side
    public static final String FLAG_SELF = "self",
            FLAG_NEW = "new",
            FLAG_MESSAGE = "message",
            FLAG_EXIT = "exit",
            FLAG_DELETE_GROUP = "delete";
    // TODO: user name should get from juicyBackend or stored as sharePreference
    public static final String usrName = "juicer";
    // chat room views
    private Button btnSend;
    private EditText inputMsg;
    // chat room param
    private String groupCode;
    private String action;
    private String isOldUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        // get corresponding group info
        Bundle extras = getIntent().getExtras();
        groupCode = extras.getString(CHATROOM_GROUP);
        action = extras.getString(CHAT_ACTION);
        isOldUser = extras.getString(IS_OLD_USER);
        Log.e(TAG, "isOldUser: " + isOldUser);
        Log.e(TAG, "groupCode: " + groupCode);

        // using corresponding group to query messages
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        mMessages = new ArrayList<>(connector.getAllMessagesByGroupOrderByTime(Integer.parseInt(groupCode)));

        // to debug
//        Toast.makeText(this, "get " + mMessages.size() + " messages", Toast.LENGTH_SHORT).show();

        // set up the adapter
        mMessageListAdapter = new MessageListAdapter(this, mMessages);

        // set up the list view
        msgListView = (ListView) findViewById(R.id.list_view_messages);
        msgListView.setAdapter(mMessageListAdapter);

        // find the input views
        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        // sending message when "send" pressed
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSend = utils.getSendMessageJSON(inputMsg.getText().toString(), groupCode);
                sendMessageToServer(toSend);
                inputMsg.setText("");
            }
        });

        // init utils
        utils = new ChatUtil(this);
        String queryParams = String.format("name=%s&groupCode=%s&action=%s&isOldUser=%s",
                usrName, groupCode, action, isOldUser);
        client = new WebSocketClient(URI.create(WsConfig.URL_WEBSOCKET
                + URLEncoder.encode(queryParams)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {}

            /**
             * On receiving the message from web socket server
             * */
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));

                parseMessage(message);
            }

            @Override
            public void onMessage(byte[] data) {}

            /**
             * Called when the connection is terminated
             * */
            @Override
            public void onDisconnect(int code, String reason) {

                String message = String.format(Locale.US,
                        "Disconnected! Code: %d Reason: %s", code, reason);
                Log.e(TAG, message);
//                showToast(message);

                // clear the session id from shared preferences
                utils.storeSessionId(null);
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error! : " + error);

//                showToast("Error! : " + error);
            }

        }, null);

        client.connect();
    }

    private void parseMessage(final String msg) {
        // reference to local db
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");
            int groupCode = Integer.parseInt(jObj.getString("groupCode"));
            String sessionId = jObj.getString("sessionId");
            Log.e(TAG, String.format("get flag: %s, group code: %d, session id: %s",
                    flag, groupCode, sessionId));

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(FLAG_SELF)) {
                // if reject disconnect
                String result = jObj.getString("message");
                if (result.equals("reject")) {
                    showToast("Group has been used or doesn't exists");
                    finish();
                }
                // if accept store group in db if not already
                else if (connector.getGroupByGroupCode(groupCode) == null) {
                    connector.addGroup(new Group(groupCode, new Date().getTime()));
                }

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);
                Log.e(TAG, "Your session id: " + utils.getSessionId());

            } else if (flag.equalsIgnoreCase(FLAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                showToast(name + message + ". Currently " + onlineCount
                        + " people online!");

            } else if (flag.equalsIgnoreCase(FLAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = usrName;
                String message = jObj.getString("message");

                int isSelf = 1;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = 0;
                }

                // get corresponding group and crete message pojo
                Group g = connector.getGroupByGroupCode(groupCode);
                Message m = new Message(g, message, fromName, new Date().getTime(), isSelf);

                // Appending the message to chat list
                appendMessage(m);

                // Save the message to database
                connector.addMessage(m);
            } else if (flag.equalsIgnoreCase(FLAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                showToast(name + message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to send message to web socket server
     * */
    private void sendMessageToServer(String message) {
        if (client != null && client.isConnected()) {
            client.send(message);
        }
    }

    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mMessages.add(m);

                mMessageListAdapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(client != null & client.isConnected()){
            client.disconnect();
        }
    }

    /**
     * Plays device's default notification sound
     * */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
