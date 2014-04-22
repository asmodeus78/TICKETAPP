package it.ticketclub.ticketapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gio on 22/04/2014.
 */
public class FragmentRistorazione extends Fragment {

    private static ProgressDialog pDialog;

    // URL to get tickets JSON
    public static String url = "";

    // JSON Node names
    private static final String TAG_ID = "idTicket";
    private static final String TAG_CODICE = "codice";
    private static final String TAG_TITOLO = "titolo";
    private static final String TAG_TITOLO_SUP = "titoloSup";
    //private static final String TAG_TICKET_SCARICATI = "nTicket";
    //private static final String TAG_VOTO = "mediaVoti";


    // tickets JSONArray
    static JSONArray tickets = null;

    // Nuova Gestione Liste
    static List list;



    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_STRING = "section_string";
    private static final String ARG_SECTION_STRING2 = "section_string2";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentRistorazione newInstance() {
        FragmentRistorazione fragment = new FragmentRistorazione();
        Bundle args = new Bundle();
        url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=TK&categoria=Ristorazione";

        Log.d("colonna", "LINK01: " + url);

        fragment.setArguments(args);



        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("colonna","HO CREATO LA VISTA");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_STRING)));
        textView.setText(getArguments().getString(ARG_SECTION_STRING));

        //ARG_SECTION_STRING2

        // Calling async task to get json
        new GetTickets().execute();
        //LinkedList list2 = new LinkedList();
        //new DownloadTicketTask().execute(ARG_SECTION_STRING2);



        //list.add(new Ticket(1,"001","STELLA FILM","TC104140219046.jpg"));
        //list.add(new Ticket(2,"002","BUSINESS ORIENTED","TC104140184049.jpg"));
        //list.add(new Ticket(3,"003","UN POSTO AL SOLE","TC104140217020.jpg"));

            /*ListView listView = (ListView)rootView.findViewById(R.id.ListViewDemo);
            CustomAdapter adapter = new CustomAdapter(this.getActivity(), R.layout.row_ticket, list);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);*/

        //View rootView = inflater.inflate(R.layout.activity_home, container, false);
        return rootView;
    }

    private class GetTickets extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            BitmapUtils Bu = new BitmapUtils();

            // Nuova Gestione Liste
            list = new LinkedList();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    tickets = jsonObj.getJSONArray("TICKET");

                    // looping through All Tickets
                    for (int i = 0; i < tickets.length(); i++) {
                        JSONObject c = tickets.getJSONObject(i);

                        Integer id = c.getInt(TAG_ID);
                        String titolo = c.getString(TAG_TITOLO);
                        String titoloSup = c.getString(TAG_TITOLO_SUP);
                        String photo = c.getString(TAG_CODICE) + ".jpg";

                        list.add(new Ticket(id,titolo,titoloSup,photo));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            ListView listView = (ListView)getActivity().findViewById(R.id.ListViewDemo);

            CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.row_ticket, list);
            listView.setAdapter(adapter);
        }
    }
}