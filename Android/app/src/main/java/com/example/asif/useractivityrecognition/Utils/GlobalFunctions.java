package com.example.asif.useractivityrecognition.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.asif.useractivityrecognition.R;

/**
 * Created by Asif on 23/12/2017.
 */

public class GlobalFunctions {


    public static void showSnackBar(Activity activity, String msg){
        View mView = activity.findViewById(android.R.id.content);
        Snackbar.make(mView,msg,Snackbar.LENGTH_LONG).show();
    }

}
