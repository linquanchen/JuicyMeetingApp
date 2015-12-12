package edu.cmu.juicymeeting.juicymeeting;

import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.cmu.juicymeeting.database.model.Event;
import edu.cmu.juicymeeting.util.Constants;
import edu.cmu.juicymeeting.util.ZoomOutPageTransformer;

public class EventDetailActivity extends AppCompatActivity {
    EventDetailPageAdapter eventDetailPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_wrapper);

        //get all events and current event position
        Bundle extras = getIntent().getExtras();
        Parcelable[] parcelables = extras.getParcelableArray(Constants.ALL_EVENTS);
        Event[] events = new Event[parcelables.length];
        for(int i = 0; i < events.length; i++) {
            events[i] = (Event)parcelables[i];
        }
        int position = extras.getInt(Constants.EVENT_INDEX);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        eventDetailPagerAdapter = new EventDetailPageAdapter(getSupportFragmentManager(), events, this);
        mViewPager = (ViewPager) findViewById(R.id.event_detail_wrapper);
        mViewPager.setAdapter(eventDetailPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setCurrentItem(position);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            //necessary for transition animation
////            case android.R.id.home:
////                supportFinishAfterTransition();
////                return true;
//            case R.id.action_settings:
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //necessary for transition animation
        //this.supportFinishAfterTransition();
    }
}
