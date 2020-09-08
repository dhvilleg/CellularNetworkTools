package com.arcotel.network.tools;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpJsonPost {

    private static  URL url;
    private static HttpURLConnection connection;
    public String responsePhrase;

    public String postJsonToServer(String url, String jsonInputString)
    {
        Log.d("postJsonToServer", "Registering device...");

        InputStream inputStream = null;
        int statusCode;
        String result = "";

        try {
            Log.d("infosend"," va a crear infosend");
            byte infoToSend[] = jsonInputString.getBytes("utf-8");

            Log.d("infosend"," crea infosend");

            /*1. create HttpClient*/
            HttpClient httpclient = new DefaultHttpClient();
            Log.d("httpclient","crea httpclient");

            /*2. make POST request to the given URL*/
            HttpPost httpPost = new HttpPost(url);

            Log.d("httpPost","crea httpPost");

            /*5. Build ByteArrayEntity*/
            //StringEntity se = new StringEntity(json);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(infoToSend);
            Log.d("byteArrayEntity","crea byteArrayEntity");

            /*6. Set httpPost Entity*/
            httpPost.setEntity(byteArrayEntity);
            //httpPost.setEntity(inputStreamEntity);


            /*7. Set some headers to inform server about the type of the content*/
            //httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            Log.d("httpPost.setHeader","crea  httpPost.setHeader");

            /*8. Execute POST request to the given URL*/
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("httpResponse","crea  httpResponse");

            /*9. receive response as inputStream*/
            statusCode = httpResponse.getStatusLine().getStatusCode();
            Log.d("statusCode","crea  statusCode");

            /*CONVERT INPUT STREAM TO STRING*/
            responsePhrase = EntityUtils.toString(httpResponse.getEntity());
            Log.v("Response", "RESPONSE" + responsePhrase);
            Log.d("Respuesta", httpResponse.toString());
            /*PARSE JSON RESPONSE*/

            if(statusCode!=404)
            {
                result = Integer.toString(statusCode);
                Log.v("StatusCode", "STATUS CODE: " + result);
            }
            else
            {
                result = Integer.toString(statusCode);
                //Log.v(TAG, "STATUS CODE: " + result);
            }
        }
        catch (Exception e)
        {
            Log.d("InputStream", "e es "+e);
        }
        return result;
    }




}
