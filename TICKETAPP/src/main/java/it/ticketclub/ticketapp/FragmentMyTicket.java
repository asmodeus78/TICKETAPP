package it.ticketclub.ticketapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.net.Uri;
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
import android.widget.Toast;

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



                        final String TK_ID = ((TextView) view.findViewById(R.id.TK_id)).getText().toString();
                        final String TK_CODICE = ((TextView) view.findViewById(R.id.TK_codice)).getText().toString();
                        final String TK_NOME =  ((TextView) view.findViewById(R.id.TK_titoloSup)).getText().toString();
                        final String TK_DOVE =  ((TextView) view.findViewById(R.id.TK_Luogo)).getText().toString();
                        final String TK_QTA =  ((TextView) view.findViewById(R.id.TK_qta)).getText().toString();
                        final String TK_CWEB = ((TextView) view.findViewById(R.id.TK_cweb)).getText().toString();
                        final String TK_USATO = ((TextView) view.findViewById(R.id.TK_usato)).getText().toString();


                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //Mostra button clicked
                                        Log.d("COLONNA","CLICK MOSTRA " + TK_ID);
                                        final Intent intent2b = new Intent(getActivity(),UsaTicket.class); // SWIPE E TAB + JSON NOT VIEW
                                        intent2b.putExtra("id",TK_ID);
                                        intent2b.putExtra("codice",TK_CODICE);
                                        intent2b.putExtra("nome",TK_NOME + " - " + TK_DOVE + "  ");
                                        intent2b.putExtra("qta",TK_QTA);
                                        intent2b.putExtra("cweb",TK_CWEB);
                                        intent2b.putExtra("usato",TK_USATO);
                                        startActivity(intent2b); // Launch the Intent

                                        break;

                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Vota button clicked
                                        //Log.d("COLONNA", "CLICK VOTA " + TK_ID);

                                            final Intent intent2 = new Intent(getActivity(), LasciaFeedback.class); // SWIPE E TAB + JSON NOT VIEW
                                            intent2.putExtra("id", TK_ID);
                                            intent2.putExtra("codice", TK_CODICE);
                                            startActivity(intent2); // Launch the Intent

                                        //getActivity().finish(); // We finish the current Activity
                                        break;
                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(TK_NOME)
                            .setPositiveButton("VOTA", dialogClickListener)
                            .setNegativeButton("MOSTRA", dialogClickListener);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        if (TK_USATO.contentEquals("0")) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }



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

                    String id = c.getString(0);
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
