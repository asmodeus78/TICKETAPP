package it.ticketclub.ticketapp;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;


/**
 * Created by Gio on 15/04/2014.
 */
public class DemoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        /*ListView listView = (ListView)findViewById(R.id.ListViewDemo);

        List list = new LinkedList();

        list.add(new Ticket("001","MAC DONALD"));
        list.add(new Ticket("002","BUSINESS ORIENTED"));
        list.add(new Ticket("003","ANTONIO & ANTONIO"));

        CustomAdapter adapter = new CustomAdapter(this, R.layout.row_item, list);
        listView.setAdapter(adapter);*/


    }








}
