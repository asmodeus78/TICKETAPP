package it.ticketclub.ticketapp;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by Gio on 22/05/2014.
 */

public class FragmentCatShopping extends Fragment {

    private Location loc = new Location(LocationManager.NETWORK_PROVIDER);
    private Location loc2 = new Location(LocationManager.NETWORK_PROVIDER);
    private String km;

    public String citta="";
    public String cerca="";

    public static FragmentCatShopping newInstance() {
        FragmentCatShopping fragment = new FragmentCatShopping();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        new GetTicketsFromDb() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                Log.d("FIRST ACTIVITY", "CALL FRAGMENT CAT SHOPPING");
            }

            @Override
            protected void onPostExecute(LinkedList result) {
                // super.onPostExecute(result);

                ListView listView = (ListView)vista.findViewById(R.id.ListViewDemo);
                CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.row_ticket, result);
                adapter.sort(
                        new Comparator<Ticket>() {
                            @Override
                            public int compare(Ticket obj1,Ticket obj2) {
                                double distanza1 = obj1.getDistanza();
                                double distanza2 = obj2.getDistanza();

                                if (distanza1>distanza2) return 1;
                                if (distanza1<distanza2) return -1;

                                return 0;
                            }
                        }
                );
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                TextView resultText = (TextView) vista.findViewById(R.id.resultText);
                if (result.isEmpty()) {
                    resultText.setText("Nessun risultato trovato");
                }else{
                    resultText.setText("");
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        final Intent intent2 = new Intent(getActivity(),SecondActivity.class); // SWIPE E TAB + JSON NOT VIEW
                        intent2.putExtra("id",((TextView) view.findViewById(R.id.TK_id)).getText());
                        intent2.putExtra("codice",((TextView) view.findViewById(R.id.TK_codice)).getText());

                        startActivity(intent2); // Launch the Intent
                        //getActivity().finish(); // We finish the current Activity

                    }
                });
            }


        }.execute(rootView);

        return rootView;
    }










    /*********************************************************/
    private class GetTicketsFromDb extends AsyncTask<View, Void, LinkedList> {

        LinkedList list;
        View vista;

        public GetTicketsFromDb() {
            this.list = list;
            this.vista = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected LinkedList doInBackground(View... arg0) {

            // Nuova Gestione Liste
            LinkedList listx = new LinkedList();


            vista = arg0[0];

            MyDatabase db=new MyDatabase(getActivity().getApplicationContext());
            db.open();  //apriamo il db

            Cursor c;

            try {

                Setup application = (Setup) getActivity().getApplication() ;
                citta = application.getTkCitta();
                cerca = application.getTkCerca();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            if (citta!="" && citta!=null && cerca!="" && cerca!=null){
                c = db.fetchTicketByCategRicercaCitta("Shopping",cerca,citta);
            }else{
                if (citta!="" && citta!= null) {
                    c = db.fetchTicketByCategCity("Shopping", citta);
                }else {
                    if (cerca!="" && cerca!=null){
                        c = db.fetchTicketByCategRicercaCitta("Shopping",cerca,"");
                    }else {
                        c = db.fetchTicketByCateg("Shopping");
                    }
                }
            }




            getActivity().startManagingCursor(c);

            Log.d("FIRST ACTIVITY", " SHOPPING " + c.getCount());



            if (c.moveToFirst()) {
                do {

                    //int id = c.getInt(0);

                    Integer id = c.getInt(0);
                    String codice = c.getString(1);
                    String photo = c.getString(1) + ".jpg";
                    String titolo = c.getString(2);
                    String titoloSup = c.getString(3);
                    String categoria = c.getString(4);
                    String lat = c.getString(9);
                    String lon = c.getString(10);

                    Integer scaricati = c.getInt(5);
                    float mediaVoto = c.getFloat(6);

                    Double distanza = Double.valueOf(0);



                    km="0";

                    try {
                        loc2.setLatitude(Double.parseDouble(lat));
                        loc2.setLongitude(Double.parseDouble(lon));

                        loc.setLatitude(Setup.getSetup().getLat());
                        loc.setLongitude(Setup.getSetup().getLon());

                        double kmTemp = loc.distanceTo(loc2) / 1000;

                        DecimalFormat f = new DecimalFormat("#0.0");
                        km = String.valueOf(f.format(kmTemp)).replace(",", ".");

                        distanza = kmTemp;

                    }catch(NullPointerException e) {
                        Log.d("POSXABERR","errore calcolo distanza");
                    }catch (NumberFormatException e){
                        Log.d("POSXABERR","errore non è un numero");
                    }


                    //String descrizione = c.getString(7);

                    //listx.add(new Ticket(id, categoria, codice, titolo, titoloSup, photo, scaricati, mediaVoto, lat, lon));
                    listx.add(new Ticket(id, categoria, codice, titolo, titoloSup, photo, scaricati, mediaVoto, lat, lon, distanza));

                } while (c.moveToNext());
            }
            //c.close();
            //db.close();



            return listx;
        }

        @Override
        protected void onPostExecute(LinkedList result) {
            super.onPostExecute(result);


            list = result;
        }
    }
    /**************************************************************************/
}
