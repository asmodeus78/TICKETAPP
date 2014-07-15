package it.ticketclub.ticketapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Gio on 08/07/2014.
 */
public class UsaTicket extends Activity
{
    private static ProgressDialog pDialog;
    public static String url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=USA";
    static JSONArray myTicketUsato = null;

    Animation slideup;
    ImageView talloncino;
    int touch=0;

    String codice,id,nome,qta;

    private static final String IMAGEVIEW_TAG = "The Android Logo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_usa_ticket);

        Bundle e = getIntent().getExtras();


        if (e != null) {
            codice = e.getString("codice");
        }
        if (e != null) {
            id = e.getString("id");
        }
        if (e != null) {
            nome = e.getString("nome");
        }
        if (e != null) {
            qta = e.getString("qta");
        }



        ImageView TK_image = (ImageView) findViewById(R.id.TK_image);
        VerticalTextView txtConvenzionato = (VerticalTextView) findViewById(R.id.txtConvenzionato);
        VerticalTextView txtValidoN = (VerticalTextView) findViewById(R.id.txtValidoN);



        Setup conf = new Setup();

        Bitmap bMap = BitmapFactory.decodeFile(conf.getPath() + "/" +  codice + ".jpg");
        TK_image.setImageBitmap(bMap);

        txtConvenzionato.setText(nome);

        if (qta.contentEquals("1")) {
            txtValidoN.setText("Valido per " + qta + " persona");
        }else{
            txtValidoN.setText("Valido per " + qta + " persone");
        }

        talloncino = (ImageView) findViewById(R.id.talloncino);

        slideup = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup);
        talloncino.setTag(IMAGEVIEW_TAG);

        //talloncino.setOnLongClickListener(new MyClickListener());

        talloncino.setOnTouchListener(new View.OnTouchListener() {
            int prevX,prevY,primaY;




            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final RelativeLayout.LayoutParams par=(RelativeLayout.LayoutParams)v.getLayoutParams();
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                    {
                        par.topMargin+=(int)event.getRawY()-prevY;
                        prevY=(int)event.getRawY();
                        v.setLayoutParams(par);

                        return true;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        par.topMargin+=(int)event.getRawY()-prevY;
                        v.setLayoutParams(par);
                        return true;
                    }
                    case MotionEvent.ACTION_DOWN:
                    {
                        prevY=(int)event.getRawY();
                        par.bottomMargin=-2*v.getHeight();
                        v.setLayoutParams(par);

                        if (touch==0){
                            MediaPlayer mp = MediaPlayer.create(UsaTicket.this, R.raw.paper_rip);
                            mp.start();
                            talloncino.startAnimation(slideup);
                            touch=1;

                            pDialog = new ProgressDialog(UsaTicket.this);
                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pDialog.setTitle("Attendere Prego...");
                            pDialog.setMessage("Sto inviando il feedback...");
                            pDialog.setCancelable(false);
                            pDialog.setProgress(0);
                            pDialog.show();

                            url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=USA";
                            url += "&idticketemesso=" + id;



                            Log.d("COLONNA5",url);
                            new TicketUsato().execute();
                        }

                        return true;
                    }
                }




                return false;
            }
        });


        /*talloncino.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {



                return true;
            }
        });*/

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

        view.startDrag(data,
                shadowBuilder, //drag shadow
                view, //local data about the drag and drop operation
                0   //no needed flags
        );
            	            view.setVisibility(View.INVISIBLE);
            return true;
         }


    }



    private class TicketUsato extends AsyncTask<Void, Void, Void> {


        LinkedList list;
        View vista;

        String messaggio = "";

        @Override
        protected Void doInBackground(Void... params) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);



            Log.d("Response: ", "> " + jsonStr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    myTicketUsato = jsonObj.getJSONArray("USA");

                    // looping through All Tickets



                    for (int i = 0; i < myTicketUsato.length(); i++) {
                        JSONObject c = myTicketUsato.getJSONObject(i);


                        messaggio = c.getString("messaggio");

                        Log.d("COLONNA5",messaggio);






                        //db.insertTicket(id,categoria,codice,titolo,titoloSup,Float.parseFloat(mediaVoto),Integer.parseInt(scaricati),descrizione,indirizzo,lat,lon,nominativo,telefono,sito,dataScadenza,prezzoCr,seo);
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

            if(pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (messaggio.equals("OK")){
                MyDatabase db=new MyDatabase(getApplicationContext());
                db.open();  //apriamo il db

                Log.d("COLONNA5 S",id);
                //db.deleteMyTicketFeedbackOk(idt);
                db.updateMyTicketUsatoOk(id);

                Toast.makeText(getApplicationContext(),"TICKET USATO",Toast.LENGTH_LONG).show();

                final Intent intent = new Intent(getApplicationContext(),MyTicket.class); // SWIPE E TAB + JSON NOT VIEW
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); // Launch the Intent

                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Errore riprova più tardi",Toast.LENGTH_LONG).show();
            }

        }



    }




}
