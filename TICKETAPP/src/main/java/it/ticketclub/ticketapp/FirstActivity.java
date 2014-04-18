package it.ticketclub.ticketapp;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

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




        actionBar.addTab(
                actionBar.newTab()
                        .setText("")
                        .setCustomView(R.layout.tab_ico_0)
                        .setTabListener(this));



        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_1)
                        .setText("")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_2)
                        .setText("")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_3)
                        .setText("")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_4)
                        .setText("")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_5)
                        .setText("")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_6)
                        .setText("")
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(R.layout.tab_ico_7)
                        .setText("")
                        .setTabListener(this));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater minf = getMenuInflater();
        minf.inflate(R.menu.first,menu);

        mSpinnerItem = menu.findItem(R.id.action_citta);

        setupSpinner(mSpinnerItem);

        return super.onCreateOptionsMenu(menu);
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
            return PlaceholderFragment.newInstance(position + 1);


        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return "Tutte le categorie"; //getString(R.string.title_section1).toString(); //.toUpperCase(l);
                case 1:
                    return "Ristorazione"; //.toUpperCase(l);
                case 2:
                    return "Benessere;"; //.toUpperCase(l);
                case 3:
                    return "Viaggi e Svago";//getString(R.string.title_section4); //.toString(); //.toUpperCase(l);
                case 4:
                    return "Casa e Servizi"; //getString(R.string.title_section5).toString(); //.toUpperCase(l);
                case 5:
                    return "Sport e Motori"; //.toString(); //.toUpperCase(l);
                case 6:
                    return "Shopping"; //getString(R.string.title_section7).toString(); //.toUpperCase(l);
                case 7:
                    return "Eventi";//getString(R.string.title_section8).toString(); //.toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ProgressDialog pDialog;

        // URL to get tickets JSON
        private static String url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=TK";

        // JSON Node names
        private static final String TAG_ID = "idTicket";
        private static final String TAG_CODICE = "codice";
        private static final String TAG_TITOLO = "titolo";
        private static final String TAG_TITOLO_SUP = "titoloSup";
        //private static final String TAG_TICKET_SCARICATI = "nTicket";
        //private static final String TAG_VOTO = "mediaVoti";


        // tickets JSONArray
        JSONArray tickets = null;

        // Nuova Gestione Liste
        List list = new LinkedList();

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {



            Log.d("colonna","HO CREATO LA VISTA");

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);




            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

            // Calling async task to get json
            new GetTickets().execute();

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


    private void setupSpinner(MenuItem item) {
        //  item.setVisible(getSupportActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_LIST);
        //item.setVisible(ab.getNavigationMode() == ActionBar.NAVIGATION_MODE_LIST);

        View view = item.getActionView();
        Context context = getSupportActionBar().getThemedContext(); //to get the declared theme
        if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;



            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(context,
                    R.array.spinner_data,
                    R.layout.support_simple_spinner_dropdown_item);
            listAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(listAdapter);


        }
    }





}


