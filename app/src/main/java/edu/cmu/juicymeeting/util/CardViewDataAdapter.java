package edu.cmu.juicymeeting.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get data from your itemsData at this position
        holder.eventListCardImage.setImageBitmap(
                decodeSampledBitmapFromResource(context.getResources(), R.drawable.coffee, 300, 300));
//        holder.eventListCardImage.setImageBitmap(R.drawable.coffee_portrait);
                holder.eventListCardName.setText(eventSet[position].getEventName());
        holder.eventListCardLocation.setText(eventSet[position].getLocation());
        holder.eventListCardMonthYear.setText(eventSet[position].getDate());
        //holder.eventListCardDay.setText("27");
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
            eventListCardName = (TextView)itemLayoutView.findViewById(R.id.event_list_card_user_name);
            eventListCardLocation = (TextView)itemLayoutView.findViewById(R.id.event_list_card_location);
            eventListCardMonthYear = (TextView)itemLayoutView.findViewById(R.id.event_list_card_time);
            //eventListCardDay = (TextView)itemLayoutView.findViewById(R.id.event_list_card_day);

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
