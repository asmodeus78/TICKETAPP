package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Gio on 30/05/2014.
 */
public class MyProfile extends Activity {

    public Setup application;

    private Button buttonLogout;
    private Button btMyTicket;
    private Button btDistributori;
    private TextView txtNominativo;
    private TextView txtCrediti;
    private ImageView imgUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("COLONNA","APRO IL PROFILO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);

        application = (Setup) this.getApplication();

        buttonLogout = (Button)findViewById(R.id.bt_logout);
        txtNominativo = (TextView)findViewById(R.id.TK_nominativo);
        txtCrediti = (TextView)findViewById(R.id.TK_crediti);
        imgUser = (ImageView)findViewById(R.id.TK_image_profile);

        btMyTicket = (Button)findViewById(R.id.btScaricati);
        btDistributori = (Button)findViewById(R.id.btDistributori);


        if (application.getTkStatusLogin()=="1"){

            //final Intent intent = new Intent(getApplication(),MapsActivity.class); // SWIPE E TAB + JSON NOT VIEW
            //startActivity(intent); // Launch the Intent
            //finish(); // We finish the current Activity

            txtNominativo.setText(application.getTkProfileName());
            txtCrediti.setText(application.getTkProfileCrediti() + " Crediti");

            //if (!application.getTkProfileEmail().contains("@facebook.com")) {
                new DownloadImageTask(imgUser).execute("https://graph.facebook.com/" + application.getTkProfileImageId() + "/picture");
            //}


            buttonLogout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });

            btMyTicket.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) { ApriMyTicket(); }
            });

            btDistributori.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) { ApriMappaDist(); }
            });



        }

    }

    public void ApriMappaDist(){
        final Intent intent = new Intent(this,MapsActivity.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriMyTicket(){
        final Intent intent;
        application = (Setup) this.getApplication();
        if (application.getTkStatusLogin()=="1"){
            new GetMyTickets().execute();
            intent = new Intent(getApplication(),MyTicket.class); // SWIPE E TAB + JSON NOT VIEW
            startActivity(intent); // Launch the Intent
        }else {
            intent = new Intent(this,MyLoginActivity.class); // SWIPE E TAB + JSON NOT VIEW
            startActivity(intent); // Launch the Intent
            finish();
        }


    }

    private void onClickLogout() {

        try {
            Session session = Session.getActiveSession();
            session.closeAndClearTokenInformation();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        application.setTkStatusLogin("0");
        finish();

    }



    private class GetMyTickets extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public GetMyTickets() {
            this.list = list;
            this.vista = null;
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

            application = (Setup) getApplication();
            String url="";

            JSONArray tickets = null;

            if (application.getTkStatusLogin()=="1"){

                url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=MY_TICKET&email=" + application.getTkProfileEmail();
                Log.d("COLONNA",url);

            }

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            MyDatabase db=new MyDatabase(getApplicationContext());
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


}
