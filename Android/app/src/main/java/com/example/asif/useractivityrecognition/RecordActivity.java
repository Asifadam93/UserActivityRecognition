package com.example.asif.useractivityrecognition;

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

import com.example.asif.useractivityrecognition.Save.SaveSampleUtils;

public class RecordActivity extends AppCompatActivity {

    private static final String TAG = RecordActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ProgressBar progress  = (ProgressBar) this.findViewById(R.id.progressBar);
        final TextView timer        = (TextView) this.findViewById(R.id.timerView);
        final Button buttonReccord  = (Button) this.findViewById(R.id.buttonReccord);

        final Handler handler = new Handler();
        progress.setProgress(0);


        final View.OnClickListener clickReccord= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonReccord.setClickable(false);
                Log.i(TAG, String.valueOf("onKey"));

                buttonReccord.setPressed(true);
                new CountDownTimer(4000, 100) {

                    public void onTick(long millisUntilFinished) {

                        int msLeft = (((4000 - (int) millisUntilFinished)));
                        int secondLeft = (int) (msLeft / 1000);

                        int i = (msLeft * 100 / 4000);

                        timer.setText(String.valueOf(secondLeft) + ":" + String.valueOf(Math.abs((secondLeft * 1000) - msLeft)));
                        progress.setProgress(i);

                        Log.i(TAG, String.valueOf(i));
                    }

                    public void onFinish() {
                        timer.setText("0.000");
                        progress.setProgress(0);
                        buttonReccord.setClickable(true);
                    }
                }.start();
            }
        };

        buttonReccord.setOnClickListener(clickReccord);





    }
}
