package it.ticketclub.ticketapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class SplashActivity extends Activity {

    public static final File root = Environment.getExternalStorageDirectory();
    private static final String NOMEDIA_FILE = "sample.nomedia";

    private static final int UPDATE_REQUEST_ID = 1;

    private static final String TAG_LOG = SplashActivity.class.getName();

    private static final String START_TIME_KEY ="it.ticketclub.ticketapp.key.START_TIME_KEY";
    private static final String IS_DONE_KEY ="it.ticketclub.ticketapp.key.IS_DONE_KEY";

    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 3000L;
    private static final int GO_HEAD_WHAT = 1;


    private long mStartTime = -1L;
    private boolean mIsDone;



    /***************************************************/
    private static ProgressDialog pDialog;

    // URL to get tickets JSON
    public static String url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=TK";

    // URL to get feedback JSON
    //public static String url2 = "http://www.ticketclub.it/APP/ticket_view.php?CMD=FEEDBACK&data_start=2014-05-10";

    // JSON Node names
    private static final String TAG_ID = "idTicket";
    private static final String TAG_CATEGORIA ="categoria";
    private static final String TAG_CODICE = "codice";
    private static final String TAG_TITOLO = "titolo";
    private static final String TAG_TITOLO_SUP = "titoloSup";
    private static final String TAG_TICKET_SCARICATI = "scaricati";
    private static final String TAG_VOTO = "mediaVoti";
    private static final String TAG_DESCRIZIONE = "descrizione";
    private static final String TAG_INDIRIZZO = "indirizzo";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LON = "lon";
    private static final String TAG_NOMINATIVO = "Nominativo";



    // tickets JSONArray
    static JSONArray tickets = null;
    /***************************************************/



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_HEAD_WHAT:
                    long elapsedTime = SystemClock.uptimeMillis() - mStartTime;
                    if (elapsedTime>=MIN_WAIT_INTERVAL && !mIsDone){
                        mIsDone = true;
                        goAhead();
                    }
                    break;
            }
        }
    };


    private void goAhead(){
        //final Intent intent = new Intent(this,MainActivity.class); // WEB VIEW
        //final Intent intent = new Intent(this,HomeActivity.class); // ONLY SWIBE E TAB
        final Intent intent = new Intent(this,FirstActivity.class); // SWIPE E TAB + JSON NOT VIEW
        //final Intent intent = new Intent(this,MainActivity2.class); // WEB VIEW

        startActivity(intent); // Launch the Intent
        finish(); // We finish the current Activity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState != null){
            this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);
        }

        MyDatabase db=new MyDatabase(getApplicationContext());
        db.open();  //apriamo il db

        db.deleteTicket();



        db.close();

        new GetTickets().execute();












    }

    @Override
    protected void onStart(){
        super.onStart();

        if(isExternalStorageWritable()){
            File path = new File(root.getAbsolutePath()+ "/Android/data/" + getApplicationInfo().packageName + "/cache/");
            path.mkdirs();

            File file= new File(path,NOMEDIA_FILE);
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(mStartTime==-1){
            mStartTime = SystemClock.uptimeMillis();
        }
        final Message goAheadMessage = mHandler.obtainMessage(GO_HEAD_WHAT);

        /*final Intent updateIntent = new Intent(this,UpdateTicketList.class);
        startActivityForResult(updateIntent,UPDATE_REQUEST_ID);*/


        /////////////mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
        Log.d(TAG_LOG, "Handler message sent!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DONE_KEY,mIsDone);
        outState.putLong(START_TIME_KEY,mStartTime);
    }

    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        this.mIsDone = savedInstanceState.getBoolean(IS_DONE_KEY);
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    private class GetTickets extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public GetTickets() {
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
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            MyDatabase db=new MyDatabase(getApplicationContext());
            db.open();  //apriamo il db

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    tickets = jsonObj.getJSONArray("TICKET");

                    // looping through All Tickets



                    for (int i = 0; i < tickets.length(); i++) {
                        JSONObject c = tickets.getJSONObject(i);

                        Integer id = c.getInt(TAG_ID);
                        String categoria = c.getString(TAG_CATEGORIA);
                        String titolo = c.getString(TAG_TITOLO);
                        String codice = c.getString(TAG_CODICE);
                        String titoloSup = c.getString(TAG_TITOLO_SUP);
                        String photo = c.getString(TAG_CODICE) + ".jpg";
                        String scaricati = c.getString(TAG_TICKET_SCARICATI);
                        String mediaVoto = c.getString(TAG_VOTO);
                        String descrizione = c.getString(TAG_DESCRIZIONE);
                        String indirizzo = c.getString(TAG_INDIRIZZO);
                        String lat = c.getString(TAG_LAT);
                        String lon = c.getString(TAG_LON);
                        String nominativo = c.getString(TAG_NOMINATIVO);
                        String telefono = c.getString("telefono");
                        String sito = c.getString("sito");



                       /* if (mediaVoto==""){
                            mediaVoto = "0";
                        }

                        if (mediaVoto==null){
                            mediaVoto = "0";
                        }*/

                        if (mediaVoto=="null"){
                            mediaVoto = "0";
                        }

                        if (scaricati=="null"){
                            scaricati="0";
                        }

                        //listx.add(new Ticket(id,categoria,codice,titolo,titoloSup,photo,scaricati,mediaVoto));


                        db.insertTicket(id,categoria,codice,titolo,titoloSup,Float.parseFloat(mediaVoto),Integer.parseInt(scaricati),descrizione,indirizzo,lat,lon,nominativo,telefono,sito);



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

            goAhead();
            // Dismiss the progress dialog
            //if (pDialog.isShowing())
           //     pDialog.dismiss();

           // list = result;
        }
    }



}
