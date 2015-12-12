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
public class PostTask extends AsyncTask<Void, Void, JSONObject> {
    private static final String TAG = "Get Json";

    private String URL;
    private JSONObject jsonObjSend;

    public PostTask(String URL, JSONObject jsonObjSend) {
        this.URL = URL;
        this.jsonObjSend = jsonObjSend;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject jsonObjRecv = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPostRequest = new HttpPost(URL);

            StringEntity se = new StringEntity(jsonObjSend.toString());
            System.out.println(jsonObjSend.toString());
            // Set HTTP parameters
            httpPostRequest.setEntity(se);

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Read the content stream
                InputStream instream = entity.getContent();

                // convert content stream to a String
                String resultString= Utility.convertInputStreamToString(instream);
                instream.close();

                //resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

                // Raw DEBUG output of our received JSON object:
                Log.i(TAG,"<JSONObject>\n"+resultString+"\n</JSONObject>");

                jsonObjRecv = new JSONObject(resultString);


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObjRecv;
    }

    protected void onPostExecute(JSONObject result) {
    }

}
