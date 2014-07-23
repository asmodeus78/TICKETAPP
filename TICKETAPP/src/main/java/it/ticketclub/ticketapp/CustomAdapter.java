package it.ticketclub.ticketapp;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import it.ticketclub.ticketapp.Ticket;

/**
 * Created by Gio on 15/04/2014.
 */
public class CustomAdapter extends ArrayAdapter<Ticket> {

    public static final File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/" + getContext().getApplicationInfo().packageName + "/cache/");


    public CustomAdapter(Context context, int textViewResourceId,
                                 List<Ticket> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewOptimize(position, convertView, parent);
    }

    public View getViewOptimize(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(crit, true);

        Location loc = locationManager.getLastKnownLocation(provider);
        Location loc2 = locationManager.getLastKnownLocation(provider);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_ticket, null);
            viewHolder = new ViewHolder();

            viewHolder.TK_id = (TextView)convertView.findViewById(R.id.TK_id);
            viewHolder.TK_codice = (TextView)convertView.findViewById(R.id.TK_codice);
            viewHolder.TK_titolo = (TextView)convertView.findViewById(R.id.TK_titolo);
            viewHolder.TK_titoloSup = (TextView)convertView.findViewById(R.id.TK_titoloSup);
            viewHolder.TK_image  = (ImageView)convertView.findViewById(R.id.TK_image);
            viewHolder.TK_voto  = (RatingBar)convertView.findViewById(R.id.TK_voto);
            viewHolder.TK_scaricati  = (TextView)convertView.findViewById(R.id.TK_scaricati);
            viewHolder.TK_km = (TextView)convertView.findViewById(R.id.TK_km);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Ticket ticket = getItem(position);

        viewHolder.TK_id.setText(ticket.getId().toString());
        viewHolder.TK_codice.setText(ticket.getCodice().toString());
        viewHolder.TK_titolo.setText(Html.fromHtml(ticket.getTitolo()));
        viewHolder.TK_titoloSup.setText(Html.fromHtml(ticket.getTitoloSup()));
        viewHolder.TK_scaricati.setText("Scaricati: " + ticket.getScaricati().toString());


        String km = "0";

        try {
            double myLat = locationManager.getLastKnownLocation(provider).getLatitude();
            double myLon = locationManager.getLastKnownLocation(provider).getLongitude();

            double destLat = Double.parseDouble(ticket.getLat());
            double destLon = Double.parseDouble(ticket.getLon());

            loc2.setLatitude(destLat);
            loc2.setLongitude(destLon);

            double kmTemp = loc.distanceTo(loc2) / 1000;

            DecimalFormat f = new DecimalFormat("#0.0");



            km = String.valueOf(f.format(kmTemp)).replace(",",".");


        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }





        viewHolder.TK_km.setText("a  " + km + " Km");

        //Log.d("colonna1",ticket.getMediaVoti().toString());

        viewHolder.TK_voto.setRating(ticket.getMediaVoti());
        viewHolder.TK_voto.setIsIndicator(true);


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
        public TextView TK_titolo;
        public TextView TK_titoloSup;
        public ImageView TK_image;
        public RatingBar TK_voto;
        public TextView TK_scaricati;
        public TextView TK_km;
    }

}

