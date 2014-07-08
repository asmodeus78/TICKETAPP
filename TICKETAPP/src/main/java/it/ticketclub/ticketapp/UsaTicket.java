package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Gio on 08/07/2014.
 */
public class UsaTicket extends Activity
{

    Animation slideup;
    ImageView talloncino;
    private static final String IMAGEVIEW_TAG = "The Android Logo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_usa_ticket);


        talloncino = (ImageView) findViewById(R.id.talloncino);

        slideup = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup);
        talloncino.setTag(IMAGEVIEW_TAG);

        talloncino.setOnLongClickListener(new MyClickListener());



        talloncino.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                talloncino.setAnimation(slideup);

                return true;
            }
        });









    }


    private final class MyClickListener implements View.OnLongClickListener {

        // called when the item is long-clicked
        @Override
        public boolean onLongClick(View view) {

        // create it from the object's tag
        ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        view.startDrag( data,
        shadowBuilder, //drag shadow
        view, //local data about the drag and drop operation
         0   //no needed flags
            );
            	            view.setVisibility(View.INVISIBLE);
            return true;
         }
    }




}
