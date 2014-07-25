package it.ticketclub.ticketapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class LasciaFeedback extends ActionBarActivity {

    private static ProgressDialog pDialog;

    public static String url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=INVIA_FEEDBACK";
    public static String idt = "";


    static JSONArray myFeedback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lascia_feedback);

        Bundle dati = getIntent().getExtras();
        final String idMyTicket = dati.getString("id");
        final String codice = dati.getString("codice");
        idt = idMyTicket;


        final TextView txtValutazione = (TextView) findViewById(R.id.txtValutazione);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView txtRecensione = (TextView) findViewById(R.id.txtRecensione);

        final Button btSendFeedback = (Button) findViewById(R.id.btSendFeedback);


        btSendFeedback.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

/*
                  if($comando=="INVIA_FEEDBACK"){
                      $codice = $_REQUEST['codice'];
                      $idutente = $_REQUEST['idutente'];
                      $voto = $_REQUEST['voto'];
                      $commento = $_REQUEST['commento'];*/

                  if (ratingBar.getRating()==0){

                      Toast toast = Toast.makeText(getApplicationContext(), "Seleziona le stelline", Toast.LENGTH_LONG);
                      toast.show();

                      return;

                  }

                  if (txtRecensione.getText().toString().contentEquals("")){

                      Toast toast = Toast.makeText(getApplicationContext(), "Descrivici la tua esperienza", Toast.LENGTH_LONG);
                      toast.show();


                      return;

                  }

                  if (txtRecensione.getText().toString().length()<30){
                      Toast toast = Toast.makeText(getApplicationContext(), "La descrizione deve essere di almeno 30 caratteri", Toast.LENGTH_LONG);
                      toast.show();

                      return;

                  }

                  pDialog = new ProgressDialog(LasciaFeedback.this);
                  pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                  pDialog.setTitle("Attendere Prego...");
                  pDialog.setMessage("Sto inviando il feedback...");
                  pDialog.setCancelable(false);
                  pDialog.setProgress(0);
                  pDialog.show();

                  Setup application = (Setup) getApplication();

                  url += "&codice=" + codice + "&idutente=" + application.getTkID() + "&voto=" + ratingBar.getRating() + "&commento=" + txtRecensione.getText().toString().replace(" ","%20") + "&idticketemesso=" + idMyTicket;


                  Log.d("COLONNA5",url);
                  new inviaFeedback().execute();




                  //Toast.makeText(getApplicationContext(),"HO CLICCATO INVIA",Toast.LENGTH_SHORT).show();

              }
          }
        );

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating == 0) {
                    txtValutazione.setText("");
                }
                if (rating == 1) {
                    txtValutazione.setText("PESSIMO");
                }
                if (rating == 2) {
                    txtValutazione.setText("INSUFFICIENTE");
                }
                if (rating == 3) {
                    txtValutazione.setText("ACCETTABILE");
                }
                if (rating == 4) {
                    txtValutazione.setText("BUONO");
                }
                if (rating == 5) {
                    txtValutazione.setText("ECCELLENTE");
                }

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lascia_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    private class inviaFeedback extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        String messaggio = "";

        public inviaFeedback() {

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

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            MyDatabase db=new MyDatabase(getApplicationContext());
            db.open();  //apriamo il db

            Log.d("Response: ", "> " + jsonStr);



            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    myFeedback = jsonObj.getJSONArray("FEEDBACK");

                    // looping through All Tickets



                    for (int i = 0; i < myFeedback.length(); i++) {
                        JSONObject c = myFeedback.getJSONObject(i);


                        messaggio = c.getString("messaggio");

                        Log.d("COLONNA5",messaggio);






                        //db.insertTicket(id,categoria,codice,titolo,titoloSup,Float.parseFloat(mediaVoto),Integer.parseInt(scaricati),descrizione,indirizzo,lat,lon,nominativo,telefono,sito,dataScadenza,prezzoCr,seo);
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

            if(pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (messaggio.equals("OK")){
                MyDatabase db=new MyDatabase(getApplicationContext());
                db.open();  //apriamo il db

                Log.d("COLONNA5 S",idt);
                db.deleteMyTicketFeedbackOk(idt);




                Toast.makeText(getApplicationContext(),"FEEDBACK INVIATO",Toast.LENGTH_LONG).show();

                final Intent intent = new Intent(getApplicationContext(),MyTicket.class); // SWIPE E TAB + JSON NOT VIEW
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); // Launch the Intent

                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Errore riprova più tardi",Toast.LENGTH_LONG).show();
            }

        }
    }
}
