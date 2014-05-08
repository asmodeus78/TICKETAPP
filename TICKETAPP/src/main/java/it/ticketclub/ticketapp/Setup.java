package it.ticketclub.ticketapp;

import android.os.Environment;

import java.io.File;

/**
 * Created by Gio on 08/05/2014.
 */
public class Setup {

    public static File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/it.ticketclub.ticketapp/cache/");



    public Setup(){

        this.path = path;

    }

    public File getPath(){
        return path;
    }


    public void setPath(File path){
        this.path = path;
    }

}
