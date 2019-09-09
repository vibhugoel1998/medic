package com.example.john.medic;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by john on 3/12/2018.
 */

public class MyIntentService extends IntentService {
    public static final String TAG="INService";
    Double latitude=0.0,longitude=0.0;
    ArrayList<String> cclocation=new ArrayList<String>();
    ArrayList<String> userdetails=new ArrayList<String>();
    private DatabaseReference mDatabase;


    public MyIntentService() {
        super("MYSERVICE");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        send_latlong();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        userdetails=intent.getStringArrayListExtra("userdetails");

    }

    public void send_latlong() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: latitude = " + location.getLatitude());
                Log.d(TAG, "onLocationChanged: longitude = " + location.getLongitude());

                cclocation.add(0, String.valueOf(location.getLatitude()));
                cclocation.add(1,String.valueOf(location.getLongitude()));

                latitude = Double.valueOf(String.valueOf(location.getLatitude()));
                longitude = Double.valueOf(String.valueOf(location.getLongitude()));

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(userdetails.get(0)).child("location").child("latitude").setValue(latitude+"");
                mDatabase.child("users").child(userdetails.get(0)).child("location").child("longitude").setValue(longitude+"");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                100,
                0,
                locationListener
        );

    }
}
