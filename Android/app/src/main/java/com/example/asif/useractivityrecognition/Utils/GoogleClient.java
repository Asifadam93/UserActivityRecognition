package com.example.asif.useractivityrecognition.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asif.useractivityrecognition.Interface.GoogleClientListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Asif on 23/12/2017.
 */

public class GoogleClient {

    private final String TAG = GoogleClient.class.getSimpleName();
    private static GoogleClient mGoogleClient;
    private GoogleApiClient mGoogleApiClient;
    public GoogleClientListener mGoogleClientListener;

    private GoogleClient(Context context) {
        initGoogleClient(context);
    }

    public static GoogleClient getInstance(Context context) {

        if (mGoogleClient == null) {
            mGoogleClient = new GoogleClient(context);
        }

        return mGoogleClient;
    }

    private void initGoogleClient(final Context context) {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.i(TAG, "onConnected");

                        if(mGoogleClientListener != null){
                            mGoogleClientListener.onConnected();
                        }

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.i(TAG, "onConnectionSuspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.i(TAG, "onConnectionFailed : " + connectionResult.getErrorCode());
                    }
                })
                .build();

        mGoogleApiClient.connect();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    /*public void disconnect() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }*/
}
