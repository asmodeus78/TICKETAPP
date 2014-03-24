package it.ticketclub.ticketapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.*;
import android.view.Display;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView webview = new WebView(this);
        setContentView(webview);


        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                //intent.putExtra("url", url);
                //startActivity(intent);
                webview.setInitialScale(1);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setLoadWithOverviewMode(true);
                webview.getSettings().setUseWideViewPort(true);
                webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                webview.setScrollbarFadingEnabled(false);
                webview.loadUrl(url);
                return true;
            }

        });

        webview.setInitialScale(1);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);


        String url;
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        } else {
            //url = "http://m.ticketclub.it/index.php?display=VGA&device=SAMSUNG/";
            url = "http://m.ticketclub.it/index.php?display=HD&device=IPHONE/";

        }
        webview.loadUrl(url);





        //webview.clearView();
        //webview.resolveSize(10,10);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash,menu);
        return true;
    }

}
