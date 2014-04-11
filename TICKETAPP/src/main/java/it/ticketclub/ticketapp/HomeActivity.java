package it.ticketclub.ticketapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener {

    private MenuItem mSpinnerItem = null;


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

        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_ico_0).setText("").setTabListener(this));
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
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

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_home, container, false);


            return rootView;
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
