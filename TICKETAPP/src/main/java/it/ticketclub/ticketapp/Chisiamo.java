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

public class Chisiamo extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chisiamo);

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
        listDataHeader.add("PAY SOCIAL: Come posso scaricare i ticket?");
        listDataHeader.add("COME DIVENTARE PARTNER");


        // Adding child data
        List<String> how1 = new ArrayList<String>();
        how1.add("Un concetto innovativo quanto semplice: l’utente, in possesso del ticket, paga il prezzo della promozione direttamente alla cassa del Partner commerciale. Niente pagamenti anticipati e carte di creditol’utente; zero provvigioni e niente attese di bonifici per i Partner. Questo si riflette in una cura e attenzione riservata al cliente pari o addirittura superiore a quella per gli altri clienti.");
        List<String> how2 = new ArrayList<String>();
        how2.add("Per scaricare i ticket occorrono i crediti. Come si ottengono i crediti? Semplice! Condividendo i ticket sui maggiori social network, lasciando un feedback sulle esperienze vissute con TicketClub, partecipando ai contest online e tanti altri modi che trovi nella scheda RICARICA CREDITI");
        List<String> how3 = new ArrayList<String>();
        how3.add("Con TicketClub la aziende possono aumentare facilmente i volumi di vendita, attrarre nuovi clienti, incentivare la prova di nuovi prodotti con uno progetto pubblicitario dai costi drasticamente ridotti. \n" +
                "TicketClub è competenza, esperienza e supporto. Un team di esperti al tuo servizio per aiutarti a sviluppare il tuo business. Insieme andremo a valutare il mercato di riferimento e obiettivi del marketing, dall’ampliamento dei volumi di vendita, al lancio di un nuovo prodotto o servizio, dalla fidelizzazione del cliente allo smaltimento veloce di prodotti in sovra stock. \n" +
                "TicketClub veste di nuovo la filosofia del buono sconto, orientata alla soddisfazione totale sia dei partner commerciali che degli utenti.\n" +
                "Prenota subito una consulenza gratuita\n");


        listDataChild.put(listDataHeader.get(0), how1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), how2);
        listDataChild.put(listDataHeader.get(2), how3);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chisiamo, menu);
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
