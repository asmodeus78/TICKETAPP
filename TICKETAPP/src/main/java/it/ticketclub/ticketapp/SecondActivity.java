package it.ticketclub.ticketapp;

import java.util.LinkedList;
import java.util.Locale;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.text.Html;
import android.text.Spanned;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SecondActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    String codice;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle e = getIntent().getExtras();


        if (e != null) {
            codice = e.getString("codice");
        }
        if (e != null) {
            id = e.getString("id");
        }

        Log.d("DATABASE::",id);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            //return PlaceholderFragment.newInstance(position + 1);

            switch (position){

                case 0:
                    //OFFERTA
                    return PlaceholderFragmentOfferta.newInstance(0,id,codice);
                case 1:
                    //MAPPA
                    return PlaceholderFragmentMappa.newInstance(0,id,codice);
                case 2:
                    //FEEDBACK
                    return PlaceholderFragmentFeedback.newInstance(0,id,codice);

            }

            return null;


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return "OFFERTA";
                case 1:
                    return "MAPPA";
                case 2:
                    return "FEEDBACK";
            }
            return null;
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragmentMappa extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_CODICE = "codice";
        private static final String ARG_SECTION_ID = "id";

        private GoogleMap mMap; // Might be null if Google Play services APK is not available.

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragmentMappa newInstance(int sectionNumber, String id, String codice) {
            PlaceholderFragmentMappa fragment = new PlaceholderFragmentMappa();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_ID, id);
            args.putString(ARG_SECTION_CODICE, codice);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragmentMappa() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_maps, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));


            setUpMapIfNeeded();

            return rootView;
        }

        private void setUpMapIfNeeded() {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        }

        private void setUpMap() {


            MyDatabase db3=new MyDatabase(getActivity().getApplicationContext());
            db3.open();  //apriamo il db
            Cursor c3;

            c3 = db3.fetchSingleTicket(getArguments().getString(ARG_SECTION_ID));
            //c2 = db2.fetchTicket();
            getActivity().startManagingCursor(c3);

            if (c3.moveToFirst()) {
                do {

                    //String indirizzo = c3.getString(8);

                    Double lat = Double.parseDouble(c3.getString(9));
                    Double lon = Double.parseDouble(c3.getString(10));
                    String nominativo = c3.getString(11);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(nominativo)).showInfoWindow();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),16.0f));

                } while (c3.moveToNext());
            }






        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragmentOfferta extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_CODICE = "codice";
        private static final String ARG_SECTION_ID = "id";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragmentOfferta newInstance(int sectionNumber, String id, String codice) {
            PlaceholderFragmentOfferta fragment = new PlaceholderFragmentOfferta();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_ID, id);
            args.putString(ARG_SECTION_CODICE, codice);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragmentOfferta() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_offerta, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.TK_image);



            //getArguments().getInt(ARG_SECTION_NUMBER);

            Setup conf = new Setup();

            Bitmap bMap = BitmapFactory.decodeFile(conf.getPath() + "/" +  getArguments().getString(ARG_SECTION_CODICE) + ".jpg");
            imageView.setImageBitmap(bMap);

            TextView textView = (TextView) rootView.findViewById(R.id.TK_descrizione);

            /****/

            MyDatabase db2=new MyDatabase(getActivity().getApplicationContext());
            db2.open();  //apriamo il db
            Cursor c2;

            String idTest = getArguments().getString(ARG_SECTION_ID);

            Log.d("DATABASE::", idTest);

            //if (getArguments().getString(ARG_SECTION_ID)!="") {
                c2 = db2.fetchSingleTicket(getArguments().getString(ARG_SECTION_ID));
                //c2 = db2.fetchTicket();
                getActivity().startManagingCursor(c2);

                if (c2.moveToFirst()) {
                    do {

                        //int id = c.getInt(0);
/*
                        Integer id = c2.getInt(0);
                        String codice = c2.getString(1);
                        String photo = c2.getString(1) + ".jpg";
                        String titolo = c2.getString(2);
                        String titoloSup = c2.getString(3);
                        String categoria = c2.getString(4);

                        Integer scaricati = c2.getInt(5);
                        float mediaVoto = c2.getFloat(6);
*/
                        String descrizione = c2.getString(7);




                        Spanned text =  Html.fromHtml(descrizione);
                        textView.setText(text);



                    } while (c2.moveToNext());
                }
            //}
            /****/









            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragmentFeedback extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public static String id="";

        // tickets JSONArray
        static JSONArray feedback = null;

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_CODICE = "codice";
        private static final String ARG_SECTION_ID = "id";



        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragmentFeedback newInstance(int sectionNumber, String id, String codice) {
            PlaceholderFragmentFeedback fragment = new PlaceholderFragmentFeedback();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_ID, id);
            args.putString(ARG_SECTION_CODICE, codice);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragmentFeedback() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            Log.d("colonna","HO CREATO LA VISTA FEEDBACK");

            final View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);

            //LinkedList listaa = new LinkedList();

            new GetFeedback() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    id = getArguments().getString(ARG_SECTION_ID);
                    Log.d("DRAGON",id);
                }

                @Override
                protected void onPostExecute(LinkedList result) {
                    // super.onPostExecute(result);

                    ListView listView = (ListView)vista.findViewById(R.id.ListViewDemo);
                    CustomAdapter_Feedback adapter = new CustomAdapter_Feedback(getActivity(), R.layout.row_feedback, result);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }


            }.execute(rootView);


            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        /*********************************************************/
        private class GetFeedback extends AsyncTask<View, Void, LinkedList> {

            LinkedList list;
            View vista;

            public GetFeedback() {
                //this.list = list;
                this.vista = null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
            }

            @Override
            protected LinkedList doInBackground(View... arg0) {


                // URL to get feedback JSON
                String url2 = "http://www.ticketclub.it/APP/ticket_view.php?CMD=FEEDBACK&idTicket=" + id;

                Log.d("JSON--",url2);

                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url2, ServiceHandler.GET);

                // Nuova Gestione Liste
                LinkedList listx = new LinkedList();


                vista = arg0[0];

                //Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        feedback = jsonObj.getJSONArray("FEEDBACK");

                        for (int i = 0; i < feedback.length(); i++) {
                            JSONObject c = feedback.getJSONObject(i);

                            Integer id = c.getInt("idFeedback");
                            String dataInserimento = c.getString("dataInserimento");

                            String idImg="null";
                            if (c.getString("idImg")!="null") {
                                idImg = "https://graph.facebook.com/" + c.getString("idImg") + "/picture";
                            }
                            String nominativo = c.getString("nominativo");
                            String voto = c.getString("voto");
                            String commento = c.getString("commento");

                            //String descrizione = c.getString(7);

                            listx.add(new Feedback(id, dataInserimento, idImg, nominativo, voto, commento));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
















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
}


