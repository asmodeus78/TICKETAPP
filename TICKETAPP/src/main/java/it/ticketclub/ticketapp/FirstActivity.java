package it.ticketclub.ticketapp;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Settings;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class FirstActivity extends ActionBarActivity implements ActionBar.TabListener {

    private MenuItem mSpinnerItem = null;

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
    public Setup application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);

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
                Log.d("FIRST ACTIVITY", String.valueOf(position));
                actionBar.setSelectedNavigationItem(position);
            }
        });

        /*actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_0).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_1).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_2).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_3).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_4).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_5).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_6).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_7).setText("").setTabListener(this));*/

        actionBar.addTab(actionBar.newTab().setText(" TUTTE ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" RISTORAZIONE ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" BENESSERE ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" VIAGGI E SVAGO ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" CASA E SERVIZI ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" SPORT E MOTORI ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" SHOPPING ").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(" EVENTI ").setTabListener(this));




    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater minf = getMenuInflater();
        minf.inflate(R.menu.first,menu);
        //minf.inflate(R.menu.fragment_menu_top,menu);


        //LayoutParams layout_params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        /*View customNav = LayoutInflater.from(this).inflate(R.layout.fragment_menu_top, null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(customNav);*/


        //mSpinnerItem = menu.findItem(R.id.action_citta);
        //setupSpinner(mSpinnerItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/



       // Log.d("AAA", String.valueOf(id));

        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menu_distributori :
                ApriMappaDist();
                return true;
            case R.id.menu_profilo :
                ApriProfilo();
                return true;
            case R.id.menu_myTicket :
                ApriMyTicket();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ApriMappaDist(){
        final Intent intent = new Intent(this,MapsActivity.class); // SWIPE E TAB + JSON NOT VIEW
        startActivity(intent); // Launch the Intent
    }

    public void ApriProfilo(){

        final Intent intent;
        application = (Setup) this.getApplication();
        if (application.getTkStatusLogin()=="1"){
            intent = new Intent(getApplication(),MyProfile.class); // SWIPE E TAB + JSON NOT VIEW
        }else {
            intent = new Intent(this,MyLoginActivity.class); // SWIPE E TAB + JSON NOT VIEW
        }

        startActivity(intent); // Launch the Intent


    }

    public void ApriMyTicket(){
        final Intent intent;
        application = (Setup) this.getApplication();
        if (application.getTkStatusLogin()=="1"){


            new GetMyTickets().execute();


            intent = new Intent(getApplication(),MyTicket.class); // SWIPE E TAB + JSON NOT VIEW
        }else {
            intent = new Intent(this,MyLoginActivity.class); // SWIPE E TAB + JSON NOT VIEW
        }

        startActivity(intent); // Launch the Intent
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
                    //TUTTI I TICKET
                    return FragmentCatTutte.newInstance();
                case 1:
                    //Ristorazione
                    return FragmentCatRistorazione.newInstance();
                case 2:
                    //Benessere
                    return FragmentCatBenessere.newInstance();
                case 3:
                    //Viaggi e Svago
                    return FragmentCatViaggi.newInstance();
                case 4:
                    //Casa e Servizi
                    return FragmentCatCasa.newInstance();
                case 5:
                    //Sport Motori
                    return FragmentCatSport.newInstance();
                case 6:
                    //Shopping
                    return FragmentCatShopping.newInstance();
                case 7:
                    //Eventi
                    return FragmentCatEventi.newInstance();
            }

            return null;


        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 8;
        }


    }





    private class GetMyTickets extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public GetMyTickets() {
            this.list = list;
            this.vista = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Nuova Gestione Liste
            //LinkedList listx = new LinkedList();

            // Making a request to url and getting response

            application = (Setup) getApplication();
            String url="";

            JSONArray tickets = null;

            if (application.getTkStatusLogin()=="1"){

                url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=MY_TICKET&email=" + application.getTkProfileEmail();
                Log.d("COLONNA",url);

            }

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            MyDatabase db=new MyDatabase(getApplicationContext());
            db.open();  //apriamo il db

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    tickets = jsonObj.getJSONArray("MY_TICKET");

                    // looping through All Tickets



                    for (int i = 0; i < tickets.length(); i++) {
                        JSONObject c = tickets.getJSONObject(i);

                        int id = c.getInt("idTicketEmesso");
                        String idTicket = c.getString("idTicket");
                        String titoloSup = c.getString("titoloSup");
                        String codice = c.getString("codice");
                        String photo = c.getString("codice") + ".jpg";
                        String cWeb = c.getString("cWeb");
                        String usato = c.getString("flagUsato");
                        String feedback = c.getString("flagFeedback");
                        String dataDownload = c.getString("dataDownload");
                        //String dataScadenza = c.getString("dataScadenza");
                        String qta = c.getString("qta");


                        //listx.add(new Ticket(id,categoria,codice,titolo,titoloSup,photo,scaricati,mediaVoto));


                        db.insertMyTicket (id,idTicket,codice,titoloSup,cWeb,qta,usato,feedback,dataDownload);
                        Log.d("COLONNA","Inserito " + i);



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

            //goAhead();
            // Dismiss the progress dialog
            //if (pDialog.isShowing())
            //     pDialog.dismiss();

            // list = result;
        }
    }





}


