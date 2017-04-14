package com.example.webapps.googleassigment_test;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Database.DBhelper;
import Database.Location_Data;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    public AlertDialog.Builder alertDialog;
    String provider;
    LatLng lastLatLng;
    String latvalue, lngvalue;
    Location lastLoc;
    double lat, lng;
    Location mLocation;
    Marker mCurrentMarker;
    AlertDialog gpsDialog;
    private GoogleMap mMap;
    private Context context;
    private Activity activity;
    private LocationManager locMan;
    private DBhelper DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = getApplicationContext();
        activity = this;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // permission has been granted, continue as usual
            Log.d("Tag", "location persmisson checking In OnCreate");
            CheckingLocationPermission();
        }
    }
    private void CheckingLocationPermission() {

        if (!checkPermission()) {
            requestPermission();
        } else {
            Toast.makeText(context, "Permission already granted.", Toast.LENGTH_SHORT).show();
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Log.d("Tag", "GPS true" + locMan);
            Criteria criteria = new Criteria();
            provider = locMan.getBestProvider(criteria, false);
            Log.d("Tag", "checking best provider" + provider);
            if (provider != null && !provider.equals("")) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                lastLoc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (isGpsEnabled()) {
                    lastLoc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLoc != null) {
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(this);
                        showLocationDetailsNMarker(lastLoc.getLatitude(),lastLoc.getLongitude());

                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Granted, Now you can access location data.", Toast.LENGTH_SHORT).show();
                    locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    lastLoc = locMan.getLastKnownLocation(NETWORK_PROVIDER);
                     if (lastLoc != null) {
                        showLocationDetailsNMarker(lastLoc.getLatitude(),lastLoc.getLongitude());
                    }

                } else {
                    //Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                    Toast.makeText(context, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MapsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean isGpsEnabled() {
        boolean isGpsEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i("TAG", "gps enabled " + isGpsEnabled + isNetworkEnabled);
        if (isGpsEnabled || isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            locMan.requestSingleUpdate(LocationManager.GPS_PROVIDER,this,null);
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 100, this);
            return true;
        } else {
            Log.d("Tag", "GPS check" + locMan);
            showSettingsAlert();
            return false;
        }
    }

    public void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("GPS is settings");
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            lastLoc = locMan.getLastKnownLocation(GPS_PROVIDER);
            Log.d("Tag", "Last Known LocaTION" + lastLoc);
            if (lastLoc != null) {
                lat = lastLoc.getLatitude();
                lng = lastLoc.getLongitude();
                lastLatLng = new LatLng(lat, lng);
                latvalue = Double.toString(lat);
                lngvalue = Double.toString(lng);
                Log.d("Tag", "last location" + latvalue + lngvalue);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

          }
    String location;
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.map_address);
        location = locationSearch.getText().toString().trim();
        if(!TextUtils.isEmpty(location)){
            showLocationDetailsNMarker(location);
        }else{
            Toast.makeText(context, "Please enter location", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSendingData(View v) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        SimpleDateFormat stime = new SimpleDateFormat("yymmddHHmmss");
        String uniqnecode = stime.format(new Date());
        DbHelper = new DBhelper(MapsActivity.this);
        Log.d("Tag", "deatils values on lication click" + lat + lng + location + currentDateandTime + uniqnecode);
       double lagvalue=lastLoc.getLatitude();
        double latvalue=lastLoc.getLongitude();
        Log.d("Tag","Location value inserting : "+lat+"   : "+lng);
        String latvalue1 = Double.toString(lagvalue);
        String lngvalue1 = Double.toString(lagvalue);
        Location_Data location_data = new Location_Data(String.valueOf(lastLatLng.latitude), String.valueOf(lastLatLng.longitude),locationAddress , currentDateandTime, uniqnecode);
      //  Location_Data location_data = new Location_Data(lat, lng , currentDateandTime, uniqnecode);

        DbHelper.open();
        DbHelper.insertLocationDetails(location_data);
        DbHelper.close();
    }public void onShowLocation(View v){

        Intent showlocation=new Intent(this,ShowLocationData.class);
        startActivity(showlocation);

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            mLocation = location;
            if (mMap != null) {
                showLocationDetailsNMarker(location.getLatitude(),location.getLongitude());
            }
        }
    }

    private void showLocationDetailsNMarker(double lat,double lng){
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(lat, lng, getApplicationContext(), new GeocoderHandler());
    }

    private void showLocationDetailsNMarker(String location){
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getLocationFromAddress(location, getApplicationContext(), new GeocoderHandler());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

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

    String locationAddress;
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            double lat=0.0d,lng=0.0d;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    lat=bundle.getDouble("lat",0.0d);
                    lng=bundle.getDouble("lng",0.0d);

                    lastLatLng=new LatLng(lat,lng);
                    Log.d("Tag","Location value  : "+lat+"   : "+lng);
                    break;
                default:
                    locationAddress = null;
            }
            // tvAddress.setText(locationAddress);
            if(lat>0 && mMap!=null){
                LatLng currentLocation = new LatLng(lat, lng);
                //clear last/previous markers
                mMap.clear();
                mCurrentMarker= mMap.addMarker(new MarkerOptions().position(currentLocation).title(locationAddress));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }else{
                Toast.makeText(MapsActivity.this, "lat and long or address not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
