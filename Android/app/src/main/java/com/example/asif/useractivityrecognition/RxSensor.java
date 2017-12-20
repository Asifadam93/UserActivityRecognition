package com.example.asif.useractivityrecognition;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Asif on 12/12/2017.
 */

public class RxSensor {

    private static RxSensor mSensor;
    private static String TAG = "RxSensor";

    private RxSensor(Context context) {
        new ReactiveSensors(context).observeSensor(Sensor.TYPE_GYROSCOPE)
            .subscribeOn(Schedulers.computation())
            .filter(ReactiveSensorFilter.filterSensorChanged())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ReactiveSensorEvent>() {
                @Override
                public void accept(ReactiveSensorEvent reactiveSensorEvent) throws Exception {
                    SensorEvent event = reactiveSensorEvent.getSensorEvent();

                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    Log.d(TAG, "Accelerometer - X : "+x+", Y : "+y+", Z : "+z);
                }
            });
    }

    public static RxSensor getInstance(Context context) {
        if (mSensor == null) {
            mSensor = new RxSensor(context);
        }
        return mSensor;
    }
}
