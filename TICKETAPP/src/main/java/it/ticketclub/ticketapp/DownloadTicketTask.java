package it.ticketclub.ticketapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gio on 22/04/2014.
 */
class DownloadTicketTask extends AsyncTask<String,Void,List> {
    ImageView bmImage;
    LinkedList list;

    // JSON Node names
    private static final String TAG_ID = "idTicket";
    private static final String TAG_CODICE = "codice";
    private static final String TAG_TITOLO = "titolo";
    private static final String TAG_TITOLO_SUP = "titoloSup";
    //private static final String TAG_TICKET_SCARICATI = "nTicket";
    //private static final String TAG_VOTO = "mediaVoti";

    // tickets JSONArray
    static JSONArray tickets = null;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        /*pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();*/
    }

    public DownloadTicketTask(LinkedList list) {
        this.list = list;
    }

    protected List doInBackground(String... urls) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        BitmapUtils Bu = new BitmapUtils();

        // Nuova Gestione Liste
        LinkedList listx = new LinkedList();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                tickets = jsonObj.getJSONArray("TICKET");

                // looping through All Tickets
                for (int i = 0; i < tickets.length(); i++) {
                    JSONObject c = tickets.getJSONObject(i);

                    Integer id = c.getInt(TAG_ID);
                    String titolo = c.getString(TAG_TITOLO);
                    String titoloSup = c.getString(TAG_TITOLO_SUP);
                    String photo = c.getString(TAG_CODICE) + ".jpg";

                    listx.add(new Ticket(id,titolo,titoloSup,photo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return listx;
    }

    protected void onPostExecute(LinkedList result) {
        list = result;
    }
}
