package it.ticketclub.ticketapp;

import android.app.Activity;
import android.os.Bundle;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.client.methods.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

//http://developer.android.com/reference/android/util/JsonReader.html
//http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android?answertab=active#tab-top

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost("http://someJSONUrl/jsonWebService");
        // Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            // Oops
        }
        finally {
            try{if(inputStream != null)inputStream.close();}
            catch(Exception squish){

            }
        }
    }


}
