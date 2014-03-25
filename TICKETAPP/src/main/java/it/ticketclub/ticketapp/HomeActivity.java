package it.ticketclub.ticketapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;



import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import org.json.JSONObject;
import org.json.JSONException;

import org.json.JSONArray;

import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;


//http://developer.android.com/reference/android/util/JsonReader.html
//http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android?answertab=active#tab-top

public class HomeActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get tickets JSON
    //private static String url = "http://api.androidhive.info/contacts/";
    private static String url = "http://www.ticketclub.it/APP/gateway2.php?CMD=TK";

    // JSON Node names
    private static final String TAG_LISTA = "TICKET";
    private static final String TAG_ID = "idTicket";
    private static final String TAG_CODICE = "codice";
    private static final String TAG_OGGETTO = "oggetto";
    private static final String TAG_TITOLO = "titoloSup";
    private static final String TAG_IMAGE = "image";

    /*private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";*/

    // tickets JSONArray
    JSONArray tickets = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, Object>> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ticketList = new ArrayList<HashMap<String, Object>>();

        //ListView lv = getListView();

        // Calling async task to get json
        new GetTickets().execute();

    }



    private class GetTickets extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    tickets = jsonObj.getJSONArray("TICKET");

                    // looping through All Tickets
                    for (int i = 0; i < tickets.length(); i++) {
                        JSONObject c = tickets.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String codice = c.getString(TAG_CODICE);
                        String titolo = c.getString(TAG_TITOLO);
                        String oggetto = c.getString(TAG_OGGETTO);

                        Drawable imgticket = null;

                        String photo = "http://www.ticketclub.it/TICKET_NEW/biglietti/" + c.getString(TAG_CODICE) + ".jpg";

                        try {
                            InputStream in = (InputStream) new URL(photo).getContent();
                            imgticket = Drawable.createFromStream(in,codice);
                            //InputStream in = new java.net.URL(photo).openStream();
                            //imgticket = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }



                        // tmp hashmap for single contact
                        HashMap<String, Object> ticket = new HashMap<String, Object>();

                        // adding each child node to HashMap key => value
                        ticket.put(TAG_ID, id);
                        ticket.put(TAG_CODICE, codice);
                        ticket.put(TAG_TITOLO, titolo);
                        ticket.put(TAG_OGGETTO, oggetto);
                        ticket.put(TAG_IMAGE, imgticket);

                        // adding contact to contact list
                        ticketList.add(ticket);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */



             ListAdapter adapter = new SimpleAdapter(
                    HomeActivity.this, ticketList,
                    R.layout.list_item, new String[] { TAG_CODICE, TAG_TITOLO,
                    TAG_OGGETTO, TAG_IMAGE }, new int[] { R.id.name,
                    R.id.email, R.id.mobile, R.id.image });



            setListAdapter(adapter);


        }

    }


}
