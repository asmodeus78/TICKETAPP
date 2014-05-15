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
import java.util.List;

/**
 * Created by Gio on 15/05/2014.
 */




/**
 * Created by Gio on 15/04/2014.
 */
public class CustomAdapter_Feedback extends ArrayAdapter<Feedback> {

    public static final File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/" + getContext().getApplicationInfo().packageName + "/cache/");


    public CustomAdapter_Feedback(Context context, int textViewResourceId,
                         List<Feedback> objects) {
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
            convertView = inflater.inflate(R.layout.row_feedback, null);
            viewHolder = new ViewHolder();

            viewHolder.FD_fotoUser = (ImageView)convertView.findViewById(R.id.FD_fotoUser);
            viewHolder.FD_nominativo = (TextView)convertView.findViewById(R.id.FD_nominativo);
            viewHolder.FD_voto = (RatingBar)convertView.findViewById(R.id.FD_voto);
            viewHolder.FD_commento  = (TextView)convertView.findViewById(R.id.FD_commento);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Feedback feedback = getItem(position);



        viewHolder.FD_nominativo.setText(feedback.getNominativo());

        viewHolder.FD_voto.setRating(Float.parseFloat(feedback.getVoto()));
        viewHolder.FD_voto.setIsIndicator(true);
        viewHolder.FD_commento.setText(feedback.getCommento());


        if (feedback.getIdImg()!="null" && feedback.getIdImg()!="") {
            String CheckFile = feedback.getIdImg();
            if (new File(path, CheckFile).exists()) {
                Bitmap bMap = BitmapFactory.decodeFile(path + "/" + CheckFile);
                viewHolder.FD_fotoUser.setImageBitmap(bMap);
                Log.d("colonna", "carico foto da sd");
            } else {
                new DownloadImageTask(viewHolder.FD_fotoUser).execute(feedback.getIdImg());
                Log.d("colonna", "scarico il file della foto da internet");
            }

        }

        // profile_photo.setImageBitmap(mIcon_val);

        return convertView;
    }

    private class ViewHolder {
        public TextView FD_nominativo;
        public ImageView FD_fotoUser;
        public RatingBar FD_voto;
        public TextView FD_commento;
    }


}

