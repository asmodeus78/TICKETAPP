package it.ticketclub.ticketapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import it.ticketclub.ticketapp.R;

public class LasciaFeedback extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lascia_feedback);


        final TextView txtValutazione = (TextView) findViewById(R.id.txtValutazione);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        final Button btSendFeedback = (Button) findViewById(R.id.btSendFeedback);


        btSendFeedback.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Toast.makeText(getApplicationContext(),"HO CLICCATO INVIA",Toast.LENGTH_SHORT).show();

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
}
