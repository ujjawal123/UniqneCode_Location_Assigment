package com.example.webapps.googleassigment_test;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Database.DBhelper;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by ujjaw on 14-04-2017.
 */


public class ShowMap extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener{
    public static final String TAG = "ShowMap";
    public AlertDialog.Builder alertDialog;
    Location lastLoc;
    double latvalue1, lngvalue1;
   TextView address, sortcode;
    AlertDialog gpsDialog;
    private GoogleMap mMap;
    private Context context;
    private Activity activity;
    //LatLng endLatLng;
    private LocationManager locMan;
    String l_address, l_code, l_lat, l_lng, l_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        context = getApplicationContext();
        Log.d("Tag","Activity 3");
        activity = this;

        intview();
        CheckingMap();
    }

    private void intview() {

        address=(TextView)findViewById(R.id.address);
        sortcode=(TextView) findViewById(R.id.sort_code);
        l_address = getIntent().getExtras().getString("address");
        l_code = getIntent().getExtras().getString("uniqne");
        l_lat = getIntent().getExtras().getString("Lat");
        l_lng = getIntent().getExtras().getString("Lng");
        l_time = getIntent().getExtras().getString("Time");
        latvalue1 = Double.parseDouble(l_lat);
        lngvalue1 = Double.parseDouble(l_lng);
        Log.d(TAG, " data on 3rd activity" + " " + l_address + " " + l_code + "  " + latvalue1 + "  " + lngvalue1 + "  " + l_time);

        address.setText(l_address);
        sortcode.setText(l_code);
    }

    private void CheckingMap() {

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            }



    private boolean isGpsEnabled() {
        boolean isGpsEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i("TAG", "gps enabled " + isGpsEnabled + isNetworkEnabled);
        if (isGpsEnabled || isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            locMan.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 100, this);
            return true;
        } else {
            Log.d("Tag", "GPS check" + locMan);
            showSettingsAlert();
            return false;
        }
    }

    public void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(ShowMap.this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // O pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        gpsDialog = alertDialog.create();
        gpsDialog.show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(latvalue1, lngvalue1);
        //Adding marker to that coordinate
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Tag", "providerEnable");
        if (lastLoc != null) {
            if (gpsDialog != null && gpsDialog.isShowing())
                gpsDialog.dismiss();
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Tag", "gps disable" + lastLoc);
        if (s.equals(GPS_PROVIDER))
            showSettingsAlert();
    }

}

