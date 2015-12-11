package edu.cmu.juicymeeting.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.juicymeeting.OnItemClickListener;
import edu.cmu.juicymeeting.juicymeeting.R;

/**
 * Created by chenlinquan on 11/18/15.
 */
public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder>  {

    public Event[] eventSet;
    public OnItemClickListener mItemClickListener;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewDataAdapter(Event[] eventSet) {
        this.eventSet = eventSet;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewDataAdapter(Event[] eventSet, Context context) {
        this.eventSet = eventSet;
        this.context = context;
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

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // get data from your itemsData at this position
//        holder.eventListCardImage.setImageBitmap(
//                decodeSampledBitmapFromResource(context.getResources(), R.drawable.coffee, 300, 300));
        Picasso.with(context).load(eventSet[position].getEventImage()).resize(500, 500)
                .centerCrop().into(holder.eventListCardImage);

        holder.eventListCardName.setText(eventSet[position].getEventName());
        holder.eventListCardDescription.setText(eventSet[position].getDescription());

        Picasso.with(context).load(eventSet[position].getCreatorImage()).resize(100, 100)
                .centerCrop().into(holder.eventListCardPortrait);
        holder.eventListCardCreatorName.setText(eventSet[position].getCreatorName());
        holder.eventListCardFollowInfo.setText("Followed by "
                + String.valueOf(eventSet[position].getFollowers()) + " pll");
        holder.eventListCardLocation.setText(eventSet[position].getLocation());
        holder.eventListCardDate.setText(eventSet[position].getDate());

        //set context color
        Log.w("color", ""+eventSet[position].getImageContextColor());
        int imageContextColor = Color.parseColor(String.format("#%06X", (0xFFFFFF & (int)(eventSet[position].getImageContextColor()))));
        int textContextColor = Color.parseColor(String.format("#%06X", (0xFFFFFF & (int)(eventSet[position].getTitleContextColor()))));
        holder.eventListCardWrapper.setBackgroundColor(imageContextColor);
        holder.eventListCardName.setTextColor(textContextColor);
        holder.eventListCardCreatorName.setTextColor(textContextColor);
        holder.eventListCardFollowInfo.setTextColor(textContextColor);
        holder.eventListCardLocation.setTextColor(textContextColor);
        holder.eventListCardDate.setTextColor(textContextColor);
    }

    @Override
    public int getItemCount() {
        return eventSet.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView eventListCardImage;
        public TextView eventListCardName;
        public TextView eventListCardDescription;
        public ImageView eventListCardPortrait;
        public TextView eventListCardCreatorName;
        public TextView eventListCardFollowInfo;
        public TextView eventListCardLocation;
        public TextView eventListCardDate;
        public FrameLayout eventListCardWrapper;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            eventListCardImage = (ImageView)itemLayoutView.findViewById(R.id.event_list_card_image);
            eventListCardName = (TextView)itemLayoutView.findViewById(R.id.event_list_card_name);
            eventListCardDescription = (TextView)itemLayoutView.findViewById(R.id.event_list_card_description);
            eventListCardPortrait = (ImageView)itemLayoutView.findViewById(R.id.event_list_card_portrait);
            eventListCardCreatorName = (TextView)itemLayoutView.findViewById(R.id.event_list_card_user_name);
            eventListCardFollowInfo = (TextView)itemLayoutView.findViewById(R.id.event_list_card_follow_info);
            eventListCardLocation = (TextView)itemLayoutView.findViewById(R.id.event_list_card_location);
            eventListCardDate = (TextView)itemLayoutView.findViewById(R.id.event_list_card_time);
            eventListCardWrapper = (FrameLayout)itemLayoutView.findViewById(R.id.event_list_card_wrapper);

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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
