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
import com.example.asif.useractivityrecognition.Interface.LocationChangeListener;
import com.example.asif.useractivityrecognition.Model.GeofencePosition;
import com.example.asif.useractivityrecognition.Utils.GlobalFunctions;
import com.example.asif.useractivityrecognition.Utils.GoogleClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {

    private static final String TAG = "mMainActivity";
    private static final int AUTHORISATION_FILE_WRITE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        }
    }
}