package edu.cmu.juicymeeting.util;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by chenlinquan on 12/8/15.
 */
public class PostTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "Get Json";

    private String URL;
    private JSONObject jsonObjSend;
    private String postType = null;

    public PostTask(String URL, JSONObject jsonObjSend) {
        this.URL = URL;
        this.jsonObjSend = jsonObjSend;
    }

    public PostTask(String URL, JSONObject jsonObjSend, String postType) {
        this.URL = URL;
        this.jsonObjSend = jsonObjSend;
        this.postType = postType;
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultString = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPostRequest = new HttpPost(URL);

            StringEntity se = new StringEntity(jsonObjSend.toString());
            System.out.println(jsonObjSend.toString());
            // Set HTTP parameters
            httpPostRequest.setEntity(se);
//           httpPostRequest.setHeader("Accept", "text");
//            httpPostRequest.setHeader("Content-type", "text");

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Read the content stream
                InputStream instream = entity.getContent();

                // convert content stream to a String
                resultString = Utility.convertInputStreamToString(instream);

                instream.close();

                // Raw DEBUG output of our received JSON object:
                Log.i(TAG,"<JSONObject>\n"+resultString+"\n</JSONObject>");
                //jsonObjRecv = new JSONObject(resultString);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    protected void onPostExecute(String result) {
        if (postType != null && postType.equals("explore")) {
            Data.exploreEvents = result;
        }
    }

}
