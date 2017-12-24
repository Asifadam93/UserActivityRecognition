package com.example.asif.useractivityrecognition;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asif.useractivityrecognition.Adapter.GeofencePositionAdapter;
import com.example.asif.useractivityrecognition.Model.GeofencePosition;
import com.example.asif.useractivityrecognition.Utils.GlobalFunctions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeofencingActivity extends AppCompatActivity {

    private Realm realm;
    private static final String TAG = "mGeofencingActivity";
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    private GeofencePositionAdapter mGeofencePositionAdapter;
    private TextView textViewCurrentPosition;
    private static final int AUTHORISATION_LOCATION = 999;
    private GoogleApiClient googleApiClient;
    private Activity activity;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);

        activity = this;

        textViewCurrentPosition = findViewById(R.id.textViewCurrentPos);

        createGoogleClient();

        realm = Realm.getDefaultInstance();

        RealmResults<GeofencePosition> realmResults = realm.where(GeofencePosition.class).findAll();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGeofencePositionAdapter = new GeofencePositionAdapter(this, realmResults);
        recyclerView.setAdapter(mGeofencePositionAdapter);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mLocation != null) {
                    showAddDialog();
                } else {
                    GlobalFunctions.showSnackBar(activity, "Location not available");
                }

            }
        });
    }

    private void createGoogleClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        GlobalFunctions.showSnackBar(activity, "Api connected");
                        startLocationMonitoring();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        //showSnackbar("Google api suspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //showSnackbar("Google api connection failed");
                        Log.i(TAG, "Failed : " + connectionResult.getErrorCode());
                    }
                })
                .build();

        googleApiClient.connect();
    }

    private void startLocationMonitoring() {

        Log.i(TAG, "startLocationMonitoring");

        if (checkLocationPermission()) {

            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10 * 1000)
                    .setFastestInterval(5 * 1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    mLocation = location;

                    String postionText = "Lat : " + location.getLatitude() + ", Long : " + location.getLongitude();
                    Log.i(TAG, postionText);
                    textViewCurrentPosition.setText(postionText);

                }
            });

        } else {
            askPermission();
        }
    }

    private void startGeoFencing(final GeofencePosition geofencePosition) {

        Log.i(TAG, geofencePosition.toString());

        if (mLocation == null) {
            GlobalFunctions.showSnackBar(activity, "Position not fixed yet");
            return;
        }

        Geofence geofence = new Geofence.Builder()
                .setRequestId(geofencePosition.getGeofenceId())
                .setCircularRegion(geofencePosition.getLatitude(), geofencePosition.getLongitude(), geofencePosition.getRadius())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        Intent intent = new Intent(this, GeofenceService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (googleApiClient.isConnected()) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.GeofencingApi.addGeofences(googleApiClient, geofencingRequest, pendingIntent)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {
                                GlobalFunctions.showSnackBar(activity, "Geofence added");

                                // save geofence info to db
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealm(geofencePosition);
                                    }
                                });

                            } else {
                                GlobalFunctions.showSnackBar(activity, "Error adding geofence");
                                Log.i(TAG, "Error adding position to geofence : " + status.getStatus());
                            }
                        }
                    });

        } else {
            GlobalFunctions.showSnackBar(activity, "Google api not connected");
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case AUTHORISATION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    startLocationMonitoring();

                } else {
                    // Permission denied
                }
                break;
            }
        }
    }

    private void showAddDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.editTextId);

        dialogBuilder.setTitle("Ajouter geofence");
        dialogBuilder.setMessage("Donner un nom : ");
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                final String geoId = edt.getText().toString();

                if (geoId.isEmpty()) {
                    edt.setError("Insert a name");
                    return;
                }

                GeofencePosition mGeofencePosition = new GeofencePosition();
                mGeofencePosition.setGeofenceId(geoId);
                mGeofencePosition.setLatitude(mLocation.getLatitude());
                mGeofencePosition.setLongitude(mLocation.getLongitude());
                mGeofencePosition.setRadius(100);

                startGeoFencing(mGeofencePosition);
            }
        });
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }

}

/*realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GeofencePosition geofencePosition = realm.createObject(GeofencePosition.class);
                geofencePosition.setGeofenceId("Home");
                geofencePosition.setLatitude(48.9679385);
                geofencePosition.setLongitude(2.2800401);
                geofencePosition.setRadius(100);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GeofencePosition geofencePosition = realm.createObject(GeofencePosition.class);
                geofencePosition.setGeofenceId("Office");
                geofencePosition.setLatitude(48.8968845);
                geofencePosition.setLongitude(2.3663293);
                geofencePosition.setRadius(100);
            }
        });*/

/*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/