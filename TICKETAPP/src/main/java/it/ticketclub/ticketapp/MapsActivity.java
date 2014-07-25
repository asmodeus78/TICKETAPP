package it.ticketclub.ticketapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;



public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Boolean StatusUpdate=false;
    static JSONArray distMappa = null;

    private static ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        pDialog = new ProgressDialog(MapsActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Attendere Prego...");
        pDialog.setMessage("Sto caricando i distributori...");
        pDialog.setCancelable(false);
        pDialog.setProgress(0);
        pDialog.show();


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if(!StatusUpdate) {
                    makeUseOfNewLocation(location);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void addPoint(String nominativo,String lat,String lon, String indirizzo){
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title(nominativo).snippet(indirizzo)).showInfoWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    public void makeUseOfNewLocation(Location location){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),12.0f));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Tu sei qui!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_place))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Tu sei qui!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
        StatusUpdate = true;
    }

    private void setUpMap() {
        new getDistributori().execute();
        //mMap.addMarker(new MarkerOptions().position(new LatLng(41.0900208, 14.255227)).title("Tabaccheria n. 34").snippet("Viale del Consiglio, 41 Santa Maria Capua Vetere")).showInfoWindow();
        //mMap.addMarker(new MarkerOptions().position(new LatLng(41.0335837, 14.3802024)).title("Caffè di notte").snippet("Via Salvator Rosa, Caserta")).showInfoWindow();
    }

    public class getDistributori extends AsyncTask<Void, Void, Void> {

        LinkedList list;
        View vista;

        public getDistributori() {
            this.list = list;
            this.vista = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //


            //ringProgressDialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://www.ticketclub.it/APP/ticket_view.php?CMD=DISTRIBUTORI", ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    distMappa = jsonObj.getJSONArray("DISTRIBUTORI");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            for (int i = 0; i < distMappa.length(); i++) {
                JSONObject c = null;
                try {
                    c = distMappa.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String lat = null;
                try {
                    lat = c.getString("lat");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String lon = null;
                try {
                    lon = c.getString("lon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String nominativo = null;
                try {
                    nominativo = c.getString("nominativo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String indirizzo = null;
                try {
                    indirizzo = c.getString("indirizzo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("COLONNA3",nominativo);

                addPoint(nominativo,lat,lon,indirizzo);


                // mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title(nominativo).snippet(indirizzo)).showInfoWindow();
            }

            if(pDialog.isShowing()) {
                pDialog.dismiss();
            }

        }
    }

}
