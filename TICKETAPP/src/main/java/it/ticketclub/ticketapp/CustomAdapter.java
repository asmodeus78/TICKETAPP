package it.ticketclub.ticketapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.List;


public class CustomAdapter extends ArrayAdapter<Ticket> {

    public static final File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/" + getContext().getApplicationInfo().packageName + "/cache/");

    private Location loc = new Location(LocationManager.NETWORK_PROVIDER);
    private Location loc2 = new Location(LocationManager.NETWORK_PROVIDER);

    private String km;


    public CustomAdapter(Context context, int textViewResourceId,
                                 List<Ticket> objects) {
        super(context, textViewResourceId, objects);



    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewOptimize(position, convertView, parent);
    }

    public View getViewOptimize(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        final int finalPosition = position;


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
            viewHolder.progress = (ProgressBar) convertView.findViewById(R.id.progress_spinner);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Ticket ticket = getItem(position);

        viewHolder.TK_id.setText(ticket.getId().toString());
        viewHolder.TK_codice.setText(ticket.getCodice());
        viewHolder.TK_titolo.setText(Html.fromHtml(ticket.getTitolo()));
        viewHolder.TK_titoloSup.setText(Html.fromHtml(ticket.getTitoloSup()));
        viewHolder.TK_scaricati.setText("Scaricati: " + ticket.getScaricati().toString());

        viewHolder.position = position;


        km="0";
        DecimalFormat f = new DecimalFormat("#0.0");
        km = String.valueOf(f.format(ticket.getDistanza())).replace(",", ".");

/*
        try {

            loc2.setLatitude(Double.parseDouble(ticket.getLat()));
            loc2.setLongitude(Double.parseDouble(ticket.getLon()));

            loc.setLatitude(Setup.getSetup().getLat());
            loc.setLongitude(Setup.getSetup().getLon());

            double kmTemp = loc.distanceTo(loc2) / 1000;
            Log.d("POSbb", String.valueOf(kmTemp));
            DecimalFormat f = new DecimalFormat("#0.0");
            km = String.valueOf(f.format(kmTemp)).replace(",", ".");

            ticket.setDistanza(kmTemp);

        }catch(NullPointerException e) {
            Log.d("POSXABERR","errore calcolo distanza");
        }catch (NumberFormatException e){
            Log.d("POSXABERR","errore non è un numero");
        }

*/


        viewHolder.TK_km.setText("a  " + km + " Km");

        //Log.d("colonna1",ticket.getMediaVoti().toString());

        viewHolder.TK_voto.setRating(ticket.getMediaVoti());
        viewHolder.TK_voto.setIsIndicator(true);


        String CheckFile = ticket.getFoto();




        if (new File(path,CheckFile).exists()){

            Bitmap bMap = BitmapFactory.decodeFile(path + "/" + CheckFile);



           /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bMap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] bitmapBytes = baos.toByteArray();

            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(bitmapBytes);
                byte[] hash = md.digest();

                try {
                    String md51 = returnHex(hash);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

*/




            viewHolder.TK_image.setImageBitmap(bMap);
            viewHolder.TK_image.setVisibility(View.VISIBLE);
            viewHolder.progress.setVisibility(View.GONE);
        }else {

            //new DownloadImageTask(viewHolder.TK_image).execute(ticket.getFoto());

            //new getImageUpdate().execute(viewHolder);
            viewHolder.TK_image.setVisibility(View.INVISIBLE);
            viewHolder.progress.setVisibility(View.VISIBLE);

            new AsyncTask<ViewHolder, Void, Bitmap>() {
                private ViewHolder v;
                public String codeimage="";

                @Override
                protected Bitmap doInBackground(ViewHolder... params) {
                    v = params[0];

                    String urldisplay;
                    if (v.TK_codice.getText().length()>30){
                        urldisplay = "" + v.TK_codice.getText() + ".jpg";
                    }else {
                        urldisplay = "http://www.ticketclub.it/TICKET_NEW/biglietti/" + v.TK_codice.getText() + ".jpg";
                    }

                    codeimage = v.TK_codice.getText() + ".jpg";

                    Bitmap mIcon11 = null;
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        //Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                    return mIcon11;

                    //return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);

                    File file = new File(path,codeimage);
                    if (!file.exists()){
                        persistImage(bitmap,codeimage,path);
                    }

                    if(v.position==position) {
                        v.progress.setVisibility(View.GONE);
                        v.TK_image.setVisibility(View.VISIBLE);
                        //v.TK_image.setImageBitmap(bitmap);

                        Bitmap bMap = BitmapFactory.decodeFile(path + "/" + v.TK_codice.getText() + ".jpg");
                        v.TK_image.setImageBitmap(bMap);
                    }
                }
            }.execute(viewHolder);

            ///viewHolder.TK_image.setImageResource(R.drawable.loading2);
            //new DownloadImageTask(viewHolder.TK_image).execute(ticket.getFoto());
            //new DownloadImageTask2().execute(ticket.getFoto());
            Log.d("colonna","scarico il file della foto da internet: " + ticket.getFoto());
        }





       // profile_photo.setImageBitmap(mIcon_val);

        return convertView;
    }

    static String returnHex(byte[] inBytes) throws Exception {
        String hexString = null;
        for (int i=0; i < inBytes.length; i++) { //for loop ID:1
            hexString +=
                    Integer.toString( ( inBytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        }                                   // Belongs to for loop ID:1
        return hexString;
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

        public ProgressBar progress;
        public int position;

    }



    private void persistImage(Bitmap bitmap, String name, File path2) {


        File imageFile = new File(path2, name);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            //Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
    }

/*
    private class getImageUpdate extends AsyncTask<ViewHolder, Void, Bitmap> {
        ImageView bmImage;
        private ViewHolder v;
        public String codeimage="";

        public getImageUpdate() {
            //this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(ViewHolder... params) {
            v = params[0];

            String urldisplay;
            if (v.TK_codice.getText().length()>30){
                urldisplay = "" + v.TK_codice.getText() + ".jpg";
            }else {
                urldisplay = "http://www.ticketclub.it/TICKET_NEW/biglietti/" + v.TK_codice.getText() + ".jpg";
            }

            codeimage = v.TK_codice.getText() + ".jpg";

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            File file = new File(path,codeimage);
            if (!file.exists()){
                persistImage(result,codeimage,path);
            }
            //bmImage.setImageBitmap(result);

            if (v.position==position) {
                v.progress.setVisibility(View.GONE);
                v.TK_image.setVisibility(View.VISIBLE);
                v.TK_image.setImageBitmap(result);
            }
        }

        private void persistImage(Bitmap bitmap, String name, File path2) {


            File imageFile = new File(path2, name);

            OutputStream os;
            try {
                os = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                //Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }
        }

    }
    */

    @Override
    public void notifyDataSetChanged() {
        //do your sorting here

        super.notifyDataSetChanged();
    }

}

