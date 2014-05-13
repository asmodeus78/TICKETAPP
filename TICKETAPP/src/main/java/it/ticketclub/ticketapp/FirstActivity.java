package it.ticketclub.ticketapp;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

        actionBar.addTab(actionBar.newTab().setText("").setCustomView(R.layout.tab_ico_0).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_1).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_2).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_3).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_4).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_5).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_6).setText("").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_7).setText("").setTabListener(this));
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
            //return PlaceholderFragment.newInstance(position + 1);

            switch (position){

                case 0:
                    //TUTTI I TICKET
                    return PlaceholderFragment.newInstance(0,"");
                case 1:
                    //Ristorazione
                    return PlaceholderFragment.newInstance(1,"Ristorazione");
                case 2:
                    //Benessere
                    return PlaceholderFragment.newInstance(2,"Benessere");
                case 3:
                    //Viaggi e Svago
                    return PlaceholderFragment.newInstance(3,"Viaggi e Svago");
                case 4:
                    //Casa e Servizi
                    return PlaceholderFragment.newInstance(4,"Casa e Servizi");
                case 5:
                    //Sport Motori
                    return PlaceholderFragment.newInstance(5,"Sport e Motori");
                case 6:
                    //Shopping
                    return PlaceholderFragment.newInstance(6,"Shopping");
                case 7:
                    //Eventi
                    return PlaceholderFragment.newInstance(7,"Eventi");
            }

            return null;


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

        private static ProgressDialog pDialog;


        public static String categoria="";
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
        public static PlaceholderFragment newInstance(int sectionNumber, String Categoria) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_STRING,Categoria);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {




            Log.d("colonna","HO CREATO LA VISTA");

            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //LinkedList listaa = new LinkedList();

            new GetTicketsFromDb() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //pDialog = new ProgressDialog(getActivity());
                    //pDialog.setMessage("Attendere prego...");
                   // pDialog.setCancelable(true);
                   // pDialog.show();

                    //if (pDialog.getProgress()==100){
                    //    pDialog.dismiss();
                    //}


                    categoria = getArguments().getString(ARG_SECTION_STRING);
                    Log.d("DRAGON",categoria);
                }

                @Override
                protected void onPostExecute(LinkedList result) {
                   // super.onPostExecute(result);

                    ListView listView = (ListView)vista.findViewById(R.id.ListViewDemo);
                    CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.row_ticket, result);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                           // When clicked, show a toast with the TextView text
                           /* Toast.makeText(getActivity().getApplicationContext(),
                                    ((TextView) view.findViewById(R.id.TK_id)).getText(), Toast.LENGTH_SHORT).show();
                           */


                            //********************************************************************************************************


                            final Intent intent2 = new Intent(getActivity(),SecondActivity.class); // SWIPE E TAB + JSON NOT VIEW
                            intent2.putExtra("id",((TextView) view.findViewById(R.id.TK_id)).getText());
                            intent2.putExtra("codice",((TextView) view.findViewById(R.id.TK_codice)).getText());

                            startActivity(intent2); // Launch the Intent
                            //getActivity().finish(); // We finish the current Activity
                            //********************************************************************************************************

                        }
                    });

                    //if (pDialog.isShowing()) {
                    //    pDialog.dismiss();
                    //}
                }


            }.execute(rootView);




            //View rootView = inflater.inflate(R.layout.activity_home, container, false);
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

                //Log.d("Response: ", "> " + jsonStr);

                MyDatabase db=new MyDatabase(getActivity().getApplicationContext());
                db.open();  //apriamo il db
                Cursor c;
                if (categoria!="") {
                     c = db.fetchTicketByCateg(categoria);
                }else{
                     c = db.fetchTicket();
                }
                getActivity().startManagingCursor(c);


                Log.d("DATABASE: ", "> " + c.getCount());



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


