package it.ticketclub.ticketapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FirstActivity extends ActionBarActivity implements ActionBar.TabListener {

    GPSTracker gps;

    private MenuItem mSpinnerItem = null;

    private MenuItem mnuCity = null ;

    private String RICERCA_CITTA="";


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

        // Track app opens.
        //ParseAnalytics.trackAppOpened(getIntent());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        final LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);



        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);


        final ImageView logos = (ImageView) findViewById(R.id.logos);
        final ImageButton btCerca = (ImageButton) findViewById(R.id.cerca);
        final ImageButton btClose = (ImageButton) findViewById(R.id.btClose);
        final ImageButton btProfile = (ImageButton) findViewById(R.id.btProfile);
        final EditText textSearch = (EditText) findViewById(R.id.search_text);



        Setup application = (Setup) getApplication() ;
        RICERCA_CITTA = application.getTkCitta();
        String RICERCA_HOLD = application.getTkCerca();

        try {
            if (!RICERCA_HOLD.contentEquals("")) {
                logos.setVisibility(View.INVISIBLE);
                btClose.setVisibility(View.VISIBLE);
                textSearch.setVisibility(View.VISIBLE);
                btProfile.setVisibility(View.INVISIBLE);

                textSearch.setText(RICERCA_HOLD);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        textSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_SEARCH){
                    Log.d("COLONNA",textSearch.getText().toString());


                    FiltraTesto(textSearch.getText().toString());
                   // InputMethodManager.HIDE_NOT_ALWAYS;

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);





                }
                return false;
            }
        });


        btCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logos.setVisibility(View.INVISIBLE);
                btClose.setVisibility(View.VISIBLE);
                textSearch.setVisibility(View.VISIBLE);
                btProfile.setVisibility(View.INVISIBLE);
            }
        });


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logos.setVisibility(View.VISIBLE);
                btClose.setVisibility(View.INVISIBLE);
                textSearch.setVisibility(View.INVISIBLE);
                btProfile.setVisibility(View.VISIBLE);

                textSearch.setText("");
                FiltraTesto(textSearch.getText().toString());
            }
        });


        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApriProfilo();
            }
        });

        gps = new GPSTracker(this);

        if(!gps.canGetLocation()){
            Toast.makeText(getApplicationContext(), "Impossibile trovare la posizione", Toast.LENGTH_LONG).show();
            gps.showSettingsAlert();
        }













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


        actionBar.addTab(actionBar.newTab().setText("TUTTE").setTabListener(this));

        actionBar.addTab(actionBar.newTab().setText("RISTORAZIONE").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("BENESSERE").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("VIAGGI E SVAGO").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("CASA E SERVIZI").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("SPORT E MOTORI").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("SHOPPING").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("EVENTI").setTabListener(this));




    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater minf = getMenuInflater();
        minf.inflate(R.menu.first,menu);




        mnuCity = menu.findItem(R.id.txtCitySel);

        try{
            if (!RICERCA_CITTA.contentEquals("")) {
                mnuCity.setTitle(RICERCA_CITTA);

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


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


            /*case R.id.menu_profilo:
                ApriProfilo();
                return true;*/

            case R.id.action_salerno:
                mnuCity.setTitle("Salerno");
                FiltraCitta("SALERNO");
                return true;

            case R.id.action_avellino:
                mnuCity.setTitle("Avellino");
                FiltraCitta("AVELLINO");
                return true;

            case R.id.action_benevento:
                mnuCity.setTitle("Benevento");
                FiltraCitta("BENEVENTO");
                return true;

            case R.id.action_caserta:
                mnuCity.setTitle("Caserta");
                FiltraCitta("CASERTA");
                return true;

            case R.id.action_napoli:
                mnuCity.setTitle("Napoli");
                FiltraCitta("NAPOLI");
                return true;

            case R.id.action_tutte:
                mnuCity.setTitle("Tutte le Città");
                FiltraCitta("");
                return true;


            case R.id.action_salerno1:
                mnuCity.setTitle("Salerno");
                FiltraCitta("SALERNO");
                return true;

            case R.id.action_avellino1:
                mnuCity.setTitle("Avellino");
                FiltraCitta("AVELLINO");
                return true;

            case R.id.action_benevento1:
                mnuCity.setTitle("Benevento");
                FiltraCitta("BENEVENTO");
                return true;

            case R.id.action_caserta1:
                mnuCity.setTitle("Caserta");
                FiltraCitta("CASERTA");
                return true;

            case R.id.action_napoli1:
                mnuCity.setTitle("Napoli");
                FiltraCitta("NAPOLI");
                return true;

            case R.id.action_tutte1:
                mnuCity.setTitle("Tutte le Città");
                FiltraCitta("");
                return true;





            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void FiltraCitta(String city){
        application = (Setup) this.getApplication();
        application.setTkCitta(city);

        overridePendingTransition(0,0);
        Intent i = getIntent();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void FiltraTesto(String testo){
        application = (Setup) this.getApplication();
        application.setTkCerca(testo);

        overridePendingTransition(0,0);
        Intent i = getIntent();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(0, 0);

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











}


