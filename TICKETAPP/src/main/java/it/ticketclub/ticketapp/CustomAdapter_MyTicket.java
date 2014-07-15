package it.ticketclub.ticketapp;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import it.ticketclub.ticketapp.Ticket;

/**
 * Created by Gio on 15/04/2014.
 */
public class CustomAdapter_MyTicket extends ArrayAdapter<Ticket2> {

    public static final File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/" + getContext().getApplicationInfo().packageName + "/cache/");


    public CustomAdapter_MyTicket(Context context, int textViewResourceId,
                         List<Ticket2> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewOptimize(position, convertView, parent);
    }

    public View getViewOptimize(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_my_ticket, null);
            viewHolder = new ViewHolder();

            viewHolder.TK_id = (TextView)convertView.findViewById(R.id.TK_id);
            viewHolder.TK_codice = (TextView)convertView.findViewById(R.id.TK_codice);
            //viewHolder.TK_titolo = (TextView)convertView.findViewById(R.id.TK_titolo);


                viewHolder.TK_titoloSup = (TextView) convertView.findViewById(R.id.TK_titoloSup);
                viewHolder.TK_Luogo = (TextView) convertView.findViewById(R.id.TK_Luogo);


            viewHolder.TK_image  = (ImageView)convertView.findViewById(R.id.TK_image);
           // viewHolder.TK_voto  = (RatingBar)convertView.findViewById(R.id.TK_voto);
            viewHolder.TK_qta  = (TextView)convertView.findViewById(R.id.TK_qta);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Ticket2 ticket = getItem(position);

        viewHolder.TK_id.setText(ticket.getId().toString());
        viewHolder.TK_codice.setText(ticket.getCodice().toString());
       // viewHolder.TK_titolo.setText(ticket.getTitolo());


        viewHolder.TK_qta.setText(ticket.getQta());

        try {
            viewHolder.TK_titoloSup.setText(ticket.getTitoloSup().split(" - ")[0]);
            viewHolder.TK_Luogo.setText(ticket.getTitoloSup().split(" - ")[1]);
        }catch(ArrayIndexOutOfBoundsException e){
            viewHolder.TK_titoloSup.setText(ticket.getTitoloSup());
            viewHolder.TK_Luogo.setText("");
        }
        //viewHolder.TK_scaricati.setText("Scaricati: " + ticket.getScaricati().toString());

        //Log.d("colonna1",ticket.getMediaVoti().toString());

        //viewHolder.TK_voto.setRating(ticket.getMediaVoti());
        //viewHolder.TK_voto.setIsIndicator(true);


        String CheckFile = ticket.getFoto();

        if (new File(path,CheckFile).exists()){
            Bitmap bMap = BitmapFactory.decodeFile(path + "/" + CheckFile);
            viewHolder.TK_image.setImageBitmap(bMap);
            Log.d("colonna","carico foto da sd");
        }else {
            new DownloadImageTask(viewHolder.TK_image).execute(ticket.getFoto());
            Log.d("colonna","scarico il file della foto da internet: " + ticket.getFoto());
        }



        // profile_photo.setImageBitmap(mIcon_val);

        return convertView;
    }

    private class ViewHolder {
        public TextView TK_id;
        public TextView TK_codice;
        //public TextView TK_titolo;
        public TextView TK_titoloSup;
        public ImageView TK_image;
        public TextView TK_qta;
        public TextView TK_Luogo;
        //public TextView TK_scaricati;
    }


}

