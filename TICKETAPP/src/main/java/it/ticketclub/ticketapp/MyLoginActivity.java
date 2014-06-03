package it.ticketclub.ticketapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

/**
 * Created by Gio on 23/05/2014.
 */

public class MyLoginActivity extends Activity {

    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
    private static final String URL_PREFIX_USER_INFO = "https://graph.facebook.com/me/?access_token=";

    private static final String URL_PREFIX_USER_INFO_TK = "http://www.ticketclub.it/APP/ticket_view.php?CMD=FBLOGIN";

    private static final String URL_PREFIX_USER_INFO_SIMPLE = "http://www.ticketclub.it/APP/ticket_view.php?CMD=LOGIN";

    public static String url = "";
    public static String url2 = "";
    public static String url3 = "";

    static JSONArray myProfile = null;
    static JSONArray myProfileTk = null;

    private TextView textInstructionsOrLink;
    private Button buttonSimplelogin;
    private Button buttonLoginLogout;
    private Button buttonSignUp;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    public Setup application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);



        application = (Setup) this.getApplication();

        buttonLoginLogout = (Button) findViewById(R.id.authButton);
        textInstructionsOrLink = (TextView) findViewById(R.id.instructionsOrLink);


        buttonSimplelogin = (Button) findViewById(R.id.bt_login);

        buttonSignUp = (Button) findViewById(R.id.bt_registrati);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }


        buttonSimplelogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onClickSimpleLogin();
            }
        });

        buttonSignUp.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onClickSignUp();
            }
        });


        //updateView();

    }

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void updateView() {

        if (isNetworkAvailable()) {
            Session session = Session.getActiveSession();
            if (session.isOpened()) {
                //textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
                Log.d("COLONNA", URL_PREFIX_USER_INFO + session.getAccessToken());

                url = URL_PREFIX_USER_INFO + session.getAccessToken();
                new GetUserInfo().execute();


                buttonLoginLogout.setText("LOGIN");
                buttonLoginLogout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        onClickLogout();
                    }
                });
            } else {
                textInstructionsOrLink.setText(""); //NON ANCORA LOGGATO
                buttonLoginLogout.setText("LOGOUT");
                buttonLoginLogout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        onClickLogin();
                    }
                });
            }
        }else{

            Log.d("COLONNA","Nessuna connessione dati");

        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));

        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void onClickSimpleLogin() {


        TextView txtUser = (TextView) findViewById(R.id.email);
        TextView txtPass = (TextView) findViewById(R.id.password);

        if (txtPass.getText().toString()==""){
            textInstructionsOrLink.setText("password obbligatoria");

        }

        if (txtUser.getText().toString()==""){
            textInstructionsOrLink.setText("password obbligatoria");

        }


        url3 = URL_PREFIX_USER_INFO_SIMPLE + "&email=" + txtUser.getText().toString() + "&pwd=" + md5(txtPass.getText().toString());

        Log.d("COLONNA","URL3: " + url3);

        new GetUserInfoSimple().execute();


    }

    private void onClickSignUp(){

        final Intent intent = new Intent(getApplication(),SignUp.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
        finish(); // We finish the current Activity


    }


    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }





    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            application.setTkStatusLogin("0");
            session.closeAndClearTokenInformation();
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }


    /******************/

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

            if (application.getTkStatusLogin()=="1"){
                Log.d("COLONNA","SIMPLE LOGIN");

                final Intent intent = new Intent(getApplication(),MyProfile.class); // SWIPE E TAB + JSON NOT VIEW
                startActivity(intent); // Launch the Intent
                finish(); // We finish the current Activity

            }else{

                Context context = getApplicationContext();
                CharSequence text = "Utente non trovato";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();



            }
        }
    }

    /*****************/

    private class GetUserInfo extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public GetUserInfo() {
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
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            application.setTkStatusLogin("0");

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject("{JSON: [" + jsonStr + "]}");

                    // Getting JSON Array node
                    myProfile = jsonObj.getJSONArray("JSON");

                    for (int i = 0; i < myProfile.length(); i++) {
                        JSONObject c = myProfile.getJSONObject(i);

                        Integer id = c.getInt("id");
                        String email = c.getString("email");
                        String nominativo = c.getString("name");




                        Log.d("COLONNA","mia email:" + email);




                        url2 = URL_PREFIX_USER_INFO_TK +"&uid=" + id + "&email=" + email + "&nominativo=" + nominativo;











                        //listx.add(new Ticket(id,categoria,codice,titolo,titoloSup,photo,scaricati,mediaVoto));


                        //db.insertTicket(id,categoria,codice,titolo,titoloSup,Float.parseFloat(mediaVoto),Integer.parseInt(scaricati),descrizione,indirizzo,lat,lon,nominativo,telefono,sito,dataScadenza);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                url2="";
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

           new GetUserInfoTK().execute();
        }
    }

    private class GetUserInfoTK extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public GetUserInfoTK() {
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
            Log.d("COLONNA","prima " + url2);
            String jsonStr = sh.makeServiceCall(url2, ServiceHandler.GET);
            Log.d("COLONNA","dopo " + url2);




            application.setTkStatusLogin("0");



            if (jsonStr != null) {

                Log.d("COLONNA","TROVATA");

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    Log.d("COLONNA","CI PROVO");

                    // Getting JSON Array node
                    myProfileTk = jsonObj.getJSONArray("PROFILO");

                    for (int i = 0; i < myProfileTk.length(); i++) {

                        Log.d("COLONNA","STO DENTRO");

                        JSONObject c = myProfileTk.getJSONObject(i);

                        Integer idUtente = c.getInt("idutente");
                        String email = c.getString("email");
                        String nominativo = c.getString("nominativo");
                        String crediti = c.getString("crediti");
                        String uid = c.getString("idImg");


                        Log.d("COLONNA","il mio ticket id è:" + email);

                        //Log.d("COLONNA","il mio ticket id è:" + idUtente);





                        application.setTkStatusLogin("1");

                        application.setTkProfileEmail(email);
                        application.setTkProfileName(nominativo);
                        application.setTkProfileImageId(uid);
                        application.setTkID(idUtente.toString());
                        application.setTkProfileCrediti(crediti);








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


            if (application.getTkStatusLogin()=="1"){

                Log.d("COLONNA","FB LOGIN");

                final Intent intent = new Intent(getApplication(),MyProfile.class); // SWIPE E TAB + JSON NOT VIEW
                startActivity(intent); // Launch the Intent
                finish(); // We finish the current Activity

            }

            //goAhead();
            // Dismiss the progress dialog
            //if (pDialog.isShowing())
            //     pDialog.dismiss();

            // list = result;
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
