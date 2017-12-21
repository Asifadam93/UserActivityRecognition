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

        String retour = this.etiquette+";";

        //création d'une liste qui concatète les données de l'accéléromètre
        List<Float> tempo = new ArrayList<Float>();
        tempo.addAll(x_acc);
        tempo.addAll(y_acc);
        tempo.addAll(z_acc);
        tempo.addAll(t_acc);

        //nécessaire pour ne pas avoir d'exposant dans notre dataset (à la place d'un toString sur notre liste de Float)
        for(Float item : tempo){
            retour += String.format("%.6f;", item);
        }

        return retour;
    }


}
