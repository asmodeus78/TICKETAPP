package it.ticketclub.ticketapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Faq extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableFaqListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Come funziona TicketClub?");
        listDataHeader.add("Come funziona il sistema dei Crediti?");
        listDataHeader.add("Posso riconvertire i Crediti in euro?");
        listDataHeader.add("A cosa servono i feedback?");
        listDataHeader.add("Dove posso ritirare il carnet cartaceo?");

        // Adding child data
        List<String> how1 = new ArrayList<String>();
        how1.add("Registrati al sito e scarica lo sconto che preferisci. Non dovrai fare altro che mostrarlo al partner al momento di acquistare per poterti godere la promo");
        List<String> how2 = new ArrayList<String>();
        how2.add("Grazie ai Crediti puoi scaricare la promo che desideri. Puoi ottenere Crediti lasciando un commento sulle tue esperienze con Ticketclub, partecipando alle numerose iniziative della nostra pagina facebook o semplicemente ricaricando in Ricarica Crediti.");
        List<String> how3 = new ArrayList<String>();
        how3.add("La ricarica crediti  è una operazione irreversibile. La conversione dei Crediti in euro non è prevista in nessuna forma.");
        List<String> how4 = new ArrayList<String>();
        how4.add("I feedback sono sinonimo di qualità e trasparenza. Le aziende partners di TicketClub non temono giudizi e, anzi, ascoltano le opinioni degli utenti per migliorarsi e piacere sempre più.");
        List<String> how5 = new ArrayList<String>();
        how5.add("Consulta la Mappa Distributori nel tuo profilo li troverai i nomi di tutte i distributori ufficiali TicketClub.");

        listDataChild.put(listDataHeader.get(0), how1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), how2);
        listDataChild.put(listDataHeader.get(2), how3);
        listDataChild.put(listDataHeader.get(3), how4);
        listDataChild.put(listDataHeader.get(4), how5);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faq, menu);
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
