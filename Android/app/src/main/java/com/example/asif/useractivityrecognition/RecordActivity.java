package com.example.asif.useractivityrecognition;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asif.useractivityrecognition.Model.DatasetLine;
import com.example.asif.useractivityrecognition.Save.SaveSampleUtils;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity  implements SensorEventListener {

    private static final String TAG = RecordActivity.class.getSimpleName();
    SensorManager sensorManager;
    Sensor accelerometer;

    public float x_acc;
    public float y_acc;
    public float z_acc;
    public float t_acc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //récupération des éléments de la vue que l'on va utiliser
        final ProgressBar progress  = (ProgressBar) this.findViewById(R.id.progressBar);
        final TextView timer        = (TextView) this.findViewById(R.id.timerView);
        final Button buttonReccord  = (Button) this.findViewById(R.id.buttonReccord);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Ajout du click sur le bouton
        buttonReccord.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {
                //désactivation du click (pour ne pas enregistrer plusieurs fois la même chose)
                buttonReccord.setClickable(false);



                // Création d'un timer de 4s
                new CountDownTimer(2000, 100) {

                    DatasetLine ligne = new DatasetLine();

                    // calcul du nombre de millisecondes restantes
                    public void onTick(long millisUntilFinished) {
                        int msLeft = (((2000 - (int) millisUntilFinished)));
                        int secondLeft = (int) (msLeft / 1000);
                        int i = (msLeft * 100 / 2000);


                        ligne.x_acc.add(RecordActivity.this.x_acc);
                        ligne.y_acc.add(RecordActivity.this.y_acc);
                        ligne.z_acc.add(RecordActivity.this.z_acc);
                        ligne.t_acc.add(RecordActivity.this.t_acc);

                        // raffraichissement de l'affichage
                        timer.setText(String.valueOf(secondLeft) + ":" + String.valueOf(Math.abs((secondLeft * 1000) - msLeft)));
                        progress.setProgress(i);
                    }

                    //réinitialisation du click sur le bouton d'enregistrement
                    public void onFinish() {
                        timer.setText("0.000");
                        progress.setProgress(0);
                        buttonReccord.setClickable(true);
                        Log.i(TAG, ligne.toString());
                        SaveSampleUtils.SaveData(ligne.toString());
                    }
                }.start();
            }
        });
    }




    @Override
    protected void onPause() {
        // unregister the sensor (désenregistrer le capteur)
        sensorManager.unregisterListener(this, accelerometer);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            this.x_acc = sensorEvent.values[0];
            this.y_acc = sensorEvent.values[1];
            this.z_acc = sensorEvent.values[2];
            this.t_acc = (this.x_acc + this.y_acc + this.z_acc);


        }


    }

}
