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
    private Button btFaq;
    private Button btContatti;
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
        btFaq = (Button)findViewById(R.id.btFaq);
        btContatti = (Button)findViewById(R.id.btContatti);


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

            btFaq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApriFaq();
                }
            });

            btContatti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApriContatti();
                }
            });

        }

    }

    public void ApriMappaDist(){
        final Intent intent = new Intent(this,MapsActivity.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriFaq(){
        final Intent intent = new Intent(this,Faq.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriContatti(){
        final Intent intent = new Intent(this,Contatti.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriMyTicket(){
        final Intent intent;
        application = (Setup) this.getApplication();
        if (application.getTkStatusLogin()=="1"){
            //new GetMyTickets().execute();
            new GetMyTicket(this).execute();
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






}
