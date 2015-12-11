package edu.cmu.juicymeeting.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import edu.cmu.juicymeeting.database.chatDB.Message;
import edu.cmu.juicymeeting.juicymeeting.R;

/**
 * List view for chatroom messages
 * Created by qiuzhexin on 12/7/15.
 */
public class MessageListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Message> mMessages;

    public MessageListAdapter(Context context, List<Message> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message m = mMessages.get(position);

        // get activity inflater
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // identify message owner
        if (m.isSelf == 1) {
            convertView = inflater.inflate(R.layout.message_right, null);
        } else {
            convertView = inflater.inflate(R.layout.message_left, null);
        }

        // get individual views in the view group
        TextView timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);
        TextView msgFrom = (TextView) convertView.findViewById(R.id.messageFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String time = formatter.format(m.createTime);

        timeStamp.setText(time);
        msgFrom.setText(m.owner);
        txtMsg.setText(m.message);

        return convertView;
    }
}
