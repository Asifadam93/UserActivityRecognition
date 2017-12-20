package com.example.asif.useractivityrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asif.useractivityrecognition.Save.SaveSampleUtils;

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {

    private static final String TAG = SaveSampleUtils.class.getSimpleName();
    private static final int MY_AUTHORISATION_FILE_WRITE = 666;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button validateButton =  this.findViewById(R.id.validateButtonView);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });

        //Récupération de la récupération d'écrire sur
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_AUTHORISATION_FILE_WRITE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_AUTHORISATION_FILE_WRITE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Reception de la demande d'authorisation WRITE_EXTERNAL_STORAGE");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "    --> OK");
                Toast.makeText(this, "Authorisation écriture : OK", Toast.LENGTH_LONG).show();
            } else {
                Log.i(TAG, "    --> KO");
                Toast.makeText(this, "Authorisation écriture : KO", Toast.LENGTH_LONG).show();;

            }
        }
    }

}