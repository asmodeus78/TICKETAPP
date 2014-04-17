package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.IOException;


public class SplashActivity extends Activity {

    public static final File root = Environment.getExternalStorageDirectory();
    private static final String NOMEDIA_FILE = "sample.nomedia";

    private static final int UPDATE_REQUEST_ID = 1;

    private static final String TAG_LOG = SplashActivity.class.getName();

    private static final String START_TIME_KEY ="it.ticketclub.ticketapp.key.START_TIME_KEY";
    private static final String IS_DONE_KEY ="it.ticketclub.ticketapp.key.IS_DONE_KEY";

    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 3000L;
    private static final int GO_HEAD_WHAT = 1;


    private long mStartTime = -1L;
    private boolean mIsDone;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_HEAD_WHAT:
                    long elapsedTime = SystemClock.uptimeMillis() - mStartTime;
                    if (elapsedTime>=MIN_WAIT_INTERVAL && !mIsDone){
                        mIsDone = true;
                        goAhead();
                    }
                    break;
            }
        }
    };


    private void goAhead(){
        //final Intent intent = new Intent(this,MainActivity.class); // WEB VIEW
        //final Intent intent = new Intent(this,HomeActivity.class); // ONLY SWIBE E TAB
        final Intent intent = new Intent(this,FirstActivity.class); // SWIPE E TAB + JSON NOT VIEW
        //final Intent intent = new Intent(this,DemoActivity3.class); // JSON TICKET DOWNLOAD
        //final Intent intent = new Intent(this,DemoActivity2.class); // TICKET FOTO DOWNLOAD ED ADAPTER CUSTOM VIEW
        startActivity(intent); // Launch the Intent
        finish(); // We finish the current Activity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState != null){
            this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(isExternalStorageWritable()){


            File path = new File(root.getAbsolutePath()+ "/Android/data/" + getApplicationInfo().packageName + "/cache/");
            path.mkdirs();

            File file= new File(path,NOMEDIA_FILE);
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }

        if(mStartTime==-1){
            mStartTime = SystemClock.uptimeMillis();
        }
        final Message goAheadMessage = mHandler.obtainMessage(GO_HEAD_WHAT);

        /*final Intent updateIntent = new Intent(this,UpdateTicketList.class);
        startActivityForResult(updateIntent,UPDATE_REQUEST_ID);*/


        mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
        Log.d(TAG_LOG, "Handler message sent!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DONE_KEY,mIsDone);
        outState.putLong(START_TIME_KEY,mStartTime);
    }

    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        this.mIsDone = savedInstanceState.getBoolean(IS_DONE_KEY);
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
