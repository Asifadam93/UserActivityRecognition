package com.example.asif.useractivityrecognition.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabibi on 12/12/2017.
 */

public class DatasetLine
{
    public String  etiquette;
    public int     localisation;

    public List<Float> x_acc = new ArrayList<Float>();
    public List<Float> y_acc = new ArrayList<Float>();
    public List<Float> z_acc = new ArrayList<Float>();
    public List<Float> t_acc = new ArrayList<Float>();


    public String toString() {
        return "Jeu de donnée :" + this.etiquette +
            "\n localisation " + String.valueOf(localisation) +
            "\n x : " + x_acc.toString() +
            "\n y : " + y_acc.toString() +
            "\n z : " + z_acc.toString() +
            "\n t : " + t_acc.toString();
    }


}
