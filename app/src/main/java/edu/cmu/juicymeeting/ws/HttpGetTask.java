package edu.cmu.juicymeeting.ws;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

import edu.cmu.juicymeeting.juicymeeting.adapter.EventAdapter;
import edu.cmu.juicymeeting.util.Data;
import edu.cmu.juicymeeting.util.Utility;

/**
 * Created by chenlinquan on 12/7/15.
 */
public class HttpGetTask extends AsyncTask<String, Void, String> {
    
    private EventAdapter adapter;
    private  Context context;

    public HttpGetTask(Context context) {
        this.context = context;
    }

    public HttpGetTask(EventAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        return GET(params[0]);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Data.upcomingEvents = Utility.getAllEvents(result, context, Data.UPCOMING_EVENTS);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            Log.v("adapter", "update.........");
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
