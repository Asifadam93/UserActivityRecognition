package com.example.asif.useractivityrecognition.Model;

import android.util.Log;

import com.example.asif.useractivityrecognition.RecordActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabibi on 12/12/2017.
 */

public class DatasetLine
{
    private static final String TAG = RecordActivity.class.getSimpleName();

    public String  etiquette;
    public int     localisation;

    public List<Float> x_acc = new ArrayList<Float>();
    public List<Float> y_acc = new ArrayList<Float>();
    public List<Float> z_acc = new ArrayList<Float>();
    public List<Float> t_acc = new ArrayList<Float>();


    public String toString() {
        Log.d(TAG, this.etiquette +
                "\n\tlocalisation : " + String.valueOf(localisation) +
                "\n\tx : " + x_acc.toString() +
                "\n\ty : " + y_acc.toString() +
                "\n\tz : " + z_acc.toString() +
                "\n\tt : " + t_acc.toString());

        String retour = x_acc.toString() + y_acc.toString() + z_acc.toString() + t_acc.toString();

        retour = retour
            .replace(",",";")
            .replace("[",";")
            .replace("]",";")
            .replace(" ",";")
            .replace(";;",";")
            ;


        return this.etiquette + retour;
    }


}
