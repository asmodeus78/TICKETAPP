package it.ticketclub.ticketapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TicketClub on 03/04/14.
 */
public class LoadTicket extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get tickets JSON
    //private static String url = "http://api.androidhive.info/contacts/";
    private static String url = "http://www.ticketclub.it/APP/gateway2.php?CMD=TK";

    // JSON Node names
    private static final String TAG_ID = "idTicket";
    private static final String TAG_CODICE = "codice";
    private static final String TAG_OGGETTO = "oggetto";
    private static final String TAG_TITOLO = "titoloSup";
    private static final String TAG_IMAGE = "image";

    // tickets JSONArray
    JSONArray tickets = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, Object>> ticketList;


    private class GetTickets extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoadTicket.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            BitmapUtils Bu = new BitmapUtils();

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
                        String photo = "http://www.ticketclub.it/TICKET_NEW/biglietti/" + c.getString(TAG_CODICE) + ".jpg";
                        Drawable d1 = null;
                        try {

                            InputStream is1 = (InputStream) new URL(photo).getContent();
                            d1 = Drawable.createFromStream(is1, "src name");

                            //Bitmap imgticket = Bu.loadBitmap(photo);

                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log.d("COLONNA",photo);

                        // tmp hashmap for single contact
                        HashMap<String, Object> ticket = new HashMap<String, Object>();

                        // adding each child node to HashMap key => value
                        ticket.put(TAG_ID, id);
                        ticket.put(TAG_CODICE, codice);
                        ticket.put(TAG_TITOLO, titolo);
                        ticket.put(TAG_OGGETTO, oggetto);
                        //ticket.put(TAG_IMAGE, Bu.loadBitmap(photo));
                        ticket.put(TAG_IMAGE, d1);

                        //BitmapFactory.decodeResource(context.)




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


            String[] from = { TAG_CODICE, TAG_TITOLO, TAG_OGGETTO, TAG_IMAGE };
            int[] views = { R.id.name, R.id.email, R.id.mobile, R.id.image };

            ListAdapter adapter = new SimpleAdapter(
                    LoadTicket.this,
                    ticketList,
                    R.layout.list_item,
                    from,
                    views);

            setListAdapter(adapter);


        }



    }
}
