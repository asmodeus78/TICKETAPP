package it.ticketclub.ticketapp;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import it.ticketclub.ticketapp.Ticket;

/**
 * Created by Gio on 15/04/2014.
 */
public class CustomAdapter extends ArrayAdapter<Ticket> {

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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_ticket, null);
            viewHolder = new ViewHolder();
            viewHolder.TK_codice = (TextView)convertView.findViewById(R.id.TK_codice);
            viewHolder.TK_titolo = (TextView)convertView.findViewById(R.id.TK_titolo);
            viewHolder.TK_image  = (ImageView)convertView.findViewById(R.id.TK_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Ticket ticket = getItem(position);
        viewHolder.TK_codice.setText(ticket.getCodice());
        viewHolder.TK_titolo.setText(ticket.getTitolo());
        //viewHolder.TK_image.setImageBitmap(getBitmapFromURL(ticket.getFoto()));

        Log.d("colonna",ticket.getFoto());
        new DownloadImageTask(viewHolder.TK_image).execute(ticket.getFoto());
        //new DownloadImageTask((ImageView)convertView.findViewById(R.id.TK_image)).execute(ticket.getFoto());

        /*URL newurl = null;
        try {
            newurl = new URL(ticket.getFoto());
            viewHolder.TK_image.setImageBitmap(BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()));
        } catch (MalformedURLException e) {
            ///Log.e("aaa",ticket.getFoto());
            e.printStackTrace();
        } catch (IOException e) {
           /// Log.e("aaa",e.getMessage());
            e.printStackTrace();
        }*/

       // profile_photo.setImageBitmap(mIcon_val);

        return convertView;
    }

    private class ViewHolder {
        public TextView TK_codice;
        public TextView TK_titolo;
        public ImageView TK_image;
    }


}

