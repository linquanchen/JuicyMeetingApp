package edu.cmu.juicymeeting.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.util.Log;



import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

                events[i].setId(jsonEvent.getInt("id"));
                events[i].setEventImage(RESTfulAPI.DNS + jsonEvent.getString("imgUrl"));
                events[i].setEventName(jsonEvent.getString("name"));
                events[i].setDescription(jsonEvent.getString("description"));

                events[i].setCreatorImage(RESTfulAPI.DNS + jsonEvent.getJSONObject("creator").getString("imgStr"));
                events[i].setCreatorName(jsonEvent.getJSONObject("creator").getString("name"));

                events[i].setFollowers(jsonEvent.getInt("followers"));
                events[i].setLocation(getLocation(jsonEvent.getDouble("lat"),
                        jsonEvent.getDouble("lon"), context));
                events[i].setDate(jsonEvent.getString("eventDateTime"));

                events[i].setTitleContextColor(jsonEvent.getLong("titleContextColor"));
                events[i].setImageContextColor(jsonEvent.getLong("imageContextColor"));
                //events[i].setCreatorEmail();



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }


    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }

    public static HttpResponse makeRequest(String path, JSONObject holder) throws Exception
    {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(path);

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        return (HttpResponse) httpclient.execute(httpost, responseHandler);
    }

    public static String convertImgToStr(String filePath) {
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;
    }
}
