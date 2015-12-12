package edu.cmu.juicymeeting.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;

import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.cmu.juicymeeting.database.chatDB.DatabaseConnector;
import edu.cmu.juicymeeting.database.chatDB.Group;
import edu.cmu.juicymeeting.helper.ItemTouchHelperAdapter;
import edu.cmu.juicymeeting.helper.ItemTouchHelperViewHolder;
import edu.cmu.juicymeeting.juicymeeting.ChatroomActivity;
import edu.cmu.juicymeeting.juicymeeting.R;

/**
 * Recycle List Adpater to show chat groups
 * Created by qiuzhexin on 12/5/15.
 */
public class GroupRecyclerListAdapter extends RecyclerView.Adapter<GroupRecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {
    private static final String TAG = GroupRecyclerListAdapter.class.getSimpleName();

    private static final String[] STRINGS = new String[]{
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    private final List<String> mItems = new ArrayList<>();
    private final List<Group> mGroups;

    private Context mContext;

    public GroupRecyclerListAdapter(List<Group> groups, Context c) {
        mGroups = groups;
        mContext = c;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_group_list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
       // holder.textView.setText(mItems.get(position));
        // change miliseconds to date for presentation
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date(mGroups.get(position).createTime));
        holder.textView.setText(dateString);
        // store the group code for db reference
        holder.textView.setTag(String.valueOf(mGroups.get(position).groupCode));
    }

    @Override
    public void onItemDismiss(int position) {
        // get group info
        Group group = mGroups.get(position);
        int groupCode = group.groupCode;

        // remove the group from db
        mGroups.remove(position);
        notifyItemRemoved(position);
        DatabaseConnector connector = DatabaseConnector.getInstance(mContext);
        connector.deleteChatGroup(groupCode);
        // TODO: tell chat server to decrement member of the group
        DeleteChatGroupWSListener listener =
                new DeleteChatGroupWSListener(ChatroomActivity.usrName, "delete", String.valueOf(groupCode));
        WebSocketClient client = new WebSocketClient(
                URI.create(WsConfig.URL_WEBSOCKET + URLEncoder.encode(listener.getQueryParams())),
                listener, null);
        client.connect();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        String prev = mItems.remove(fromPosition);
//        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        Group prev = mGroups.remove(fromPosition);
        mGroups.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
//        return mItems.size();
        return mGroups.size();
    }

    // View Holder for each chat group item
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView imageView;
        public final Context viewHolderContext;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.chat_group_code);
            imageView = (ImageView) itemView.findViewById(R.id.chat_group_img);
            viewHolderContext = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(viewHolderContext, ChatroomActivity.class);
                    // put corresponding group code, action, isOlder
                    int groupCode = Integer.parseInt((String) textView.getTag());
                    i.putExtra(ChatroomActivity.CHATROOM_GROUP, String.valueOf(groupCode));
                    i.putExtra(ChatroomActivity.CHAT_ACTION, "join");
                    i.putExtra(ChatroomActivity.IS_OLD_USER, "false");
                    // change to chatroom
                    viewHolderContext.startActivity(i);
                    Toast.makeText(viewHolderContext, "Group " + textView.getTag() + " clicked",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
            Toast.makeText(viewHolderContext, "Group " + textView.getTag() + " selected",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

    // helper websocket class for deleting group records on chat server
    private class DeleteChatGroupWSListener implements WebSocketClient.Listener {
        private String queryParams;

        public DeleteChatGroupWSListener(String usrName, String action, String groupCode) {
            queryParams = String.format("name=%s&groupCode=%s&action=%s",
                    usrName, groupCode, action);
        }

        @Override
        public void onConnect() {
            Log.e(TAG, "Connected to chat server for delete group");
        }

        @Override
        public void onMessage(String message) {
            Log.e(TAG, "Get message: " + message);
        }

        @Override
        public void onMessage(byte[] data) {

        }

        @Override
        public void onError(Exception error) {
            Log.e(TAG, "Error! : " + error);
        }

        @Override
        public void onDisconnect(int code, String reason) {
            String message = String.format(Locale.US,
                    "Disconnected! Code: %d Reason: %s", code, reason);
            Log.e(TAG, message);
        }

        public String getQueryParams() {
            return queryParams;
        }
    }
}
