package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

/**
 * Created by Gio on 30/05/2014.
 */
public class SignUp extends Activity {

    public static String url4="";
    static JSONArray myProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);


        final EditText nominativo = (EditText) findViewById(R.id.nominativo);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText passwd = (EditText) findViewById(R.id.txtPassword);

        Button btRegistrati = (Button) findViewById(R.id.bt_registrati);




        btRegistrati.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!nominativo.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !passwd.getText().toString().isEmpty()){


                    TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String mPhoneNumber = tMgr.getLine1Number();

                    try {
                        url4 = "http://www.ticketclub.it/APP/ticket_view.php?CMD=REG_USER&email=" + email.getText().toString() + "&nominativo=" + URLEncoder.encode(nominativo.getText().toString(),"UTF-8") + "&pwd=" + passwd.getText().toString() + "&cellulare=" + mPhoneNumber;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }



                    //Log.d("REGISTRATI",mPhoneNumber);

                    new regNewUser().execute();





                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "Tutti i campi sono obbligatori";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });



    }


    public class regNewUser extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public regNewUser() {
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
            String jsonStr = sh.makeServiceCall(url4, ServiceHandler.GET);

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

                finish();
            }else {
                Context context = getApplicationContext();
                CharSequence text = "Errore nella registrazione";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
