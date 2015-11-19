package edu.cmu.juicymeeting.juicymeeting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cmu.juicymeeting.database.model.Event;

/**
 * Created by chenlinquan on 11/18/15.
 */
public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder>  {

    public Event[] eventSet;
    public OnItemClickListener mItemClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewDataAdapter(Event[] eventSet) {
        this.eventSet = eventSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.event_list_card, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get data from your itemsData at this position
        //holder.eventListCardImage.setImageResource(R.drawable.coffee_portrait);
        holder.eventListCardName.setText(eventSet[position].getEventName());
        holder.eventListCardLocation.setText(eventSet[position].getLocation());
        holder.eventListCardMonthYear.setText(eventSet[position].getDate());
        holder.eventListCardDay.setText("27");
    }

    @Override
    public int getItemCount() {
        return eventSet.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //public TextView tvtinfo_text;
        public ImageView eventListCardImage;
        public TextView eventListCardName;
        public TextView eventListCardLocation;
        public TextView eventListCardMonthYear;
        public TextView eventListCardDay;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            eventListCardImage = (ImageView)itemLayoutView.findViewById(R.id.event_list_card_image);
            eventListCardName = (TextView)itemLayoutView.findViewById(R.id.event_list_card_name);
            eventListCardLocation = (TextView)itemLayoutView.findViewById(R.id.event_list_card_location);
            eventListCardMonthYear = (TextView)itemLayoutView.findViewById(R.id.event_list_card_month_year);
            eventListCardDay = (TextView)itemLayoutView.findViewById(R.id.event_list_card_day);

            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public void setmItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }
}
