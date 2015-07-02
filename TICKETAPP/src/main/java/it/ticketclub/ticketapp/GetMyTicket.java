package it.ticketclub.ticketapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import utility.ServiceHandler;

/**
 * Created by Gio on 04/07/2014.
 */
public class GetMyTicket extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    LinkedList list;
    View vista;

    public GetMyTicket(Context context) {
        this.list = list;
        this.vista = null;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Nuova Gestione Liste
        //LinkedList listx = new LinkedList();

        // Making a request to url and getting response

        //application = (Setup) getApplication();



        Setup application = Setup.getSetup();

        String url="";

        JSONArray tickets = null;

        if (application.getTkStatusLogin()=="1"){
            url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=MY_TICKET&email=" + application.getTkProfileEmail();
            Log.d("COLONNA", url);
        }else{
            url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=MY_TICKET&email=no";
        }

        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);



        MyDatabase db= MyDatabase.getInstance(mContext);
        db.open();  //apriamo il db

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                tickets = jsonObj.getJSONArray("MY_TICKET");

                // looping through All Tickets



                for (int i = 0; i < tickets.length(); i++) {
                    JSONObject c = tickets.getJSONObject(i);

                    int id = c.getInt("idTicketEmesso");
                    String idTicket = c.getString("idTicket");
                    String titoloSup = c.getString("titoloSup");
                    String codice = c.getString("codice");
                    String photo = c.getString("codice") + ".jpg";
                    String cWeb = c.getString("cWeb");
                    String usato = c.getString("flagUsato");
                    String feedback = c.getString("flagFeedback");
                    String dataDownload = c.getString("dataDownload");
                    //String dataScadenza = c.getString("dataScadenza");
                    String qta = c.getString("qta");


                    //listx.add(new Ticket(id,categoria,codice,titolo,titoloSup,photo,scaricati,mediaVoto));


                    db.insertMyTicket (id,idTicket,codice,titoloSup,cWeb,qta,usato,feedback,dataDownload);
                    Log.d("COLONNA","Inserito " + i);



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

        //goAhead();
        // Dismiss the progress dialog
        //if (pDialog.isShowing())
        //     pDialog.dismiss();

        // list = result;
    }
}