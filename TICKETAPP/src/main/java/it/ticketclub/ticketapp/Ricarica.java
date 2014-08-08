package it.ticketclub.ticketapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.ticketclub.ticketapp.R;

public class Ricarica extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricarica);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableFaqListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ricarica, menu);
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

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Mi piace su Facebook");
        listDataHeader.add("Condividi un Ticket");
        listDataHeader.add("Lascia un feedback");
        listDataHeader.add("Ricarica");
        listDataHeader.add("Abbonamento");

        // Adding child data
        List<String> how1 = new ArrayList<String>();
        how1.add("Metti mi piace su facebook dal nostro sito e guadagna 10 crediti");
        List<String> how2 = new ArrayList<String>();
        how2.add("Dalla nostra app o dal nostro sito puoi condividere un ticket con i tuoi amici per guadagnare 5 crediti.");
        List<String> how3 = new ArrayList<String>();
        how3.add("Dopo aver utilizzato uno dei nostri ticket lascia un feedback per recuperare i crediti spesi");
        List<String> how4 = new ArrayList<String>();
        how4.add("Recati sul nostro sito www.ticketclub.it per scoprire quest'altro metodo di ricarica");
        List<String> how5 = new ArrayList<String>();
        how5.add("Abbonati ai nostri servzi tramite il nostro sito web e non dovrai più preoccuparti di finire i crediti!");

        listDataChild.put(listDataHeader.get(0), how1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), how2);
        listDataChild.put(listDataHeader.get(2), how3);
        listDataChild.put(listDataHeader.get(3), how4);
        listDataChild.put(listDataHeader.get(4), how5);


    }
}
