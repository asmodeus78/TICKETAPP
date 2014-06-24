package it.ticketclub.ticketapp;

import android.content.Intent;
import android.database.Cursor;
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

import java.util.LinkedList;

/**
 * Created by Gio on 22/05/2014.
 */

public class FragmentCatCasa extends Fragment {

    public String citta="";

    public static FragmentCatCasa newInstance() {
        FragmentCatCasa fragment = new FragmentCatCasa();
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

                Log.d("FIRST ACTIVITY", "CALL FRAGMENT CAT CASA");
            }

            @Override
            protected void onPostExecute(LinkedList result) {
                // super.onPostExecute(result);

                ListView listView = (ListView)vista.findViewById(R.id.ListViewDemo);
                CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.row_ticket, result);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

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

            } catch (NullPointerException e) {
                e.printStackTrace();
            }



            if (citta!="" && citta!= null){
                c = db.fetchTicketByCategCity("Casa e Servizi",citta);
            }else {
                c = db.fetchTicketByCateg("Casa e Servizi");
            }
            getActivity().startManagingCursor(c);



            Log.d("FIRST ACTIVITY", " CASA E SERVIZI " + c.getCount());



            if (c.moveToFirst()) {
                do {

                    //int id = c.getInt(0);

                    Integer id = c.getInt(0);
                    String codice = c.getString(1);
                    String photo = c.getString(1) + ".jpg";
                    String titolo = c.getString(2);
                    String titoloSup = c.getString(3);
                    String categoria = c.getString(4);

                    Integer scaricati = c.getInt(5);
                    float mediaVoto = c.getFloat(6);

                    //String descrizione = c.getString(7);

                    listx.add(new Ticket(id, categoria, codice, titolo, titoloSup, photo, scaricati, mediaVoto));

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
