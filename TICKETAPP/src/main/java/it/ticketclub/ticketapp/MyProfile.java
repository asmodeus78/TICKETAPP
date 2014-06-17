package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Created by Gio on 30/05/2014.
 */
public class MyProfile extends Activity {

    public Setup application;

    private Button buttonLogout;
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



        }

    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            application.setTkStatusLogin("0");
            session.closeAndClearTokenInformation();
            finish();
        }
    }


}
