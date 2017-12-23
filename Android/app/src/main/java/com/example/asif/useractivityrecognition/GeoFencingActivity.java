package com.example.asif.useractivityrecognition;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.asif.useractivityrecognition.Adapter.GeofencePositionAdapter;
import com.example.asif.useractivityrecognition.Model.GeofencePosition;
import com.google.android.gms.location.GeofencingEvent;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeofencingActivity extends AppCompatActivity {

    private Realm realm;
    private static final String TAG  = GeofencingActivity.class.getSimpleName();
    private GeofencePositionAdapter mGeofencePositionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);

        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
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
        });

        RealmResults<GeofencePosition> realmResults = realm.where(GeofencePosition.class).findAll();

        for (GeofencePosition geofencePosition : realmResults){
            Log.i(TAG, geofencePosition.getGeofenceId());
        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGeofencePositionAdapter = new GeofencePositionAdapter(this, realmResults);
        recyclerView.setAdapter(mGeofencePositionAdapter);


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
    }

}
