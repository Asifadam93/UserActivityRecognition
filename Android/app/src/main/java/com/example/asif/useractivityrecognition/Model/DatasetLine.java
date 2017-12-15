package com.example.asif.useractivityrecognition.Model;

/**
 * Created by fabibi on 12/12/2017.
 */

public class DatasetLine {
    private String etiquette;
    private int localisation;


    public String getEtiquette() {
        return etiquette;
    }

    public DatasetLine setEtiquette(String etiquette) {
        this.etiquette = etiquette;
        return this;
    }

    public int getLocalisation() {
        return localisation;
    }

    public DatasetLine setLocalisation(int localisation) {
        this.localisation = localisation;
        return this;
    }
}
