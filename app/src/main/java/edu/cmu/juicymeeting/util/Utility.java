package edu.cmu.juicymeeting.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.cmu.juicymeeting.database.model.Event;

/**
 * Created by chenlinquan on 12/7/15.
 */
public class Utility {

    public static String getLocation(double lat, double lng, Context context) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            return addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality();
        }
        else return null;
    }

    public static Event[] getAllUpcomingEvent(String result, Context context) {

        JSONArray jsonObj = null;
        try {
            jsonObj = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Event[] events = new Event[jsonObj.length()];
        for (int i = 0; i < jsonObj.length(); i++) {
            events[i] = new Event();
            try {
                JSONObject jsonEvent = jsonObj.getJSONObject(i);
                events[i].setEventImage(jsonEvent.getString("imgStr"));
                events[i].setEventName(jsonEvent.getString("name"));
                events[i].setDescription(jsonEvent.getString("description"));

                events[i].setCreatorImage(jsonEvent.getJSONObject("creator").getString("imgStr"));
                events[i].setCreatorName(jsonEvent.getJSONObject("creator").getString("name"));

                events[i].setFollowers(jsonEvent.getInt("followers"));
                events[i].setLocation(getLocation(jsonEvent.getDouble("lat"),
                        jsonEvent.getDouble("lon"), context));
                events[i].setDate(jsonEvent.getString("eventDateTime"));

                //events[i].setCreatorEmail();
                //events[i].setId();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }
}
