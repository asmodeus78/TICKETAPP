package it.ticketclub.ticketapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;


class DownloadImageTask2 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public static final File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/" + getClass().getPackage().getName() + "/cache/");

    public String codeimage="";

    public DownloadImageTask2() {

    }

    protected Bitmap doInBackground(String... urls) {
        Log.d("colonna", path.toString());

        String urldisplay;
        if (urls[0].length()>30){
            urldisplay = "" + urls[0];
        }else {
            urldisplay = "http://www.ticketclub.it/TICKET_NEW/biglietti/" + urls[0];
        }

        codeimage = urls[0];

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

    protected void onPostExecute(Bitmap result) {
        File file = new File(path,codeimage);
        if (!file.exists()){
            persistImage(result,codeimage,path);
        }
        //bmImage.setImageBitmap(result);
    }

    private static void persistImage(Bitmap bitmap, String name, File path2) {


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