package it.ticketclub.ticketapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    String codice,id,nome,qta,cweb,usato;
    AlertDialog dialog;

    private static final String IMAGEVIEW_TAG = "The Android Logo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_usa_ticket);

        Bundle e = getIntent().getExtras();


        if (e != null) {
            codice = e.getString("codice");
            id = e.getString("id");
            nome = e.getString("nome");
            qta = e.getString("qta");
            cweb = e.getString("cweb");
            usato = e.getString("usato");
        }

        Log.d("COLONNAA",usato);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Mostra button clicked
                        final Intent intent2 = new Intent(getApplicationContext(), LasciaFeedback.class); // SWIPE E TAB + JSON NOT VIEW
                        intent2.putExtra("id", id);
                        intent2.putExtra("codice", codice);
                        startActivity(intent2); // Launch the Intent

                        break;

                    case DialogInterface.BUTTON_POSITIVE:
                        //Vota button clicked
                        //Log.d("COLONNA", "CLICK VOTA " + TK_ID);

                        final Intent intent = new Intent(getApplicationContext(),MyTicket.class); // SWIPE E TAB + JSON NOT VIEW
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent); // Launch the Intent

                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ticket usato!")
                .setMessage("Ora puoi lasciare un feedback per recuperare i crediti spesi!")
                .setPositiveButton("Vota più tardi", dialogClickListener)
                .setNegativeButton("VAI", dialogClickListener);

        dialog = builder.create();



        ImageView TK_image = (ImageView) findViewById(R.id.TK_image);
        VerticalTextView txtConvenzionato = (VerticalTextView) findViewById(R.id.txtConvenzionato);
        VerticalTextView txtValidoN = (VerticalTextView) findViewById(R.id.txtValidoN);
        final TextView codice_sicurezza = (TextView)findViewById(R.id.codice_sicurezza);



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
        codice_sicurezza.setText(cweb);

        if (usato.contentEquals("1")){
            talloncino.setVisibility(View.INVISIBLE);
            codice_sicurezza.setVisibility(View.INVISIBLE);
        }else {

            slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
            talloncino.setTag(IMAGEVIEW_TAG);
            talloncino.setOnTouchListener(new View.OnTouchListener() {
                int prevX
                        ,
                        prevY
                        ,
                        primaY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE: {
                            par.topMargin += (int) event.getRawY() - prevY;
                            prevY = (int) event.getRawY();
                            v.setLayoutParams(par);

                            return true;
                        }
                        case MotionEvent.ACTION_UP: {
                            par.topMargin += (int) event.getRawY() - prevY;
                            v.setLayoutParams(par);
                            return true;
                        }
                        case MotionEvent.ACTION_DOWN: {
                            prevY = (int) event.getRawY();
                            par.bottomMargin = -2 * v.getHeight();
                            v.setLayoutParams(par);

                            if (touch == 0) {
                                MediaPlayer mp = MediaPlayer.create(UsaTicket.this, R.raw.strappo);
                                codice_sicurezza.setVisibility(View.INVISIBLE);

                                mp.start();
                                talloncino.startAnimation(slideup);
                                touch = 1;



                                url = "http://www.ticketclub.it/APP/ticket_view.php?CMD=USA";
                                url += "&idticketemesso=" + id;


                                Log.d("COLONNA5", url);
                                new TicketUsato().execute();
                            }

                            return true;
                        }
                    }


                    return false;
                }
            });

        }


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



            if (messaggio.equals("OK")){
                MyDatabase db=new MyDatabase(getApplicationContext());
                db.open();  //apriamo il db

                Log.d("COLONNA5 S",id);
                //db.deleteMyTicketFeedbackOk(idt);
                db.updateMyTicketUsatoOk(id);

                Toast.makeText(getApplicationContext(),"TICKET USATO",Toast.LENGTH_LONG).show();

                dialog.show();


            }else{
                Toast.makeText(getApplicationContext(),"Errore riprova più tardi",Toast.LENGTH_LONG).show();
            }

        }



    }




}
