package it.ticketclub.ticketapp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;




public class UpdateTicketList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ticket_list);
    }

    @Override
    protected void onStart(){

        Intent returnIntent = new Intent();
        return returnIntent.putExtra("risultato", "ciao");
        setResult(RESULT_OK,returnIntent);
        finish();

    }




}
