package edu.cmu.juicymeeting.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.cmu.juicymeeting.database.model.Event;

/**
 * Created by chenlinquan on 12/7/15.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    private CardViewDataAdapter mAdapter;
    private Event[] events;
    private  Context context;

    public HttpAsyncTask() {

    }

    public HttpAsyncTask(CardViewDataAdapter mAdapter, Event[] events, Context context) {
        this.mAdapter = mAdapter;
        this.events = events;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        return GET(params[0]);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Data.upComingEvents = result;
        Data.exploreEvents = result;
        if (mAdapter != null) {
            events = Utility.getAllUpcomingEvent(Data.upComingEvents, context);
            mAdapter.notifyDataSetChanged();
            Log.v("mAdapter", "update.........");
        }
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = Utility.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        System.out.println("Get result: " + result);
        return result;
    }
}
