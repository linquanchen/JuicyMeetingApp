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
        System.out.println("lat : " + lat + ", lng: " + lng);
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            System.out.println(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality());
            return addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality();
        }
        else return null;
    }

    public static Event[] getAllUpcomingEvent(String result, Context context) {

       // JSONArray jsonObj = ParserJson.getJSONFromURL(RESTfulAPI.upcomingEventURL + email);
        JSONArray jsonObj = null;
        try {
            jsonObj = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Event[] events = new Event[jsonObj.length()];
        System.out.println(events.length);
        for (int i = 0; i < jsonObj.length(); i++) {
            events[i] = new Event();
            try {
                JSONObject jsonEvent = jsonObj.getJSONObject(i);
                events[i].setDate(jsonEvent.getString("eventDateTime"));
                //events[i].setImg();
                //events[i].setFollowers();
                //events[i].setCreatorEmail();
                events[i].setEventName(jsonEvent.getString("name"));
                events[i].setDescription(jsonEvent.getString("description"));
                //events[i].setLon();
                //events[i].setLat();
                //events[i].setId();
                events[i].setLocation(getLocation(jsonEvent.getDouble("lat"),
                        jsonEvent.getDouble("lon"), context));
                System.out.println(events[i].getLocation());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }
}
