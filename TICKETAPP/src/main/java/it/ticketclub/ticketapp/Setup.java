package it.ticketclub.ticketapp;

import android.app.Application;
import android.os.Environment;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

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

    public Boolean getTkStatusGps() {
        return tkStatusGps;
    }

    public void setTkStatusGps(Boolean tkStatusGps) {
        this.tkStatusGps = tkStatusGps;
    }

    private Boolean tkStatusGps = false;

    private String tkCitta;
    private String tkCerca;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    private Double lat;
    private Double lon;



    private static Setup instance;

    public void onCreate()
    {
        super.onCreate();
        instance = this;

        Parse.initialize(this, "t8ZFE43JWi0GWw0xv56T4PsQfp2YhUpQfszTuZr3", "GXjxj3LcZTnHZFc3fcy57vCoenQSqMHB4RgfOR3J");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        PushService.setDefaultPushCallback(this, SplashActivity.class);

    }



    public static Setup getSetup() {
        return instance;
    }

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


    public String getTkCerca() {
        return tkCerca;
    }

    public void setTkCerca(String tkCerca) {
        this.tkCerca = tkCerca;
    }



}
