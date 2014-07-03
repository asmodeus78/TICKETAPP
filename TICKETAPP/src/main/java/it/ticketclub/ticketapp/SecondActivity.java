package it.ticketclub.ticketapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.ShareActionProvider;
import android.widget.TextView;

import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

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
    private ShareActionProvider mShareActionProvider;


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

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        // Locate MenuItem with ShareActionProvider
        /*MenuItem item = menu.findItem(R.id.menu_item_share);

        ShareActionProvider myShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent myIntent = new Intent();
        myIntent.setAction(Intent.ACTION_SEND);
        myIntent.putExtra(Intent.EXTRA_TEXT, "Whatever message you want to share");
        myIntent.setType("text/plain");

        try {
            myShareActionProvider.setShareIntent(myIntent);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        //if (mShareActionProvider != null) {
            //if (getApplicationInfo().targetSdkVersion>=14) {
                mShareActionProvider.setShareIntent(shareIntent);
            //}
        //}
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, FirstActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_item_share:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

            final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            View rootView = inflater.inflate(R.layout.fragment_mappa, container, false);


            MyDatabase db3=new MyDatabase(getActivity().getApplicationContext());
            db3.open();  //apriamo il db
            Cursor c3;

            c3 = db3.fetchSingleTicket(getArguments().getString(ARG_SECTION_ID));
            //c2 = db2.fetchTicket();
            getActivity().startManagingCursor(c3);

            final TextView textView = (TextView) rootView.findViewById(R.id.TK_nominativo);
            final TextView textView1 = (TextView) rootView.findViewById(R.id.TK_indirizzo);
            final TextView textView2 = (TextView) rootView.findViewById(R.id.TK_telefono);
            final TextView textView3 = (TextView) rootView.findViewById(R.id.TK_sitoweb);
            textView1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            textView2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            textView3.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


            final String LocationDest;


            if (c3.moveToFirst()) {
            //    do {



            String indirizzo = c3.getString(8);
            String nominativo = c3.getString(11);
            String telefono = c3.getString(12);
            String sitoWeb = c3.getString(13);

            Double latDest = Double.valueOf("0");
            Double lonDest = Double.valueOf("0");


            try {

                 latDest = Double.parseDouble(c3.getString(9));
                 lonDest = Double.parseDouble(c3.getString(10));




            } catch (NumberFormatException e) {
                e.printStackTrace();

            }

            LocationDest = latDest + "," + lonDest;



            textView.setText(nominativo);
            textView1.setText(indirizzo);
            textView2.setText(telefono);
            textView3.setText(sitoWeb);


            //    } while (c3.moveToNext());
            }else{

                LocationDest="";

            }

            textView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked


                                    Criteria criteria = new Criteria();
                                    criteria.setAccuracy(Criteria.ACCURACY_LOW);
                                    criteria.setAltitudeRequired(false);
                                    criteria.setBearingRequired(false);
                                    criteria.setCostAllowed(true);
                                    criteria.setPowerRequirement(Criteria.POWER_LOW);
                                    String provider = locationManager.getBestProvider(criteria, true);




                                    if (provider != null) {

                                        Toast.makeText(getActivity(),
                                                "Navigo verso " + textView1.getText().toString() + " ... ",
                                                Toast.LENGTH_LONG).show();

                                        Double Latitude = locationManager.getLastKnownLocation(provider).getLatitude();
                                        Double Longitude= locationManager.getLastKnownLocation(provider).getLongitude();
                                        //String Location = textView1.getText().toString();


                                        Uri routeUri = Uri.parse("http://maps.google.com/maps?&saddr=" + Latitude + "," + Longitude + "&daddr=" + LocationDest);

                                        Intent i = new Intent(Intent.ACTION_VIEW, routeUri);
                                        startActivity(i);
                                    } else{

                                        Toast.makeText(getActivity(),
                                                "Attiva il GPS per avviare la navigazione ... ",
                                                Toast.LENGTH_LONG).show();

                                        System.out.println("objectInstance is null, do not attempt to initialize field2");
                                    }


                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Vuoi andare a " + textView1.getText().toString() + "?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });

            textView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    Toast.makeText(getActivity(),
                                            "Sto chiamando " + textView.getText().toString() + " ... ",
                                            Toast.LENGTH_LONG).show();

                                    Integer pos = textView2.getText().toString().indexOf(" ");

                                    String toDial = "tel:";
                                    if (pos>0){
                                        toDial = toDial + textView2.getText().toString().substring(0,pos);
                                    }else{
                                        toDial = toDial + textView2.getText().toString();
                                    }

                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(toDial)));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Vuoi chiamare " + textView.getText().toString() + "?").setPositiveButton("Yes", dialogClickListener)
                                                       .setNegativeButton("No", dialogClickListener).show();

                }
            });

            textView3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    Toast.makeText(getActivity(),
                                            "Sto aprendo " + textView3.getText().toString() + " ... ",
                                            Toast.LENGTH_LONG).show();

                                    String url = textView3.getText().toString();

                                    if (!url.substring(0,4).matches("http")) {
                                        url = "http://" + url;
                                    }
                                    Log.d("COLONNA",url);
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));




                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Vuoi aprire il sito " + textView3.getText().toString() + "?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });

            if (textView1.getText().toString()!="") {
                setUpMapIfNeeded();
            }

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

                    String indirizzo = c3.getString(8);

                    if (indirizzo!="") {

                        try {

                            Double lat = Double.parseDouble(c3.getString(9));
                            Double lon = Double.parseDouble(c3.getString(10));
                            String nominativo = c3.getString(11);

                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(nominativo)).showInfoWindow();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 16.0f));

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }




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



        public Setup application;
        public String urlDownload="";
        public String messaggio="";

        // tickets JSONArray
        static JSONArray MyTicket = null;

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

            final TextView textView = (TextView) rootView.findViewById(R.id.TK_descrizione);
            final TextView textView2 = (TextView) rootView.findViewById(R.id.TK_crediti);
            final TextView textView3 = (TextView) rootView.findViewById(R.id.TK_scadenza);
            final TextView textView4 = (TextView) rootView.findViewById(R.id.TK_DataScadenza);
            final TextView textView5 = (TextView) rootView.findViewById(R.id.TK_codice);



            /****/

            MyDatabase db2=new MyDatabase(getActivity().getApplicationContext());
            db2.open();  //apriamo il db
            Cursor c2;

            String idTest = getArguments().getString(ARG_SECTION_ID);

            Log.d("DATABASE::", idTest);


                c2 = db2.fetchSingleTicket(getArguments().getString(ARG_SECTION_ID));

                getActivity().startManagingCursor(c2);

                if (c2.moveToFirst()) {
                    do {


                        String descrizione = c2.getString(7);
                        String dataScadenza = c2.getString(14);
                        String crediti = c2.getString(15);
                        String codice = c2.getString(1);






                        Spanned text =  Html.fromHtml(descrizione);
                        textView.setText(text);

                        textView2.setText(crediti);

                        textView4.setText(dataScadenza);

                        textView5.setText(codice);

                        long diffMs, diffInSec=0;
                        Calendar dataOggi = Calendar.getInstance();
                        Date dataFine = null;
                        String giorni,minuti,ore,secondi,FIMALEEE = null;

                        Log.d("SCADENZA 1", String.valueOf(dataOggi.getTimeInMillis()));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            dataFine = dateFormat.parse(dataScadenza);

                            diffMs = dataFine.getTime() - dataOggi.getTimeInMillis();
                            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffMs);

                            giorni = String.valueOf(diffInSec / 86400);
                            ore = String.valueOf(((diffInSec  % 86400) / 3600));
                            minuti =  String.valueOf(((diffInSec  % 86400) % 3600) / 60);
                            secondi = String.valueOf(((diffInSec  % 86400) % 3600) % 60);

                            if(ore.length()<2){ore="0"+ore;}
                            if(minuti.length()<2){minuti="0"+minuti;}
                            //if(secondi.length()<2){secondi="0"+secondi;}

                            FIMALEEE = giorni + "g " + ore + "h " + minuti + "m"; // + secondi + "s";



                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        textView3.setText(FIMALEEE);







                    } while (c2.moveToNext());
                }

            /****/

            Button btscarica = (Button) rootView.findViewById(R.id.btScarica);

            application = (Setup) getActivity().getApplication();




            btscarica.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                final Intent intent;

                                if (application.getTkStatusLogin()=="1"){

                                    //new GetMyTickets().execute();

                                    String cmd = "SCARICA_TICKET";
                                    String email = application.getTkProfileEmail();
                                    String crediti = textView2.getText().toString();
                                    String codice = textView5.getText().toString();

                                    urlDownload = "http://www.ticketclub.it/APP/ticket_view.php?CMD=" + cmd + "&crediti=" + crediti + "&email=" + email + "&&qta=1&codice=" + codice;

                                    Log.d("URLD",urlDownload);



                                    new GetDownloadTickets().execute();



                                }else {
                                    intent = new Intent(getActivity(),MyLoginActivity.class); // SWIPE E TAB + JSON NOT VIEW
                                    startActivity(intent); // Launch the Intent
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                        }
                    };


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Vuoi scaricare il ticket ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();






                }
            });













            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }



        /*********************************************************/
        private class GetDownloadTickets extends AsyncTask<Void, Void, Void> {
            LinkedList list;
            View vista;

            public GetDownloadTickets() {
                //this.list = list;
                //this.vista = null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog



            }

            @Override
            protected Void doInBackground(Void... arg0) {
                // URL to get feedback JSON
                //String url2 = "http://www.ticketclub.it/APP/ticket_view.php?CMD=FEEDBACK&idTicket=" + id;

                Log.d("JSON--",urlDownload);

                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(urlDownload, ServiceHandler.GET);

                // Nuova Gestione Liste
                LinkedList listx = new LinkedList();


                //vista = arg0[0];

                //Log.d("Response: ", "> " + jsonStr);



                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        MyTicket = jsonObj.getJSONArray("SCARICA_TICKET");

                        for (int i = 0; i < MyTicket.length(); i++) {
                            JSONObject c = MyTicket.getJSONObject(i);

                            messaggio = c.getString("messaggio");

                            if (messaggio.contentEquals("TICKET SCARICATO")){
                                String crediti = c.getString("crediti");
                                application.setTkProfileCrediti(crediti);

                            }else{

                                Log.d("MSGGG:" , messaggio);

                            }

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

                Toast.makeText(getActivity(),messaggio,Toast.LENGTH_LONG).show();


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:



                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                final Intent intent = new Intent(getActivity(),MyTicket.class); // SWIPE E TAB + JSON NOT VIEW
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent); // Launch the Intent

                                break;
                        }
                    }
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Ticket scaricato!")
                        .setMessage("Vai ai tuoi ticket per mostrare l'offerta")
                        .setPositiveButton("ANNULLA", dialogClickListener)
                        .setNegativeButton("VAI", dialogClickListener)
                        .show();


               // list = result;
            }
        }
        /**************************************************************************/
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


                            String idImg = c.getString("idImg");
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


