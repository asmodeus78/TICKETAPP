package it.ticketclub.ticketapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class SplashActivity extends Activity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "695474049963";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;






    public static final File root = Environment.getExternalStorageDirectory();
    private static final String NOMEDIA_FILE = "sample.nomedia";

    private static final int UPDATE_REQUEST_ID = 1;

    private static final String TAG_LOG = SplashActivity.class.getName();

    private static final String START_TIME_KEY ="it.ticketclub.ticketapp.key.START_TIME_KEY";
    private static final String IS_DONE_KEY ="it.ticketclub.ticketapp.key.IS_DONE_KEY";

    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 5000L;
    private static final int GO_HEAD_WHAT = 1;


    private long mStartTime = -1L;
    private boolean mIsDone;



    /***************************************************/
    private static ProgressDialog pDialog;

    // URL to get tickets JSON
    public static String url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=TK";
    public static String url3 ="http://www.ticketclub.it/APP/ticket_view.php?CMD=LOGINID&userid=";

    public static String userid="";
    static JSONArray myProfile = null;


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
                        Log.d("COLONNA","FORSE INTERNET NON C'E'?");

                        if (!isNetworkAvailable()) {

                            Toast.makeText(getApplication(),
                                    "Connessione Internet Assente",
                                    Toast.LENGTH_LONG).show();

                            goAhead();
                        }



                    }
                    break;
            }
        }
    };


    private void goAhead(){

        //aggiorno ultima data aggiornamento ticket
        Calendar data = Calendar.getInstance();


        String anno = String.valueOf(data.get(Calendar.YEAR));
        String mese = String.valueOf(data.get(Calendar.MONTH) + 1) ;
        String giorno = String.valueOf(data.get(Calendar.DAY_OF_MONTH));

        if (mese.length()<2){mese="0" + mese;}
        if (giorno.length()<2){giorno="0" + giorno;}

        String filtro = anno + "-" + mese + "-" + giorno;


        MyDatabase db=new MyDatabase(getApplicationContext());
        db.open();  //apriamo il db
        Cursor c;
        c = db.fetchConfig();

        if (c.getCount()>0){
            db.updateLastDownload(filtro);
            
            //recuperare dati utente


            if (userid!="0") {
                url3 = url3 + userid;
                Log.d("COLONNA" , url3);
                new GetUserInfoSimple().execute();
            }


        }else{
            db.insertConfig(filtro,0,"","","0","0");
        }





        //final Intent intent = new Intent(this,MainActivity.class); // WEB VIEW
        final Intent intent = new Intent(this,FirstActivity.class); // SWIPE E TAB + JSON NOT VIEW


        startActivity(intent); // Launch the Intent
        finish(); // We finish the current Activity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mDisplay = (TextView) findViewById(R.id.display);

        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i("PUSHNOTIFY", "No valid Google Play Services APK found.");
        }





        if (savedInstanceState != null){
            this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);
        }

        MyDatabase db=new MyDatabase(getApplicationContext());
        db.open();  //apriamo il db
        Cursor c;

        /*CANCELLO TICKET SCADUTI*/
        Calendar data = Calendar.getInstance();

        String anno = String.valueOf(data.get(Calendar.YEAR));
        String mese = String.valueOf(data.get(Calendar.MONTH) + 1) ;
        String giorno = String.valueOf(data.get(Calendar.DAY_OF_MONTH));

        if (mese.length()<2){mese="0" + mese;}
        if (giorno.length()<2){giorno="0" + giorno;}

        String filtro = anno + "-" + mese + "-" + giorno;
        Log.d("COLONNA","DATA OGGI " +filtro);

        db.deleteTicketByData(filtro);




        c = db.fetchConfig();
        String lastUpdate = "";

        if (c.moveToFirst()) {
            do {
                userid = c.getString(0);
                lastUpdate = c.getString(5);
            } while (c.moveToNext());
        }

        Log.d("COLONNA","ULTIMO AGGIORNAMENTO " + lastUpdate);


        if (!lastUpdate.equals("")){

            url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=TK&lastUpdate=" + lastUpdate;

        }

        Log.d("COLONNA:","URL UPDATE " + url);


        db.close();

        if (!isNetworkAvailable()) {

            Toast.makeText(getApplication(),
                    "Connessione Internet Assente",
                    Toast.LENGTH_LONG).show();
        }else{
            new GetTickets().execute();
        }














    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("PUSHNOTIFY", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("PUSHNOTIFY", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("PUSHNOTIFY", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(SplashActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private class registerAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;



        }

        @Override
        protected void onPostExecute(String msg) {
            mDisplay.append(msg + "\n");
        }

    }

    private void registerInBackground()  {


        new registerAsync().execute();



    }


    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.

        String MyRegistrationUrl = "http://www.ticketclub.it/APP/gcm/register.php";

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("name", "android"));
        nameValuePairs.add(new BasicNameValuePair("email", "asmodeus78@gmail.com"));
        nameValuePairs.add(new BasicNameValuePair("regId", regid));

        Log.d("PUSHNOTIFY",regid);


        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(MyRegistrationUrl, ServiceHandler.POST, nameValuePairs);




    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("PUSHNOTIFY", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
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


        mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
        Log.d("COLONNA", "Handler message sent!");
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

                        String dataScadenza = c.getString("dataScadenza");
                        String prezzoCr = c.getString("prezzoCR");

                        String seo = c.getString("SEO").toUpperCase();

                        String ordine = c.getString("ordine");

                        Log.d("COLONNA",seo);



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


                        db.insertTicket(id,categoria,codice,titolo,titoloSup,Float.parseFloat(mediaVoto),Integer.parseInt(scaricati),descrizione,indirizzo,lat,lon,nominativo,telefono,sito,dataScadenza,prezzoCr,seo,ordine);
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

            goAhead();
            // Dismiss the progress dialog
            //if (pDialog.isShowing())
           //     pDialog.dismiss();

           // list = result;
        }
    }

    private class GetUserInfoSimple extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public GetUserInfoSimple() {
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

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url3, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            Setup application = (Setup) getApplication();

            application.setTkStatusLogin("0");

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    myProfile = jsonObj.getJSONArray("PROFILO");

                    for (int i = 0; i < myProfile.length(); i++) {
                        JSONObject c = myProfile.getJSONObject(i);

                        String idUtente = c.getString("idutente");

                        if (idUtente!="0") {

                            String email = c.getString("email");
                            String nominativo = c.getString("nominativo");
                            String crediti = c.getString("crediti");
                            String uid = c.getString("idImg");


                            Log.d("COLONNA", "mia email:" + email);



                            MyDatabase db=new MyDatabase(getApplicationContext());
                            db.open();  //apriamo il db
                            Cursor d;
                            d = db.fetchConfig();

                            if (d.getCount()>0){
                                //update
                                db.updateConfig(idUtente);
                            }


                            application.setTkStatusLogin("1");
                            application.setTkProfileEmail(email);
                            application.setTkProfileName(nominativo);
                            application.setTkProfileImageId(uid);
                            application.setTkID(idUtente);
                            application.setTkProfileCrediti(crediti);

                        }





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

            Setup application = (Setup) getApplication();

            if (application.getTkStatusLogin()=="1"){
                Context context = getApplicationContext();
                CharSequence text = "Ciao, " + application.getTkProfileName() ;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }


    public boolean isNetworkAvailable() {

        Context context = getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {

            //boitealerte(this.getString(R.string.alert),"getSystemService rend null");
            Log.d("COLONNA","getSystemService rend null");

        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;
                    }
                }

            }
        }
        return false;

    }



}
