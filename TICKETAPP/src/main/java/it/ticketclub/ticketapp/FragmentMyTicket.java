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

public class FragmentMyTicket extends Fragment {


    public static FragmentMyTicket newInstance() {
        FragmentMyTicket fragment = new FragmentMyTicket();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_my_ticket, container, false);

        new GetMyTicketsFromDb() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(LinkedList result) {
                // super.onPostExecute(result);

                ListView listView = (ListView)vista.findViewById(R.id.ListViewDemo);
                CustomAdapter_MyTicket adapter = new CustomAdapter_MyTicket(getActivity(), R.layout.row_my_ticket, result);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        final Intent intent2 = new Intent(getActivity(),LasciaFeedback.class); // SWIPE E TAB + JSON NOT VIEW
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
    private class GetMyTicketsFromDb extends AsyncTask<View, Void, LinkedList> {

        LinkedList list;
        View vista;

        public GetMyTicketsFromDb() {
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
            c = db.fetchMyTicket();
            getActivity().startManagingCursor(c);

            Log.d("FIRST ACTIVITY", " TUTTE " + c.getCount());



            if (c.moveToFirst()) {
                do {

                    //int id = c.getInt(0);

                    String id = c.getString(1);
                    String codice = c.getString(2);
                    String photo = c.getString(2) + ".jpg";
                    String cWeb = c.getString(3);
                    String titoloSup = c.getString(4);
                    String qta = c.getString(5);
                    String usato = c.getString(6);
                    String feedback = c.getString(7);
                    String dataDownload = c.getString(8);



                    //String descrizione = c.getString(7);

                    listx.add(new Ticket2(id,codice, cWeb, qta, titoloSup, photo, usato, feedback,dataDownload));

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
