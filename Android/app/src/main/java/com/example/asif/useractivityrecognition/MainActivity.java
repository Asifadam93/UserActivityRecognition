package com.example.asif.useractivityrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.asif.useractivityrecognition.Interface.GoogleClientListener;
import com.example.asif.useractivityrecognition.Utils.GlobalFunctions;
import com.example.asif.useractivityrecognition.Utils.GoogleClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback, GoogleClientListener {

    private static final String TAG = "mMainActivity";
    private static final int AUTHORISATION_FILE_WRITE = 666;
    private static final int AUTHORISATION_LOCATION = 999;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add google client callback to receive onConnected status
        GoogleClient.getInstance(getApplicationContext()).mGoogleClientListener = this;

        findViewById(R.id.validateButtonView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.geoLocButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GeofencingActivity.class);
                startActivity(intent);
            }
        });

        /*//Récupération de la récupération d'écrire sur
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_AUTHORISATION_FILE_WRITE);
        }*/

    }

    private void startLocationMonitoring() {

        Log.i(TAG, "startLocationMonitoring");

        if (checkLocationPermission()) {

            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10 * 1000)
                    .setFastestInterval(5 * 1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            GoogleApiClient googleApiClient = GoogleClient.getInstance(this).getGoogleApiClient();

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    String postionText = "Lat : " + location.getLatitude() + ", Long : " + location.getLongitude();
                    Log.i(TAG, postionText);
                }
            });

        } else {
            askPermission();
        }
    }

    private boolean checkLocationPermission() {

        Log.i(TAG, "checkLocationPermission");

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {

        Log.i(TAG, "askPermission");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                AUTHORISATION_LOCATION);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionsResult");

        switch (requestCode) {

            case AUTHORISATION_FILE_WRITE:

                Log.i(TAG, "Reception de la demande d'authorisation WRITE_EXTERNAL_STORAGE");

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted, preview can be displayed
                    Log.i(TAG, "    --> OK");
                    Toast.makeText(this, "Authorisation écriture : OK", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "    --> KO");
                    Toast.makeText(this, "Authorisation écriture : KO", Toast.LENGTH_LONG).show();
                }

                break;


            case AUTHORISATION_LOCATION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "permission granted");
                    startLocationMonitoring();

                } else {
                    Log.i(TAG, "permission denied");
                    GlobalFunctions.showSnackBar(this, "Permission location refusée");
                }

                break;

        }
    }

    @Override
    public void onConnected() {
        startLocationMonitoring();
    }
}