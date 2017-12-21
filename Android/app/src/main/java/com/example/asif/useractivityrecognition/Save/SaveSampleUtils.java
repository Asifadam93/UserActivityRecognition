package com.example.asif.useractivityrecognition.Save;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by fabibi on 12/12/2017.
 */

public class SaveSampleUtils {

    private static final String  TAG =  SaveSampleUtils.class.getSimpleName();
    private static final String FILE =  "dataset.csv";
    private static Boolean result =     false;



    public static void  SaveData(String donnees) {

        Log.i(TAG, "Sauvegarde d'un jeu de donnée");

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Récupération du fichier
        File datasetFile        =  new File(root, FILE);

        // Si le fichier n'existe pas, on le créé
        if (!datasetFile.exists()) {
            Log.i(TAG, "Création du fichier "+FILE);
            try {
                result = datasetFile.createNewFile();
                Log.i(TAG, " -> " + (result?"OK":"KO") );
            } catch (Exception e){
                Log.i(TAG, e.getMessage());
                Log.i(TAG, " -> KO");
            }
        }

        // Ecriture sur le fichier
        FileWriter fileWriter   = null;

        try {
            fileWriter = new FileWriter(datasetFile, true);
            fileWriter.append("\n"+ donnees);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
