package it.ticketclub.ticketapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;


public class DemoActivity2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listView = (ListView)findViewById(R.id.ListViewDemo);

        List list = new LinkedList();

        list.add(new Ticket("001","STELLA FILM","TC104140219046.jpg"));
        list.add(new Ticket("002","BUSINESS ORIENTED","TC104140184049.jpg"));
        list.add(new Ticket("003","UN POSTO AL SOLE","TC104140217020.jpg"));

        CustomAdapter adapter = new CustomAdapter(this, R.layout.row_ticket, list);
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo_activity2, menu);
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

}
