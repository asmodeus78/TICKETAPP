package it.ticketclub.ticketapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
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
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragmaint_risto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragmaint_risto#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class fragmaint_risto extends Fragment {

    private static ProgressDialog pDialog;

    // URL to get tickets JSON
    public static String url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=TK&categoria=Ristorazione";

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


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmaint_risto.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmaint_risto newInstance(String param1, String param2) {
        fragmaint_risto fragment = new fragmaint_risto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public fragmaint_risto() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View rootView = inflater.inflate(R.layout.fragment_fragmaint_risto, container, false);

        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getArguments().getString(ARG_SECTION_STRING));



        // Calling async task to get json
        new GetTickets().execute();


        //View rootView = inflater.inflate(R.layout.activity_home, container, false);




        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_fragmaint_risto, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
