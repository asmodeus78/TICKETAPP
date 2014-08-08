package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;


public class MyProfile extends Activity {

    public Setup application;

    private TextView buttonLogout;
    private TextView btMyTicket;
    private TextView btDistributori;
    private TextView btFaq;
    private TextView btContatti;
    private TextView btChisiamo;
    private TextView txtNominativo;
    private TextView txtCrediti;
    private ImageView imgUser;
    //private TextView btImpostazioni;
    private TextView btRicaricaCrediti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("COLONNA","APRO IL PROFILO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);

        application = (Setup) this.getApplication();

        buttonLogout = (TextView)findViewById(R.id.bt_logout);
        txtNominativo = (TextView)findViewById(R.id.TK_nominativo);
        txtCrediti = (TextView)findViewById(R.id.TK_crediti);
        imgUser = (ImageView)findViewById(R.id.TK_image_profile);

        btMyTicket = (TextView)findViewById(R.id.btScaricati);
        btDistributori = (TextView)findViewById(R.id.btDistributori);
        btFaq = (TextView)findViewById(R.id.btFaq);
        btContatti = (TextView)findViewById(R.id.btContatti);
        btChisiamo = (TextView)findViewById(R.id.btChisiamo);
        //btImpostazioni = (TextView)findViewById(R.id.bt_impostazioni);
        btRicaricaCrediti = (TextView)findViewById(R.id.btRicaricaCrediti);


        if (application.getTkStatusLogin().contentEquals("1")){

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

            btChisiamo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApriChisiamo();
                }
            });

            /*btImpostazioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApriImpostazioni();
                }
            });*/

            btRicaricaCrediti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApriRicaricaCrediti();
                }
            });



        }

    }

    public void ApriRicaricaCrediti(){
        final Intent intent = new Intent(this,Ricarica.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriImpostazioni(){
        final Intent intent = new Intent(this,Impostazioni.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
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

    public void ApriChisiamo(){
        final Intent intent = new Intent(this,Chisiamo.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriMyTicket(){
        final Intent intent;
        application = (Setup) this.getApplication();
        if (application.getTkStatusLogin().contentEquals("1")){
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
