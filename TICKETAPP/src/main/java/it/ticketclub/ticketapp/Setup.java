package it.ticketclub.ticketapp;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by Gio on 08/05/2014.
 */
public class Setup extends Application {

    public static File root = Environment.getExternalStorageDirectory();
    File path = new File(root.getAbsolutePath()+ "/Android/data/it.ticketclub.ticketapp/cache/");

    private String tkStatusLogin;
    private String tkProfileName;
    private String tkProfileEmail;
    private String tkProfileImageId;
    private String tkProfileCrediti;
    private String tkID;

    private String tkCitta;



    /*public Setup(){
        this.path = path;
    }*/

    // GET IN CLASSE
    public String getTkProfileName(){
        return tkProfileName;
    }
    public String getTkProfileEmail(){
        return tkProfileEmail;
    }
    public String getTkProfileImageId(){
        return tkProfileImageId;
    }
    public String getTkID(){
        return tkID;
    }
    public String getTkProfileCrediti(){return tkProfileCrediti;}
    public String getTkStatusLogin(){
        return tkStatusLogin;
    }
    public File getPath(){
        return path;
    }

    public String getTkCitta(){return tkCitta;}

    // SET IN CLASS
    public void setTkProfileName(String s){
        tkProfileName = s;
    }
    public void setTkProfileEmail(String s){
        tkProfileEmail = s;
    }
    public void setTkProfileImageId(String s){
        tkProfileImageId = s;
    }
    public void setTkID(String s){
        tkID = s;
    }
    public void setTkProfileCrediti(String s){tkProfileCrediti = s;}
    public void setTkStatusLogin(String s){
        tkStatusLogin = s;
    }
    public void setPath(File path){
        this.path = path;
    }

    public void setTkCitta(String s){tkCitta=s;}

}
