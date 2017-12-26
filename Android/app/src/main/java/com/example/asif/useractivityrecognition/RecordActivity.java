package com.example.asif.useractivityrecognition;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.asif.useractivityrecognition.Model.DatasetLine;
import com.example.asif.useractivityrecognition.Save.SaveSampleUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordActivity extends AppCompatActivity  implements SensorEventListener {

    @BindView(R.id.progressBar) ProgressBar progress;
    @BindView(R.id.timerView) TextView timer;
    @BindView(R.id.buttonReccord) Button buttonReccord;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.spinnerEtiquete) Spinner etiquette;
    @BindView(R.id.localisation) TextView localisation;


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
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        localisation.setText(GeofenceService.localisation);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @OnClick(R.id.buttonReccord)
    public void touchReccord()
    {
        //désactivation du click (pour ne pas enregistrer plusieurs fois la même chose)
        buttonReccord.setClickable(false);

        // Création d'un timer de 2s
        new CountDownTimer(2100, 100) {

            DatasetLine ligne = new DatasetLine();

            // calcul du nombre de millisecondes restantes
            public void onTick(long millisUntilFinished) {
                int msLeft = (((2000 - (int) millisUntilFinished)));
                int secondLeft = (int) (msLeft / 1000);
                int i = (msLeft * 100 / 2000);

                //ajout des informations concernant l'accéléromètre
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
                ligne.etiquette = RecordActivity.this.etiquette.getSelectedItem().toString();
                ligne.localisation = GeofenceService.localisation;
                timer.setText("0.000");
                progress.setProgress(0);
                buttonReccord.setClickable(true);
                Log.i(TAG, ligne.toString());
                SaveSampleUtils.SaveData(ligne.toString());
            }
        }.start();
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
