package edu.cmu.juicymeeting.juicymeeting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cmu.juicymeeting.database.model.ChatGroup;

/**
 * Created by chenlinquan on 11/18/15.
 */
public class ChatGroupAdapter extends RecyclerView.Adapter<ChatGroupAdapter.ViewHolder>  {

    public ChatGroup[] chatGroups;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatGroupAdapter(ChatGroup[] chatGroups) {
        this.chatGroups = chatGroups;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.group_list_card, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get data from your itemsData at this position
        //holder.groupImage.setImageResource(R.drawable.coffee_portrait);
        holder.groupName.setText(chatGroups[position].getGroupName());
        holder.groupDescription.setText(chatGroups[position].getGroupDescription());

    }

    @Override
    public int getItemCount() {
        return chatGroups.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView groupImage;
        public TextView groupName;
        public TextView groupDescription;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            groupImage = (ImageView)itemLayoutView.findViewById(R.id.group_image);
            groupName = (TextView)itemLayoutView.findViewById(R.id.group_name);
            groupDescription = (TextView)itemLayoutView.findViewById(R.id.group_description);
        }
    }
}

